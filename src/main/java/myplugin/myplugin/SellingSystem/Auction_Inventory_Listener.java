package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Auction_Inventory_Listener implements Listener {

    @EventHandler
    public void onAuctionMenuClick(InventoryClickEvent event) throws IOException {
        if (event.getView().getTitle().startsWith(Tools.colorMSG("&e&lSUBASTAS &7&oPagina "))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            List<Integer> display_Pane_Slots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 36, 37, 39, 40, 41, 43, 44, 46, 47, 48, 50, 51, 52, 45, 49, 53);

            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getSlot() == 49) {
                player.openInventory(Player_Settings_Menu.getOptionsInventory(player.getName()));
            }
            if (display_Pane_Slots.contains(event.getSlot())) {
                return;
            }


            String pageString = event.getView().getTitle();
            pageString = ChatColor.stripColor(pageString);
            pageString = pageString.replace("SUBASTAS Pagina ", "");
            int number = Integer.parseInt(pageString);
            if (event.getSlot() == 53 && Objects.requireNonNull(event.getInventory().getItem(53)).getType() == Material.ARROW) {
                player.openInventory(MyPlugin.selling_system.pages.get(number + 1));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 1.9F);
            }
            if (event.getSlot() == 45 && Objects.requireNonNull(event.getInventory().getItem(45)).getType() == Material.ARROW) {
                player.openInventory(MyPlugin.selling_system.pages.get(number - 1));
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 1.9F);
            }


            Selling_Item selling = null;

            for (Selling_Item selling_item : MyPlugin.selling_system.itemsList) {
                if (selling_item.getAsDisplay().equals(event.getCurrentItem())) {
                    selling = selling_item;
                }
            }


            if (!MyPlugin.selling_system.itemsList.contains(selling)) {
                player.sendMessage(Tools.langText("Item_Already_Sold"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                player.closeInventory();
                return;
            }
            assert selling != null;
            int price = selling.getItem_Price();
            if (Tools.getPlayerCoins(player) < price) {
                player.sendMessage(Tools.langText("Not_Enough_Money"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                player.closeInventory();
                return;
            }

            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + selling.getSeller_Name() + ".yml");

            try {
                yamlFile.load();
                if (!yamlFile.getConfigurationSection("Items").contains(selling.getItem_ID())) {
                    player.sendMessage(Tools.langText("Item_Already_Sold"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                    player.closeInventory();
                    return;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            //REMINDER:: Make a method that won't allow a player to buy an item if he does not have enough space
            //on his inventory.

            player.getInventory().addItem(selling.getRawItem());
            Tools.removePlayerCoins(player, price);
            Tools.addSoldPlayerCoins(selling.getSeller_Name(), price);
            selling.deleteSellingItem();
            MyPlugin.selling_system.setUpSells();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.4F);
            player.closeInventory();

            player.sendMessage(Tools.langText("Item_Purchased"));


        }
    }

}
