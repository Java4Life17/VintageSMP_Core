package myplugin.myplugin.LivesSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LivesCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("corazones")) {
            Player player = (Player) sender;

            try {
                if (args.length == 0) {
                    double hearts = (int) (Tools.getPlayerHearts(player) / 2);
                    StringBuilder toSend = new StringBuilder();
                    for (int i = 0; i < hearts; i++) {
                        toSend.append(Tools.colorMSG("&4❤"));
                        if (i == (hearts - 1)) {
                            toSend.append(Tools.colorMSG(" &a[&7")).append(hearts).append(Tools.colorMSG("&a]"));
                        }
                    }
                    player.sendMessage(Tools.colorMSG(toSend.toString()));
                    return true;
                }

                if (args[0].equalsIgnoreCase("cuenta")) {
                    String playerName = args[1];
                    if (Bukkit.getServer().getPlayer(playerName) == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }

                    double hearts = (int) (Tools.getPlayerHearts(Bukkit.getPlayer(playerName)) / 2);
                    StringBuilder toSend = new StringBuilder();
                    for (int i = 0; i < hearts; i++) {
                        toSend.append(Tools.colorMSG("&4❤"));
                        if (i == (hearts - 1)) {
                            toSend.append(Tools.colorMSG(" &a[&7")).append(hearts).append(Tools.colorMSG("&a]"));
                        }
                    }
                    player.sendMessage(Tools.colorMSG(toSend.toString()));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    if (!player.hasPermission("LivesSystem.SetHearts")) {
                        player.sendMessage(Tools.langText("NoPerm"));
                        return false;
                    }

                    String playerName = args[1];
                    int amount = Integer.parseInt(args[2]);
                    Tools.updatePlayerHearts(Bukkit.getPlayer(playerName), amount * 2);
                    String text = Tools.langText("Hearts_Set");
                    text = text.replace("%player%", playerName);
                    text = text.replace("%count%", Integer.toString(amount));
                    player.sendMessage(text);
                    return true;
                }

                if (args[0].equalsIgnoreCase("top")) {

                    Map<String, Integer> records = new HashMap<>();

                    for (String key : MyPlugin.livesSystem.heartsFile.getKeys(false)) {
                        int toAdd = (int) MyPlugin.livesSystem.heartsFile.getDouble(key);
                        toAdd = toAdd / 2;
                        records.put(key, toAdd);
                    }

                    StringBuilder builder = new StringBuilder();
                    builder.append("\n\n&b&l&n___________________________\n\n");
                    int count = 1;

                    Map<String, Integer> sorted = records.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .limit(10)
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                    for(Map.Entry<String, Integer> entry : sorted.entrySet()){

                        builder.append("&6&l ").append(count).append("&7-&7&o ").append(entry.getKey())
                                .append(" &8[").append(entry.getValue()).append("&4❤&8]\n");
                        count++;
                    }
                    builder.append("&b&l&n___________________________\n\n\n");
                    player.sendMessage(Tools.colorMSG(builder.toString()));

                }


            } catch (Exception e) {
                player.sendMessage(Tools.colorMSG("&cUso -> &7&o/corazones <cuenta/set/top> <jugador> || <cantidad>"));
            }
        }

        return false;
    }
}
