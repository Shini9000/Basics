package me.shini9000.basics.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private static File file;
    private static FileConfiguration permConfig;

    // Generates config
    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("Basics").getDataFolder(), "PermissionConfig.yml");
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        permConfig = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return permConfig;
    }

    public static void save() {
        try {
            permConfig.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public static void reload() {
        permConfig = YamlConfiguration.loadConfiguration(file);
    }
}