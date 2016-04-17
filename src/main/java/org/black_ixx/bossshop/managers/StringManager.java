package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.core.BSBuy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class StringManager {

	public String transform(String s){
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

	public String transform(String s, BSBuy item){
		s=item.transformMessage(s, null, null);
		return transform(s);	
	}

	public String transform(String s, Player target){
		if(ClassManager.manager.getPlaceholderHandler()!=null){
			s = ClassManager.manager.getPlaceholderHandler().transformString(s, target);
		}
		s=s.replace("%name%", target.getName()).replace("%player%", target.getName());
		return transform(s);
	}

}
