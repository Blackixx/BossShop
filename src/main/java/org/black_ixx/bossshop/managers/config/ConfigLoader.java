package org.black_ixx.bossshop.managers.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLoader {

	public static YamlConfiguration loadConfiguration(File file) throws InvalidConfigurationException{
		Validate.notNull(file, "File cannot be null");

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(file);
		} catch (FileNotFoundException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
		}

		return config;
	}

}
