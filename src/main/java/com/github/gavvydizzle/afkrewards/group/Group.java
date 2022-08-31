package com.github.gavvydizzle.afkrewards.group;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class Group {

    private final GroupType groupType;
    private final ProtectedRegion region;
    private int points;
    private final List<String> onJoinCommands, rewardCommands;
    private final String groupPermission, onJoinMessage, rewardMessage;

    public Group(GroupType groupType, String groupPermission, ProtectedRegion region, int points,
                 String onJoinMessage, List<String> onJoinCommands,
                 String rewardMessage, List<String> rewardCommands) {
        this.groupType = groupType;
        this.groupPermission = groupPermission;
        this.region = region;
        this.points = points;
        this.onJoinMessage = onJoinMessage;
        this.onJoinCommands = onJoinCommands;
        this.rewardMessage = rewardMessage;
        this.rewardCommands = rewardCommands;
    }

    public boolean isPlayerInRegion(Player player) {
        if (player.hasPermission("afkrewards.bypass")) return true;

        if (region == null) return false;
        return region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }

    /**
     * Updates the region that the players of this group need to AFK in.
     * This method validates its parameters
     * @param admin The admin changing the region
     * @param regionName The name of the region to set
     */
    public void updateRegion(Player admin, String regionName) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(admin.getWorld()));

        if (regionManager == null) {
            admin.sendMessage(ChatColor.RED + "Invalid region manager");
            return;
        }

        ProtectedRegion protectedRegion = regionManager.getRegion(regionName);
        if (protectedRegion == null) {
            admin.sendMessage(ChatColor.RED + "The region '" + regionName + "' could not be found");
            return;
        }

        if (protectedRegion == region) {
            admin.sendMessage(ChatColor.YELLOW + "This region is already in use");
            return;
        }

        FileConfiguration config = AFKRewards.getInstance().getConfig();
        config.set("group." + groupType.toString().toLowerCase() + ".region.world", admin.getWorld().getName());
        config.set("group." + groupType.toString().toLowerCase() + ".region.regionName", regionName);
        AFKRewards.getInstance().saveConfig();

        admin.sendMessage(ChatColor.GREEN + "Successfully updated " + groupType + " region to " + regionName);
    }

    /**
     * Messages and runs commands for this player when they join this group
     * @param player The player who joined
     */
    public void onPlayerJoinGroup(Player player) {
        if (!onJoinMessage.trim().isEmpty()) {
            player.sendMessage(onJoinMessage);
        }
        for (String cmd : onJoinCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player_name}", player.getName()));
        }
    }

    /**
     * Rewards the player and adds 1 point to this group's total
     * @param player The player to reward
     */
    public void rewardPlayer(Player player) {
        points++;
        updateTotalPoints();

        if (!rewardMessage.trim().isEmpty()) {
            player.sendMessage(rewardMessage);
        }
        for (String cmd : rewardCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player_name}", player.getName()));
        }
    }

    private void updateTotalPoints() {
        FileConfiguration config = AFKRewards.getInstance().getConfig();
        config.set("group." + groupType.toString().toLowerCase() + ".points", config.getInt("group." + groupType.toString().toLowerCase() + ".points") + 1);
        AFKRewards.getInstance().saveConfig();
    }

    public String getGroupPermission() {
        return groupPermission;
    }

    public int getPoints() {
        return points;
    }

}
