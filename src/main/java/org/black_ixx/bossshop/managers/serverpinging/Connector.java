package org.black_ixx.bossshop.managers.serverpinging;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.inventory.ItemStack;


public class Connector {
	// An old Connector. Will probably be never used again.

	private String host;
	private int port;
	
	//Custom
	private List<String> old_lore;
	private String old_name;
	private ItemStack item;
	private BSShop shop;
	private int location;
	//Custom end
	
	
	public Connector(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public Connector(String host){
		this.host = host;
		this.port = 25565;
	}
	
	public Connector(){
		this.host = "127.0.0.1";
		this.port = 25565;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}
	
	//Custom
	public void setOldData(List<String> lore, String name, ItemStack item, BSShop shop, int location){
		old_lore=lore;
		old_name=name;
		this.item=item;
		this.shop=shop;
		this.location=location;
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	public List<String> getOldLore(){
		return old_lore;
	}
	
	public String getOldName(){
		return old_name;
	}
	
	public BSShop getShop(){
		return shop;
	}
	
	public int getLocation(){
		return location;
	}
	//Custom end
	
	
	public String parseData(Connection connection){
		
		Socket socket = new Socket();
		
		try {
			OutputStream os;
			DataOutputStream dos;
			InputStream is;
			InputStreamReader isr;
			socket.setSoTimeout(3000);
			
			socket.connect(new InetSocketAddress(host, port), 3000);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			is = socket.getInputStream();
			
			isr = new InputStreamReader(is, Charset.forName("UTF-16BE"));
			dos.write(new byte[] {(byte)0xFE, (byte) 0x01 });
			int packetId = is.read();
			
			if(packetId == -1)
				System.out.println("[BossShop] [ServerPinging] EOS");
			
			if(packetId != 0xFF)
				System.out.println("[BossShop] [ServerPinging] Invalid packetId!"+packetId);
			int length = isr.read();
			
			if(length == -1)
				System.out.println("[BossShop] [ServerPinging] EOS");
			if(length == 1)
				System.out.println("[BossShop] [ServerPinging] Invalid length");
			
			char[] chars = new char[length];
			
			if(isr.read(chars, 0, length) != length)
				System.out.println("[BossShop] [ServerPinging] End of stream");
			
			String string =  new String(chars);
			String[] data = string.split("\0");
			
			if(connection == Connection.PLAYERS_ONLINE){
				return data[4];
			} 
			if(connection == Connection.MOTD) {
				return data[3];
			} else {
				System.out.println("[BossShop] [ServerPinging] Connection value not handled!");
				return null;
			}
			
		} catch (Exception e) {
			
			try {//new
				socket.close();
			} catch (IOException e1) {
			}//new end
			
			System.out.print("Exception when trying to ping to "+host+":"+port);
			return null;
		}
	}
	
	
	public enum Connection {
		PLAYERS_ONLINE, MOTD;
	}
	
	
}
