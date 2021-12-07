package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class JoinEventPet implements Listener {

    private MyPlugin plugin;
    private Pet_System system;

    public JoinEventPet(MyPlugin plugin, Pet_System system) {
        this.plugin = plugin;
        this.system = system;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        if (system.getByName(event.getPlayer().getName()) == null) {
            system.pets.add(new PlayerPet(system, event.getPlayer(), plugin));
            Bukkit.getLogger().info(Tools.colorMSG("&aSe encendio el pet de " + event.getPlayer().getName() + "!"));
        }

        MyPlugin.system.getByName(p.getName()).spawnPet(event.getPlayer().getLocation());

    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        MyPlugin.system.getByName(event.getPlayer().getName()).getPetStorage().saveContent();
        MyPlugin.system.getByName(event.getPlayer().getName()).getNpc().destroy();

        boolean offlineEnabled = system.getByName(event.getPlayer().getName()).getOfflineMode();
        try {
            system.getByName(event.getPlayer().getName()).getFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (offlineEnabled && system.getByName(event.getPlayer().getName()).getTimeAccordingToTire() == 0) {
            system.getByName(event.getPlayer().getName()).eliminate();
        }
        if (!offlineEnabled) {
            if (system.getByName(event.getPlayer().getName()) != null) {
                system.getByName(event.getPlayer().getName()).eliminate();
                system.pets.remove(system.getByName(event.getPlayer().getName()));
                Bukkit.getLogger().info(Tools.colorMSG("&cSe apago el pet de " + event.getPlayer().getName() + "!"));
            }
        }
        system.discordLink.remove(event.getPlayer());
    }


}
