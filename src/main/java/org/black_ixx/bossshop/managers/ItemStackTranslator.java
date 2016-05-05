package org.black_ixx.bossshop.managers;

import java.util.List;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.misc.StringManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemStackTranslator {



	public ItemStack translateItemStack(BSBuy buy, BSShop shop, ItemStack item, Player target){
		if(item!=null){
			if(item.hasItemMeta()){
				ItemMeta meta = item.getItemMeta();

				//Normal itemdata
				if(meta.hasDisplayName()){
					meta.setDisplayName(ClassManager.manager.getStringManager().transform(meta.getDisplayName(), buy, shop, target));
				}

				if(meta.hasLore()){
					List<String> lore = meta.getLore();
					for(int i = 0; i < lore.size(); i++){
						lore.set(i, ClassManager.manager.getStringManager().transform(lore.get(i), buy, shop, target));
					}
					meta.setLore(lore);
				}

				//Skull itemdata
				if(meta instanceof SkullMeta){
					SkullMeta skullmeta = (SkullMeta) meta;
					if(skullmeta.hasOwner()){
						skullmeta.setOwner(ClassManager.manager.getStringManager().transform(skullmeta.getOwner(), buy, shop, target));
					}
				}


				item.setItemMeta(meta);
			}
		}
		return item;
	}

	public String getFriendlyText(List<ItemStack> items){
		if(items!=null){
			String msg = "";
			int x = 0;
			for (ItemStack i : items) {
				x++;
				String material = i.getType().name().toLowerCase();
				material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
				msg += "" + i.getAmount() + " " + material + (x < items.size() ? ", " : "");
			}
			return msg;
		}
		return null;
	}


	public boolean checkItemStackForFeatures(ItemStack item){ //Returns true if this would make a shop customizable
		boolean b = false;
		if(item!=null){
			if(item.hasItemMeta()){
				StringManager s = ClassManager.manager.getStringManager();
				ItemMeta meta = item.getItemMeta();

				//Normal itemdata
				if(meta.hasDisplayName()){
					if(s.checkStringForFeatures(meta.getDisplayName())){
						b = true;
					}
				}

				if(meta.hasLore()){
					List<String> lore = meta.getLore();
					for(int i = 0; i < lore.size(); i++){
						if(s.checkStringForFeatures(lore.get(i))){
							b = true;
						}
					}
				}

				//Skull itemdata
				if(meta instanceof SkullMeta){
					SkullMeta skullmeta = (SkullMeta) meta;
					if(skullmeta.hasOwner()){
						if(s.checkStringForFeatures(skullmeta.getOwner())){
							b = true;
						}
					}
				}
			}
		}
		return b;
	}



	public boolean isItemList(Object o){
		if(o instanceof List<?>){
			return isItemList((List<?>) o);
		}
		return false;
	}
	public boolean isItemList(List<?> list){
		if(list!=null){
			if(list.size()>=1){
				Object first = list.get(0);
				if(first instanceof ItemStack){
					return true;
				}
			}
		}
		return false;
	}
}
