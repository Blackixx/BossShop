package org.black_ixx.bossshop.managers.external;


import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeCordManager {


	public void sendToServer(String server, PlayerMoveEvent event, BossShop plugin){
		event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
		event.setCancelled(true);
		sendToServer(server, event.getPlayer(), plugin);
	}

	public void sendToServer(String server, Player p, BossShop plugin){
		sendPluginMessage(p, plugin, "Connect", server);
	}

	public boolean sendPluginMessage(Player p ,BossShop plugin, String... args ){
		if(p == null){
			p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		}

		if(p != null){
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			for(String arg : args){
				out.writeUTF(arg);
			}
			p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
			return true;
		}

		return false;
	}




}
