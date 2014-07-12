package org.black_ixx.bossshop.managers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ISBackup {

	public ISBackup(){
	}


	public boolean inventoryContainsItem(Player p, ItemStack i){
		
		if (i.getEnchantments().isEmpty()&&i.getDurability()==0){
			if (p.getInventory().contains(i.getType(),i.getAmount())){
				return true;
			}
			return false;
		}
		
		if (getAmountOfSameItems(p, i)>=i.getAmount()){
			return true;
		}
		return false;
		
		
	}

	public void takeItem(ItemStack i, Player p){

		int a = 0;

		if (i.getEnchantments().isEmpty()&&i.getDurability()==0){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
					if (s.getType()==i.getType()){
							a=a+s.getAmount();
							//p.getInventory().remove(s);
							remove(p, s);
					}
				}
			}
			a=a-i.getAmount();
			if (a>0){
				addItem(i, p, a);
			}
			return;
		}
		

		if (!i.getEnchantments().isEmpty()&&i.getDurability()!=0){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
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

		if (!i.getEnchantments().isEmpty()){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
					if (s.getType()==i.getType()){
						if (!s.getEnchantments().isEmpty()){
							if (containsSameEnchantments(i, s)){
								a=a+s.getAmount();
								//p.getInventory().remove(s);
								remove(p, s);
							}}
					}
				}
			}
			a=a-i.getAmount();
			if (a>0){
				addItem(i, p, a);
			}
			return;
		}

		if (i.getDurability()!=0){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
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

		

	}
	
	private void remove(Player p, ItemStack toR){
		if (toR.hasItemMeta()){
		    ItemMeta m = toR.getItemMeta();
		    m.setDisplayName(null);
		    toR.setItemMeta(m);
			toR.setItemMeta(null);
		}
		//p.getInventory().remove(toR);
		p.getInventory().removeItem(new ItemStack(toR.getType(),toR.getAmount()));
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


		if (!i.getEnchantments().isEmpty()&&i.getDurability()!=0){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
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

		if (!i.getEnchantments().isEmpty()){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
					if (s.getType()==i.getType()){
						if (!s.getEnchantments().isEmpty()){
							if (containsSameEnchantments(i, s)){
								a=a+s.getAmount();
							}}
					}
				}
			}
			return a;
		}

		if (i.getDurability()!=0){
			for (ItemStack s : p.getInventory().getContents()){
				if (s!=null){
					if (s.getType()==i.getType()){
						if (i.getDurability()==s.getDurability()){
							a=a+s.getAmount();
						}
					}
				}
			}
			return a;
		}

		for (ItemStack s : p.getInventory().getContents()){
			if (s!=null){
				if (s.getType()==i.getType()){
					a=a+s.getAmount();
				}
			}
		}
		return a;

	}


	private void addItem(ItemStack i, Player p, int amount){
		
	ItemStack s = new ItemStack(i.getType(),amount,i.getDurability());
	if (!i.getEnchantments().isEmpty()){
		s.addEnchantments(i.getEnchantments());
	}
	p.getInventory().addItem(s);
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


}
