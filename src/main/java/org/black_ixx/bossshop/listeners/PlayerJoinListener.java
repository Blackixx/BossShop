package org.black_ixx.bossshop.listeners;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.UpdaterManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener{

	
	private boolean enabled = false;
	
	public void setEnabled(boolean b){
		enabled=b;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		
		if(!enabled){
			return;
		}
		
		if(e.getPlayer().hasPermission("BossShop.Update")){
			
			final UpdaterManager u = ClassManager.manager.getUpdaterManager();
			
			if(u!=null){
				
			final String name = e.getPlayer().getName();
			
			new BukkitRunnable() {
				@Override
				public void run() {
					Player p  = Bukkit.getPlayerExact(name);
					if(p!=null){
						p.sendMessage(ChatColor.GOLD+"A BossShop Update was found: "+ChatColor.RED+u.getName()+ChatColor.GOLD+". You can download it here: "+ChatColor.RED+u.getLink()+ChatColor.GOLD+".");
					}					
				}
			}.runTaskLater(ClassManager.manager.getPlugin(), 10);
				
			}
			
			
		}
	}
}
