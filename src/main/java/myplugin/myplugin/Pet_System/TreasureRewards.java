package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TreasureRewards implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // /tr add/reload/list  <max> <min> <weight>
        if (command.getName().equalsIgnoreCase("treasureRewards")) {

            Player player = (Player) sender;

            if (!player.hasPermission("MyPlugin.TreasureRewards")) {
                player.sendMessage(Tools.langText("noPerm"));
                return false;
            }

            try {

                String option = args[0];

                switch (option.toLowerCase()) {
                    case "add" -> {
                        String identifier = Tools.getAlphaNumericString(5);
                        while (MyPlugin.system.treasureFile.getKeys(false).contains(identifier)) {
                            identifier = Tools.getAlphaNumericString(5);
                        }
                        int max = Integer.parseInt(args[1]);
                        int min = Integer.parseInt(args[2]);
                        int weight = Integer.parseInt(args[3]);

                        MyPlugin.system.treasureFile.createSection(identifier);
                        if(player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            MyPlugin.system.treasureFile.set(identifier + ".Item", "Nothing");
                        }else{
                            MyPlugin.system.treasureFile.set(identifier + ".Item", Tools.itemStackToBase64(player.getInventory().getItemInMainHand()));
                        }
                        List<String> commands = new ArrayList<>();
                        commands.add("say Default");
                        MyPlugin.system.treasureFile.set(identifier + ".Commands", commands);
                        List<String> messages = new ArrayList<>();
                        messages.add("Default");
                        MyPlugin.system.treasureFile.set(identifier + ".Messages", messages);
                        MyPlugin.system.treasureFile.set(identifier + ".Min", min);
                        MyPlugin.system.treasureFile.set(identifier + ".Max", max);
                        MyPlugin.system.treasureFile.set(identifier + ".Weight", weight);
                        MyPlugin.system.treasureFile.save();
                        player.sendMessage(Tools.hexColorMSG("&aSe creo un nuevo tesoro. Configuralo en los archivos! Se genero con la key &7" + identifier + "&a!"));
                    }
                    case "list" -> {
                        for (String key : MyPlugin.system.treasureFile.getKeys(false)) {
                            player.sendMessage(Tools.colorMSG("&4&l" + key + " &7 -> &aMin: " + MyPlugin.system.treasureFile.getInt(key + ".Min")
                                    + " &7 -> &6Max: " + MyPlugin.system.treasureFile.getInt(key + ".Max")
                                    + " &7 -> &dWeight: " + MyPlugin.system.treasureFile.getInt(key + ".Weight")));
                        }
                    }
                    case "reload" -> {
                        MyPlugin.system.generateTreasurePetFile();
                        player.sendMessage(Tools.colorMSG("&aSuccessfully reloaded this system."));
                        MyPlugin.system.loadKeys();
                    }
                    default -> {
                        player.sendMessage(Tools.colorMSG("&c&lUso: &7/tr add/list/reload <max> <min>"));
                    }
                }


            } catch (Exception e) {
                player.sendMessage(Tools.colorMSG("&c&lUso: &7/tr add/list <max> <min>"));
                e.printStackTrace();
            }
        }

        return false;
    }
}
