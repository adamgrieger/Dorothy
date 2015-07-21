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

    public DorothyCommandExecutor(Dorothy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (cmd.getName().equalsIgnoreCase("home") && player.hasPermission("dorothy.home")) {
                    if (plugin.playerHomes.get(player.getName()) != null) {
                        double[] homeCoords = plugin.playerHomes.get(player.getName());
                        Location home = new Location(player.getWorld(), homeCoords[0], homeCoords[1], homeCoords[2]);

                        if (!home.getChunk().isLoaded()) {
                            home.getChunk().load();
                        }

                        player.teleport(home);
                    } else {
                        player.sendMessage(plugin.messagePrefix + "You have not set a home yet!");
                    }
                } else if (cmd.getName().equalsIgnoreCase("sethome") && player.hasPermission("dorothy.sethome")) {
                    Location playerLoc = player.getLocation();
                    double[] coordinates = {playerLoc.getX(), playerLoc.getY(), playerLoc.getZ()};
                    plugin.playerHomes.put(player.getName(), coordinates);
                    player.sendMessage(plugin.messagePrefix + "Home set.");
                } else {
                    player.sendMessage(permissionMessage);
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
