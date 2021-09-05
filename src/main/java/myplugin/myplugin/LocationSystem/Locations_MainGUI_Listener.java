package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class Locations_MainGUI_Listener implements Listener {
    private MyPlugin plugin;

    public Locations_MainGUI_Listener(MyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Tools.colorMSG("&0&lDISTRICT GUI"))) {
            event.setCancelled(true);


            Player player = (Player) event.getWhoClicked();

            if (event.getSlot() == 11) {
                int i = 1;
                player.sendMessage("\n\n");
                for (String key : MyPlugin.locations.locationsFile.getKeys(false)) {
                    player.sendMessage(Tools.colorMSG("&4&l&o" + i + ". &7&o" + key));
                    i++;
                }
                player.sendMessage("\n\n");
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 0.8F);
                return;
            }

            if (event.getSlot() == 13) {

                new AnvilGUI.Builder()
                        .onComplete((user, text) -> {                                    //called when the inventory output slot is clicked
                            if (!MyPlugin.locations.locationsFile.contains(text)) {
                                MyPlugin.locations.locationsFile.createSection(text);
                                user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1.1F);
                                user.sendMessage(Tools.langText("District_Created"));
                                Tools.saveLocationsFile();
                                return AnvilGUI.Response.close();
                            } else {
                                user.playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 1.1F);
                                user.sendMessage(Tools.langText("District_Exists"));
                                return AnvilGUI.Response.close();
                            }
                        })
                        .text("DISTRICT NAME")                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.IRON_SWORD))                      //use a custom item for the first slot
                        .itemRight(new ItemStack(Material.IRON_SWORD))                     //use a custom item for the second slot
                        .onLeftInputClick(user -> user.sendMessage(Tools.colorMSG("&cPlease type a district Name!")))     //called when the left input slot is clicked
                        .onRightInputClick(user -> user.sendMessage(Tools.colorMSG("&cPlease type a district Name!")))   //called when the right input slot is clicked
                        .title("TYPE A NAME")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(plugin)                                          //set the plugin instance
                        .open(player);
                return;

            }

            if (event.getSlot() == 15) {
                player.openInventory(Locations_MainGUI.getListToEdit());
                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 1.5F);
                return;
            }


        }

    }
}
