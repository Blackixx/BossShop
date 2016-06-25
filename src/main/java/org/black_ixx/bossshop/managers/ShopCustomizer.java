package org.black_ixx.bossshop.managers;

import java.util.HashMap;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSDisplayItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopCustomizer {

	public ShopCustomizer(boolean hide_items_no_perms, ClassManager m ){
		hide_noperm=hide_items_no_perms;
	}



	private boolean hide_noperm;


	public Inventory createInventory(BSShop shop,HashMap<ItemStack, BSBuy> shop_items, boolean displaying, Player p, ClassManager m){
		BSShopHolder holder = new BSShopHolder(shop);
		Inventory inventory = Bukkit.createInventory(holder, shop.getInventorySize(), shop.getValidDisplayName(p));

		return createInventory(shop, shop_items, displaying, p, m, inventory, holder);
	}


	public Inventory createInventory(BSShop shop,HashMap<ItemStack, BSBuy> shop_items, boolean displaying, Player p, ClassManager m, Inventory inventory){
		if(inventory.getHolder() instanceof BSShopHolder){
			BSShopHolder holder = (BSShopHolder) inventory.getHolder();
			return createInventory(shop, shop_items, displaying, p, m, inventory, holder);
		}else{
			return inventory;
		}
	}

	public Inventory createInventory(BSShop shop,HashMap<ItemStack, BSBuy> shop_items, boolean displaying, Player p, ClassManager m, Inventory inventory, BSShopHolder holder){			
		inventory.clear();		

		HashMap<Integer, BSBuy> locs = new HashMap<Integer, BSBuy>();

		for (ItemStack item : shop_items.keySet()){
			BSBuy buy = shop_items.get(item);
			if(buy!=null){
				if(hide_noperm &! buy.hasPermission(p,false)){
					continue;
				}
				if(locs.containsKey(buy.getInventoryLocation())){
					continue;
				}

				BSDisplayItemEvent event = new BSDisplayItemEvent(p, shop, buy, inventory);
				Bukkit.getPluginManager().callEvent(event);
				if(event.isCancelled()){
					continue;
				}

				item=item.clone();

				if(displaying){
					if(item.hasItemMeta()){
						ClassManager.manager.getItemStackTranslator().translateItemStack(buy, shop, item, p);
					}
				}

				locs.put(buy.getInventoryLocation(), buy);
				if(buy.getInventoryLocation()>BSShop.ROWS_LIMIT*9){
					ClassManager.manager.getBugFinder().warn("Unable to add shop-item '"+buy.getName()+"' to shop '"+shop.getShopName()+"': Highest possible inventory location of "+(BSShop.ROWS_LIMIT*9)+" can't be exceeded!");
				}else{
					inventory.setItem(buy.getInventoryLocation(), item);
				}
			}

		}
		holder.setItems(locs);
		return inventory;
	}



}
