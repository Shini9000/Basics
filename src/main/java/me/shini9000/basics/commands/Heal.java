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

public class Heal implements CommandExecutor {
    private final Basics plugin;

    public Heal(Basics plugin) {
        this.plugin = plugin;
        plugin.getCommand("heal").setExecutor(this);
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
            if (args.length == 1) { // if command contains an arg (/heal <arg>
                return handleHealingFromConsole(sender, args[0], permConfig); // console can run
            } else { // arg not found console error
                sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerOnly")));
                return true;
            }
        }

        Player player = (Player) sender;
        if (args.length == 0) { // if player sends command and args are not written
            return healSelf(player, permConfig);
        } else if (args.length == 1) { // if args are found
            if (args[0].matches(player.getName())) {
                return healSelf(player, permConfig); // Heals self (args = command user)
            } else {
                return healOther(player, args[0], permConfig); // Heals playername based on args
            }

        }
        // else invalid arguments are met
        sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.InvalidArguments")));
        return true;
    }

    private boolean healSelf(Player player, ConfigurationSection permConfig) { // Heal self
        if (!player.hasPermission("basics.command.heal")) { // perm check
            player.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // if perm is not found
            return true;
        }

        if (player.getHealth() >= player.getMaxHealth()) { // if perm is found and health if over 20. Decline heal
            player.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Self")));
        } else { // else Heal
            healPlayer(player, permConfig.getString("Commands.Heal.Success.Self"), null);
        }
        return true;

    }

    private boolean healOther(Player sender, String targetName, ConfigurationSection permConfig) {
        if (!sender.hasPermission("basics.command.heal.others")) { // Perm check
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.Permission"))); // perm not found
            return true;
        }

        Player target = Bukkit.getPlayerExact(targetName); // get target name [args]
        if (target == null || !target.isOnline()) { // if player is not found or offline
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound")));
            return true;
        }

        //if (target.getName().equals(sender.getName())) {
        //    if (target.getHealth() >= target.getMaxHealth()) {
        //        sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Self")));
        //        return true;
        //    } else {
        //        healSelf(sender, permConfig);
        //        return true;
        //    }
        //}

        if (target.getHealth() >= target.getMaxHealth()) { // if target has full health
            sender.sendMessage(Utils.colorize(permConfig.getString("Commands.Heal.FullHealth.Others").replace("%playerName%", target.getName()))); // dont heal and state target has full health
        } else {
            healPlayer(target, permConfig.getString("Commands.Heal.Alert.Args").replace("%playerName%", sender.getName()),
                    permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getName())); // heal target
            return true;
        }
        return true;
    }


    private boolean handleHealingFromConsole(CommandSender sender, String targetName, ConfigurationSection permConfig) { // heal command from console
        Player target = Bukkit.getPlayerExact(targetName); // get target [args]
        if (target == null || !target.isOnline()) { // check if player/target/[args] is online
            sender.sendMessage(Utils.colorize(permConfig.getString("Error.Command.PlayerNotFound"))); // not found
            return true;
        }
        // if found do
        healPlayer(target, permConfig.getString("Commands.Heal.Alert.Args").replace("%playerName%", sender.getName()),
                permConfig.getString("Commands.Heal.Success.Args").replace("%playerName%", target.getName())); // heal success
        return true;
    }

    // Heal process
    private void healPlayer(Player target, String targetMessage, String senderMessage) {
        double maxHealth = target.getMaxHealth(); // get maxHealth
        target.setHealth(maxHealth); // set health
        target.spawnParticle(Particle.HEART, target.getLocation().add(0.0D, 1.2D, 0.0D), 3); // spawn particle
        target.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        if (targetMessage != null) {
            target.sendMessage(Utils.colorize(targetMessage)); // send target heal by message
        }

        if (senderMessage != null) {
            target.getServer().getConsoleSender().sendMessage(Utils.colorize(senderMessage)); // send sender heal by message
        }
    }
}