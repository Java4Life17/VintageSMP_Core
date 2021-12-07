package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class onClanBattleChoose implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Tools.colorMSG("&6&lELIJE UN CLAN"))) {
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if(event.getRawSlot() == 26){
                player.openInventory(MyPlugin.clan_system.manager().getClanBattle(event.getInventory()));
                if (event.getInventory().getItem(17) == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.4F);
                    return;
                }
                return;
            }
            if(event.getRawSlot() == 18){
               MyPlugin.clan_system.manager().getPreviousInventory(event.getInventory());
                /*player.openInventory(MyPlugin.clan_system.manager().getPreviousClan(event.getInventory(), name));*/
                return;
            }
            
            List<Integer> slots = Arrays.asList(9,10,11,12,13,14,15,16,17);
            if(!slots.contains(event.getRawSlot())) return;
            if(event.getCurrentItem() == null) return;
            
            ItemStack stack = event.getCurrentItem();
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            String name = meta.getDisplayName();
            name = ChatColor.stripColor(name);
            
            if(!MyPlugin.clan_system.manager().getOnlineClans().contains(name)){
                player.sendMessage(Tools.colorMSG("&cNo hay ningún clan con ese nombre en línea."));
                player.closeInventory();
                return;
            }
            if(MyPlugin.clan_system.manager().getPlayerClan(player).getClanName().equals(name)){
                player.sendMessage(Tools.colorMSG("&cNo puedes retar a tu propio clan."));
                player.closeInventory();
                return;
            }
            if(Bukkit.getPlayer(MyPlugin.clan_system.manager().getClanByName(name).getClanOwner()) == null){
                player.sendMessage(Tools.colorMSG("&cSolo el líder del clan puede aceptar esta solicitud, pero parece estar desconectado."));
                player.closeInventory();
                return;
            }

            String finalName = name;
            MyPlugin.clan_system.manager().getPlayerClan(player).getOnlineMembers().forEach(member -> {
                Tools.message(member, "#aabd4dEl lider de tu clan reto al clan &d" + finalName + " #aabd4da una guerra. " +
                        "Si no quieres que esta guerra suceda, habla con el lider de tu clan o abandona el servidor.");
            });
            MyPlugin.clan_system.manager().getClanByName(name).getOnlineMembers().forEach(member -> {
                Tools.message(member, "#aabd4dTu clan fue retado a una guerra. " +
                        "Si no quieres que esta guerra suceda, habla con el lider de tu clan o abandona el servidor.");
            });

            TextComponent msg = new TextComponent(Tools.colorMSG("&6El jugador &c" + player.getName() + "&6 te esta retando a una guerra de clanes en una arena."
                    + " &6. Que quieres hacer?"));
            TextComponent accept = new TextComponent(Tools.colorMSG(" &8&l[&a&lACCEPTAR&8&l]"));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans fight accept"));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.hexColorMSG("&aClic para acceptar"))));

            TextComponent deny = new TextComponent(Tools.colorMSG(" &8&l[&4&lRECHAZAR&8&l]"));
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans fight deny"));
            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.hexColorMSG("&cClic para rechazar"))));

            ComponentBuilder builder = new ComponentBuilder();
            builder.append(msg).append(accept).append(deny);

            Objects.requireNonNull(Bukkit.getPlayer(MyPlugin.clan_system.manager().getClanByName(name).getClanOwner())).spigot().sendMessage(builder.create());
            MyPlugin.clan_system.getFightRequest().put(Bukkit.getPlayer(MyPlugin.clan_system.manager().getClanByName(name).getClanOwner()), player);
            player.closeInventory();

            TextComponent cancelRequest = new TextComponent(Tools.colorMSG("&cPeticion de pelea enviada. Para cancelarla de click aqui."));
            cancelRequest.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans cancel"));
            cancelRequest.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.colorMSG("&cCancelar peticion"))));
            player.spigot().sendMessage(cancelRequest);


        }

        if(event.getView().getTitle().startsWith( Tools.colorMSG("&0Editor: " ))){
            Player player = (Player) event.getWhoClicked();

            event.setCancelled(true);

            String arenaName = event.getView().getTitle();
            arenaName = ChatColor.stripColor(arenaName);
            arenaName = arenaName.replace("Editor: ", "");

            if(event.getRawSlot() == 10){
                MyPlugin.clan_system.arenas.set(arenaName + ".Team_One_Spawn", Tools.playerLocationToString(player));
                player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_LAY_EGG, 100, 1.4F);
                try {
                    MyPlugin.clan_system.arenas.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.closeInventory();
            }
            if(event.getRawSlot() == 13){
                //Team_Waiting_Lobby
                MyPlugin.clan_system.arenas.set(arenaName + ".Team_Two_Spawn", Tools.playerLocationToString(player));
                player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_LAY_EGG, 100, 1.4F);
                try {
                    MyPlugin.clan_system.arenas.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.closeInventory();
            }

            if(event.getRawSlot() == 16){
                MyPlugin.clan_system.arenas.set(arenaName + ".Team_Waiting_Lobby", Tools.playerLocationToString(player));
                player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_LAY_EGG, 100, 1.4F);
                try {
                    MyPlugin.clan_system.arenas.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.closeInventory();
            }

        }

    }

}
