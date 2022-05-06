package io.github.tgkasarcik.waypointguimaven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Data Manager class copied from online tutorial.
 * 
 * @author T. Kasarcik, et al.
 *
 */

public class DataManager {

	/*
	 * Private members --------------------------------------------------------
	 */

	/**
	 * Reference to instance of main class of plugin.
	 */
	private WaypointGUI plugin;

	/**
	 * {@code FileConfiguration} object.
	 */
	private FileConfiguration dataConfig = null;

	/**
	 * Data file.
	 */
	private File configFile = null;

	/*
	 * Constructor ------------------------------------------------------------
	 */

	public DataManager(WaypointGUI plugin) {
		this.plugin = plugin;
	}

	/*
	 * Instance methods -------------------------------------------------------
	 */

	/**
	 * Load input stream from {@code configFile}.
	 */
	public void reloadConfig() {
		if (this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
		}

		this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

		InputStream defaultStream = this.plugin.getResource("data.yml");
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.dataConfig.setDefaults(defaultConfig);
		}
	}

	/**
	 * Returns the {@code FileConfiguration} object of {@code this}.
	 * 
	 * @return this.dataConfig
	 */
	public FileConfiguration getConfig() {
		if (this.dataConfig == null) {
			reloadConfig();
		}

		return this.dataConfig;
	}

	/**
	 * Attempts to save {@code configFile} and prints error message to console if it
	 * is unsuccessful.
	 */
	public void saveConfig() {
		if (this.dataConfig == null || this.configFile == null) {
			return;
		}

		try {
			this.getConfig().save(this.configFile);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save data to " + this.configFile, e);
		}
	}

	/**
	 * Save {@code configFile} or create it if it does not exist.
	 */
	public void saveDefaultConfig() {
		if (this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
		}

		if (!this.configFile.exists()) {
			this.plugin.saveResource("data.yml", false);
		}
	}

}
