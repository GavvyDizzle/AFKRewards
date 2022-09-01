package com.github.gavvydizzle.afkrewards.afk;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.group.Group;
import com.github.gavvydizzle.afkrewards.group.GroupType;
import com.github.mittenmc.serverutils.Colors;
import com.github.mittenmc.serverutils.RepeatingTask;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AFKManager implements Listener {

    private final HashMap<UUID, AFKPlayer> afkPlayers;
    private final HashMap<UUID, Long> afkMillisProgress;
    private Group dolphinGroup, pigGroup;
    private int secondsBetweenTokens;
    private String nextTokenMessage, barName;
    private BarColor barColor;
    private BarStyle barStyle;

    public AFKManager() {
        afkPlayers = new HashMap<>();
        afkMillisProgress = new HashMap<>();
        reload();
        startTask();
    }

    public void reload() {
        FileConfiguration config = AFKRewards.getInstance().getConfig();
        config.options().copyDefaults(true);
        config.addDefault("secondsBetweenTokens", 1800);
        config.addDefault("nextTokenMessage", "&aYou will receive your next token in {time}");
        config.addDefault("bossbar.name", "&dZen Token Progress");
        config.addDefault("bossbar.color", "BLUE");
        config.addDefault("bossbar.style", "SOLID");

        config.addDefault("group.dolphin.groupPermission", "afkrewards.group.dolphin");
        config.addDefault("group.dolphin.points", 0);
        config.addDefault("group.dolphin.region.world", "world");
        config.addDefault("group.dolphin.region.regionName", "region");
        config.addDefault("group.dolphin.onJoinMessage", "&aYou joined the &3&lDolphins");
        config.addDefault("group.dolphin.onJoinCommands", new ArrayList<>());
        config.addDefault("group.dolphin.rewardMessage", "&aYou received 1 Zen Token!");
        config.addDefault("group.dolphin.rewardCommands", new ArrayList<>());

        config.addDefault("group.pig.groupPermission", "afkrewards.group.pig");
        config.addDefault("group.pig.points", 0);
        config.addDefault("group.pig.region.world", "world");
        config.addDefault("group.pig.region.regionName", "region");
        config.addDefault("group.pig.onJoinMessage", "&aYou joined the &3&lDolphins");
        config.addDefault("group.pig.onJoinCommands", new ArrayList<>());
        config.addDefault("group.pig.rewardMessage", "&aYou received 1 Zen Token!");
        config.addDefault("group.pig.rewardCommands", new ArrayList<>());

        AFKRewards.getInstance().saveConfig();

        secondsBetweenTokens = Math.max(1, config.getInt("secondsBetweenTokens"));
        nextTokenMessage = Colors.conv(config.getString("nextTokenMessage"));
        barName = Colors.conv(config.getString("bossbar.name"));
        try {
            barColor = BarColor.valueOf(Objects.requireNonNull(config.getString("bossbar.color")).toUpperCase());
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The color '" + config.getString("bossbar.color") + "' is not a valid bossbar color! Setting to default");
            barColor = BarColor.BLUE;
        }
        try {
            barStyle = BarStyle.valueOf(Objects.requireNonNull(config.getString("bossbar.style")).toUpperCase());
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The color '" + config.getString("bossbar.style") + "' is not a valid bossbar style! Setting to default");
            barStyle = BarStyle.SOLID;
        }

        loadDolphinGroup(config);
        loadPigGroup(config);

        for (AFKPlayer afkPlayer : afkPlayers.values()) {
            afkPlayer.updateTimeOnReload();
            afkPlayer.updateBarOnReload();
        }
    }

    private void loadDolphinGroup(FileConfiguration config) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("group.dolphin.region.world")));
        if (world == null) {
            AFKRewards.getInstance().getLogger().warning("The DOLPHIN world '" + config.getString("group.dolphin.region.world") + "' does not exist");
            return;
        }

        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            AFKRewards.getInstance().getLogger().warning("Invalid DOLPHIN region manager");
            return;
        }

        ProtectedRegion protectedRegion = regionManager.getRegion(Objects.requireNonNull(config.getString("group.dolphin.region.regionName")));
        if (protectedRegion == null) {
            AFKRewards.getInstance().getLogger().warning("The DOLPHIN region '" + config.getString("group.dolphin.region.regionName") + "' could not be found");
        }

        dolphinGroup = new Group(
                GroupType.DOLPHIN,
                config.getString("group.dolphin.groupPermission"),
                protectedRegion,
                config.getInt("group.dolphin.points"),
                Colors.conv(config.getString("group.dolphin.onJoinMessage")),
                config.getStringList("group.dolphin.onJoinCommands"),
                Colors.conv(config.getString("group.dolphin.rewardMessage")),
                config.getStringList("group.dolphin.rewardCommands")
        );
    }

    private void loadPigGroup(FileConfiguration config) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("group.pig.region.world")));
        if (world == null) {
            AFKRewards.getInstance().getLogger().warning("The PIG world '" + config.getString("group.pig.region.world") + "' does not exist");
            return;
        }

        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null) {
            AFKRewards.getInstance().getLogger().warning("Invalid PIG region manager");
            return;
        }

        ProtectedRegion protectedRegion = regionManager.getRegion(Objects.requireNonNull(config.getString("group.pig.region.regionName")));
        if (protectedRegion == null) {
            AFKRewards.getInstance().getLogger().warning("The PIG region '" + config.getString("group.pig.region.regionName") + "' could not be found");
        }

        pigGroup = new Group(
                GroupType.PIG,
                config.getString("group.pig.groupPermission"),
                protectedRegion,
                config.getInt("group.pig.points"),
                Colors.conv(config.getString("group.pig.onJoinMessage")),
                config.getStringList("group.pig.onJoinCommands"),
                Colors.conv(config.getString("group.pig.rewardMessage")),
                config.getStringList("group.pig.rewardCommands")
        );
    }

    private void startTask() {
        new RepeatingTask(AFKRewards.getInstance(), 0, 20) {
            @Override
            public void run() {
                updateAFKPlayers();
            }
        };
    }

    /**
     * Attempts to reward players
     */
    private void updateAFKPlayers() {
        for (AFKPlayer afkPlayer : afkPlayers.values()) {
            afkPlayer.updateBossbar();
            if (afkPlayer.shouldGiveToken()) {
                switch (afkPlayer.getGroupType()) {
                    case DOLPHIN:
                        dolphinGroup.rewardPlayer(afkPlayer.getPlayer());
                        break;
                    case PIG:
                        pigGroup.rewardPlayer(afkPlayer.getPlayer());
                        break;
                }
                AFKRewards.getInstance().getDatabase().incrementScore(afkPlayer.getPlayer());
            }
        }
    }

    public boolean isInGroup(Player player) {
        return isInDolphinGroup(player) || isInPigGroup(player);
    }

    public boolean isInDolphinGroup(Player player) {
        return player.hasPermission(dolphinGroup.getGroupPermission());
    }

    public void setDolphinPlayerAFK(Player player) {
        afkPlayers.put(player.getUniqueId(), new AFKPlayer(player, GroupType.DOLPHIN, afkMillisProgress.get(player.getUniqueId())));
    }

    public boolean isInPigGroup(Player player) {
        return player.hasPermission(pigGroup.getGroupPermission());
    }

    public void setPigPlayerAFK(Player player) {
        afkPlayers.put(player.getUniqueId(), new AFKPlayer(player, GroupType.PIG, afkMillisProgress.get(player.getUniqueId())));
    }

    public boolean removePlayerFromAFK(Player player) {
        if (afkPlayers.containsKey(player.getUniqueId())) {
            AFKPlayer afkPlayer = afkPlayers.remove(player.getUniqueId());
            if (afkPlayer != null) {
                afkPlayer.removeFromAFK();
                afkMillisProgress.put(player.getUniqueId(), afkPlayer.getProgress());
            }
            return true;
        }
        return false;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        AFKRewards.getInstance().getDatabase().insertPlayer(e.getPlayer());
        afkMillisProgress.put(e.getPlayer().getUniqueId(), AFKRewards.getInstance().getDatabase().getProgress(e.getPlayer()));
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent e) {
        removePlayerFromAFK(e.getPlayer());
        AFKRewards.getInstance().getDatabase().saveProgress(e.getPlayer(), afkMillisProgress.remove(e.getPlayer().getUniqueId()));
    }

    /**
     * Saves all player's progress on server shutdown
     */
    public void onServerShutdown() {
        AFKRewards.getInstance().getDatabase().saveProgress(afkMillisProgress);
    }

    /**
     * Gets the amount of time until the player gets their next reward.
     * If the player is afk, it will check their AFKPlayer, otherwise it will check the map
     * @param player The player
     * @return The amount of milliseconds until the next reward
     */
    public int getSecondsUntilNextToken(Player player) {
        if (afkPlayers.containsKey(player.getUniqueId())) {
            return (int) (secondsBetweenTokens - (afkPlayers.get(player.getUniqueId()).getProgress() / 1000));
        }
        else {
            return (int) (secondsBetweenTokens - (afkMillisProgress.get(player.getUniqueId()) / 1000));
        }
    }


    public Group getDolphinGroup() {
        return dolphinGroup;
    }

    public Group getPigGroup() {
        return pigGroup;
    }

    public int getSecondsBetweenTokens() {
        return secondsBetweenTokens;
    }

    public String getNextTokenMessage() {
        return nextTokenMessage;
    }

    public String getBarName() {
        return barName;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }
}
