package org.black_ixx.bossshop.core;

import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BSShopHolder implements InventoryHolder{

	public BSShopHolder(BSShop shop, HashMap<Integer, BSBuy> items){
		this.shop=shop;
		this.items=items;
	}
	
	public BSShopHolder(BSShop shop){
		this.shop=shop;
	}
	
	
	private BSShop shop;
	private HashMap<Integer, BSBuy> items; 
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
	public BSBuy getShopItem(int i){
		return items.get(i);
	}
	
	
	public BSShop getShop(){
		return shop;
	}
	
	public void setItems(HashMap<Integer, BSBuy> items){
		this.items=items;
	}
	
	

}
