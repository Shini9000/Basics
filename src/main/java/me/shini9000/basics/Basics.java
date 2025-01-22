package me.shini9000.basics;

import me.shini9000.basics.commands.Feed;
import me.shini9000.basics.commands.Flight;
import me.shini9000.basics.commands.Heal;
import me.shini9000.basics.files.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

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
        Config.getConfig().options().setHeader(List.of("All", "TEST", "123"));
        Config.getConfig().addDefault("CONFIG_VERSION", "1.0.6");
        Config.getConfig().addDefault("Error.Command.PlayerOnly", "&#FF0000Only players can execute this command");
        Config.getConfig().addDefault("Error.Command.Permission", "&#FF0000You don't have permission to execute this command");
        Config.getConfig().addDefault("Error.Command.InvalidArgs", "&#FF0000Invalid argument");
        Config.getConfig().addDefault("Error.Command.PlayerNotFound", "&#FF0000This player was not found");
        Config.getConfig().addDefault("Error.Command.PlayerOffline", "&#FF0000This player is currently offline");

        // Commands config gen , HEAL
        Config.getConfig().addDefault("Commands.Heal.Alert.Args", "&#70ff70You have been healed by &#FFD700%playerName%&#70ff70!");
        Config.getConfig().addDefault("Commands.Heal.FullHealth.Args", "&#FFD700%playerName% &#ffc0cbalready has full health!");
        Config.getConfig().addDefault("Commands.Heal.FullHealth.Self", "&#ffc0cbYou already have full health!");
        Config.getConfig().addDefault("Commands.Heal.Success.Args", "&#70ff70You have healed &#FFD700%playerName%&#70ff70!");
        Config.getConfig().addDefault("Commands.Heal.Success.Self", "&#70ff70You are now healed!");

        // Commands config gen, FEED
        Config.getConfig().addDefault("Commands.Feed.Alert.Args", "&#70ff70You have been fed by &#FFD700%playerName%&#70ff70!");
        Config.getConfig().addDefault("Commands.Feed.Full.Args", "&#FFD700%playerName% &#ffc0cbalready has full hunger!");
        Config.getConfig().addDefault("Commands.Feed.Full.Self", "&#ffc0cbYou already have full hunger!");
        Config.getConfig().addDefault("Commands.Feed.Success.Args", "&#70ff70You have fed &#FFD700%playerName%&#70ff70!");
        Config.getConfig().addDefault("Commands.Feed.Success.Self", "&#70ff70You are now fed!");

        // Commands config gen, FLIGHT
        Config.getConfig().addDefault("Commands.Flight.Alert.Off.Args", "&#FFD700%playerName% &#ffc0cbhas disabled your ability to fly!");
        Config.getConfig().addDefault("Commands.Flight.Alert.On.Args", "&#FFD700%playerName% &#70ff70has enabled your ability to fly!");
        Config.getConfig().addDefault("Commands.Flight.Toggle.Off.Args", "&#FFD700%playerName%'s &#ffc0cbability to fly has been disabled!");
        Config.getConfig().addDefault("Commands.Flight.Toggle.On.Args", "&#FFD700%playerName%&'s #70ff70ability to fly has been enabled!");
        Config.getConfig().addDefault("Commands.Flight.Toggle.Off.Self", "&#ffc0cbYour ability to fly has been disabled!");
        Config.getConfig().addDefault("Commands.Flight.Toggle.On.Self", "#70ff70Your ability to fly has been enabled!");

        // save and copy
        Config.getConfig().options().copyDefaults(true);
        Config.save();
        // Listeners
        //getServer().getPluginManager().registerEvent(new JoinLeave(), this);

        // Commands
        new Heal(this);
        new Feed(this);
        new Flight(this);

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
