package myplugin.myplugin.DeathRecords;

import myplugin.myplugin.MyPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathRecord {

    private final Player player;
    private int delay;
    private Location location;

    public DeathRecord(Player player, int delay, Location location) {
        this.player = player;
        this.delay = delay;
        this.location = location;
        startCounter();
    }

    private void startCounter(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(delay <= 0){
                    MyPlugin.deathRecordMap.remove(player);
                    this.cancel();
                    return;
                }
                delay--;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);
    }

    public void update(Location location, int delay){
        this.location = location;
        this.delay = delay;
    }

    public boolean teleportSuccessful(){
        try {
            if(location == null)
                return false;
            if(player == null)
                return false;
            if(!MyPlugin.deathRecordMap.containsKey(player))
                return false;

            player.teleport(location);
            MyPlugin.deathRecordMap.remove(player);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public Player getPlayer() {
        return player;
    }

    public int getDelay() {
        return delay;
    }

    public Location getLocation() {
        return location;
    }
}
