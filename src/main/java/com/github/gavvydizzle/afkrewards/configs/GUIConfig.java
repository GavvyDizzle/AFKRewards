package com.github.gavvydizzle.afkrewards.configs;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GUIConfig {

    private static File file;
    private static FileConfiguration fileConfiguration;

    static {
        setup();
        save();
    }

    //Finds or generates the config file
    public static void setup() {
        file = new File(AFKRewards.getInstance().getDataFolder(), "gui.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.options().copyDefaults(true);
    }

    public static FileConfiguration get(){
        return fileConfiguration;
    }

    public static void save() {
        try {
            fileConfiguration.save(file);
        }
        catch (IOException e) {
            AFKRewards.getInstance().getLogger().severe("Could not save gui.yml");
        }
    }

    public static void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

}