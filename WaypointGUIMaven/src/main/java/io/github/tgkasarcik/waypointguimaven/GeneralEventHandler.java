package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

/**
 * Class to handle events associated with {@code CustomGUI} objects.
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

	// TODO: make this configurable from config.yml
	/**
	 * Boolean variable to store whether teleportation is allowed from within the
	 * WaypointGUI menu.
	 */
	private static boolean teleportationEnabled = true;

	/**
	 * Default constructor
	 * 
	 * @param plugin Reference to class to register events for
	 */
	public GeneralEventHandler(WaypointGUI plugin) {
		this.plugin = plugin;
	}

	/*
	 * Events -----------------------------------------------------------------
	 */

	/**
	 * Listen for events inside of Waypoint GUI.
	 * 
	 * @param event {@code InventoryClickEvent} object
	 */
	@EventHandler
	public void waypointGUIClick(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();

		if (!event.getInventory().equals(WaypointManager.getGUI(player).inventory()))
			return;
		if (event.getCurrentItem() == null)
			return;
		if (event.getCurrentItem().getItemMeta() == null)
			return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;

		event.setCancelled(true);

		/*
		 * Close when exit button is clicked
		 */
		if (event.getCurrentItem().equals(GUI1.EXIT_BUTTON)) {
			player.closeInventory();

			/*
			 * Close and send message to player when empty slot is clicked.
			 */
		} else if (event.getCurrentItem().equals(GUI1.EMPTY_SLOT_ITEM)) {
			player.closeInventory();
			player.sendMessage(ChatColor.RESET + "Use " + ChatColor.LIGHT_PURPLE + "/waypoint create <name>"
					+ ChatColor.RESET + " to create a new Waypoint!");

			/*
			 * Do specified action according to type of click for the selected block.
			 */
		} else {
			ClickType click = event.getClick();
			String locName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
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
				WaypointManager.getGUI(player).advanceItem(event.getSlot());
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
