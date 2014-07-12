package org.black_ixx.bossshop.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class BSAddonConfig {

	private Plugin plugin;
	private File file;
	private YamlConfiguration config;
	boolean isSaving = false;

	public BSAddonConfig(Plugin plugin, String file_name) {
		this.plugin = plugin;
		file = new File(plugin.getDataFolder().getAbsolutePath() + "/" + file_name + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void save() {
		if (isSaving)
			return;
		isSaving = true;
		
		try {
			config.save(file);
		} catch (IOException e1) {
			plugin.getLogger().warning("File I/O Exception on saving " + file.getName());
			e1.printStackTrace();
		}
		
		isSaving = false;
	}
	
	public void saveAsync() {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				save();
			}
			
		});
	}

	public void reload() {
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}

}
