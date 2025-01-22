package me.shini9000.basics.commands;

import me.shini9000.basics.Basics;
import me.shini9000.basics.files.Config;
import me.shini9000.basics.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Feed implements CommandExecutor {
    private final Basics plugin;

    public Feed(Basics plugin) {
        this.plugin = plugin;
        plugin.getCommand("feed").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Load the config/s
        var permConfig = Config.getConfig();
        if (permConfig == null) {
            sender.sendMessage("Configuration is not loaded.");
            return true;
        }

        if (!(sender instanceof Player)) { // if sender is not a player
            if (args.length == 1) { // if command contains an arg (/feed <arg>
                return handleFeedFromConsole(sender, args[0], permConfig); // console can run
            } else { // arg not found console error
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOnly")));
                return true;
            }
        }

        Player player = (Player) sender;
        if (args.length == 0) { // if player sends command and args are not written
            return feedSelf(player, permConfig);
        } else if (args.length == 1) { // if args are found
            if (args[0].matches(player.getName())) {
                return feedSelf(player, permConfig); // Feeds self (args = command user)
            } else {
                return feedOther(player, args[0], permConfig); // Feeds playername based on args
            }

        }
        // else invalid arguments are met
        sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.InvalidArguments")));
        return true;
    }

    private boolean feedSelf(Player player, ConfigurationSection permConfig) { // Feed self
        if (!player.hasPermission("basics.command.feed")) { // perm check
            player.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // if perm is not found
            return true;
        }
        int maxFoodLevel = 20;
        if (player.getFoodLevel() >= maxFoodLevel) { // if perm is found and food if over 20. Decline feed
            player.sendMessage(Utils.colorize(permConfig.getString("Commands.Feed.Full.Self")));
        } else { // else Feed
            feedPlayer(player, permConfig.getString("Commands.Feed.Success.Self"), null);
        }
        return true;

    }

    private boolean feedOther(Player sender, String targetName, ConfigurationSection permConfig) {
        if (!sender.hasPermission("basics.command.feed.others")) { // Perm check
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // perm not found
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName); // get target name [args]
        if (target == null || !target.isOnline()) { // if player is not found or offline
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
            return true;
        }

        int maxFoodLevel = 20;
        if (target.getFoodLevel() >= maxFoodLevel) { // if target has full food
            sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Feed.Full.Others").replace("%playerName%", target.getName()))); // dont feed and state target has full food
        } else {
            feedPlayer(target, permConfig.getString("Commands.Feed.Alert.Args").replace("%playerName%", sender.getName()),
                    permConfig.getString("Commands.Feed.Success.Args").replace("%playerName%", target.getName())); // feed target
            return true;
        }
        return true;
    }


    private boolean handleFeedFromConsole(CommandSender sender, String targetName, ConfigurationSection permConfig) { // feed command from console
        Player target = Bukkit.getPlayerExact(targetName); // get target [args]
        if (target == null || !target.isOnline()) { // check if player/target/[args] is online
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound"))); // not found
            return true;
        }
        // if found do
        feedPlayer(target, permConfig.getString("Commands.Feed.Alert.Args").replace("%playerName%", sender.getName()),
                permConfig.getString("Commands.Feed.Success.Args").replace("%playerName%", target.getName())); // feed success
        return true;
    }

    // Feed process
    private void feedPlayer(Player target, String targetMessage, String senderMessage) {
        int maxFoodLevel = 20; // get max food
        target.setFoodLevel(maxFoodLevel); // set food
        target.spawnParticle(Particle.CRIT, target.getLocation().add(0.0D, 1.2D, 0.0D), 3); // spawn particle
        target.playSound(target, Sound.ENTITY_PLAYER_BURP, 1, 1);

        if (targetMessage != null) {
            target.sendMessage(Utils.colorize(targetMessage)); // send target feed by message
        }

        if (senderMessage != null) {
            target.getServer().getConsoleSender().sendMessage(Utils.colorize(senderMessage)); // send sender feed by message
        }
    }
}