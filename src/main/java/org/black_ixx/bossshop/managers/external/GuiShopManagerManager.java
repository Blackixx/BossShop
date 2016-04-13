package org.black_ixx.bossshop.managers.external;


import org.black_ixx.bossshop.addon.guishopmanager.GuiShopManager;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GuiShopManagerManager {
	
	public GuiShopManager getPlugin(){
		Plugin p = Bukkit.getPluginManager().getPlugin("GuiShopManager");
		if(p==null){
			return null;
		}
		return (GuiShopManager) p;
	}
	
	public ItemStack getGuiShopManagerItem(String name){
		GuiShopManager plugin = getPlugin();
		
		if(plugin==null){
			ClassManager.manager.getBugFinder().warn("Tried to get a GuiShopManager item but GuiShopManager was not found. Stopped search.");
			return null;
		}
		ItemStack item = plugin.getGSMItems().getItemByName(name);
		
		if(item==null){
			ClassManager.manager.getBugFinder().warn("GuiShopManager item "+name+" was not found. Maybe you wrote its name wrong?");
			return null;
		}
		
		
		return item;
		
		
	}

}
