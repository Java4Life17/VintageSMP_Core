package myplugin.myplugin.Combat;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class onQuit implements Listener {

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {



        if (MyPlugin.combat.getCombatCoolDown().containsKey(event.getPlayer())) {

            AtomicBoolean inArena = new AtomicBoolean(false);
            if(MyPlugin.clan_system.getActiveArenas().size() > 0){
                MyPlugin.clan_system.getActiveArenas().forEach(arena -> {
                    if(arena.checkForPlayer(event.getPlayer())) inArena.set(true);
                });
            }

            if(!inArena.get()){
                return;
            }

            event.getPlayer().setHealth(0);

            if (Tools.getPlayerHearts(event.getPlayer()) <= 2.0) {
                Tools.updatePlayerHearts(event.getPlayer(), 2.0);
                Tools.savePlayersHearts(event.getPlayer());
                List<String> commands;
                commands = MyPlugin.languageFile.getStringList("Death_Commands");
                for (String command : commands) {
                    String commandReady = command.replace("%player%", event.getPlayer().getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandReady);
                }
            }
            MyPlugin.combat.getCombatCoolDown().remove(event.getPlayer());
        }
    }


    @EventHandler
    public void playerCommand(PlayerCommandPreprocessEvent e) {
        if (MyPlugin.combat.getCombatCoolDown().containsKey(e.getPlayer())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Tools.colorMSG("&cEstas en modo de combate. Tienes que esperar &7" + MyPlugin.combat.getCombatCoolDown()
                    .get(e.getPlayer()) + " &csegundos antes de intentar de nuevo."));
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event) {


        if (event.getEntity().hasMetadata("NPC") || event.getDamager().hasMetadata("NPC")) {
            return;
        }
        if(event.isCancelled())
            return;
        if (event.getEntity() instanceof Player) {
            if(event.getEntity().hasPermission("VintageSMP.Combat.Bypass"))
                return;

            if(event.getDamager() instanceof Player){
                if(MyPlugin.clan_system.manager().playerInArena((Player) event.getDamager())){
                    return;
                }
            }
            if(MyPlugin.clan_system.manager().playerInArena((Player) event.getEntity())){
                return;
            }


            if (!MyPlugin.combat.getCombatCoolDown().containsKey((Player) event.getEntity()))
                event.getEntity().sendMessage(Tools.colorMSG("&aEntraste en el modo de combate."));
            MyPlugin.combat.getCombatCoolDown().put((Player) event.getEntity(), 20);
            if (event.getDamager() instanceof Player) {
                if(event.getDamager().hasPermission("VintageSMP.Combat.Bypass"))
                    return;
                if (!MyPlugin.combat.getCombatCoolDown().containsKey((Player) event.getDamager())) {
                    event.getDamager().sendMessage(Tools.colorMSG("&aEntraste en el modo de combate."));
                }
                MyPlugin.combat.getCombatCoolDown().put((Player) event.getDamager(), 20);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(MyPlugin.combat.getCombatCoolDown().containsKey(event.getEntity())){
            MyPlugin.combat.getCombatCoolDown().remove(event.getEntity());
            event.getEntity().sendMessage(Tools.colorMSG("&aSaliste del modo combate."));
        }
    }

}
