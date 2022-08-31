package com.github.gavvydizzle.afkrewards.gui;

import com.github.gavvydizzle.afkrewards.configs.GUIConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.UUID;

public class InventoryManager implements Listener {

    private final GroupSelectionInventory groupSelectionInventory;
    private final LeaderboardInventory leaderboardInventory;
    private final HashMap<UUID, ClickableGUI> playersInInventory;

    public InventoryManager() {
        groupSelectionInventory = new GroupSelectionInventory();
        leaderboardInventory = new LeaderboardInventory();
        playersInInventory = new HashMap<>();
        reloadAllGUIs();
    }

    public void reloadAllGUIs() {


        groupSelectionInventory.reload();
        leaderboardInventory.reload();
        GUIConfig.save();
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent e) {
        if (playersInInventory.containsKey(e.getPlayer().getUniqueId())) {
            removePlayerFromGUI((Player) e.getPlayer());
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;

        if (playersInInventory.containsKey(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            if (e.getClickedInventory() == e.getView().getTopInventory()) {
                playersInInventory.get(e.getWhoClicked().getUniqueId()).handleClick(e);
            }
        }
    }

    public void setClickableGUI(Player player, ClickableGUI clickableGUI) {
        playersInInventory.put(player.getUniqueId(), clickableGUI);
    }

    public void removePlayerFromGUI(Player player) {
        playersInInventory.remove(player.getUniqueId());
    }

    public GroupSelectionInventory getGroupSelectionInventory() {
        return groupSelectionInventory;
    }

    public LeaderboardInventory getLeaderboardInventory() {
        return leaderboardInventory;
    }
}
