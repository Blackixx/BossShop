package org.black_ixx.bossshop.managers.features;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.black_ixx.bossshop.misc.MathTools;
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


	public double calculateWithMultiplier(Player p, BSPriceType type, double d){ //Used for prices
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					d*=m.getMultiplier();		
				}
			}
		}
		return MathTools.round(d, 2);
	}

	public int calculateWithMultiplier(Player p, BSPriceType type, int d){ //Used for prices
		double x = d;
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					x*=m.getMultiplier();			
				}
			}
		}
		return (int)x;
	}

	public int calculateRewardWithMultiplier(Player p, BSPriceType type, int d){ //Used for reward; Works the other way around
		double x = d;
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					x/=m.getMultiplier();			
				}
			}
		}
		return (int) x;
	}
	
	public double calculateRewardWithMultiplier(Player p, BSPriceType type, double d){ //Used for reward; Works the other way around
		for(BSMultiplier m : multipliers){
			if(m.getType()==type){
				if(m.hasPermission(p)){
					d/=m.getMultiplier();			
				}
			}
		}
		return MathTools.round(d, 2);
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
