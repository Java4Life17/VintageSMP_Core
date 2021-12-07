package myplugin.myplugin.Server_Selling;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SellingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("SellPlayerItem")) {
            Player player = (Player) sender;

            if (!(player.isOp())) {
                return false;
            }
            try {
                //SellPlayerItem playerName
                Player receiver = Bukkit.getPlayer(args[0]);
                assert receiver != null;
                Material material = receiver.getInventory().getItemInMainHand().getType();
                int amount = receiver.getInventory().getItemInMainHand().getAmount();

                if (material.equals(Material.AIR) || material == Material.AIR) {
                    return false;
                }

                if (!MyPlugin.server_selling.items_and_prices.containsKey(material)) {
                    receiver.sendMessage(Tools.langText("Item_Not_Selling"));
                    return false;
                }

                receiver.openInventory(Sell_Inventory.getSellConfirmation(material, amount));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (command.getName().equalsIgnoreCase("setPrice")) {
            Player player = (Player) sender;
            try {


            if (!player.hasPermission("MyPlugin.Selling.SetValue")) {
                player.sendMessage(Tools.langText("noPerm"));
                return false;
            }

            Material material = player.getInventory().getItemInMainHand().getType();



            Tools.addNewPriceToItem(material, Integer.parseInt(args[0]));
            player.sendMessage(Tools.colorMSG("&7&oAdded &a" + material.toString() + "&7&o for &a&o" + args[0]));
            }catch (Exception e){
                player.sendMessage(Tools.colorMSG("&c&o/setPrice <price as integer>"));
            }
        }

        return true;
    }
}
