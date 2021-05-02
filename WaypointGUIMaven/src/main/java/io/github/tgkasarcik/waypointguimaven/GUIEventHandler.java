package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

/**
 * Class to handle events associated with {@code CustomGUI} objects.
 * 
 * @author T. Kasarcik
 *
 */

public class GUIEventHandler implements Listener {

	/**
	 * Reference to class to register events for
	 */
	@SuppressWarnings("unused")
	private final WaypointGUI plugin;

	/**
	 * Default constructor
	 * 
	 * @param plugin Reference to class to register events for
	 */
	public GUIEventHandler(WaypointGUI plugin) {
		this.plugin = plugin;
	}

	/*
	 * Events -----------------------------------------------------------------
	 */

	@EventHandler
	public void testWelcomeMessage(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendMessage("Test successful!");
	}

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
			 * Teleport player to waypoint or send message containing waypoint's coords,
			 * depending on config.
			 */
		} else {
			// teleport player to specified location
		}
	}

}
