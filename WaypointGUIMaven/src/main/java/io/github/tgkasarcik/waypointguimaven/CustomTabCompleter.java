package io.github.tgkasarcik.waypointguimaven;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * Class implementing {@code TabCompleter} interface to allow for custom tab
 * completion of commands implemented by this Plugin.
 * 
 * @author T. Kasarcik
 *
 */
public class CustomTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

		/*
		 * List arg. options for "/waypoint" command.
		 */
		if (cmd.getName().equalsIgnoreCase("waypoint")) {

			/*
			 * List options for first arg.
			 */
			if (args.length == 1) {
				List<String> arguments = new ArrayList<String>();
				arguments.add("create");
				arguments.add("update");
				arguments.add("delete");
				return arguments;
			}

			/*
			 * List player's waypoints as option for second arg. when they enter "update" or
			 * "delete" for first arg.
			 */
			if (args.length == 2 && (args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("delete"))) {
				return WaypointManager.locationList((Player) sender);
			}
		}

		return null;
	}

}
