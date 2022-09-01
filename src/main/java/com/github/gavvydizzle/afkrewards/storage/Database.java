package com.github.gavvydizzle.afkrewards.storage;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.gui.LeaderboardPlacement;
import com.github.mittenmc.serverutils.UUIDConverter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public abstract class Database {

    private final String CREATE_TOKEN_TABLE = "CREATE TABLE IF NOT EXISTS tokens(" +
            "uuid BINARY(16)           NOT NULL," +
            "score INT DEFAULT 0       NOT NULL," +
            "time BIGINT DEFAULT 0     NOT NULL," +
            "progress BIGINT DEFAULT 0 NOT NULL," +
            "PRIMARY KEY (uuid)" +
            ");";

    private final String LOAD_LEADERBOARD_DATA = "SELECT * FROM tokens ORDER BY score DESC, time ASC limit ?;";

    private final String INSERT_PLAYER = "INSERT OR IGNORE INTO tokens(uuid, time) VALUES(?,?);";

    private final String INCREMENT_PLAYER_SCORE = "UPDATE tokens SET score=score+1, time=? WHERE uuid=?;";

    private final String SET_PLAYER_SCORE = "UPDATE tokens SET score=?, time=? WHERE uuid=?;";

    private final String GET_PLAYER_PROGRESS = "SELECT progress FROM tokens WHERE uuid=?;";

    private final String SET_PLAYER_PROGRESS = "UPDATE tokens SET progress=? WHERE uuid=?;";

    AFKRewards plugin;
    Connection connection;

    public Database(AFKRewards instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    /**
     * If the table for this plugin does not exist one will be created
     */
    public void load() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(CREATE_TOKEN_TABLE);
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    /**
     * Insert the player into the table with default values
     * @param player The player
     */
    public void insertPlayer(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INSERT_PLAYER);
            ps.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            ps.setLong(2, System.currentTimeMillis());
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    /**
     * Increase the score by one for the player
     * @param player The player
     */
    public void incrementScore(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(INCREMENT_PLAYER_SCORE);
            ps.setLong(1, System.currentTimeMillis());
            ps.setBytes(2, UUIDConverter.convert(player.getUniqueId()));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    /**
     * Sets the score of this player in the database
     * @param offlinePlayer The player
     * @param newScore The new score
     */
    public void setScore(OfflinePlayer offlinePlayer, int newScore) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(SET_PLAYER_SCORE);
            ps.setInt(1, newScore);
            ps.setLong(2, System.currentTimeMillis());
            ps.setBytes(3, UUIDConverter.convert(offlinePlayer.getUniqueId()));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public long getProgress(Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(GET_PLAYER_PROGRESS);
            ps.setBytes(1, UUIDConverter.convert(player.getUniqueId()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0;
    }

    /**
     * Saves the progress of the given player
     * @param player The player
     * @param millis The amount of time in milliseconds the player has towards the next reward
     */
    public void saveProgress(Player player, long millis) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(SET_PLAYER_PROGRESS);
            ps.setLong(1, millis);
            ps.setBytes(2, UUIDConverter.convert(player.getUniqueId()));
            ps.execute();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    /**
     * Saves all players' progress
     * @param afkMillis A hashmap containing UUIDs and their progress in milliseconds towards the next reward
     */
    public void saveProgress(HashMap<UUID, Long> afkMillis) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(SET_PLAYER_PROGRESS);

            for (UUID uuid : afkMillis.keySet()) {
                ps.setLong(1, afkMillis.get(uuid));
                ps.setBytes(2, UUIDConverter.convert(uuid));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public ArrayList<LeaderboardPlacement> getLeaderboardPlacements(int size) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement(LOAD_LEADERBOARD_DATA);
            ps.setInt(1, size);
            ResultSet resultSet = ps.executeQuery();

            ArrayList<LeaderboardPlacement> leaderboardPlacements = new ArrayList<>(size);
            while (resultSet.next()) {
                leaderboardPlacements.add(new LeaderboardPlacement(
                        Bukkit.getOfflinePlayer(UUIDConverter.convert(resultSet.getBytes(1))),
                        resultSet.getInt(2),
                        resultSet.getLong(3)
                ));
            }
            return leaderboardPlacements;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return new ArrayList<>();
    }

}