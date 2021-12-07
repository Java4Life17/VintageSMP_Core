package myplugin.myplugin.Clan_System;

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

import java.util.HashMap;

public class onClanMainMenu implements Listener {

    @EventHandler
    public void onMenu(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(event.getView().getTitle().equals(Tools.hexColorMSG("#353445Seleccione una opción"))){
            event.setCancelled(true);

            // Logic for creating new clan option.
            if(event.getRawSlot() == 12){
                Clan clan = MyPlugin.clan_system.manager().getPlayerClan(player);

                if(clan != null){
                    player.sendMessage(Tools.langText("inAClan"));
                    player.closeInventory();
                    return;
                }

                new AnvilGUI.Builder()
                        .onComplete((user, text) -> {                                    //called when the inventory output slot is clicked
                            if (MyPlugin.clan_system.manager().clanExists(text)) {
                                user.sendMessage(Tools.langText("clanExists"));
                                return AnvilGUI.Response.close();
                            } else {
                               if(!MyPlugin.clan_system.manager().nameIsValid(text)){
                                   user.sendMessage(Tools.langText("clanInvalidName"));
                                   return AnvilGUI.Response.close();
                               }
                               if(text.toCharArray().length < 3 || text.toCharArray().length > 8){
                                   user.sendMessage(Tools.langText("clanNameLength"));
                                   return AnvilGUI.Response.close();
                               }
                               MyPlugin.clan_system.manager().createNewClan(text, user.getName());
                                HashMap<String, String> map = new HashMap<>();
                                map.put("%clan%", text);
                               player.sendMessage(Tools.langText("clanCreated", map));
                            }
                            return AnvilGUI.Response.close();
                        })
                        .text("Nombre de Clan")                              //sets the text the GUI should start with
                        .itemLeft(new ItemStack(Material.IRON_SWORD))                      //use a custom item for the first slot
                        .itemRight(new ItemStack(Material.IRON_SWORD))                     //use a custom item for the second slot
                        .onLeftInputClick(user -> user.sendMessage(Tools.colorMSG("&dEscribe el nombre del clan")))     //called when the left input slot is clicked
                        .onRightInputClick(user -> user.sendMessage(Tools.colorMSG("&dEscribe el nombre del clan")))   //called when the right input slot is clicked
                        .title(Tools.hexColorMSG("#372354Crea tu Clan"))                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(MyPlugin.getMyPlugin())                                          //set the plugin instance
                        .open(player);
            }



            if(event.getRawSlot() == 14){
                if(MyPlugin.clan_system.manager().getPlayerClan(player) == null){
                    player.sendMessage(Tools.langText("notInAClan"));
                    player.closeInventory();
                    return;
                }

                if(!player.getName().equals(MyPlugin.clan_system.manager().getPlayerClan(player).getClanOwner())){
                    player.sendMessage(Tools.langText("notClanOwner"));
                    player.closeInventory();
                    return;
                }

                player.openInventory(MyPlugin.clan_system.getEditClan());

            }

        }
    }

}
