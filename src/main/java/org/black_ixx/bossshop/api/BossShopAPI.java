package org.black_ixx.bossshop.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSCustomActions;
import org.black_ixx.bossshop.core.BSCustomLink;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.enums.BSBuyType;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BossShopAPI {
	
	
		
	
	private BossShop plugin;
	private List<BossShopAddon> enabled_addons;
	
	
	public BossShopAPI(BossShop plugin){
		this.plugin=plugin;
	}
	
	
	//For Single Shop	
	public boolean isValidShop(InventoryView v){
		if(v!=null){
			if(v.getTopInventory() != null){
				return isValidShop(v.getTopInventory());
			}
		}
		return false;
	}
	public boolean isValidShop(Inventory i){
		if(i == null){
			return false;
		}
		return (i.getHolder() instanceof BSShopHolder);
	}
	
	
	public BSShop getShop(String name){
		return plugin.getClassManager().getShops().getShop(name.toLowerCase());
	}
	
	
	public void openShop(Player p, String name){
		BSShop shop = getShop(name);
		if (shop ==null){
			System.out.print("[BossShop] [API] Error: Tried to open Shop "+name+" but it was not found...");
			return;
		}
		openShop(p, shop);
	}
	

	public void openShop(Player p, BSShop shop){
		plugin.getClassManager().getShops().openShop(p, shop);
	}
	
	//Get Managers
	public BSShops getShopHandler(){
		return plugin.getClassManager().getShops();
	}
	
	
	//Modify Shop/Shops	
	public void addItemToShop(ItemStack menu_item, BSBuy shop_item, BSShop shop){
		shop.addShopItem(shop_item, menu_item, ClassManager.manager);
	}
	
	public void finishedAddingItemsToShop(BSShop shop){
		shop.finishedAddingItems();
	}
	
	
	
	//Create Things
	public BSBuy createBSBuy(String name, BSBuyType reward_type, BSPriceType price_type, Object reward, Object price, String msg, int location, String permission){
		return new BSBuy(reward_type, price_type, reward, price, msg, location, permission,name);
	}
	

	public BSBuy createBSBuyCustom(String name, BSBuyType reward_type, BSPriceType price_type, BSCustomLink reward, Object price, String msg, int location, String permission){
		return new BSBuy(reward_type, price_type, reward, price, msg, location, permission,name);
	}
	
	public BSBuy createBSBuy(BSBuyType reward_type, BSPriceType price_type, Object reward, Object price, String msg, int location, String permission){
		return new BSBuy(reward_type, price_type, reward, price, msg, location, permission,"");
	}
	

	public BSBuy createBSBuyCustom(BSBuyType reward_type, BSPriceType price_type, BSCustomLink reward, Object price, String msg, int location, String permission){
		return new BSBuy(reward_type, price_type, reward, price, msg, location, permission,"");
	}
	
	public BSCustomLink createBSCustomLink(BSCustomActions actions, int action_id){
		return new BSCustomLink(action_id, actions);
	}
	
	//Get Shop Items	
	public HashMap<BSShop, List<BSBuy>> getAllShopItems(){
		HashMap<BSShop, List<BSBuy>> all= new HashMap<BSShop, List<BSBuy>>();
		for (int i : plugin.getClassManager().getShops().getShops().keySet()){
			BSShop shop = plugin.getClassManager().getShops().getShop(i);
			if(shop==null){
				continue;
			}
			List<BSBuy> items = new ArrayList<BSBuy>();
			for (ItemStack item : shop.getItems().keySet()){
				BSBuy buy = shop.getItems().get(item);
				if(item==null||buy==null){
					continue;
				}
				items.add(buy);
			}
			all.put(shop, items);
		}
		
		return all;
	}
	
	public HashMap<BSConfigShop, List<BSBuy>> getAllShopItems(String config_option){
		HashMap<BSConfigShop, List<BSBuy>> all= new HashMap<BSConfigShop, List<BSBuy>>();
		for (int i : plugin.getClassManager().getShops().getShops().keySet()){
			BSShop shop = plugin.getClassManager().getShops().getShop(i);
			if(shop==null|!(shop instanceof BSConfigShop)){
				continue;
			}
			BSConfigShop sho = (BSConfigShop) shop;
			List<BSBuy> items = new ArrayList<BSBuy>();
			for (ItemStack item : shop.getItems().keySet()){
				BSBuy buy = shop.getItems().get(item);
				if(item==null||buy==null){
					continue;
				}
				if(buy.getConfigurationSection(sho).getBoolean(config_option)==false && buy.getConfigurationSection(sho).getInt(config_option)==0){
					continue;
				}
				items.add(buy);
			}
			all.put(sho, items);
		}
		
		return all;
	}
	

	//Addon API	
	protected void addEnabledAddon(BossShopAddon addon){
		Plugin addonplugin = Bukkit.getPluginManager().getPlugin(addon.getAddonName());
		if(addonplugin==null){
			return;
		}
		if(enabled_addons==null){
			enabled_addons = new ArrayList<BossShopAddon>();
		}
		if(enabled_addons.contains(addon)){
			return;
		}
		enabled_addons.add(addon);
	}
	
	public List<BossShopAddon> getEnabledAddons(){
		return enabled_addons;
	}
	
	

}
