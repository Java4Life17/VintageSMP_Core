package myplugin.myplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RestartSystem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("custodian")) {
            if (sender instanceof ConsoleCommandSender) {
                int tier2 = 0;
                int tier3 = 90;
                int tier4 = 140;
                MyPlugin.whiteList = true;

                List<String> names = new ArrayList<>();


                for (Player player : MyPlugin.getMyPlugin().getServer().getOnlinePlayers()) {
                    YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
                    names.add(player.getName());

                    try {
                        yamlFile.load();
                        if (yamlFile.contains("Pet_Items")) {
                            yamlFile.set("Pet_Items", null);
                            yamlFile.createSection("Pet_Items");
                            switch (MyPlugin.system.getByName(player.getName()).getPetTier()) {
                                case 1, 2 -> yamlFile.set("TimeLeft", tier2);
                                case 3 -> yamlFile.set("TimeLeft", tier3);
                                case 4 -> yamlFile.set("TimeLeft", tier4);
                            }
                            yamlFile.set("Offline", false);
                            yamlFile.save();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player.getName() + " El servidor " +
                            "se esta reiniciando.");
                }
                Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                        "restart"), 15 * 20L);

                File[] files = new File("plugins/MyPlugin/SellingSystem").listFiles();

                if (files == null) {
                    return false;
                }

                for (File file : files) {
                    String name = file.getName();
                    name = name.replace(".yml", "");
                    if (!names.contains(name)) {
                        YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + file.getName());
                        try {
                            yamlFile.load();
                            if (yamlFile.contains("Pet_Items")) {
                                yamlFile.set("Pet_Items", null);
                                yamlFile.createSection("Pet_Items");
                                switch (MyPlugin.system.getByName(sender.getName()).getPetTier()) {
                                    case 1, 2 -> yamlFile.set("TimeLeft", tier2);
                                    case 3 -> yamlFile.set("TimeLeft", tier3);
                                    case 4 -> yamlFile.set("TimeLeft", tier4);
                                }
                                yamlFile.set("Offline", false);
                                yamlFile.save();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        if(command.getName().equalsIgnoreCase("mwhitelist")){
            if(sender instanceof Player) {
                if (!sender.hasPermission("VintageSMP.Whitelist")) {
                    sender.sendMessage(Tools.colorMSG("&cNo Permission for this command!"));
                    return false;
                }
            }

            try {

                if(args[0].equalsIgnoreCase("add")){
                    String playerName = args[1];

                    List<String> names = MyPlugin.languageFile.getStringList("Whitelist");
                    names.add(playerName);
                    MyPlugin.languageFile.set("Whitelist", names);
                    MyPlugin.languageFile.save();
                    sender.sendMessage(ChatColor.GREEN + "Added " + args[1]);

                }

                if(args[0].equalsIgnoreCase("remove")){
                    List<String> names = MyPlugin.languageFile.getStringList("Whitelist");
                    names.remove(args[1]);
                    MyPlugin.languageFile.set("Whitelist", names);
                    MyPlugin.languageFile.save();
                    sender.sendMessage(ChatColor.GREEN + "Removed " + args[1]);
                }
                if(args[0].equalsIgnoreCase("on")){
                    MyPlugin.ipControl.whitelist = true;
                    sender.sendMessage(ChatColor.GREEN + "Whitelist on ");
                }
                if(args[0].equalsIgnoreCase("off")){
                    MyPlugin.ipControl.whitelist = false;
                    sender.sendMessage(ChatColor.GREEN + "Whitelist off");
                }

            }catch (Exception e){
                sender.sendMessage(ChatColor.RED + "Uso: /mwhitelist add/remove <player>");
            }

         }

        return false;
    }
}
