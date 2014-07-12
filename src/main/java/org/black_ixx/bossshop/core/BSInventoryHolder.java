package org.black_ixx.bossshop.core;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BSInventoryHolder implements InventoryHolder{

	@Override
	public Inventory getInventory() {
		return null;
	}
	
	private int id = -1;
	
	public BSInventoryHolder(int id){
		this.id=id;
	}
	
	public int getId(){
		return id;
	}

}
