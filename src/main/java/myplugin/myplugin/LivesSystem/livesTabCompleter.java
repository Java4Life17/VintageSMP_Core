package myplugin.myplugin.LivesSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class livesTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {


        if (command.getName().equalsIgnoreCase("corazones")) {
            List<String> options = Arrays.asList("set", "top", "cuenta");
            List<String> finalOne = new ArrayList<>();


            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("LivesSystem.SetHearts")) {
                        return null;
                    }
                }
                for (String s : options) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;
                // /gamemode set <player> <mode>
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("top")) {
                    List<String> canceled = new ArrayList<>();
                    return (canceled);
                }
                List<String> players = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }

                for (String s : players) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;

            }

        } else if (command.getName().equalsIgnoreCase("opItem")) {
            List<String> options = Arrays.asList("PetTreat", "SomethingElse");
            List<String> finalOne = new ArrayList<>();
            if (args.length == 1) {
                if (!sender.hasPermission("MyPlugin.OpItems")) {
                    return null;
                }
                for (String s : options) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;
            }
        } else if (command.getName().equalsIgnoreCase("monedas")) {
            List<String> options = Arrays.asList("player", "top", "give", "remove", "set", "reload");
            List<String> finalOne = new ArrayList<>();


            if (args.length == 1) {
                for (String s : options) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;
                // /gamemode set <player> <mode>
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("top")) {
                    List<String> canceled = new ArrayList<>();
                    return (canceled);
                }
                List<String> players = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    players.add(player.getName());
                }

                for (String s : players) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                        finalOne.add(s);
                    }
                }
                return finalOne;

            }

        }


        return null;
    }
}
