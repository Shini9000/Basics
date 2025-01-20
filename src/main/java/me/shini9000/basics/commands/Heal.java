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
        // check for console /heal
        if (!(sender instanceof Player)) {
            // If the command was executed from the console
            if (args.length == 1) {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null || !target.isOnline()) {
                    // Player not found
                    sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
                } else {
                    // Heal the target player
                    target.setHealth(maxHealth);
                    target.spawnParticle(Particle.HEART, target.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                    target.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Alert.Args").replace("%playerName%", sender.getName())));
                    sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getName())));
                }
            } else {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOnly")));
            }
            return true;
        }


        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            // check if target is offline or nonExisting
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
            } else {
                Player p = (Player) sender;
                if (args[0].matches(p.getName())) {
                    if (p.hasPermission("basics.command.heal") || p.hasPermission("basics.command.*") || p.hasPermission("basics.*")) { // check for perms
                        if (p.getHealth() < maxHealth) { // is player executing the commands health is below 20
                            p.setHealth(maxHealth);
                            p.spawnParticle(Particle.HEART, p.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                            p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Self")));
                            return true;
                        } else {
                            p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Self")));
                        }
                    } else {
                        p.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission")));
                    }
                    return true;
                }
                if (args[0].matches(target.getName())) {
                    // if the sender has perms
                    if (sender.hasPermission("basics.command.heal.others") || sender.hasPermission("basics.command.*") || sender.hasPermission("basics.*")) {
                        if (target.getHealth() < maxHealth) {
                            target.setHealth(maxHealth);
                            target.spawnParticle(Particle.HEART, target.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                            sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getName())));
                            target.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Alert.Args").replace("%playerName%", sender.getName())));
                            return true;
                        }
                    } else {
                        sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission")));
                    }
                }
            }
        } else {
            //if (args.length == 0)
            Player p = (Player) sender;
            if (p.hasPermission("basics.command.heal") || p.hasPermission("basics.command.*") || p.hasPermission("basics.*")) { // check for perms
                if (p.getHealth() < maxHealth) { // is player executing the commands health is below 20
                    p.setHealth(maxHealth);
                    p.spawnParticle(Particle.HEART, p.getLocation().add(0.0D, 1.2D, 0.0D), 3);
                    p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.Success.Self")));
                    return true;
                } else {
                    p.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Self")));
                }
            } else {
                p.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission")));
            }
            return true;

        }
        return true;
    }
}