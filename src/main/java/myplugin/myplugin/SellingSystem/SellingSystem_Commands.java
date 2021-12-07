package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SellingSystem_Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("subastas")) {

            Player player = (Player) sender;
            try {
                if (!MyPlugin.languageFile.exists()) {
                    MyPlugin.getMyPlugin().saveResource("Lang.yml", true);
                }
                MyPlugin.languageFile.load();

                if (args.length == 0) {
                    if (MyPlugin.selling_system.pages.get(1) == null) {
                        player.sendMessage(Tools.langText("Subastas_Empty"));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                        return false;
                    }
                    player.openInventory(MyPlugin.selling_system.pages.get(1));
                    player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_OPEN, 100, 1.4F);
                    player.playSound(player.getLocation(), Sound.ENTITY_PILLAGER_CELEBRATE, 100, 1.0F);
                    return true;
                }

                if (args[0].equalsIgnoreCase("sell")) {
                    if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)
                            || player.getInventory().getItemInMainHand().getType().equals(Material.VOID_AIR)) {
                        player.sendMessage(Tools.langText("Cannot_Sell_Air"));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                        return false;
                    }

                    int price = Integer.parseInt(args[1]);
                    YamlFile playerFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
                    if (MyPlugin.system.getByName(player.getName()) != null) {
                        playerFile = MyPlugin.system.getByName(player.getName()).getFile();
                    } else {
                        playerFile.load();
                    }

                    List<String> keys;

                    if(playerFile.getConfigurationSection("Items").getKeys(false) == null) {
                        keys = playerFile.getConfigurationSection("Items").getKeys(false).stream().toList();
                    }else{
                        keys = new ArrayList<>();
                    }
                    String key = Tools.getAlphaNumericString(6);

                    while (keys.contains(key)) {
                        key = Tools.getAlphaNumericString(6);
                    }

                    playerFile.getConfigurationSection("Items").createSection(key);
                    playerFile.set("Items." + key + ".Base64", Tools.itemStackToBase64(player.getInventory().getItemInMainHand()));
                    playerFile.set("Items." + key + ".Price", price);
                    playerFile.set("Items." + key + ".Expiration_Date", Tools.getDateAndTimeFormatted());
                    playerFile.set("Items." + key + ".Expired", false);
                    playerFile.save();
                    player.getInventory().getItemInMainHand().setAmount(0);
                    MyPlugin.selling_system.setUpSells();

                    String message = Tools.langText("Item_Auctioned");
                    message = message.replace("%price%", Integer.toString(price));
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 100, 1.0F);

                }


            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(Tools.colorMSG("&e-------------------------------------------------"));
                player.sendMessage(" ");
                player.sendMessage(Tools.colorMSG("&4&l/subastas &b- &7&oAbrir el inventario principalde subastas."));
                player.sendMessage(Tools.colorMSG("&4&l/subastas &a<sell> <precio>&b- &7&oSubastar el objeto en tu mano."));
                player.sendMessage(" ");
                player.sendMessage(Tools.colorMSG("&e-------------------------------------------------"));

            }
        } else if (command.getName().equalsIgnoreCase("monedas")) {
            Player player = (Player) sender;
            try {


                if (args.length == 0) {
                    player.sendMessage(Tools.colorMSG("&e&lMonedas &7&o(" + player.getName() + ")&a: &f&o" + Tools.getPlayerCoins(player)));
                    return false;
                }

                if (args[0].equalsIgnoreCase("player")) {
                    String onlinePlayer = args[1];
                    if (Bukkit.getPlayer(onlinePlayer) == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }

                    player.sendMessage(Tools.colorMSG("&e&lMonedas &7&o(" + onlinePlayer + ")&a: &f&o" + Tools.getPlayerCoins(Bukkit.getPlayer(onlinePlayer))));

                } else if (args[0].equalsIgnoreCase("top")) {
                    player.sendMessage(Tools.colorMSG("&e&l___________________________________"));
                    player.sendMessage(MyPlugin.selling_system.getTopString());
                    player.sendMessage(Tools.colorMSG("&e&l___________________________________"));
                } else if (args[0].equalsIgnoreCase("give")) {

                    if (!player.hasPermission("MyPlugin.Coins.Give")) {
                        player.sendMessage(Tools.langText("noPerm"));
                        return false;
                    }

                    Player toAdd = Bukkit.getPlayer(args[1]);

                    if (toAdd == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }

                    Tools.addPlayerCoins(toAdd, Integer.parseInt(args[2]));
                    String message = Tools.langText("Money_Added");
                    message = message.replace("%Coins%", args[2]);
                    message = message.replace("%receiver%", toAdd.getName());
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1.4F);
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!player.hasPermission("MyPlugin.Coins.Remove")) {
                        player.sendMessage(Tools.langText("noPerm"));
                        return false;
                    }
                    Player toAdd = Bukkit.getPlayer(args[1]);

                    if (toAdd == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }

                    Tools.removePlayerCoins(toAdd, Integer.parseInt(args[2]));
                    String message = Tools.langText("Money_Removed");
                    message = message.replace("%Coins%", args[2]);
                    message = message.replace("%receiver%", toAdd.getName());
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1.4F);
                } else if (args[0].equalsIgnoreCase("set")) {

                    if (!player.hasPermission("MyPlugin.Coins.Set")) {
                        player.sendMessage(Tools.langText("noPerm"));
                        return false;
                    }
                    Player toAdd = Bukkit.getPlayer(args[1]);

                    if (toAdd == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }

                    Tools.setPlayerCoins(toAdd, Integer.parseInt(args[2]));
                    String message = Tools.langText("Money_Set");
                    message = message.replace("%Coins%", args[2]);
                    message = message.replace("%receiver%", toAdd.getName());
                    player.sendMessage(message);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 100, 1.4F);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    MyPlugin.locations.locationsFile.load();
                    Tools.reloadLangFile();
                    player.sendMessage(Tools.colorMSG("DONE"));
                }


            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(Tools.colorMSG("&e-------------------------------------------------"));
                player.sendMessage(" ");
                player.sendMessage(Tools.colorMSG("&4&l/monedas &b- &7&oMirar tu balance de monedas."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &aplayer <player> &b- &7&oMirar las monedas de otro jugador."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &atop &b- &7&oMirar los top 20 balances."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &agive <player> <cantidad> &b- &7&oAgregar monedas a un jugador."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &aremove <player> <cantidad> &b- &7&oQuitar monedas a un jugador."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &aset <player> <cantidad> &b- &7&oEstablecer las monedas de un jugador."));
                player.sendMessage(Tools.colorMSG("&4&l/monedas &areload &b- &7&oRecarga los archivos de este plugin."));
                player.sendMessage(" ");
                player.sendMessage(Tools.colorMSG("&e-------------------------------------------------"));

            }
        }

        return false;
    }
}
