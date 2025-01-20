package me.shini9000.basics;

import me.shini9000.basics.commands.Heal;
import me.shini9000.basics.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class Basics extends JavaPlugin {
    public static Basics THIS;
    private BukkitTask task;


    @Override
    public void onEnable() {
        PluginDescriptionFile pdfFile = getDescription();
        Bukkit.getConsoleSender().sendMessage("Basics loading version " + pdfFile.getVersion());
        getLogger().info("Basics enabling!");

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Config.setup();
        // Error for configs
        Config.getConfig().addDefault("Error.Command.PlayerOnly", "&#FF0000Only players can execute this command");
        Config.getConfig().addDefault("Error.Command.Permission", "&#FF0000You don't have permission to execute this command");
        Config.getConfig().addDefault("Error.Command.InvalidArgs", "&#FF0000Invalid argument");
        Config.getConfig().addDefault("Error.Command.PlayerNotFound", "&#FF0000This player was not found");
        Config.getConfig().addDefault("Error.Command.PlayerOffline", "&#FF0000This player is currently offline");
        // Commands config gen , HEAL
        Config.getConfig().addDefault("Commands.Heal.FullHealth.Self", "&#ffc0cbYou already have full health!");
        Config.getConfig().addDefault("Commands.Heal.FullHealth.Args", "&#FFD700%playerName% &#ffc0cbalready has full health!");
        Config.getConfig().addDefault("Commands.Heal.Success.Self", "&#70ff70You are now healed!");
        Config.getConfig().addDefault("Commands.Heal.Success.Args", "&#70ff70You have healed &#FFD700%playerName%&#70ff70!");
        Config.getConfig().addDefault("Commands.Heal.Alert.Args", "&#70ff70You have been healed by &#FFD700%playerName%&#70ff70!");
        // Commands config gen, FEED
        Config.getConfig().options().copyDefaults(true);
        Config.save();

        //commands
        new Heal(this);

        getLogger().info("Basics enabled!");
        THIS = this;
    }

    @Override
    public void onDisable() {
        getLogger().info("Basics disabling!");
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
        getLogger().info("Basics disabled!");
    }

    public static Basics getInstance() {
        return getPlugin(Basics.class);
    }
}
