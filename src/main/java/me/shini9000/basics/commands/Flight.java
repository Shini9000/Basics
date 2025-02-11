package me.shini9000.basics.commands;

import me.shini9000.basics.Basics;
import me.shini9000.basics.files.Config;
import me.shini9000.basics.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Flight implements CommandExecutor {
    private final Basics plugin;

    public Flight(Basics plugin) {
        this.plugin = plugin;
        plugin.getCommand("flight").setExecutor(this);
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
            if (args.length == 1) { // if a target name is found
                return handleFlightFromConsole(sender, args[0], permConfig); // console run command
            } else {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOnly")));
                return true;
            }
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            return flightSelf(player, permConfig);
        } else if (args.length == 1) {
            if (args[0].matches(player.getName())) {
                return flightSelf(player, permConfig);
            } else {
                return flightOther(player, args[0], permConfig);
            }
        }
        sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.InvalidArguments")));
        return true;
    }

    public boolean flightSelf(Player player, ConfigurationSection permConfig) {
        if (!player.hasPermission("basics.command.flight")) { // perm check
            player.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // if perm is not found
            return true;
        }

        boolean isFlying = player.isFlying();
        if (isFlying) {
            toggleFlight(player, permConfig.getString("Commands.Flight.Toggle.Off.Self"), null);
        } else {
            toggleFlight(player, permConfig.getString("Commands.Flight.Toggle.On.Self"), null);
        }
        return true;
    }

    private boolean flightOther(Player sender, String targetName, ConfigurationSection permConfig) {
        if (!sender.hasPermission("basics.command.flight.others")) { // Perm check
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // perm not found
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
            return true;
        }

        if (target.isFlying()) {
            toggleFlight(target, permConfig.getString("Commands.Flight.Toggle.Off.Other"), null);
        } else {
            toggleFlight(target, permConfig.getString("Commands.Flight.Toggle.On.Other"), null);
            return true;
        }
        return true;
    }


    private boolean handleFlightFromConsole(CommandSender sender, String targetName, ConfigurationSection permConfig) { // flight command from console
        Player target = Bukkit.getPlayerExact(targetName); // get target [args]
        if (target == null || !target.isOnline()) { // check if player/target/[args] is online
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound"))); // not found
            return true;
        }

        if (target.isFlying()) {
            toggleFlight(target, permConfig.getString("Commands.Flight.Toggle.Off.Other"), null);
        } else {
            toggleFlight(target, permConfig.getString("Commands.Flight.Toggle.On.Other"), null);
            return true;
        }

        //toggleFlight(target, null, null);
        return true;
    }

    public boolean toggleFlight(Player player, String targetMessage, String senderMessage) {
        if () {

        }

        return true;
    }
}