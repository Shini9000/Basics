package me.shini9000.basics.commands;

import me.shini9000.basics.Basics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Flight implements CommandExecutor {
    private final Basics plugin;

    public Flight(Basics plugin) {
        this.plugin = plugin;
        plugin.getCommand("flight").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}