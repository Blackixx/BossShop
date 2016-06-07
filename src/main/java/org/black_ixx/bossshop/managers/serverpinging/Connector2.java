package org.black_ixx.bossshop.managers.serverpinging;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class Connector2{
	// An old Connector. Will probably be never used again.

	private String ip;
	private int port;		


	public String getHost(){
		return ip;
	}

	public int getPort(){
		return port;
	}


	public Connector2(String ip, int port){
		this.ip = ip;
		this.port = port;
	}


	public synchronized String getData(ConnectionType con){
		try	{
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(this.ip, this.port), 1000);
			if (!socket.isConnected())	{
				socket.close();
				return null;
			}

			socket.setSoTimeout(2500);
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16BE"));

			dos.write(new byte[] { -2, 1 });

			int packetID = is.read();
			if (packetID == -1)
				System.out.println("(EOS)");
			if (packetID != 255) {
				System.out.println("[BossShop] [ServerPinging] Invalid Packet ID: " + packetID);
			}
			int length = isr.read();

			if (length == -1)
				System.out.println("EOS");
			if (length == 0)
				System.out.println("[BossShop] [ServerPinging] Invalid Packet Length.");
			char[] chars = new char[length];

			if (isr.read(chars, 0, length) != length) {
				System.out.println("EOS");
			}
			String string = new String(chars);
			String[] data = string.split("");
			socket.close();
			if (con == ConnectionType.PLAYERS_ONLINE)	{
				return Integer.parseInt(data[4]) + "/" + 
						Integer.parseInt(data[5]);
			}
			if (con == ConnectionType.CURRENT_PLAYERS)	{
				return data[4];
			}
			if (con == ConnectionType.IS_ONLINE)	{
				return data[3];
			}
			if (con == ConnectionType.MOTD)	{
				return data[3] + "&r";
			}
			if (con == ConnectionType.SERVER_VERSION)	{
				return data[2];
			}
			if (con == ConnectionType.MAX_PLAYERS)	{
				return data[5];
			}

			System.out.println("[BossShop] [ServerPinging] Connection value not handled!");
			return null;
		}
		catch (Exception e)	{
			e.printStackTrace();
		}

		return null;
	}

	public boolean isOnline(){
		String isOnline = getData(ConnectionType.IS_ONLINE);
		if (isOnline != null)	{
			return true;
		}
		return false;
	}

	public String getMOTD(){
		return getData(ConnectionType.MOTD);
	}

	public String getPlayers()	{
		return getData(ConnectionType.CURRENT_PLAYERS);
	}

	public String getMaxPlayers()	{
		return getData(ConnectionType.MAX_PLAYERS);
	}

	public String getVersion()	{
		return getData(ConnectionType.SERVER_VERSION);
	}


	public enum ConnectionType{
		PLAYERS_ONLINE, SERVER_VERSION, MOTD, MAX_PLAYERS, IS_ONLINE, CURRENT_PLAYERS;
	}


}
