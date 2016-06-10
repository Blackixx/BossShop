package org.black_ixx.bossshop.managers.serverpinging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import org.black_ixx.bossshop.managers.ClassManager;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class Connector5 extends BasicConnector{
	// This connector worked fine on recent (1.8 and 1.9) Spigot and Bukkit builds however it caused errors on a PaperSpigot 1.8.8 server

	private InetSocketAddress host;
	private int timeout = 7000;
	private Gson gson = new Gson();

	private StatusResponse response;
	
	private long latest_failure;




	public Connector5(String host, int port, int timeout){
		setAddress(new InetSocketAddress(host, port));
		setTimeout(timeout);		
	}


	public InetSocketAddress getAddress() {
		return this.host;
	}
	int getTimeout() {
		return this.timeout;
	}
	@Override
	public String getHost() {
		return host.getAddress().getHostName();
	}

	@Override
	public int getPort(){
		return host.getPort();
	}	

	@Override
	public String getPlayerCount() {
		if(response==null){
			return null;
		}
		return ""+response.getPlayers().getOnline();
	}

	@Override
	public String getMotd() {
		if(response==null){
			return null;
		}
		return response.getDescription().getText();
	}

	@Override
	public boolean isOnline() {
		if(response!=null){
			if(latest_failure==-1){
				return response.getDescription().getText()!=null;
			}
		}
		return false;
	}


	public void setAddress(InetSocketAddress host) {
		this.host = host;
	}
	void setTimeout(int timeout) {
		this.timeout = timeout;
	}


	@Override
	public boolean update(){
		try {
			response = fetchData();
			latest_failure = -1;
			return true;
		} catch (Exception e) {
			if(System.currentTimeMillis() > latest_failure + 90000){ //When there are issues: Do not spam the server.log with huge error messages but rather print a short line every 1 1/2 minutes.
				ClassManager.manager.getBugFinder().warn("Serverpinging error: Unable to connect with '"+host.getHostName()+":"+host.getPort()+"'!");
				latest_failure = System.currentTimeMillis();
				System.out.println("exception: "+e.getMessage());
			}
			return false;
		}
	}





	public int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}
		return i;
	}
	public void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}


	@SuppressWarnings("resource")
	public StatusResponse fetchData() throws IOException{

		Socket socket = new Socket();
		OutputStream outputStream;
		DataOutputStream dataOutputStream;
		InputStream inputStream;
		InputStreamReader inputStreamReader;

		socket.setSoTimeout(this.timeout);

		socket.connect(host, timeout);

		outputStream = socket.getOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);

		inputStream = socket.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream handshake = new DataOutputStream(b);
		handshake.writeByte(0x00); //packet id for handshake
		writeVarInt(handshake, 4); //protocol version
		writeVarInt(handshake, this.host.getHostString().length()); //host length
		handshake.writeBytes(this.host.getHostString()); //host string
		handshake.writeShort(host.getPort()); //port
		writeVarInt(handshake, 1); //state (1 for handshake)

		writeVarInt(dataOutputStream, b.size()); //prepend size
		dataOutputStream.write(b.toByteArray()); //write handshake packet


		dataOutputStream.writeByte(0x01); //size is only 1
		dataOutputStream.writeByte(0x00); //packet id for ping
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		@SuppressWarnings("unused")
		int size = readVarInt(dataInputStream); //size of packet
		int id = readVarInt(dataInputStream); //packet id

		if (id == -1) {
			throw new IOException("Premature end of stream.");
		}

		if (id != 0x00) { //we want a status response
			throw new IOException("Invalid packetID");
		}
		int length = readVarInt(dataInputStream); //length of json string

		if (length == -1) {
			throw new IOException("Premature end of stream.");
		}

		if (length == 0) {
			throw new IOException("Invalid string length.");
		}

		byte[] in = new byte[length];
		dataInputStream.readFully(in);  //read json string
		String json = new String(in);


		long now = System.currentTimeMillis();
		dataOutputStream.writeByte(0x09); //size of packet
		dataOutputStream.writeByte(0x01); //0x01 for ping
		dataOutputStream.writeLong(now); //time!?

		readVarInt(dataInputStream);
		id = readVarInt(dataInputStream);
		if (id == -1) {
			throw new IOException("Premature end of stream.");
		}

		if (id != 0x01) {
			throw new IOException("Invalid packetID");
		}
		long pingtime = dataInputStream.readLong(); //read response

		StatusResponse response = gson.fromJson(json, StatusResponse.class);
		response.setTime((int) (now - pingtime));

		dataOutputStream.close();
		outputStream.close();
		inputStreamReader.close();
		inputStream.close();
		socket.close();

		return response;
	}

	
	
	
	
	
	public class StatusResponse {
		private Description description;
		private Players players;
		private Version version;
		private String favicon;
		private int time;

		public Description getDescription() {
			return description;
		}

		public Players getPlayers() {
			return players;
		}

		public Version getVersion() {
			return version;
		}

		public String getFavicon() {
			return favicon;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}

	public class Players {
		private int max;
		private int online;
		private List<Player> sample;

		public int getMax() {
			return max;
		}

		public int getOnline() {
			return online;
		}

		public List<Player> getSample() {
			return sample;
		}
	}
	public class Player {
		private String name;
		private String id;

		public String getName() {
			return name;
		}

		public String getId() {
			return id;
		}
	}
	public class Version {
		private String name;
		private String protocol;

		public String getName() {
			return name;
		}

		public String getProtocol() {
			return protocol;
		}
	}

	public class Description {
		private String text;

		public String getText(){
			return text;
		}
	}

}
