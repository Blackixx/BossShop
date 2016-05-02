package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.enums.BSPriceType;
import org.bukkit.entity.Player;

public class BSMultiplier {

	private String permission = "Permission.Node";
	private BSPriceType type = BSPriceType.Nothing;
	private double multiplier = 1.0;
	

	public BSMultiplier(BossShop plugin, String config_line){
		String[] parts = config_line.split(":",3);

		if(parts.length!=3){
			plugin.getClassManager().getBugFinder().warn("Invalid Multiplier Group Line... \""+config_line+"\"! It should look like this: \"Permission.Node:<type>:<multiplier>\"");
			return;
		}

		String permission = parts[0].trim();
		
		if(parts[1].trim().equalsIgnoreCase("<type>")){
			return;
		}
		
		
		
		BSPriceType type = null;
		for(BSPriceType t : BSPriceType.values()){
			if(t.name().equalsIgnoreCase(parts[1].trim())){
				type=t;
			}
		}

		if(type==null|| type == BSPriceType.Item || type == BSPriceType.Nothing || type == BSPriceType.Free){
			plugin.getClassManager().getBugFinder().warn("Invalid Multiplier Group Line... \""+config_line+"\"! It should look like this: \"Permission.Node:<type>:<multiplier>\". '"+parts[1].trim()+"' is no valid PriceType... you can use: 'Money', 'Points' and 'EXP'!");
			return;	
		}
		double multiplier = 1.0;
		try{
			multiplier = Double.parseDouble(parts[2].trim());
		}catch(Exception e){
			plugin.getClassManager().getBugFinder().warn("Invalid Multiplier Group Line... \""+config_line+"\"! It should look like this: \"Permission.Node:<type>:<multiplier>\". '"+parts[2].trim()+"' is no valid multiplier... What you can use instead (examples): 0.25, 0.3, 0.75, 1.0, 1.5, 2.0 etc.!");
			return;
		}
		
		setup(permission, type, multiplier);

	}
	
	public BSMultiplier(String permission, BSPriceType type, double multiplier){
		setup(permission, type, multiplier);
	}

	public void setup (String permission, BSPriceType type, double multiplier){
		this.permission=permission;
		this.type=type;
		this.multiplier=multiplier;
	}
	
	public boolean isValid(){
		return type!=BSPriceType.Nothing;
	}
	
	public BSPriceType getType(){
		return type;
	}
	
	public double getMultiplier(){
		return multiplier;
	}
	
	public String getPermission(){
		return permission;
	}
	
	public boolean hasPermission(Player p){
		return p.hasPermission(permission);
	}
	
	public double calculateWithMultiplier(double d){
		return d*multiplier;
	}

	public int calculateWithMultiplier(int d){
		return (int) (d*multiplier);
	}
	
	
	
	
	
}
