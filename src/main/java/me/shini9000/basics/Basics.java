package me.shini9000.basics;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Basics extends JavaPlugin {
    private BukkitTask task;


    @Override
    public void onEnable() {
        getLogger().info("Basics enabling!");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Basics getInstance() {
        return getPlugin(Basics.class);
    }
}
