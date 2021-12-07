package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class treasurePlace implements Listener {

    @EventHandler
    public void onTreasurePlace(BlockPlaceEvent event){


        if(Tools.ItemMatch(event.getItemInHand().clone(), Tools.getPetAppreciation())){
            runAnimation(event.getBlockPlaced().getLocation().add(0, -1.8, 0), event.getPlayer());
            event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
            event.setCancelled(true);
        }
    }

    private void runAnimation(Location location, Player player) {
        ArmorStand stand = Objects.requireNonNull(location.getWorld()).spawn(location, ArmorStand.class);
        stand.setVisible(false);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        Objects.requireNonNull(stand.getEquipment()).setHelmet(Tools.getPetAppreciation());
        new BukkitRunnable() {
            int timesRan = 0;
            float note = 0.4F;
            @Override
            public void run() {
                timesRan++;

                EulerAngle angle = stand.getHeadPose();


                if(timesRan < 30){
                    EulerAngle newAngle = angle.add(0.0, 0.5, 0.0);
                    stand.setHeadPose(newAngle);
                    Location location1 = stand.getLocation().clone();
                    location1.add(0, 2.1, 0);
                    location.getWorld().spawnParticle(Particle.HEART, location1, 1);
                    stand.teleport(location.add(0, 0.1, 0));
                    stand.getWorld().playSound(stand.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 100, note);
                }


                if(timesRan >= 35){
                    this.cancel();
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        stand.remove();
                        Location location1 = stand.getLocation().clone();
                        location1.add(0, 1.7, 0);
                        Objects.requireNonNull(stand.getLocation().getWorld()).spawnParticle(Particle.ITEM_CRACK, location1, 20, 1, 0.1, 0.1, 0.1, new ItemStack(Material.SAND));
                        stand.getLocation().getWorld().playSound(stand.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, 100, 0.2F);
                        dropRewardItem(player, stand.getLocation());
                    });
                }
                note += 0.05F;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 2);
    }

    private void dropRewardItem(Player player, Location location) {
        int random = Tools.getRandomNumber(1, MyPlugin.system.keys.size());
        String key = MyPlugin.system.keys.get(random - 1);

        int min = MyPlugin.system.treasureFile.getInt(key + ".Min");
        int max = MyPlugin.system.treasureFile.getInt(key + ".Max");
        List<String> messages = MyPlugin.system.treasureFile.getStringList(key + ".Messages");
        List<String> commands = MyPlugin.system.treasureFile.getStringList(key + ".Commands");

        for(String cmd : commands){
            String toExecute = cmd.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
        }

        for(String msg : messages){
            String toSend = msg.replace("%player%", player.getName());
            player.sendMessage(Tools.hexColorMSG(toSend));
        }

        if(!MyPlugin.system.treasureFile.getString(key + ".Item").equalsIgnoreCase("Nothing")){
            try {
                ItemStack toDrop = Tools.itemStackFromBase64(MyPlugin.system.treasureFile.getString(key + ".Item"));
                toDrop.setAmount(Tools.getRandomNumber(min, max));
                location.add(0, 2.0, 0);
                Objects.requireNonNull(location.getWorld()).dropItem(location, toDrop);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
