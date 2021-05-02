package io.github.tgkasarcik.waypointguimaven;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class implementing {@code CommandExecutor} interface to handle command
 * execution for instances of {@code WaypointGUI}.
 * 
 * @author T. Kasarcik
 *
 */
public class MainCommandExecutor implements CommandExecutor {

	/**
	 * Reference to class to execute commands for
	 */
	@SuppressWarnings("unused")
	private final WaypointGUI plugin;

	/**
	 * Default constructor
	 * 
	 * @param plugin Reference to class to execute commands for
	 */
	public MainCommandExecutor(WaypointGUI plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/*
		 * For commands that target players
		 */
		if (sender instanceof Player) {
			Player player = (Player) sender;

			/*
			 * "/w" command to display player's waypoint menu
			 */
			if (cmd.getName().equalsIgnoreCase("w")) {
				WaypointManager.getGUI(player).display();
			}

			/*
			 * "/waypoint" command for creating, updating, and deleting waypoints.
			 */
			if (cmd.getName().equalsIgnoreCase("waypoint")) {

				/*
				 * If no args are specified after command, just display waypoint menu.
				 */
				if (args.length == 0) {
					WaypointManager.getGUI(player).display();
				} else if (args.length == 2) {

					/*
					 * Create a new waypoint at player's current location.
					 */
					if (args[0].equalsIgnoreCase("create")) {

						//TODO: tidy up messages
						if (WaypointManager.createWaypoint(player, player.getLocation(), args[1])) {
							player.sendMessage("Waypoint " + args[1] + " created successfully!");
						} else {
							player.sendMessage("Error: Unable to create Waypoint--no available slots!");
						}

					}

					/*
					 * Update the specifed waypoint to player's current location.
					 */
					if (args[0].equalsIgnoreCase("update")) {
						// TODO: implement this
						// update existing waypoint
						player.sendMessage("This will update an existing waypoint in the future!");
					}

					/*
					 * Delete the specified waypoint.
					 */
					if (args[0].equalsIgnoreCase("delete")) {
						// TODO: implement this
						// delete existing waypoint
						player.sendMessage("This will delete an existing waypoint in the future!");
					}

				} else {
					player.sendMessage("Error: too few arguments!");
					// TODO: implement proper error message.
				}
			}

			/*
			 * Send error message to console if it tries to call commands that are only
			 * defined for players.
			 */
		} else {
			sender.sendMessage("Error: Command \"/" + label + "\" cannot be used from the console!");
		}
		return true;
	}

}
