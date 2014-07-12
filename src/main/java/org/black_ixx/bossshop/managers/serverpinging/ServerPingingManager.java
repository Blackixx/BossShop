package org.black_ixx.bossshop.managers.serverpinging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ServerPingingManager {


	private HashSet<BasicConnector> to_ping = new HashSet<BasicConnector>();

	private ServerPingingRunnableHandler runnablehandler= new ServerPingingRunnableHandler();


	public ServerPingingManager(){
		System.out.println("[BossShop] [ServerPinging] Loading ServerPinging Package!");
	}


	public void updateItem(BasicConnector c){
		c.update();
		
		ItemStack i = c.getItem();

		ItemMeta meta = i.getItemMeta();

		String motd = c.getMotd();

		if(motd==null){
			System.out.print("[BossShop] [ServerPinging] Was not able to get the motd of Server "+c.getHost()+":"+c.getPort());
			return;
		}

		String players = c.getPlayerCount();

		if (meta.hasDisplayName()){			
			meta.setDisplayName(transform(c.getOldName(),meta.getDisplayName(), motd, players));				
		}

		if (meta.hasLore()){
			List<String> list = c.getOldLore();
			List<String> l = new ArrayList<String>();
			for (String s : list){
				l.add(transform(s,meta.getLore().get(l.size()),motd, players));
			}
			if (l!=null&&l.size()>0){
				meta.setLore(l);
			}
		}

		i.setItemMeta(meta);
		if(c.getShop()!=null){
			if(c.getShop().getInventory()!=null){
				//TODO wenn inventory customizable ist ist es auch nicht existent hier
				c.getShop().getInventory().setItem(c.getLocation(), i);
			}
		}

	}

	private String transform(String s,String current, String motd, String players){
		boolean b=false;;
		if(s.contains("%motd%")&&motd!=null){
			s=s.replace("%motd%", motd);
			b=true;
		}
		if(s.contains("%players%")&&players!=null){
			s=s.replace("%players%", players);
			b=true;
		}
		if(!b){
			return current;
		}
		return s;
	}

	public void updateItems(){
		for(BasicConnector c : to_ping){
			if(c==null){
				System.out.println("[BossShop] [ServerPinging] Connector not found?!");
				continue;
			}
			updateItem(c);
		}
	}

	public void addItem(ItemStack i,int loc, BSShop shop, BasicConnector c){
		to_ping.add(c);
		if(!i.hasItemMeta()){
			c.setOldData(null, null,i,shop,loc);
			return;
		}
		c.setOldData(i.getItemMeta().getLore(), i.getItemMeta().getDisplayName(),i,shop,loc);
	}


	public void clearItems(){
		to_ping.clear();
	}


	public ServerPingingRunnableHandler getServerPingingRunnableHandler(){
		return runnablehandler;
	}


}
