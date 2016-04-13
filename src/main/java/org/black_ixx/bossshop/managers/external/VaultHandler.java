package org.black_ixx.bossshop.managers.external;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.NoEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler {

	public VaultHandler(boolean eco, boolean per){


		Plugin VaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");	     
		if (VaultPlugin == null) {
			ClassManager.manager.getBugFinder().warn("Vault was not found... You need it if you want to work with Permissions, Permission Groups or Money! Get it there: http://dev.bukkit.org/server-mods/vault/");
			return;
		}	     
		Bukkit.getLogger().info("[BossShop] Vault found.");

		if (eco){
			setupEconomy();
		}
		if (per){
			setupPermissions();
		}

	}

	private void setupEconomy(){

		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider == null) {
			ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
			economy = new NoEconomy();
			return;
		}
		economy = economyProvider.getProvider();
	}

	private void setupPermissions(){

		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		if (perms == null) {
			ClassManager.manager.getBugFinder().warn("No Permissions Plugin was found... You need one if you want to work with Permissions or Permission Groups! Get it there: http://plugins.bukkit.org/");
			return;
		}

	}

	///////////////////////////////////////	

	private Permission perms;

	private Economy economy;

	///////////////////////////////////////	

	public Permission getPermission(){
		return perms;
	}

	public Economy getEconomy(){
		return economy;
	}

	///////////////////////////////////////	




}
