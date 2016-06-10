package org.black_ixx.bossshop.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MessageHandler {
	private final BossShop plugin;
	private final String fileName = "messages.yml";
	private final File file;
	private FileConfiguration config = null;

	public MessageHandler(final BossShop plugin) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
		config = YamlConfiguration.loadConfiguration(this.file);
		setDefaults();
		reloadConfig();
	}

	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();

		return config;
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(file);
		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			@SuppressWarnings("deprecation")
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	public void saveConfig() {
		try {
			getConfig().save(file);
		} catch (IOException e) {
			ClassManager.manager.getBugFinder().warn("Could not save message config to " + file);
		}
	}

	public void sendMessage(String node, CommandSender sender) {
		sendMessage(node, sender, null, null, null, null);
	}
	
	public void sendMessage(String node, CommandSender sender, String offline_target) {
		sendMessage(node, sender, offline_target, null, null, null);
	}
	
	public void sendMessage(String node, CommandSender sender, Player target) {
		sendMessage(node, sender, null, target, null, null);
	}

	public void sendMessage(String node, CommandSender sender, String offline_target, Player target, BSShop shop, BSBuy item) {
		if (sender != null) {
			
			if (node==null||node==""){
				return;
			}
			
			String message = get(node, target, shop, item);
			
			if (message==null||message==""|| message==" "||message.length()<2){
				return;
			}
			
			if(offline_target != null){
				message = message.replace("%player%", offline_target).replace("%name%", offline_target).replace("%target%", offline_target);
			}

			for (String line : message.split("\n"))
				sender.sendMessage(line);
		}
	}

	public String get(String node) {
		return get(node, null, null, null);
	}

	private String get(String node, Player target, BSShop shop, BSBuy item) {
		return replace(config.getString(node, node), target, shop, item);
	}

	private String replace(String message, Player target, BSShop shop, BSBuy item) {
		return ClassManager.manager.getStringManager().transform(message, item, shop, target);
	}
	
	
	private void setDefaults(){
		config.addDefault("Main.NoPermission", "&cYou are not allowed to do this!");
		config.addDefault("Main.AlreadyBought", "&cYou already purchased that!");
		config.addDefault("Main.ShopNotExisting", "&cThat Shop is not existing...");
		config.addDefault("Main.OpenShop", "&6Opening Shop &c%shop%&6.");
		config.addDefault("Main.OpenShopOtherPlayer", "&6Opening Shop &c%shop% &6for %displayname%&6.");
		config.addDefault("Main.CloseShop", "");
		config.addDefault("Main.CloseShopOtherPlayer", "&6Closed inventory of %displayname%&6.");
		config.addDefault("PlayerNotFound", "&cPlayer %name% not found!");
		config.addDefault("Economy.NoAccount", "&cYou don't have an economy account!");
		config.addDefault("NotEnough.Money", "&cYou don't have enough Money!");
		config.addDefault("NotEnough.Item", "&cYou don't have enough Items!");
		config.addDefault("NotEnough.Points", "&cYou don't have enough Points!");
		config.addDefault("NotEnough.Exp", "&cYou don't have enough EXP!");
		config.addDefault("Enchantment.Invalid", "&cYou can't enchant that item with this enchantment!");
		config.options().copyDefaults(true);
		saveConfig();
	}
	
}