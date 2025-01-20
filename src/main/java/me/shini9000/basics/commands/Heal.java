package me.shini9000.basics.commands;

import me.shini9000.basics.Basics;
import me.shini9000.basics.files.Config;
import me.shini9000.basics.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Heal implements CommandExecutor {
    private final Basics plugin;

    public Heal(Basics plugin) {
        this.plugin = plugin;
        plugin.getCommand("heal").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var permConfig = Config.getConfig();
        double maxHealth = 20D;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOnly")));
            } else {
                Player p = (Player) sender;
                if (p.hasPermission("basics.command.heal") || p.hasPermission("basics.command.*") || p.hasPermission("basics.*")) {
                    if (p.getHealth() < maxHealth) {
                        p.setHealth(maxHealth);
                        p.spawnParticle(Particle.HEART, p.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                        p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Self")));
                    } else {
                        p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Self")));
                    }
                } else {
                    p.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission")));
                }
                return true;
            }

        } else if (args.length == 1) {
            // console can access this command
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
            } else if (target.isOnline()) {
                if (sender.hasPermission("basics.command.heal.others") || sender.hasPermission("basics.command.*") || sender.hasPermission("basics.*")) {
                    if (target.getHealth() < maxHealth) {
                        target.setHealth(maxHealth);
                        target.spawnParticle(Particle.HEART, target.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                        //sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getDisplayName())));
                        sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getName())));
                        target.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Alert.Args").replace("%playerName%", sender.getName())));
                        return true;
                    }
                } else {
                    sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission")));
                    return true;
                }
            } else {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOffline")));
                return true;
            }
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
        }
        return true;
    }
}