package com.adamgrieger.dorothy;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * Class for executing Dorothy commands.
 */
public class DorothyCommandExecutor implements CommandExecutor {

    private final Dorothy plugin;

    public String permissionMessage = "Sorry, you don't have permission to do that!";

    /**
     * Constructor for DorothyCommandExecutor
     *
     * @param plugin Instance of Dorothy
     */
    public DorothyCommandExecutor(Dorothy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Dorothy cannot be used by the console (doesn't make sense)
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Commands cannot have extra arguments
            if (args.length == 0) {
                if (cmd.getName().equalsIgnoreCase("home") && player.hasPermission("dorothy.home")) {
                    if (plugin.playerHomes.get(player.getName()) != null) {
                        double[] homeCoords = plugin.playerHomes.get(player.getName());
                        Location home = new Location(player.getWorld(), homeCoords[0], homeCoords[1], homeCoords[2], (float) homeCoords[3], (float) homeCoords[4]);

                        // Loads the chunk if not loaded yet (prevents suffocation inside of blocks)
                        if (!home.getChunk().isLoaded()) {
                            home.getChunk().load();
                        }

                        player.teleport(home);
                    } else {
                        // Player tries to teleport and does not have a "home"
                        player.sendMessage(plugin.messagePrefix + "You have not set a home yet!");
                    }
                } else if (cmd.getName().equalsIgnoreCase("sethome") && player.hasPermission("dorothy.sethome")) {
                    Location playerLoc = player.getLocation();
                    double[] coordinates = {playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getYaw(), playerLoc.getPitch()};

                    // Saves player's coordinates as their "home"
                    plugin.playerHomes.put(player.getName(), coordinates);
                    player.sendMessage(plugin.messagePrefix + "Home set.");
                } else {
                    // Sender is a player but does not have permission
                    player.sendMessage(permissionMessage);
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
