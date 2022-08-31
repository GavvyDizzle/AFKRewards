package com.github.gavvydizzle.afkrewards.gui;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.configs.GUIConfig;
import com.github.mittenmc.serverutils.Colors;
import com.github.mittenmc.serverutils.PlayerHeads;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardInventory implements ClickableGUI {

    private Inventory inventory;
    private String leaderboardItemName;
    private int updateDelaySeconds;
    private int taskID;

    public LeaderboardInventory() {
        this.taskID = -1;
    }

    public void reload() {
        FileConfiguration config = GUIConfig.get();
        config.addDefault("leaderboard.updateDelaySeconds", 900);
        config.addDefault("leaderboard.name", "Zen Token Leaderboard");
        config.addDefault("leaderboard.rows", 6);
        config.addDefault("leaderboard.itemName", "&a#{position} &3{player_name} - {score} Zen Tokens");

        updateDelaySeconds = Math.max(60, config.getInt("leaderboard.updateDelaySeconds"));
        String inventoryName = Colors.conv(config.getString("leaderboard.name"));
        int inventorySize = config.getInt("leaderboard.rows") * 9;
        inventory = Bukkit.createInventory(null, inventorySize, inventoryName);

        leaderboardItemName = Colors.conv(config.getString("leaderboard.itemName"));

        runUpdateTask();
    }

    /**
     * Updates the leaderboard immediately when called then repeats every updateDelaySeconds seconds
     */
    private void runUpdateTask() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(AFKRewards.getInstance(), this::updateLeaderboard, 0, updateDelaySeconds * 20L);
    }

    public void updateLeaderboard() {
        Bukkit.getScheduler().runTaskAsynchronously(AFKRewards.getInstance(), () -> {
            ArrayList<LeaderboardPlacement> placements = AFKRewards.getInstance().getDatabase().getLeaderboardPlacements(inventory.getSize());

            for (int i = 0; i < placements.size(); i++) {
                LeaderboardPlacement leaderboardPlacement = placements.get(i);
                ItemStack itemStack = PlayerHeads.getHead(leaderboardPlacement.getOfflinePlayer());
                ItemMeta meta = itemStack.getItemMeta();
                assert meta != null;
                meta.setDisplayName(leaderboardItemName
                        .replace("{position}", "" + (i + 1))
                        .replace("{player_name}", Objects.requireNonNull(leaderboardPlacement.getOfflinePlayer().getName()))
                        .replace("{score}", "" + leaderboardPlacement.getScore())
                );
                itemStack.setItemMeta(meta);

                inventory.setItem(i, itemStack);
            }
        });
    }

    @Override
    public void openInventory(Player player) {
        player.openInventory(inventory);
        AFKRewards.getInstance().getInventoryManager().setClickableGUI(player, this);
    }

    @Override
    public void handleClick(InventoryClickEvent e) {

    }
}
