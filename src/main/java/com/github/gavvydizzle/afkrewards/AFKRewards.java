package com.github.gavvydizzle.afkrewards;

import com.github.gavvydizzle.afkrewards.afk.AFKManager;
import com.github.gavvydizzle.afkrewards.commands.AdminCommandManager;
import com.github.gavvydizzle.afkrewards.commands.PlayerCommandManager;
import com.github.gavvydizzle.afkrewards.gui.InventoryManager;
import com.github.gavvydizzle.afkrewards.storage.Database;
import com.github.gavvydizzle.afkrewards.storage.SQLite;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AFKRewards extends JavaPlugin {

    private static AFKRewards instance;
    private Database database;
    private AFKManager afkManager;
    private InventoryManager inventoryManager;
    private PlayerCommandManager playerCommandManager;
    private AdminCommandManager adminCommandManager;

    @Override
    public void onEnable() {
        instance = this;

        afkManager = new AFKManager();
        inventoryManager = new InventoryManager();
        getServer().getPluginManager().registerEvents(afkManager, this);
        getServer().getPluginManager().registerEvents(inventoryManager, this);

        playerCommandManager = new PlayerCommandManager();
        Objects.requireNonNull(getCommand("religion")).setExecutor(playerCommandManager);
        adminCommandManager = new AdminCommandManager();
        Objects.requireNonNull(getCommand("afkadmin")).setExecutor(adminCommandManager);

        database = new SQLite(this);
        database.load();
    }

    @Override
    public void onDisable() {
        if (afkManager != null) {
            afkManager.onServerShutdown();
            getLogger().info("Successfully saved data on server shutdown");
        }
        else {
            getLogger().severe("Failed to save data on server shutdown");
        }
    }

    public static AFKRewards getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public AFKManager getAfkManager() {
        return afkManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public PlayerCommandManager getPlayerCommandManager() {
        return playerCommandManager;
    }

    public AdminCommandManager getAdminCommandManager() {
        return adminCommandManager;
    }
}
