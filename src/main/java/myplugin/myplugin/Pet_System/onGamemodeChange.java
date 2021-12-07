package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class onGamemodeChange implements Listener {

    @EventHandler
    public void gamemodeChange(PlayerGameModeChangeEvent event){
        if(event.getNewGameMode().equals(GameMode.SPECTATOR)){
            MyPlugin.system.getByName(event.getPlayer().getName()).deletePet();
            return;
        }
    }

}
