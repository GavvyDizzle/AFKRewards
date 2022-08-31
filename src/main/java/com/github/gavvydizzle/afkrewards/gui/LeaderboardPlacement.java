package com.github.gavvydizzle.afkrewards.gui;

import org.bukkit.OfflinePlayer;

public class LeaderboardPlacement {

    private final OfflinePlayer offlinePlayer;
    private final int score;
    private final long time;

    public LeaderboardPlacement(OfflinePlayer offlinePlayer, int score, long time) {
        this.offlinePlayer = offlinePlayer;
        this.score = score;
        this.time = time;
    }

    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    public int getScore() {
        return score;
    }

    public long getTime() {
        return time;
    }
}
