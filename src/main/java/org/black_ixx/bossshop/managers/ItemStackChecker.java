package org.black_ixx.bossshop.managers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackChecker {

	private final static int INVENTORY_SLOT_START = 0;
	private final static int INVENTORY_SLOT_END = 35;

	public ItemStackChecker(){
	}


	public boolean inventoryContainsItem(Player p, ItemStack i, boolean can_player_sell_item_with_greater_enchants){
		if (getAmountOfSameItems(p, i, can_player_sell_item_with_greater_enchants)>=i.getAmount()){
			return true;
		}
		return false;
	}

	public void takeItem(ItemStack shop_item, Player p, boolean can_player_sell_item_with_greater_enchants){
		int a = 0;
		int slot = -1;

			for (ItemStack player_item : p.getInventory().getContents()){
				slot++;
				if (player_item!=null){					
					if(canSell(p, player_item, shop_item, slot, can_player_sell_item_with_greater_enchants)){
						a+=player_item.getAmount();
						remove(p, player_item);						
						if(a>=shop_item.getAmount()){ //Reached amount. Can stop!
							break;
						}
					}
				}
			}
			
			a=a-shop_item.getAmount();
			if (a>0){
				addItem(shop_item, p, a);
			}
			return;
	}
	private void remove(Player p, ItemStack toR){
		if (toR.hasItemMeta()){
			ItemMeta m = toR.getItemMeta();
			m.setDisplayName(null);
			toR.setItemMeta(m);
			toR.setItemMeta(null);
		}
		p.getInventory().removeItem(new ItemStack(toR.getType(),toR.getAmount(), toR.getDurability()));
		return;

	}

	private int getAmountOfSameItems(Player p, ItemStack shop_item, boolean can_player_sell_item_with_greater_enchants){
		int a = 0;
		int slot = -1;

			for (ItemStack player_item : p.getInventory().getContents()){
				slot++;
				if (player_item!=null){
					if(canSell(p, player_item, shop_item, slot, can_player_sell_item_with_greater_enchants)){
						a+=player_item.getAmount();
					}
				}
			}
			return a;
	}

	private void addItem(ItemStack i, Player p, int amount){
		ItemStack s = new ItemStack(i.getType(),amount,i.getDurability());
		if (!i.getEnchantments().isEmpty()){
			s.addUnsafeEnchantments(i.getEnchantments());
		}

		//Max stack size = 1
		if(i.getMaxStackSize()==1){
			s.setAmount(1);
			for(int x = 0; x < amount; x++) {
				p.getInventory().addItem(s);
			}
			return;
		}

		p.getInventory().addItem(s);
	}

	public void addItemSafe(Player p, ItemStack i){
		if(p.getInventory().firstEmpty()==-1){ //Inventory full
			if(i.getMaxStackSize()==1){ //Max Stack Size == 1
				p.getWorld().dropItem(p.getLocation(), i); // -> Drop Item
				return;
			}
			int free = 0;
			for (ItemStack item : p.getInventory().getContents()){ //Loop durch Inventar
				if(item!=null){
					if(item.getType()==i.getType()&&item.getDurability()==i.getDurability()){ //Selbes Item?
						free+=item.getMaxStackSize()-item.getAmount(); //Freier Platz wird addiert
					}
				}				
			}
			if(free<i.getAmount()){ //Nicht genug Platz? 
				p.getWorld().dropItem(p.getLocation(), i);// Drop Item!
				return;
			}
		}
		p.getInventory().addItem(i);
	}

	public void tellPlayerItemsNeeded(List<ItemStack> items, Player p, boolean can_player_sell_item_with_greater_enchants){
		for (ItemStack i : items){
			if (!inventoryContainsItem(p, i, can_player_sell_item_with_greater_enchants)){
				p.sendMessage(ChatColor.RED+"- "+i.getAmount()+" "+i.getType().name());
			}else{
				p.sendMessage(ChatColor.GREEN+"- "+i.getAmount()+" "+i.getType().name());
			}


		}
	}


	private boolean canSell(Player p, ItemStack player_item, ItemStack shop_item, int slot, boolean can_player_sell_item_with_greater_enchants){
		if(slot<INVENTORY_SLOT_START || slot>INVENTORY_SLOT_END){ //Has to be inside normal inventory
			return false;
		}
		
		if (player_item.getType()!=shop_item.getType()){ //Both need to be the same item type
			return false;
		}
		
		if (!sameDurability(player_item, shop_item)){ //Both need to have the same durability
			return false;
		}

		if(!containsSameEnchantments(shop_item, player_item, can_player_sell_item_with_greater_enchants)){ //Both need to have the same enchants
			return false;
		}


		return true;
	}


	public boolean isValidEnchantment(ItemStack item, Enchantment enchantment, int level){
		try{
			item.clone().addEnchantment(enchantment, level);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	private boolean containsSameEnchantments(ItemStack shop_item, ItemStack player_item, boolean can_player_sell_item_with_greater_enchants){
		for (Enchantment e : shop_item.getEnchantments().keySet()){
			if (!player_item.containsEnchantment(e)){
				return false;
			}
			if (player_item.getEnchantmentLevel(e)<shop_item.getEnchantmentLevel(e)){
				return false;
			}
		}
		if(!can_player_sell_item_with_greater_enchants){
			for (Enchantment e : player_item.getEnchantments().keySet()){
				if (!shop_item.containsEnchantment(e)){
					return false;
				}
				if (shop_item.getEnchantmentLevel(e)<player_item.getEnchantmentLevel(e)){
					return false;
				}
			}
		}
		return true;
	}
	private boolean sameDurability(ItemStack i, ItemStack s){
		if (i.getDurability()==s.getDurability()){
			return true;
		}
		return false;
	}

}
