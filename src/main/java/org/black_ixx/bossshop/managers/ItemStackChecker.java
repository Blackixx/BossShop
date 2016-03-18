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


	public boolean inventoryContainsItem(Player p, ItemStack i){
		if (getAmountOfSameItems(p, i)>=i.getAmount()){
			return true;
		}
		return false;
	}

	public void takeItem(ItemStack i, Player p){
		int a = 0;
		int slot = -1;

		if (!i.getEnchantments().isEmpty()){
			for (ItemStack s : p.getInventory().getContents()){
				slot++;
				if (s!=null){

					if (!canSell(p, s, slot)){
						continue;
					}

					if (s.getType()==i.getType()){
						if (sameDurability(i, s)){
							if (!s.getEnchantments().isEmpty()){
								if (containsSameEnchantments(i, s)){
									a=a+s.getAmount();
									//p.getInventory().remove(s);
									remove(p, s);
								}}
						}
					}
				}
			}
			a=a-i.getAmount();
			if (a>0){
				addItem(i, p, a);
			}
			return;
		}

		for (ItemStack s : p.getInventory().getContents()){
			slot++;
			if (s!=null){

				if (!canSell(p, s, slot)){
					continue;
				}

				if (s.getType()==i.getType()){
					if (i.getDurability()==s.getDurability()){
						a=a+s.getAmount();
						//p.getInventory().remove(s);
						remove(p, s);
					}
				}
			}
		}
		a=a-i.getAmount();
		if (a>0){
			addItem(i, p, a);
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


	private boolean containsSameEnchantments(ItemStack i, ItemStack s){
		for (Enchantment e : i.getEnchantments().keySet()){
			if (!s.containsEnchantment(e)){
				return false;
			}
			if (s.getEnchantmentLevel(e)<i.getEnchantmentLevel(e)){
				return false;
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

	private int getAmountOfSameItems(Player p, ItemStack i){
		int a = 0;
		int slot = -1;

		if (!i.getEnchantments().isEmpty()){
			for (ItemStack s : p.getInventory().getContents()){
				slot++;
				if (s!=null){

					if (!canSell(p, s, slot)){
						continue;
					}

					if (s.getType()==i.getType()){
						if (sameDurability(i, s)){
							if (!s.getEnchantments().isEmpty()){
								if (containsSameEnchantments(i, s)){
									a=a+s.getAmount();
								}}
						}
					}
				}
			}
			return a;
		}

		for (ItemStack s : p.getInventory().getContents()){
			slot++;
			if (s!=null){

				if (!canSell(p, s, slot)){
					continue;
				}

				if (s.getType()==i.getType()){
					if (i.getDurability()==s.getDurability()){
						a=a+s.getAmount();
					}
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

	public void tellPlayerItemsNeeded(List<ItemStack> items, Player p){
		for (ItemStack i : items){
			if (!inventoryContainsItem(p, i)){
				p.sendMessage(ChatColor.RED+"- "+i.getAmount()+" "+i.getType().name());
			}else{
				p.sendMessage(ChatColor.GREEN+"- "+i.getAmount()+" "+i.getType().name());
			}


		}
	}

	private boolean isTool(ItemStack i){
		//		String n = i.getType().name().toLowerCase();
		//		if (n.contains("steal")||n.contains("wood")||n.contains("stone")||n.contains("iron")||n.contains("gold")||n.contains("diamond")||n.contains("chain")||n.contains("leather")){
		//			return true;
		//		}
		return false;
	}

	private boolean canSell(Player p, ItemStack i, int slot){
		if(slot<INVENTORY_SLOT_START || slot>INVENTORY_SLOT_END){
			return false;
		}

		return isTool(i)?i.getDurability()==0:true;
	}

	public boolean isValidEnchantment(ItemStack item, Enchantment enchantment, int level){
		try{
			item.clone().addEnchantment(enchantment, level);
		}catch(Exception e){
			return false;
		}
		return true;
	}


}
