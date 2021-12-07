package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.Clan_System.Clan;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.collections4.BagUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.tools.Tool;
import java.util.HashMap;
import java.util.Objects;

public class onClanEditor implements Listener {

    @EventHandler
    public void onClanEditor(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(Tools.hexColorMSG("#06452aEdita tu clan"))) {
            Player player = (Player) e.getWhoClicked();
            e.setCancelled(true);

            if (e.getRawSlot() == 10) {
                TextComponent message = new TextComponent(Tools.colorMSG("&6/clans invitar <jugador>"));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/clans invitar "));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.hexColorMSG("Clic para generar el comando"))));
                player.spigot().sendMessage(message);
                player.closeInventory();
                return;
            }

            if(e.getRawSlot() == 12){
                player.openInventory(clanRemoveInventory(player));
                return;
            }
            if(e.getRawSlot() == 14){
                player.openInventory(MyPlugin.clan_system.manager().getClanBattle(null));
            }
            if(e.getRawSlot() == 16){
                player.sendMessage(Tools.colorMSG("&aClan eliminado!"));
                player.sendMessage(" ");
                MyPlugin.clan_system.manager().deleteClan(MyPlugin.clan_system.manager().getPlayerClan(player));
                player.closeInventory();

                return;
            }


        }else if(e.getView().getTitle().equals(Tools.colorMSG("&cElimina a un miembro!"))){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(e.getCurrentItem() == null)
                return;
            if(!e.getCurrentItem().getType().equals(Material.PLAYER_HEAD))
                return;

            String member = Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName();
            member = ChatColor.stripColor(member);

            if(!MyPlugin.clan_system.manager().getPlayerClan(player).getClanMembers().contains(member))
                return;
            if(player.getName().equals(member)){
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                return;
            }

            MyPlugin.clan_system.manager().getPlayerClan(player).removeMember(member);
            HashMap<String, String> map = new HashMap<>();
            map.put("%player%", member);
            player.sendMessage(Tools.langText("removedFromClan", map));
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 100, 0.5F);
        }
    }

    private Inventory clanRemoveInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, Tools.colorMSG("&cElimina a un miembro!"));
        for(String member : MyPlugin.clan_system.manager().getPlayerClan(player).getClanMembers()){
            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = stack.getItemMeta();
            if(member.equals(player.getName())){
                assert meta != null;
                meta.setDisplayName(Tools.colorMSG("&c&l" + member));
            }else{
                assert meta != null;
                meta.setDisplayName(Tools.colorMSG("&e" + member));
            }
            stack.setItemMeta(meta);
            inventory.addItem(stack);
        }
        return inventory;
    }
}


