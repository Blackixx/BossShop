package org.black_ixx.bossshop.managers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class BungeeCordManager {


	public void sendToServer(String server, PlayerMoveEvent event, BossShop plugin){

		event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
		event.setCancelled(true);
		sendToServer(server, event.getPlayer(), plugin);
		return;
	}

	public void sendToServer(String server, Player p, BossShop plugin){

		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server); // Target Server
		} catch (IOException e) {
			// Can never happen
		}
		p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
		return;
	}

}
