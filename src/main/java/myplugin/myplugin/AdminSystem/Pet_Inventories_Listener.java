package myplugin.myplugin.AdminSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class Pet_Inventories_Listener implements Listener {

    @EventHandler
    public void onInventory(InventoryClickEvent e){

        if(e.getView().getTitle().startsWith(Tools.hexColorMSG("#174f2dAlmacenamientos de &c"))){
            Player player = (Player) e.getWhoClicked();
            e.setCancelled(true);

            int option = e.getRawSlot();

           if(Objects.requireNonNull(e.getInventory().getItem(option)).getType().equals(Material.WHITE_STAINED_GLASS_PANE)){
               return;
           }

           String name = ChatColor.stripColor(e.getView().getTitle());
           name = name.replace("Almacenamientos de ", "");

           String number = Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName();
           number = ChatColor.stripColor(number);
           number = number.replace("Almacenamiento ", "");
           int storageNumber = Integer.parseInt(number);

           if(MyPlugin.system.getByName(name).getPetStorage().getRawStorages().get(storageNumber) == null){
               player.sendMessage((Tools.langText("playerNotFound")));
               player.closeInventory();
               return;
           }

           player.openInventory(MyPlugin.system.getByName(name).getPetStorage().getRawStorages().get(storageNumber));
           MyPlugin.adminSystem.playersOnStorageSpy.add(player);
        }
    }
}
