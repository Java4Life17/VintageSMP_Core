package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaEvents implements Listener {

    @EventHandler
    public void onArenaPVP(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (MyPlugin.clan_system.manager().getPlayerArena(player) != null) {
            if (!MyPlugin.clan_system.manager().getPlayerArena(player).pvpEnabled) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (MyPlugin.clan_system.manager().playerInArena(event.getEntity().getKiller())) {
            MyPlugin.clan_system.manager().getPlayerArena(event.getEntity().getKiller()).increasePoints(event.getEntity().getKiller());
            event.getEntity().getWorld().spawnParticle(Particle.FLAME, event.getEntity().getLocation(), 10, 0.2, 0.2, 0.2, 1);
            event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100, 1.8F);
            event.getEntity().spigot().respawn();
            event.getEntity().teleport(MyPlugin.clan_system.manager().getPlayerArena(event.getEntity()).getPlayerSpawn(event.getEntity()));
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (MyPlugin.clan_system.manager().playerInArena(event.getPlayer())) {

            if (event.getMessage().equalsIgnoreCase("/petstorages " + event.getPlayer().getName())) {
                return;
            }

            Tools.message(event.getPlayer(), "&cNo puedes usar comandos mientras estas en una batalla de clanes.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player joining = event.getPlayer();

        if (joining.getWorld().getName().equalsIgnoreCase("Arenas")) {
            if (!joining.getGameMode().equals(GameMode.CREATIVE)) {
                MyPlugin.getMyPlugin().getServer().dispatchCommand(joining, "spawn");
            }
        }

        if (MyPlugin.clan_system.manager().playerInArena(joining)) {
            if (MyPlugin.clan_system.manager().getPlayerArena(joining).getSecondsLeft() < 8) return;
            MyPlugin.clan_system.manager().getPlayerArena(joining).getBossBar().addPlayer(joining);
            Tools.message(joining, "&aMientras no estabas, tu clan decidio entrar a una batalla contra otro clan. Si no quieres formar parte de este evento, abandona el servidor ahora. Seras " +
                    "teletransportado en 3 segundos.");
            Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> {
                joining.teleport(MyPlugin.clan_system.manager().getPlayerArena(joining).getPlayerSpawn(joining));
            }, 3 * 20L);
        }
    }

}


//    @EventHandler
//    public void onRespawn(PlayerRespawnEvent event) {
//        if (MyPlugin.clan_system.manager().playerInArena(event.getPlayer())) {
//            event.getPlayer().teleport(MyPlugin.clan_system.manager().getPlayerArena(event.getPlayer()).getPlayerSpawn(event.getPlayer()));
//        }
//    }
//}