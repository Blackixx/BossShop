package org.black_ixx.bossshop.managers;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemStackCreator {

	@SuppressWarnings("deprecation")
	public ItemStack createItemStack(List<String> itemData){
		ItemStack i = new ItemStack(Material.STONE);

		for (String x : itemData){

			String parts[] = x.split(":",2);
			String s = parts[0].trim();
			String a = parts[1].trim();

			if (s.equalsIgnoreCase("id")){
				a=stringFix(a);

				if(a.contains(":")){
					String pa[] = a.split(":", 2);
					String type = pa[0].trim();
					String dur = pa[1].trim();
					try{
						short dura = Short.parseShort(dur);
						i.setDurability(dura);
						a=type;						
					}catch(Exception e){
						//Do not change anything
					}
				}
				
				if (!isInteger(a)){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (id) needs to be a number!");
					continue;
				}
				if (Material.getMaterial(Integer.parseInt(a))==null){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (id) is no valid Material!");
					continue;
				}
				i.setTypeId(Integer.parseInt(a));
				continue;
			}


			if (s.equalsIgnoreCase("type")){
				a=stringFix(a);
				
				if(a.contains(":")){
					String pa[] = a.split(":", 2);
					String type = pa[0].trim();
					String dur = pa[1].trim();
					try{
						short dura = Short.parseShort(dur);
						i.setDurability(dura);
						a=type;						
					}catch(Exception e){
						//Do not change anything
					}
				}
				a=a.toUpperCase();
				
				if (Material.getMaterial(a)==null){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (type) is no valid Material!");
					continue;
				}
				i.setType(Material.getMaterial(a));
				continue;
			}


			if (s.equalsIgnoreCase("amount")){
				a=stringFix(a);
				if (!isInteger(a)){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (amount) needs to be a number!");
					continue;
				}
				i.setAmount(Integer.parseInt(a));
				continue;
			}


			if (s.equalsIgnoreCase("durability")||s.equalsIgnoreCase("damage")){
				a=stringFix(a);
				if (!isShort(a)){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (durability) needs to be a number!");
					continue;
				}
				i.setDurability(Short.parseShort(a));
				continue;
			}

			if (s.equalsIgnoreCase("name")){
				ItemMeta meta = i.getItemMeta();
				meta.setDisplayName(ClassManager.manager.getStringManager().transform(a));

				//ShopCustomizer
				if(a.contains("%balance%")){
					ClassManager.manager.getSettings().setInventoryCustomizingBalanceEnabled(true);
				}
				if(a.contains("%balancepoints%")){
					ClassManager.manager.getSettings().setInventoryCustomizingBalancePointsEnabled(true);
				}
				//ShopCustomizer

				i.setItemMeta(meta);
				continue;
			}

			if (s.equalsIgnoreCase("lore")){
				ItemMeta meta = i.getItemMeta();

				//ShopCustomizer
				if(a.contains("%balance%")){
					ClassManager.manager.getSettings().setInventoryCustomizingBalanceEnabled(true);
				}
				if(a.contains("%balancepoints%")){
					ClassManager.manager.getSettings().setInventoryCustomizingBalancePointsEnabled(true);
				}
				//ShopCustomizer

				String par[] = a.split("#");
				List<String> lore = new ArrayList<String>();
				for (String b : par){
					lore.add(ClassManager.manager.getStringManager().transform(b));
				}
				meta.setLore(lore);
				i.setItemMeta(meta);
				continue;
			}

			if (s.equalsIgnoreCase("enchantment")){
				a=stringFix(a);
				try{
					String par[] = a.split("#");
					String eType = par[0].trim().toUpperCase();
					String eLvl = par[1].trim();

					Enchantment e = Enchantment.DURABILITY;
					if (isInteger(eType)){
						e=Enchantment.getById((Integer)Integer.parseInt(eType));
					}else{
					e = Enchantment.getByName(eType);
					}

					if (e == null){
						ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (enchantment) contains mistakes: Invalid Enchantment name");
						continue;
					}


					if(i.getType()==Material.ENCHANTED_BOOK) {
						EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
						meta.addStoredEnchant(e, Integer.parseInt(eLvl), true);
						//meta.addEnchant(e, Integer.parseInt(eLvl), true);
						i.setItemMeta(meta);
						continue;
					}




					i.addUnsafeEnchantment(e, Integer.parseInt(eLvl));
				} catch (Exception e){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (enchantment) contains mistakes!");
				}
				continue;
			}

			if (s.equalsIgnoreCase("enchantmentid")){
				a=stringFix(a);
				try{
					String par[] = a.split("#");
					String eType = par[0].trim();
					String eLvl = par[1].trim();
					Enchantment e = Enchantment.getById(Integer.parseInt(eType));

					if (e == null){
						ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (enchantmentid) contains mistakes: Invalid Enchantment id");
						continue;
					}


					if(i.getType()==Material.ENCHANTED_BOOK) {
						EnchantmentStorageMeta meta = (EnchantmentStorageMeta) i.getItemMeta();
						meta.addStoredEnchant(e, Integer.parseInt(eLvl), true);
						//meta.addEnchant(e, Integer.parseInt(eLvl), true);
						i.setItemMeta(meta);
						continue;
					}					



					i.addUnsafeEnchantment(e, Integer.parseInt(eLvl));
				} catch (Exception e){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (enchantmentid) contains mistakes!");
				}
				continue;
			}

			if (s.equalsIgnoreCase("color")){
				a=stringFix(a);
				Color c = Color.AQUA;
				try{
					String par[] = a.split("#");
					String c1 = par[0].trim();
					String c2 = par[1].trim();
					String c3 = par[2].trim();
					Integer i1 = Integer.parseInt(c1);
					Integer i2 = Integer.parseInt(c2);
					Integer i3 = Integer.parseInt(c3);

					c = Color.fromRGB(i1, i2, i3);				

				} catch (Exception e){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (color) contains mistakes! A color Line should look like this: \"color:<red number>#<green number>#<blue number>\". You can find a list of RGB Colors here: http://www.farb-tabelle.de/de/farbtabelle.htm");
					continue;
				}

				if (!(i.getItemMeta() instanceof Colorable)&!(i.getItemMeta() instanceof LeatherArmorMeta)){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: The item "+a+" (Type "+i.getType()+")"+"(color) can't be colored/dyed! Tip: Always define the Material Type before you color the item!");
					continue;
				}

				if (i.getItemMeta()instanceof Colorable){				
					Colorable ic = (Colorable) i.getItemMeta();
					DyeColor color = DyeColor.getByColor(c);
					ic.setColor(color);
					i.setItemMeta((ItemMeta) ic);
					continue;}

				if (i.getItemMeta()instanceof LeatherArmorMeta){				
					LeatherArmorMeta ic = (LeatherArmorMeta) i.getItemMeta();
					ic.setColor(c);
					i.setItemMeta((ItemMeta) ic);
					continue;}


				continue;
			}
			if (s.equalsIgnoreCase("playerhead")){
				if (i.getType()!=Material.SKULL_ITEM){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (playerhead) You can't use \"PlayerHead\" on items which are not skulls...");
					continue;
				}

				SkullMeta meta = (SkullMeta) i.getItemMeta();
				meta.setOwner(a);
				i.setItemMeta(meta);				
				continue;
			}

			if (s.equalsIgnoreCase("potioneffect")){
				a=stringFix(a);
				if (i.getType()!=Material.POTION){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (potioneffect) You can't add PotionEffects to items which are not potions...");
					continue;
				}
				PotionMeta meta = (PotionMeta) i.getItemMeta();

				try{
					String par[] = a.split("#");
					String pType = par[0].trim().toUpperCase();
					String pLvl = par[1].trim();
					String pTime = par[2].trim();

					PotionEffectType type = null;
					if (isInteger(pType)){
						type = PotionEffectType.getById(Integer.parseInt(pType));
					}else{
						type = PotionEffectType.getByName(pType);
					}

					meta.addCustomEffect(new PotionEffect(type, getTicksFromSeconds(pTime), Integer.parseInt(pLvl)), true);

					i.setItemMeta(meta);

				} catch (Exception e){
					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (potioneffect) contains mistakes!");
				}

				continue;
			}
			
			
			

			if (s.equalsIgnoreCase("guishopmanageritem")||s.equalsIgnoreCase("gsmitem")||s.equalsIgnoreCase("guishopmanager")||s.equalsIgnoreCase("gsm")){
				ItemStack gi = new GuiShopManagerManager().getGuiShopManagerItem(a);
				if(gi==null){
					continue;
				}
				i=gi; 
				continue;
			}
			
			
			
			
			
			

			//			if (s.equalsIgnoreCase("splashpotion")){
			//
			//				boolean b = true;
			//				if (a.equalsIgnoreCase("false")){
			//					b=false;
			//				}
			//				if (i.getType()!=Material.POTION){
			//					ClassManager.manager.getBugFinder().severe("Mistake in Config: "+a+" (splashpotion) You can't set items which are not potions to SplashPotions...");
			//					continue;
			//				}
			//				Potion splash = new Potion(1);
			//				splash.setSplash(b);
			//				splash.apply(i);
			//				//TODO Funktioniert noch nicht richtig
			//				continue;
			//			}

		}



		return i;
	}

	private boolean isInteger(String str)  
	{  
		try  
		{  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}


	private boolean isShort(String str)  
	{  
		try  
		{  
			Short.parseShort(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}
	
	private String stringFix(String s){
		if (s.contains(" ")){
			s=s.replaceAll(" ", "");
		}
		return s;
	}
	
	private int getTicksFromSeconds(String s){
		try{
			Double d = Double.parseDouble(s);
			return (int) (d*20);			
		}catch(Exception e){
		}
		
		try{
			Integer i = Integer.parseInt(s);
			return (int) (i*20);
		}catch (Exception e){
		}
		
		return 0;
	}

}
