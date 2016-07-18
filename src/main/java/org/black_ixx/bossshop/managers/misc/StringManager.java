package org.black_ixx.bossshop.managers.misc;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class StringManager {

	public String transform(String s){
		if(s==null){
			return null;
		}
		s = s.replace("[<3]", "❤");
		s = s.replace("[*]", "★");
		s = s.replace("[**]", "✹");
		s = s.replace("[o]", "●");
		s = s.replace("[v]", "✔");
		s = s.replace("[+]", "♦");
		s = s.replace("[x]", "✦");
		s = s.replace("[%]", "♠");
		s = s.replace("[%%]", "♣");
		s = s.replace("[radioactive]", "☢");
		s = s.replace("[peace]", "☮");
		s = s.replace("[moon]", "☾");
		s = s.replace("[crown]", "♔");
		s = s.replace("[snowman]", "☃");
		s = s.replace("[tools]", "⚒");
		s = s.replace("[swords]", "⚔");
		s = s.replace("[note]", "♩ ");
		s = s.replace("[block]", "█");
		s = s.replace("[triangle]", "▲");
		s = s.replace("[warn]", "⚠");
		s = s.replace("[left]", "←");
		s = s.replace("[right]", "→");
		s = s.replace("[up]", "↑");
		s = s.replace("[down]", "↓");

		s = ChatColor.translateAlternateColorCodes('&', s); 

		s = s.replace("[and]", "&");
		s = s.replace("[colon]", ":");
		return s;
	}


	public String transform(String s, BSBuy item, BSShop shop, Player target){
		if(s==null){
			return null;
		}
		if(item!=null){
			s=item.transformMessage(s, shop, target);
		}
		if(shop!=null){
			if(shop.getShopName() != null){
				s = s.replace("%shop%", shop.getShopName());
			}
			if(shop.getDisplayName() != null){
				s = s.replace("%shopdisplayname%", shop.getDisplayName());
			}
		}
		return transform(s, target);	
	}

	public String transform(String s, Player target){
		if(s==null){
			return null;
		}

		if(target!=null){
			s=s.replace("%name%", target.getName()).replace("%player%", target.getName()).replace("%target%", target.getName());
			s=s.replace("%displayname%", target.getDisplayName());

			if(s.contains("%balance%") && ClassManager.manager.getVaultHandler() != null){
				double balance = MathTools.round(ClassManager.manager.getVaultHandler().getEconomy().getBalance(target.getName()), 2);
				s=s.replace("%balance%", MathTools.displayDouble(balance, 2));
			}
			if(s.contains("%balancepoints%") && ClassManager.manager.getPointsManager() != null){
				int balance_points = ClassManager.manager.getPointsManager().getPoints(target);
				s=s.replace("%balancepoints%",  MathTools.displayDouble(balance_points, 0));
			}


			if(ClassManager.manager.getPlaceholderHandler()!=null){
				s = ClassManager.manager.getPlaceholderHandler().transformString(s, target);
			}
		}

		return transform(s);
	}


	public boolean checkStringForFeatures(String s){ //Returns true if this would make a shop customizable
		boolean b = false;		
		if(s!=null){
			if(s.contains("%balance%")){
				ClassManager.manager.getSettings().setBalanceVariableEnabled(true);
				ClassManager.manager.getSettings().setVaultEnabled(true);
				ClassManager.manager.getSettings().setMoneyEnabled(true);
				b = true;
			}

			if(s.contains("%balancepoints%")){
				ClassManager.manager.getSettings().setBalancePointsVariableEnabled(true);
				ClassManager.manager.getSettings().setPointsEnabled(true);
				b = true;
			}

			if(s.contains("%name%") || s.contains("%player%")){
				b = true;
			}

			if(ClassManager.manager.getPlaceholderHandler()!=null){
				if(ClassManager.manager.getPlaceholderHandler().containsPlaceholder(s)){
					b = true;
				}
			}
		}
		return b;
	}


}
