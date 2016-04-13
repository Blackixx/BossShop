package org.black_ixx.bossshop.managers.external;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIHandler {
	
	public PlaceholderAPIHandler(){
		Bukkit.getLogger().info("[BossShop] Hooked into PlaceholderAPI.");
	}

	public String transformString(String s, Player p){
		if(containsPlaceholder(s)){
			s = PlaceholderAPI.setPlaceholders(p, s);
		}
		return s;
	}
	
	public boolean containsPlaceholder(String s){
		return PlaceholderAPI.containsPlaceholders(s);
	}
	
	
	

}
