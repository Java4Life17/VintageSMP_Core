package myplugin.myplugin.TeleportRequest;

import myplugin.myplugin.MyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TeleportRequest {

    //               TO       WHO
    private HashMap<Player, Player> onRequest;
    private HashMap<Player, Integer> cooldown;

    //Player whom they requested to teleport to.
    //Player who requested the teleport.
    //Integer, Cooldown.

    //Leslie requested teleport to Jdaniel - It will expire after 5 seconds.
    //Leslie -> Jdaniel - 30 seconds

    public void start(){
        onRequest = new HashMap<>();
        cooldown = new HashMap<>();
        startOnRequestCooldown();
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("tprequest")).setExecutor(new tpaCommand());
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("tpaccept")).setExecutor(new tpaCommand());
    }

    public HashMap<Player, Player> getOnRequest(){
        return onRequest;
    }
    public HashMap<Player, Integer> getCooldown(){
        return cooldown;
    }

    private void startOnRequestCooldown() {
        new BukkitRunnable() {
            @Override
            public void run() {

                HashMap<Player, Integer> finalOne = new HashMap<>();

                for(Map.Entry<Player, Integer> entry : cooldown.entrySet()){
                    if((entry.getValue() - 1) > 0){
                        finalOne.put(entry.getKey(), (entry.getValue() - 1));
                    }else{
                        onRequest.remove(entry.getKey());
                    }
                }
                cooldown = finalOne;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);
    }

}
