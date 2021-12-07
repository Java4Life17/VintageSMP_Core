package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;

public class playerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.getPersistentDataContainer().has(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING)) {
            return;
        }

        String clanName = player.getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING);

        if (!MyPlugin.clan_system.manager().clanExists(clanName)) {
            player.getPersistentDataContainer().remove(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"));
            return;
        }

        if (!MyPlugin.clan_system.manager().getOfflineClan(clanName).getClanMembers().contains(player.getName())) {
            player.getPersistentDataContainer().remove(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"));
        } else {
            MyPlugin.clan_system.manager().addClan(clanName);
        }

        if (MyPlugin.clan_system.getActiveArenas().size() == 0) {
            return;
        }

        /*MyPlugin.clan_system.getActiveArenas().forEach(arena -> {
            if (arena.getTeam1().getClanName().equals(MyPlugin.clan_system.manager().getPlayerClan(player).getClanName()) ||
                    arena.getTeam2().getClanName().equals(MyPlugin.clan_system.manager().getPlayerClan(player).getClanName())) {
                arena.getLocationMap().put(player, player.getLocation());
                player.teleport(arena.getSpawnLocationFor(MyPlugin.clan_system.manager().getPlayerClan(player)));
                player.sendMessage(Tools.colorMSG("&aMientras estuvo ausente, su clan en" +
                        "tra en batalla con otro clan. Serás teletransportado al campo de batalla." +
                        " Si no desea participar, simplemente puede irse y esperar a que termine la batalla." +
                        " ¡Buena suerte! &7La batalla termina en&l " + arena.getMinutesLeft() + " &7minuto(s)!"));
            }
        });*/

        if(MyPlugin.clan_system.getPending().containsKey(player)){
            player.sendMessage(Tools.colorMSG("&aLa batalla en el que estabas terminó " +
                    "cuando te fuiste. Serás teletransportado de regreso a tu última ubicación conocida."));
            Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> player.teleport(MyPlugin.clan_system.getPending().get(player)), 20L);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (MyPlugin.clan_system.manager().getPlayerClan(player) == null)
            return;

        int online = 0;
        for (String member : MyPlugin.clan_system.manager().getPlayerClan(player).getClanMembers()) {
            if (Bukkit.getPlayer(member) != null) {
                if (!member.equals(player.getName()))
                    online++;
            }
        }

        if (online == 0) {
            MyPlugin.clan_system.manager().getPlayerClan(player).saveClan();
            MyPlugin.clan_system.manager().removeClan(MyPlugin.clan_system.manager().getPlayerClan(player));
        }

        MyPlugin.clan_system.invitePending.remove(player);

    }

}
