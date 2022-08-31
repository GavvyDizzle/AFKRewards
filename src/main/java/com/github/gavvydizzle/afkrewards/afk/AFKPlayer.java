package com.github.gavvydizzle.afkrewards.afk;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.group.GroupType;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class AFKPlayer {

    private final Player player;
    private final GroupType groupType;
    private long lastTokenTime, nextTokenTime;
    private BossBar bossBar;

    public AFKPlayer(Player player, GroupType groupType, Long millis) {
        this.player = player;
        this.groupType = groupType;
        updateFirstTokenTime(millis);

        AFKManager manager = AFKRewards.getInstance().getAfkManager();
        bossBar = Bukkit.createBossBar(manager.getBarName(), manager.getBarColor(), manager.getBarStyle());
        updateBossbar();
        bossBar.addPlayer(player);
    }

    /**
     * Determines if the player should be given a token.
     * This will only return true once because if it is true, then it updates the nextTokenTime
     * @return True if the player should be rewarded
     */
    public boolean shouldGiveToken() {
        if (System.currentTimeMillis() > nextTokenTime) {
            updateNextTokenTime();
            return true;
        }
        return false;
    }

    private void updateFirstTokenTime(long progress) {
        lastTokenTime = System.currentTimeMillis() - progress;
        nextTokenTime = lastTokenTime + AFKRewards.getInstance().getAfkManager().getSecondsBetweenTokens() * 1000L;
    }

    private void updateNextTokenTime() {
        lastTokenTime = System.currentTimeMillis();
        nextTokenTime = lastTokenTime + AFKRewards.getInstance().getAfkManager().getSecondsBetweenTokens() * 1000L;
    }

    public void updateTimeOnReload() {
        nextTokenTime = lastTokenTime + AFKRewards.getInstance().getAfkManager().getSecondsBetweenTokens() * 1000L;
    }

    public void updateBossbar() {
        double currTime = System.currentTimeMillis();
        double progress = (currTime - lastTokenTime) / (nextTokenTime - lastTokenTime);
        bossBar.setProgress(Math.min(1, progress));
    }

    public void updateBarOnReload() {
        bossBar.removeAll();
        AFKManager manager = AFKRewards.getInstance().getAfkManager();
        bossBar = Bukkit.createBossBar(manager.getBarName(), manager.getBarColor(), manager.getBarStyle());
        bossBar.addPlayer(player);
    }

    /**
     * Removes the bossbar when the player stops AFKing
     */
    public void removeFromAFK() {
        bossBar.removeAll();
    }

    public Player getPlayer() {
        return player;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public long getProgress() {
        return System.currentTimeMillis() - lastTokenTime;
    }

}
