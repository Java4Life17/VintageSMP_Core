package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Locations_List_Listener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(Tools.colorMSG("&0&lDISTRICT EDIT GUI"))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if (Objects.requireNonNull(event.getCurrentItem()).getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                return;
            }

            String id = ChatColor.stripColor(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName());
            player.openInventory(Locations_MainGUI.getSpecificDisEditor(id));


            return;
        }

        if (event.getView().getTitle().startsWith(Tools.colorMSG("&0&lEditor&f: &7"))) {

            String key = event.getView().getTitle();
            key = ChatColor.stripColor(key);
            key = key.replace("Editor: ", "");

            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getSlot() == 3) {
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                String world = Objects.requireNonNull(player.getLocation().getWorld()).getName();

                MyPlugin.locations.locationsFile.set(key + ".X", x);
                MyPlugin.locations.locationsFile.set(key + ".Y", y);
                MyPlugin.locations.locationsFile.set(key + ".Z", z);
                MyPlugin.locations.locationsFile.set(key + ".Yaw", yaw);
                MyPlugin.locations.locationsFile.set(key + ".Pitch", pitch);
                MyPlugin.locations.locationsFile.set(key + ".World", world);
                List<String> list = new ArrayList<>();
                list.add("Test");
                MyPlugin.locations.locationsFile.set(key + ".Messages", list);
                Tools.saveLocationsFile();

                player.closeInventory();
                player.sendMessage(Tools.langText("District_Set_Location"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.3F);
                return;
            }
            if (event.getSlot() == 5) {
                player.openInventory(new SoundsMenuCreator(key).getSoundPage(1));
                return;
            }
            if (event.getSlot() == 7) {
                MyPlugin.locations.locationsFile.set(key, null);
                player.sendMessage(Tools.langText("District_Deleted"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 0.4F);
                player.openInventory(Locations_MainGUI.getListToEdit());
            }

        }

    }


}
