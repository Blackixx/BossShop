package org.black_ixx.bossshop.managers.serverpinging;

import java.util.List;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.inventory.ItemStack;

public abstract class BasicConnector {

	private List<String> old_lore;
	private String old_name;
	private ItemStack item;
	private BSShop shop;
	private BSBuy buy;

	public void setOldData(List<String> lore, String name, ItemStack item, BSShop shop, BSBuy buy){
		old_lore=lore;
		old_name=name;
		this.item=item;
		this.shop=shop;
		this.buy=buy;
	}
	
	public ItemStack getItem(){
		return item;
	}
	
	public List<String> getOldLore(){
		return old_lore;
	}
	
	public String getOldName(){
		return old_name;
	}
	
	public BSShop getShop(){
		return shop;
	}
	
	public BSBuy getShopItem(){
		return buy;
	}
	
	
	public abstract String getMotd();
	
	public abstract String getPlayerCount();
	
	public abstract boolean isOnline();
	
	public abstract boolean update();
	
	public abstract String getHost();
	
	public abstract int getPort();
	
	
}
