package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.managers.features.PointsManager.PointsPlugin;

public class Settings {
	
	public Settings(){
		
	}
	
	private boolean signs;
	private boolean money;
	private boolean points;
	private boolean vault;
	private boolean permissions;
	private boolean timedCommand;
	private boolean bungeecord;
	private boolean ic_bal;
	private boolean ic_bal_points;
	private boolean ic_hide;
	private PointsPlugin points_plugin;
	private boolean transaction_log;
	private boolean server_pinging;
	private int server_pinging_speed;
	private boolean metrics = true;
	private boolean updater = true;
	private boolean unsafe_enchantments;
	private boolean load_subfolders;
	private boolean auto_download_update;
	private boolean can_players_sell_items_with_greater_enchants;
	private boolean advanced_security;
	
	private String mainshop;
	
	public void setMetricsEnabled(boolean b){
		metrics=b;
	}
	
	public void setSignsEnabled(boolean b){
		signs = b;
		if (ClassManager.manager.getPlugin().getSignListener()!=null){
			ClassManager.manager.getPlugin().getSignListener().setSignsEnabled(b);
		}
	}
	
	
	public void setMoneyEnabled(boolean b){
		money=b;
	}
	
	public void setPointsEnabled(boolean b){
		points = b;
	}
	
	public void setVaultEnabled(boolean b){
		vault=b;
	}
	
	public void setPermissionsEnabled(boolean b){
		permissions = b;
	}
	
	public void setTimedCommandEnabled(boolean b){
		timedCommand=b;
	}
	
	public void setBungeeCordServerEnabled(boolean b){
		bungeecord=b;
	}

	public void setBalanceVariableEnabled(boolean b){
		ic_bal=b;
	}
	
	public void setBalancePointsVariableEnabled(boolean b){
		ic_bal_points=b;
	}
	
	public void setInventoryCustomizingHideEnabled(boolean b){
		ic_hide=b;
	}
	
	public void setPointsPlugin(PointsPlugin p){
		points_plugin=p;
	}
	
	public void setTransactionLogEnabled(boolean b){
		transaction_log=b;
	}

	public void setMainShop(String main){
		mainshop=main;
	}
	
	public void setServerPingingEnabled(boolean b){
		server_pinging=b;
	}
	
	public void setServerPingingSpeed(int i){
		server_pinging_speed=i;
	}
	
	public void setUpdaterEnabled(boolean b){
		updater=b;
	}
	
	public void setUnsafeEnchantmentsEnabled(boolean b){
		unsafe_enchantments=b;
	}
	
	public void setLoadSubfoldersEnabled(boolean b){
		load_subfolders=b;
	}
	
	public void setAutoDownloadUpdateEnabled(boolean b){
		auto_download_update=b;
	}
	
	public void setCanPlayersSellItemsWithGreaterEnchants(boolean b){
		can_players_sell_items_with_greater_enchants=b;
	}
	
	public void setAdvancedSecurityEnabled(boolean b){
		advanced_security = b;
	}
	
	public boolean getMetricsEnabled(){
		return metrics;
	}
	
	public boolean getSignsEnabled(){
		return signs;
	}	
	
	public boolean getMoneyEnabled(){
		return money;
	}
	
	public boolean getPointsEnabled(){
		return points;
	}
	
	public boolean getVaultEnabled(){
		return vault;
	}
	
	public boolean getPermissionsEnabled(){
		return permissions;
	}
	
	public boolean getTimedCommandEnabled(){
		return timedCommand;
	}
	
	public String getMainShop(){
		return mainshop;
	}
	
	public boolean getBungeeCordServerEnabled(){
		return bungeecord;
	}

	public boolean getBalanceVariableEnabled(){//TODO: probably remove this
		return ic_bal;
	}
	
	public boolean getBalancePointsVariableEnabled(){
		return ic_bal_points;
	}
	
	public boolean getInventoryCustomizingHideEnabled(){
		return ic_hide;
	}
	
	public PointsPlugin getPointsPlugin(){
		return points_plugin;
	}
	
	public boolean getTransactionLogEnabled(){
		return transaction_log;
	}
	
	public boolean getServerPingingEnabled(){
		return server_pinging;
	}
	
	public int getServerPingingSpeed(){
		return server_pinging_speed;
	}

	public boolean getUpdaterEnabled(){
		return updater;
	}
	
	public boolean getUnsafeEnchantmentsEnabled(){
		return unsafe_enchantments;
	}
	
	public boolean getLoadSubfoldersEnabled(){
		return load_subfolders;
	}
	
	public boolean getAutoDownloadUpdateEnabled(){
		return auto_download_update;
	}
	
	public boolean getCanPlayersSellItemsWithGreaterEnchants(){
		return can_players_sell_items_with_greater_enchants;
	}
	
	public boolean getAdvancedSecurityEnabled(){
		return advanced_security;
	}
	
	

}
