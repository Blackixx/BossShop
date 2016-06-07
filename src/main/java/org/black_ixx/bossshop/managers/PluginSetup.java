package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.config.FileHandler;

public class PluginSetup {

	public void setupConfigs(BossShop plugin){
			FileHandler filehandler = new FileHandler();
			filehandler.copyFromJar(plugin, false
					, "config.yml"
					, "messages.yml");
	}

	public void setupShops(BossShop plugin){
		FileHandler filehandler = new FileHandler();
		filehandler.copyFromJar(plugin, true
				, "BungeeCordServers.yml"
				, "BuyShop.yml"
				, "LilyPadServers.yml"
				, "Menu.yml"
				, "PointShop.yml"
				, "Potions.yml"
				, "SellShop.yml"
				, "Spells.yml"
				, "Warps.yml");
	}
}
