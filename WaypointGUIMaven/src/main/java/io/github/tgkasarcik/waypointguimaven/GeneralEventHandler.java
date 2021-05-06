package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

/**
 * Class to handle all events associated with this plugin.
 * 
 * @author T. Kasarcik
 *
 */

public class GeneralEventHandler implements Listener {

	/**
	 * Reference to class to register events for
	 */
	@SuppressWarnings("unused")
	private final WaypointGUI plugin;

	/**
	 * Boolean variable to store whether teleportation is allowed from within the
	 * WaypointGUI menu.
	 */
	private static boolean teleportationEnabled;

	/**
	 * Local reference to {@code FileConfiguration} object from {@code WaypointGUI}
	 * class.
	 */
	private FileConfiguration config;

	/**
	 * Default constructor
	 * 
	 * @param plugin Reference to class to register events for
	 */
	public GeneralEventHandler(WaypointGUI plugin) {
		this.plugin = plugin;
		this.config = WaypointGUI.config;
		teleportationEnabled = this.config.getBoolean("waypoints.teleportation-enabled");
	}

	/*
	 * Events -----------------------------------------------------------------
	 */

	/**
	 * Listen for events inside of Waypoint GUI.
	 * 
	 * @param e {@code InventoryClickEvent} object
	 */
	@EventHandler
	public void waypointGUIClick(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

		if (!e.getInventory().equals(WaypointManager.getGUI(player).inventory()))
			return;
		if (e.getCurrentItem() == null)
			return;
		if (e.getCurrentItem().getItemMeta() == null)
			return;
		if (e.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;

		e.setCancelled(true);

		/*
		 * Close when exit button is clicked
		 */
		if (e.getCurrentItem().equals(GUI1.EXIT_BUTTON)) {
			player.closeInventory();

			/*
			 * Close and send message to player when empty slot is clicked.
			 */
		} else if (e.getCurrentItem().equals(GUI1.EMPTY_SLOT_ITEM)) {
			player.closeInventory();
			player.sendMessage(ChatColor.RESET + "Use " + ChatColor.LIGHT_PURPLE + "/waypoint create <name>"
					+ ChatColor.RESET + " to create a new Waypoint!");

			/*
			 * Do specified action according to type of click for the selected block.
			 */
		} else {
			ClickType click = e.getClick();
			String locName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
			Location loc = WaypointManager.getWaypoint(player, locName);

			switch (click) {

			/*
			 * On left click either teleport player to specified Waypoint, or send player a
			 * message containing the coordinates of the specified Waypoint, depending on
			 * the configuration of the plugin.
			 */
			case LEFT:
				// TODO make messages look better
				if (teleportationEnabled) {
					player.teleport(loc);
					player.sendMessage(ChatColor.GOLD + "Teleported to Waypoint " + locName + "!");
				} else {
					String x = Integer.toString(loc.getBlockX());
					String y = Integer.toString(loc.getBlockY());
					String z = Integer.toString(loc.getBlockZ());
					player.sendMessage(
							ChatColor.GOLD + "Waypoint " + locName + " located at " + x + ", " + y + ", " + z);
					player.closeInventory();
				}
				break;

			/*
			 * On middle click, update the specified Waypoint location to {@code player}'s
			 * current location.
			 */
			case MIDDLE:
				WaypointManager.updateWaypoint(player, locName, player.getLocation());
				player.sendMessage(ChatColor.GOLD + "Successfully updated location of Waypoint " + locName + "!");
				player.closeInventory();
				break;

			/*
			 * On right click, change current item.
			 */
			case RIGHT:
				WaypointManager.getGUI(player).advanceItem(e.getSlot());
				break;
			default:
				break;

			}

		}
	}

	/**
	 * Load Waypoint data from data.yml for {@code Player} specified by
	 * {@code PlayerJoinEvent}.
	 * 
	 * @param e {@code PlayerJoinEvent} object.
	 */
	@EventHandler
	public void loadPlayerWaypointsOnJoin(PlayerJoinEvent e) {
		WaypointManager.createGUI(e.getPlayer());
		WaypointManager.loadFromFile(e.getPlayer());
	}

	/**
	 * Save Waypoint data to data.yml and delete {@code CustomGUI} object for
	 * {@code Player} specified by {@code PlayerQuitEvent}.
	 * 
	 * @param e {@code PlayerQuitEvent} object.
	 */
	@EventHandler
	public void savePlayerWaypointsOnQuit(PlayerQuitEvent e) {
		WaypointManager.saveToFile(e.getPlayer());
		WaypointManager.deleteGUI(e.getPlayer());
	}

}
