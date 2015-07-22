package com.adamgrieger.dorothy;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Main Dorothy class for plugin initialization and main methods.
 */
public class Dorothy extends JavaPlugin {

    // [Dorothy] tag before all messages
    public String messagePrefix = ChatColor.AQUA + "[" + ChatColor.RESET + "Dorothy" + ChatColor.AQUA + "] " + ChatColor.RESET;

    // Storing home coordinates
    public Map<String, double[]> playerHomes = new HashMap<>();

    @Override
    public void onDisable() {
        File filePlayerHomes = new File("plugins/Dorothy/playerhomes.ser");

        // Creates Dorothy directory if it doesn't exist
        if (!filePlayerHomes.exists()) {
            if (new File("plugins/Dorothy").mkdirs()) {
                getLogger().info("Dorothy directory created");
            }
        }

        // Saves player home data (creates file if not found)
        try {
            if (filePlayerHomes.createNewFile()) {
                getLogger().info("playerhomes.ser created");
            } else {
                FileOutputStream fileOut = new FileOutputStream(filePlayerHomes);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                out.writeObject(playerHomes);

                fileOut.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        File filePlayerHomes = new File("plugins/Dorothy/playerhomes.ser");

        // Creates Dorothy directory if it doesn't exist
        if (!filePlayerHomes.exists()) {
            if (new File("plugins/Dorothy").mkdirs()) {
                getLogger().info("Dorothy directory created");
            }
        }

        // Loads player home data (creates file if not found)
        try {
            if (filePlayerHomes.createNewFile()) {
                getLogger().info("playerhomes.ser created");
            } else {
                FileInputStream fileIn = new FileInputStream(filePlayerHomes);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                playerHomes = (Map<String, double[]>) in.readObject();

                fileIn.close();
                in.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        DorothyCommandExecutor cmdExe = new DorothyCommandExecutor(this);

        // Sets command executors
        getCommand("home").setExecutor(cmdExe);
        getCommand("sethome").setExecutor(cmdExe);
    }

    /**
     * Reloads the plugin.
     */
    public void onReload() {
        getLogger().info("Reloading...");

        onDisable();
        onEnable();

        getLogger().info("Plugin reloaded.");
    }
}
