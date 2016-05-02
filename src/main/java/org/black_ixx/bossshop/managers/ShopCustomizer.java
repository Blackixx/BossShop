package org.black_ixx.bossshop.managers;

import java.util.HashMap;
import java.util.List;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSDisplayItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopCustomizer {

	public ShopCustomizer(boolean display_balance, boolean display_balance_points, boolean hide_items_no_perms, ClassManager m ){
		bal=display_balance;
		bal_points=display_balance_points;
		hide_noperm=hide_items_no_perms;


		if(bal){

			if(m.getVaultHandler()==null){
				m.getBugFinder().severe("You need Vault in order to display the balance of a player using Items! Get Vault here: http://dev.bukkit.org/bukkit-mods/vault/! Disabling the \"Display Balance\" feature...");
				bal=false;
			}

		}

		if(bal_points){

			if(m.getPointsManager()==null){
				m.getBugFinder().severe("You need a Points Plugin in order to display the points balance of a player using Items! Get PlayerPoints here: http://dev.bukkit.org/bukkit-mods/playerpoints/! Disabling the \"Display Points Balance\" feature...");
				bal_points=false;
			}

		}

	}



	private boolean bal, bal_points, hide_noperm;


	public Inventory createInventory(BSShop shop,HashMap<ItemStack, BSBuy> shop_items, boolean displaying, Player p, ClassManager m){
		BSShopHolder holder = new BSShopHolder(shop);
		Inventory inventory = Bukkit.createInventory(holder, shop.getInventorySize(), shop.getDisplayName());

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
		
		double balance = 0;
		int balance_points = 0;
		if(displaying){
			if(bal){
				balance= m.getVaultHandler().getEconomy().getBalance(p.getName());
				int b = (int) (balance*100);
				balance = (double)(((double)b)/100);
			}
			if(bal_points){
				balance_points= m.getPointsManager().getPoints(p);
			}
		}

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
						ItemMeta meta = item.getItemMeta();
						boolean meta_change = false;

						if(meta.hasDisplayName()){
							String name = item.getItemMeta().getDisplayName();
							String new_name = updateString(name, p, balance, balance_points, shop, buy);
							if(!new_name.equals(name)){
								meta.setDisplayName(new_name);
								meta_change = true;
							}
						}

						if(meta.hasLore()){
							boolean lore_change = false;
							List<String> lore =meta.getLore();

							int co = 0;
							for (String line : lore){
								String new_line = updateString(line, p, balance, balance_points, shop, buy);
								if(!line.equals(new_line)){
									lore.set(co, new_line);
									lore_change = true;
								}
								co++;
							}
							
							if(lore_change){
								meta.setLore(lore);
								meta_change = true;
							}
						}

						if(meta_change){
							item.setItemMeta(meta);
						}
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
	
	
	private String updateString(String s, Player p, double balance, int balance_points, BSShop shop, BSBuy buy){
		if(ClassManager.manager.getPlaceholderHandler()!=null){
			s = ClassManager.manager.getPlaceholderHandler().transformString(s, p);
		}
		if(bal&&s.contains("%balance%")){
			s=s.replace("%balance%",  ""+balance);
		}

		if(bal_points&&s.contains("%balancepoints%")){
			s=s.replace("%balancepoints%",  ""+balance_points);
		}
		if(s.contains("%price%") || s.contains("%reward%")){
			s=buy.transformMessage(s, shop, p);
		}
		return s;
	}

}
