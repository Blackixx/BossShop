package org.black_ixx.bossshop.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.BSEnums.BSPriceType;
import org.bukkit.entity.Player;


public class MultiplierHandler {

	public MultiplierHandler(BossShop plugin){
		if(plugin.getConfig().getBoolean("MultiplierGroups.Enabled")==false){
			return;
		}
		List<String> lines = plugin.getConfig().getStringList("MultiplierGroups.List");
		if(lines==null){
			return;
		}
		setup(plugin, lines);
	}

	private Set<BSMultiplier> multipliers = new HashSet<BSMultiplier>();

	public void setup(BossShop plugin, List<String> config_lines){
		multipliers.clear();
		for (String s : config_lines){
			BSMultiplier m = new BSMultiplier(plugin, s);
			if(m.isValid()){
				multipliers.add(m);
			}
		}
	}


	public double calculateWithMultiplier(Player p, BSPriceType type, double d){
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					d=d*m.getMultiplier();		
				}
			}
		}
		return d;
	}

	public int calculateWithMultiplier(Player p, BSPriceType type, int d){
		double x = d;
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					x=x*m.getMultiplier();			
				}
			}
		}
		return (int)x;
	}



	public Set<BSMultiplier> getMultipliers(){
		return multipliers;
	}
	
	public boolean hasMultipliers(){
		if(multipliers==null){
			return false;
		}
		return !multipliers.isEmpty();
	}

}
