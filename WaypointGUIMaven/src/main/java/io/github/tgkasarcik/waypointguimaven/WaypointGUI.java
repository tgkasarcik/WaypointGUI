package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * Waypoint GUI plugin for Minecraft Servers running Spigot.
 * 
 * @author T. Kasarcik
 *
 */
public class WaypointGUI extends JavaPlugin {

	/**
	 * Configuration file
	 */
	public static FileConfiguration config;

	/**
	 * Data file
	 */
	public static DataManager data;

	@Override
	public void onEnable() {

		/*
		 * Initialize config.yml file
		 */
		config = this.getConfig();
		this.saveDefaultConfig();
		getLogger().info("config.yml successfully initialized!");
		
		/*
		 * Initialize data.yml file.
		 */
		data = new DataManager(this);
		data.saveDefaultConfig();
		getLogger().info("data.yml successfully initialized!");

		/*
		 * Initialize Event Handling.
		 */
		Bukkit.getServer().getPluginManager().registerEvents(new GUIEventHandler(this), this);

		/*
		 * Initialize WaypointManager class.
		 */
		WaypointManager.initialize();
		getLogger().info("Waypoint Manager successfully initialized!");

		/*
		 * In reloads, re-initialize Waypoint GUIs for all online players. During
		 * regular startups, this will be skipped, since there won't be any online
		 * players.
		 */
		for (Player p : Bukkit.getOnlinePlayers()) {
			WaypointManager.createGUI(p);
			getLogger().info("Waypoint GUIs loaded for all online players!");
		}

		/*
		 * Initialize command execution
		 */
		CommandExecutor commandExecutor = new MainCommandExecutor(this);
		this.getCommand("w").setExecutor(commandExecutor);
		this.getCommand("waypoint").setExecutor(commandExecutor);

		/*
		 * Initialize custom tab completion.
		 */
		TabCompleter tabComp = new CustomTabCompleter();
		this.getCommand("waypoint").setTabCompleter(tabComp);

	}

	@Override
	public void onDisable() {

		getLogger().info("Successfully closed");

	}
}
