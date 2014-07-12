package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.core.BSBuy;
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
	    s = s.replace("[note]", "♩ ");
		return s.replace("&", "\u00a7");	
	}

	public String transform(String s, BSBuy item){
		s=item.transformMessage(s);
		return transform(s);	
	}

	public String transform(String s, Player target){
		s=s.replace("%name%", target.getName()).replace("%player%", target.getName());
		return transform(s);
	}

}
