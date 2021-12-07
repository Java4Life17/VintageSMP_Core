package myplugin.myplugin.LivesSystem;

import myplugin.myplugin.DeathRecords.DeathRecord;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.Objects;

public class Events implements Listener {

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        if (MyPlugin.livesSystem.heartsFile.contains(event.getPlayer().getName())) {
            double heartScale = MyPlugin.livesSystem.heartsFile.getDouble(event.getPlayer().getName());
            Tools.updatePlayerHearts(event.getPlayer(), heartScale);
        } else {
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

    /*
    @EventHandler
    public void playerKilledEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (!(event.getDamager() instanceof Player killer)) {
            if ((victim.getHealth() - event.getDamage()) <= 0) {
                if (Tools.getPlayerHearts(victim) <= 2.0) {
                    Tools.updatePlayerHearts(victim, 2.0);
                    Tools.savePlayersHearts(victim);
                    List<String> commands;
                    commands = MyPlugin.languageFile.getStringList("Death_Commands");
                    for (String command : commands) {
                        String commandReady = command.replace("%player%", victim.getName());
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandReady);
                    }

                    return;
                }
                Tools.updatePlayerHearts(victim, (Tools.getPlayerHearts(victim) - 2.0));
            }
            return;
        }


        if ((victim.getHealth() - event.getDamage()) <= 0) {

            if (Tools.getPlayerHearts(victim) <= 2.0) {
                Tools.updatePlayerHearts(victim, 2.0);
                Tools.savePlayersHearts(victim);
                List<String> commands;
                commands = MyPlugin.languageFile.getStringList("Death_Commands");
                for (String command : commands) {
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

     */


    @EventHandler
    public void onDeathEvent(PlayerDeathEvent event) {

        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();


        if (MyPlugin.clan_system.manager().playerInArena(victim)) {
            return;
        }
        if (MyPlugin.clan_system.manager().playerInArena(killer)) {
            return;
        }

        if (MyPlugin.deathRecordMap.containsKey(victim)) {
            MyPlugin.deathRecordMap.get(victim).update(victim.getLocation(), 120);
        } else {
            MyPlugin.deathRecordMap.put(victim, new DeathRecord(victim, 120, victim.getLocation()));
        }

        String locationStringFormat = Objects.requireNonNull(victim.getLocation().getWorld()).getName() + " " +
                victim.getLocation().getBlockX() + " " +
                victim.getLocation().getBlockY() + " " +
                victim.getLocation().getBlockZ();

        TextComponent component = new TextComponent(TextComponent.fromLegacyText
                (Tools.hexColorMSG("#2fa130 Has muerto, pero no te preocupes, te cubrimos hermano/a. " +
                        "Moriste en #53edc9" + locationStringFormat + ". #2fa130Si desea que le teletransportemos a la ubicación, " +
                        "puede hacer clic en este mensaje o use #53edc9/back  #2fa130y le llevaremos de regreso por un costo " +
                        "de 100 monedas en el juego. Puedes consultar sus monedas con #53edc9/bal. #2fa130Tienes " +
                        "dos minutos para aceptar esta oferta.")));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/back"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click")));
        Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> victim.spigot().sendMessage(component), 25L);
        victim.playSound(victim.getLocation(), Sound.ENTITY_AXOLOTL_HURT, 100, 0.4F);

        if (Tools.getPlayerHearts(victim) <= 2.0) {
            Tools.updatePlayerHearts(victim, 2.0);
            Tools.savePlayersHearts(victim);
            List<String> commands;
            commands = MyPlugin.languageFile.getStringList("Death_Commands");
            for (String command : commands) {
                String commandReady = command.replace("%player%", victim.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandReady);
            }
            return;
        }
        Tools.updatePlayerHearts(victim, (Tools.getPlayerHearts(victim) - 2.0));

        if (killer == null) {
            return;
        }

        if (MyPlugin.clan_system.manager().getPlayerClan(victim) != null &&
                MyPlugin.clan_system.manager().getPlayerClan(killer) != null){
            if (!MyPlugin.clan_system.manager().getPlayerClan(victim).equals(MyPlugin.clan_system.manager().getPlayerClan(killer))) {
                if (MyPlugin.clan_system.manager().getPlayerClan(victim) != null && MyPlugin.clan_system.manager().getPlayerClan(killer) != null) {
                    MyPlugin.clan_system.manager().getPlayerClan(victim).setClanLevel(MyPlugin.clan_system.manager().getPlayerClan(victim).getClanLevel() - 1);
                    MyPlugin.clan_system.manager().getPlayerClan(killer).setClanLevel(MyPlugin.clan_system.manager().getPlayerClan(killer).getClanLevel() + 1);
                }

                String victimMSG = Tools.langText("clanLevelMinus");
                String killerMSG = Tools.langText("clanLevelPlus");
                victimMSG = victimMSG.replace("%count%", "1");
                killerMSG = killerMSG.replace("%count%", "1");

                victim.sendMessage(victimMSG);
                killer.sendMessage(killerMSG);


            }
    }
        Tools.updatePlayerHearts(killer, (Tools.getPlayerHearts(killer) + 2.0));




    }

}
