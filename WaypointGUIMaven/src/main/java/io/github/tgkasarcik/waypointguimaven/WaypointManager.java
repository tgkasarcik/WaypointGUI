package io.github.tgkasarcik.waypointguimaven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

/**
 * Utility class to manage GUI1 objects associated with waypoint menus, store
 * waypoint locations during execution, and house associated utility methods.
 * 
 * @author T. Kasarcik
 *
 */
public class WaypointManager {

	/*
	 * Private members --------------------------------------------------------
	 */

	/**
	 * Map of GUI1 objects to store Waypoint GUIs for all players on server.
	 */
	private static Map<UUID, GUI1> guiMap;

	/**
	 * Map that stored a Player's UUID paired with a Map containing all of their
	 * waypoints paired with names for each.
	 */
	private static Map<UUID, Map<String, Location>> waypointMap;

	/**
	 * Local reference to data.yml.
	 */
	private static DataManager data;

	/*
	 * Constructor ------------------------------------------------------------
	 */

	/**
	 * Private constructor so this utility class cannot be instantiated.
	 */
	private WaypointManager() {
	}

	/**
	 * Initialize internal representation of {@code this}.
	 */
	static {
		guiMap = new HashMap<UUID, GUI1>();
		waypointMap = new HashMap<UUID, Map<String, Location>>();
		data = WaypointGUI.data;
	}

	/*
	 * Static methods ---------------------------------------------------------
	 */

	/**
	 * Creates a GUI1 object and location map for designated player, if they have
	 * not already been created.
	 * 
	 * @param p Player to create GUI for
	 */
	public static void createGUI(Player p) {
		UUID u = p.getUniqueId();
		if (!guiMap.containsKey(u)) {
			guiMap.put(u, new GUI1(p));
			waypointMap.put(u, new HashMap<String, Location>());
		}
	}

	/**
	 * Removes the GUI of the designated player from {@code guiList} and the
	 * location map from {@code waypointMap}.
	 * 
	 * @param p Player to delete GUI for
	 */
	public static void deleteGUI(Player p) {
		guiMap.remove(p.getUniqueId());
		waypointMap.remove(p.getUniqueId());
	}

	/**
	 * Returns the CustomGUI object of the specified player, or creates a new one if
	 * it does not exist.
	 * 
	 * @param p
	 * @return
	 */
	public static CustomGUI getGUI(Player p) {
		if (!guiMap.containsKey(p.getUniqueId())) {
			createGUI(p);
		}
		return guiMap.get(p.getUniqueId());
	}

	/**
	 * Returns the location specified by {@code waypointName} for Player {@code p},
	 * provided it exists.
	 * 
	 * @param p            Target player
	 * @param waypointName Name of desired waypoint
	 * @return Location of specified waypoint, provided it exists
	 */
	public static Location getWaypoint(Player p, String waypointName) {

		Location waypoint = null;
		UUID u = p.getUniqueId();

		if (waypointMap.containsKey(u)) {
			if (waypointMap.get(u).containsKey(waypointName)) {
				waypoint = waypointMap.get(u).get(waypointName);
			}
		}

		return waypoint;
	}

	/**
	 * Creates a Waypoint for the specified player at the specified location with
	 * the specified name.
	 * 
	 * @param p    Specified player.
	 * @param loc  Specified location for Waypoint.
	 * @param name Specified Name for Waypoint.
	 * @return true iff the operation was successful, false otherwise.
	 */
	public static boolean createWaypoint(Player p, Location loc, String name) {

		boolean availSlot = false;
		GUI1 localGUI = (GUI1) getGUI(p);

		/*
		 * Determine if GUI has any open slots. If it does, fill the first empty and
		 * return true. Otherwisem, return false.
		 */
		int index = localGUI.firstEmptyIndex(GUI1.EMPTY_SLOT_ITEM);
		if (index != -1) {
			availSlot = true;

			/*
			 * Create block and put it in GUI.
			 */
			ItemStack block = createItem(Material.GRASS_BLOCK, name); // TODO: change this??
			localGUI.setItem(index, block);

			/*
			 * Create data representation of Waypoint.
			 */
			Map<String, Location> localLocMap = waypointMap.remove(p.getUniqueId());
			localLocMap.put(name, loc);
			waypointMap.put(p.getUniqueId(), localLocMap);
		}

		guiMap.put(p.getUniqueId(), localGUI);
		return availSlot;
	}

	/**
	 * Updates the Waypoint specified by {@code waypointName} for the specified
	 * player to the new location specified by {@code newLocation}, or creates a new
	 * waypoint at that location if one does not already exist.
	 * 
	 * @param p            the specified player
	 * @param waypointName specified waypoint name
	 * @param newLocation  location to update waypoint to
	 */
	public static void updateWaypoint(Player p, String waypointName, Location newLocation) {
		UUID u = p.getUniqueId();
		if (waypointMap.containsKey(u)) {
			if (waypointMap.get(u).containsKey(waypointName)) {
				waypointMap.get(u).remove(waypointName);
			}
		}
		waypointMap.get(u).put(waypointName, newLocation);
	}

	/**
	 * Changes the name of the Waypoint
	 * 
	 * @param p
	 * @param oldName
	 * @param newName
	 */
	public static void renameWaypoint(Player p, String oldName, String newName) {
		Location loc = null;
		UUID u = p.getUniqueId();
		if (waypointMap.containsKey(u)) {
			if (waypointMap.get(u).containsKey(oldName)) {
				loc = waypointMap.get(u).remove(oldName);
			}
		}
		if (!loc.equals(null)) {
			waypointMap.get(u).put(newName, loc);
		}

		GUI1 gui = guiMap.remove(u);
		int index = gui.indexOf(oldName);
		Material oldMaterial = gui.item(index).getType();

		ItemStack newItem = createItem(oldMaterial, newName);
		gui.setItem(index, newItem);
		guiMap.put(u, gui);

	}

	/**
	 * Returns a {@code List} containing the set of the specified player's Waypoint
	 * names.
	 * 
	 * @param p Specified player
	 * @return
	 */
	public static List<String> locationList(Player p) {
		return new ArrayList<String>(waypointMap.get(p.getUniqueId()).keySet());
	}

	/**
	 * Creates an {@code ItemStack} object with the specified {@code Material} and
	 * {@code displayName}.
	 * 
	 * @param material    The specified material
	 * @param displayName The specified display name
	 * @return The created ItemStack object
	 */
	public static ItemStack createItem(Material material, String displayName) {
		ItemStack block = new ItemStack(material);
		ItemMeta meta = block.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + displayName);
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "Left Click To Travel");
		lore.add(ChatColor.GOLD + "Middle Click To Update to Current Location");
		lore.add(ChatColor.GOLD + "Right Click To Edit Block");
		meta.setLore(lore);
		block.setItemMeta(meta);
		return block;
	}

	/**
	 * Loads Waypoint data of specified {@code Player} from data.yml
	 * 
	 * @param p specified player
	 */
	public static void loadFromFile(Player p) {
		UUID u = p.getUniqueId();

	}

	/**
	 * Saves Waypoint data of specified {@code Player} to data.yml
	 * 
	 * @param p specified player
	 */
	public static void saveToFile(Player p) {
		UUID u = p.getUniqueId();
		GUI1 gui = guiMap.remove(u);

		for (Map.Entry<String, Location> entry : waypointMap.get(u).entrySet()) {
			Location loc = entry.getValue();
			int itemIndex = gui.indexOf(entry.getKey());
			Material material = gui.item(itemIndex).getType();
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".world",
					loc.getWorld().getName());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".x",
					loc.getX());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".y",
					loc.getY());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".z",
					loc.getZ());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".pitch",
					loc.getPitch());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".yaw",
					loc.getYaw());
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".gui-slot",
					itemIndex);
			data.getConfig().set("waypoints." + u.toString() + "." + entry.getKey() + ".gui-material",
					material.name());
		}
	}

	/*
	 * Private helper methods -------------------------------------------------
	 */

}
