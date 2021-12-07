package myplugin.myplugin;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class MiningEvent implements Listener {

    @EventHandler
    public void onMining(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!player.getGameMode().equals(GameMode.SURVIVAL))
            return;
        if (!player.getWorld().getName().equalsIgnoreCase("MiningZone"))
            return;


        if (event.getBlock().getType().equals(Material.BROWN_CONCRETE) || event.getBlock().getType().equals(Material.GRAY_TERRACOTTA)) {
            MyPlugin.onEffect.put(event.getBlock(), event.getBlock().getType());
            event.getBlock().setType(Material.BEDROCK);

            Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> {
                Block block = event.getBlock();
                block.setType(MyPlugin.onEffect.get(block));
                Objects.requireNonNull(block.getLocation().getWorld()).spawnParticle(Particle.SQUID_INK, block.getLocation(), 100, 1, 0.1, 0.1, 0.1);
            }, 20 * 5L);

            int random = Tools.getRandomNumber(1, 160);
            if (random > 0 && random < 10) {
                playEffect(event.getBlock().getLocation());
            }
        }
        event.setCancelled(true);
    }

    private void playEffect(Location loc) {
        new BukkitRunnable() {
            final float limit = 2.0F;
            float pitch = 0.1F;
            Location temp = new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() + 0.8F, loc.getZ() + 0.5);

            @Override
            public void run() {
                Objects.requireNonNull(loc.getWorld()).playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, 100, pitch);
                Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                    loc.getWorld().spawnParticle(Particle.FLAME, temp, 50, 1, 0.1, 0.1, 0.1);
                });
                if (pitch >= limit) {
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 100, 1.0F);
                        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 10);
                        int index = Tools.getRandomNumber(1, MyPlugin.specialItems.size());
                        loc.getWorld().dropItem(loc, MyPlugin.specialItems.get(index - 1));
                    });

                    this.cancel();
                }
                pitch = pitch + 0.1F;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 1);
    }

}
