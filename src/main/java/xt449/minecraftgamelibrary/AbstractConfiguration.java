package xt449.minecraftgamelibrary;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
abstract class AbstractConfiguration {

	private final String filePath;
	private File file;

	final Plugin plugin;
	YamlConfiguration config;

	AbstractConfiguration(Plugin plugin, String filePath) {
		this.plugin = plugin;
		this.filePath = filePath;
	}

	abstract void setDefaults();

	abstract void readValues();

	final void initialize() {
		file = new File(plugin.getDataFolder(), filePath);
		config = YamlConfiguration.loadConfiguration(file);

		// Folder Setup:
		if(!plugin.getDataFolder().exists()) {
			try {
				if(!plugin.getDataFolder().mkdirs()) {
					throw new Exception();
				}
			} catch(Exception exc) {
				plugin.getLogger().warning("Unable to create path to " + filePath + " configuration!");
			}
		}

		// File Setup:
		if(!file.exists()) {
			try {
				if(!file.createNewFile()) {
					throw new Exception();
				}
			} catch(Exception exc) {
				plugin.getLogger().warning("Unable to create file " + filePath + " configuration!");
			}
		}

		// Config Setup:
		config.options().copyDefaults(true);

		setDefaults();

		readValues();

		// This configuration save is only important for the first plugin
		// load or any paths removed by the user or added in a new version
		save();
	}

	final void save() {
		try {
			config.save(file);
		} catch(IOException exc) {
			plugin.getLogger().warning("Unable to save file " + filePath + " configuration!");
			// TODO: Debug - exc.printStackTrace();
		}
	}
}
