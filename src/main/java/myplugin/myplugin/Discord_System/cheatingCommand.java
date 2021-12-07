package myplugin.myplugin.Discord_System;

import myplugin.myplugin.MyPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class cheatingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("discordCheater")){
            if(!(sender instanceof ConsoleCommandSender)){
                return false;
            }

            Objects.requireNonNull(MyPlugin.discordSystem.getBot().getTextChannelById("896445241333252136")).sendMessage(
                    "**El jugador " + args[0] + " ah sido detectado usando el hack " + args[1] + ".**"
            ).queue();

        }

        return false;
    }
}
