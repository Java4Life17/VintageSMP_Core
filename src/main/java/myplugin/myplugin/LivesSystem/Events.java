package myplugin.myplugin.LivesSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        if(MyPlugin.livesSystem.heartsFile.contains(event.getPlayer().getName())){
            double heartScale = MyPlugin.livesSystem.heartsFile.getDouble(event.getPlayer().getName());
            Tools.updatePlayerHearts(event.getPlayer(), heartScale);
        }else{
            Tools.updatePlayerHearts(event.getPlayer(), 20.0);
            Tools.savePlayersHearts(event.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        double heartScale = Tools.getPlayerHearts(event.getPlayer());
        Tools.savePlayersHearts(event.getPlayer());
        MyPlugin.livesSystem.playerLives.remove(event.getPlayer());
    }
    @EventHandler
    public void playerKilledEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player killer = (Player) event.getDamager();

        if(victim.getHealth() - event.getDamage() <= 0){

            if(Tools.getPlayerHearts(victim) <= 2.0){
                Tools.updatePlayerHearts(victim, 2.0);
                Tools.savePlayersHearts(victim);
                List<String> commands;
                commands = MyPlugin.languageFile.getStringList("Death_Commands");
                for(String command : commands){
                    String commandReady = command.replace("%player%", victim.getName());
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandReady);
                }
                Tools.updatePlayerHearts(killer, (Tools.getPlayerHearts(killer) + 2.0));
                return;
            }

            Tools.updatePlayerHearts(victim, (Tools.getPlayerHearts(victim) - 2.0));
            Tools.updatePlayerHearts(killer, (Tools.getPlayerHearts(killer) + 2.0));


        }



    }

}
