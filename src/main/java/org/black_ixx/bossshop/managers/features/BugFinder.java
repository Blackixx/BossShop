package org.black_ixx.bossshop.managers.features;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BugFinder {
	

	private final BossShop plugin;
	private final String fileName = "BugFinder.yml";
	private final File file;
	private FileConfiguration config = null;
	private SimpleDateFormat formatter = new SimpleDateFormat ("yyyy dd-MM 'at' hh:mm:ss a (E)");

	public BugFinder(final BossShop plugin) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder().getAbsolutePath(), fileName);
		config = YamlConfiguration.loadConfiguration(this.file);
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
			Bukkit.getLogger().severe("[BossShop] Could not save BugFinder config to " + file);
		}
	}
	
	public String getDate(){
		Date dNow = new Date();
		return formatter.format(dNow);
	}
	
	public void addMessage(String message){
		config.set(getDate(), message);
		saveConfig();
	}

	public void warn(String message){
		addMessage(message);
		Bukkit.getLogger().warning("[BossShop] "+message);
	}
	public void severe(String message){
		addMessage(message);
		Bukkit.getLogger().severe("[BossShop] "+message);
	}
	

}
