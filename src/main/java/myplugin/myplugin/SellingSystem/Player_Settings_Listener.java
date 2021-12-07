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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Player_Settings_Listener implements Listener {

    @EventHandler
    public void onPlayerSettings(InventoryClickEvent event) throws IOException {
        if (event.getView().getTitle().equalsIgnoreCase(Tools.colorMSG("&2Configuracion de vendedor."))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            List<Integer> pane_Slots = Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
            if(pane_Slots.contains(event.getRawSlot())){
                return;
            }
            if(event.getRawSlot() == 4){
                int coins_earned = 0;

                YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
                try {
                    if (MyPlugin.system.getByName(player.getName()) != null) {
                        yamlFile = MyPlugin.system.getByName(player.getName()).getFile();
                    } else {
                        yamlFile.load();
                    }
                    coins_earned = yamlFile.getInt("Ganancia");

                    if(coins_earned == 0){
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1.1F);
                        return;
                    }

                    Tools.addPlayerCoins(player, coins_earned);
                    yamlFile.set("Ganancia", 0);
                    yamlFile.save();
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100, 1.4F);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100, 1.4F);
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 100, 1.4F);
                    player.closeInventory();
                    String toSend = Tools.langText("Money_Claimed");
                    toSend = toSend.replace("%Coins%",Integer.toString(coins_earned));
                    player.sendMessage(toSend);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            ItemStack current = event.getCurrentItem();
            assert current != null;
            ItemMeta meta = current.getItemMeta();
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;
            String key = lore.get(lore.size() - 2);
            key = ChatColor.stripColor(key);

            ItemStack toGive = null;

            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");



            try {

                if (MyPlugin.system.getByName(player.getName()) != null) {
                    yamlFile = MyPlugin.system.getByName(player.getName()).getFile();
                } else {
                    yamlFile.load();
                }
                if(!yamlFile.getConfigurationSection("Items").contains(key)){
                    player.sendMessage(Tools.langText("Item_Already_Sold"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                    player.closeInventory();
                    return;
                }

                toGive = Tools.itemStackFromBase64(yamlFile.getString("Items." + key + ".Base64"));

            }catch (Exception e ){
                e.printStackTrace();
            }



            boolean emptySlot = false;

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null || Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getType(), Material.AIR) ||
                        Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getType(), Material.VOID_AIR)) {
                    emptySlot = true;
                }
            }

            if(!emptySlot){
                player.sendMessage(Tools.langText("No_Empty_Slot"));
                player.closeInventory();
                return;
            }


            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 1.0F);
            player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 100, 0.4F);
            player.getInventory().addItem(toGive);

            try {
                yamlFile.set("Items." + key, null);
                yamlFile.save();
            }catch (Exception e){
                e.printStackTrace();
            }

            player.openInventory(Player_Settings_Menu.getOptionsInventory(player.getName()));

        }
    }

}
