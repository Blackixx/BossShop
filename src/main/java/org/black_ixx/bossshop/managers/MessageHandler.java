package org.black_ixx.bossshop.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
		sendMessage(node, sender, null);
	}

	public void sendMessage(String node, CommandSender sender, String targetName) {
		if (sender != null) {
			
			if (node==null||node==""){
				return;
			}
			
			String message = get(node, sender.getName(), targetName);
			
			if (message==null||message==""|| message==" "||message.length()<1){
				return;
			}

			for (String line : message.split("\n"))
				sender.sendMessage(line);
		}
	}

	public String get(String node) {
		return get(node, null, null);
	}

	private String get(String node, String playerName, String targetName) {
		return replace(config.getString(node, node), playerName, targetName);
	}

	private String replace(String message, String playerName, String targetName) {
		message = message.replace("&", "\u00a7");
		

		// player
		if (playerName != null)
			message = message.replace("%name%", playerName);

		// target
		if (targetName != null)
			message = message.replace("%target%", targetName);


		// newline
		return message;
	}
	
	
	private void setDefaults(){
		config.addDefault("Main.NoPermission", "&cYou are not allowed to do this!");
		config.addDefault("Main.AlreadyBought", "&cYou already purchased that!");
		config.addDefault("Main.ShopNotExisting", "&cThat Shop is not existing...");
		config.addDefault("Main.OpenShop", "&6Opening the BossShop...");
		config.addDefault("Main.CloseShop", "");
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