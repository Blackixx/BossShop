package org.black_ixx.bossshop.managers.serverpinging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.black_ixx.bossshop.managers.ClassManager;




public class Connector6 extends BasicConnector{	
	//This connector did not work at all
	
	private String host = "localhost";
	private int port = 25565;

	private int timeout;
	private int pingversion = -1;
	private int protocolversion = -1;
	private String gameversion;

	private boolean online;
	private int playercount;
	private int maxplayers;
	private String motd;

	private long latest_failure;

	public Connector6(String host, int port, int timeout){
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}


	public String getHost() {
		return this.host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public int getPingVersion() {
		return this.pingversion;
	}

	public int getProtocolVersion() {
		return this.protocolversion;
	}

	public String getGameVersion() {
		return this.gameversion;
	}

	public String getMotd() {
		return this.motd;
	}

	public String getPlayerCount() {
		return String.valueOf(this.playercount);
	}

	public int getMaxPlayers() {
		return this.maxplayers;
	}
		
	public boolean isOnline() {
		return online;
	}


	public boolean update() {
		Exception e = fetchData();
		if(e == null){
			latest_failure = -1;
			return true;
		}else{
			if(System.currentTimeMillis() > latest_failure + 90000){ //When there are issues: Do not spam the server.log with huge error messages but rather print a short line every 1 1/2 minutes.
				ClassManager.manager.getBugFinder().warn("Serverpinging error: Unable to connect with '"+host+":"+port+"'!");
				latest_failure = System.currentTimeMillis();
				System.out.println("exception: "+e.getMessage());
			}
			return false;
		}
	}

	public Exception fetchData() {
		Exception exception = null;
		try {
			Socket socket = new Socket();
			socket.setSoTimeout(this.timeout);
			socket.connect(new InetSocketAddress(host, port), getTimeout());

			OutputStream outputStream = socket.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-16BE"));

			dataOutputStream.write(new byte[] { -2, 1 });

			int packetId = inputStream.read();

			if (packetId == -1)	{
				try {
					socket.close();
				} catch (IOException e) {
					exception = e;
				}
				socket = null;
				online = false;
				return exception;
			}

			if (packetId != 255) {
				try {
					socket.close();
				} catch (IOException e) {
					exception = e;
				}
				socket = null;
				online = false;
				return exception;
			}

			int length = inputStreamReader.read();

			if (length == -1) {
				try {
					socket.close();
				} catch (IOException e) {
					exception = e;
				}
				socket = null;
				online = false;
				return exception;
			}

			if (length == 0) {
				try {
					socket.close();
				} catch (IOException e) {
					exception = e;
				}
				socket = null;
				online = false;
				return exception;
			}

			char[] chars = new char[length];

			if (inputStreamReader.read(chars, 0, length) != length) {
				try {
					socket.close();
				} catch (IOException e) {
					exception = e;
				}
				socket = null;
				online = false;
				return exception;
			}

			String string = new String(chars);

			if (string.startsWith("§")) {
				String[] data = string.split("");

				pingversion = Integer.parseInt(data[0].substring(1));
				protocolversion = Integer.parseInt(data[1]);
				gameversion = data[2];
				motd = data[3];
				playercount = Integer.parseInt(data[4]);
				maxplayers = Integer.parseInt(data[5]);
				online = true;
			} else {
				String[] data = string.split("§");

				motd = data[0];
				playercount = Integer.parseInt(data[1]);
				maxplayers = Integer.parseInt(data[2]);
				online = true;
			}

			dataOutputStream.close();
			outputStream.close();

			inputStreamReader.close();
			inputStream.close();

			socket.close();
		} catch (SocketException e) {
			online = false;
			return e;
		} catch (IOException e) {
			online = false;
			return e;
		}

		return null;
	}


}
