package myplugin.myplugin.Pet_System;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TreasureRewards_TabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("treasureRewards")){
            if(!sender.hasPermission("MyPlugin.TreasureRewards")){
                return new ArrayList<>();
            }

            List<String> options = Arrays.asList("add", "reload", "list");
            List<String> finalOne = new ArrayList<>();

            if(args.length == 1){
                for(String option : options){
                    if(option.toLowerCase().startsWith(args[0].toLowerCase())){
                        finalOne.add(option);
                    }
                }

                return finalOne;
            }
            //<max> <min> <weight>
            if(args.length == 2){
                if(!args[0].equalsIgnoreCase("add")){
                    return new ArrayList<>();
                }else{
                    finalOne.add("max");
                    return finalOne;
                }
            }

            if(args.length == 3){
                if(!args[0].equalsIgnoreCase("add")){
                    return new ArrayList<>();
                }else{
                    finalOne.add("min");
                    return finalOne;
                }
            }

            if(args.length == 4){
                if(!args[0].equalsIgnoreCase("add")){
                    return new ArrayList<>();
                }else{
                    finalOne.add("weight");
                    return finalOne;
                }
            }


        }

        return null;
    }
}
