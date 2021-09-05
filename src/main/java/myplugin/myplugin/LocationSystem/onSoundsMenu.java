package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class onSoundsMenu implements Listener {

    private MyPlugin plugin;

    public onSoundsMenu(MyPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onSoundMenu(InventoryClickEvent event) {


        if (event.getView().getTitle().startsWith(Tools.colorMSG("&4&lSOUNDS &d-"))) {
            event.setCancelled(true);
            String title = ChatColor.stripColor(event.getView().getTitle());
            String[] titleComponents = title.split(" ");
            String identifier = titleComponents[2];
            String sound;
            String[] nameSplit;
            int nextPage;
            SoundsMenuCreator creator;
            if (event.getSlot() == 45 && ((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getType().equals(Material.ARROW)) {
                sound = ChatColor.stripColor(((ItemMeta) Objects.requireNonNull(((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getItemMeta())).getDisplayName());
                nameSplit = sound.split(" ");
                nextPage = Integer.parseInt(nameSplit[2]);
                creator = new SoundsMenuCreator(identifier);
                event.getWhoClicked().openInventory(creator.getSoundPage(nextPage));
                return;
            }

            if (event.getSlot() == 53 && ((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getType().equals(Material.ARROW)) {
                sound = ChatColor.stripColor(((ItemMeta) Objects.requireNonNull(((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getItemMeta())).getDisplayName());
                nameSplit = sound.split(" ");
                nextPage = Integer.parseInt(nameSplit[1]);
                creator = new SoundsMenuCreator(identifier);
                event.getWhoClicked().openInventory(creator.getSoundPage(nextPage));
                return;
            }

            if (((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                return;
            }

            if (((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getType().equals(Material.ARROW)) {
                return;
            }

            sound = ChatColor.stripColor(((ItemMeta) Objects.requireNonNull(((ItemStack) Objects.requireNonNull(event.getCurrentItem())).getItemMeta())).getDisplayName());
            if (event.getClick().isLeftClick()) {
                Player player = (Player) event.getWhoClicked();
                new AnvilGUI.Builder()
                        .onComplete((player1, text) -> {
                            try {
                                int pitch = Integer.parseInt(text);
                                if (pitch < 0) {
                                    pitch = 0;
                                }

                                if (pitch > 100) {
                                    pitch = 100;
                                }

                                MyPlugin.locations.locationsFile.set(identifier + ".Sound", sound + " " + pitch);
                                Tools.saveLocationsFile();
                            } catch (Exception var6) {
                                String messageToSend = Tools.colorMSG("&c This is not an instance of a number. Make sure your input is a number!");
                                player1.sendMessage(messageToSend);
                                return AnvilGUI.Response.close();
                            }
                            return AnvilGUI.Response.close();
                        }).text(Tools.colorMSG("&00 - 100"))
                        .itemLeft(new ItemStack(Material.IRON_SWORD))                      //use a custom item for the first slot
                        .itemRight(new ItemStack(Material.IRON_SWORD))                     //use a custom item for the second slot
                        .onLeftInputClick(user -> user.sendMessage(Tools.colorMSG("&cPlease type a district Name!")))     //called when the left input slot is clicked
                        .onRightInputClick(user -> user.sendMessage(Tools.colorMSG("&cPlease type a district Name!")))   //called when the right input slot is clicked
                        .title("TYPE A PITCH")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(plugin)                                          //set the plugin instance
                        .open(player);
                return;
            }
            if (event.getClick().isRightClick()) {
                Player player = (Player) event.getWhoClicked();
                Sound[] var7 = Sound.values();
                int var8 = var7.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    Sound sound1 = var7[var9];

                    try {
                        player.stopSound(sound1);
                    } catch (Exception var12) {
                        this.doNothing();
                    }
                }

                player.playSound(player.getLocation(), Sound.valueOf(sound), 100, 1.0F);
            }
        }
    }

    private void doNothing() {
    }
}
