package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class onTreatContainer implements Listener {

    @EventHandler
    public void treatContainerListener(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Tools.hexColorMSG("#eb3452Contenedor de Golosinas"))) {
            Player player = (Player) event.getWhoClicked();
            List<Integer> slots = Arrays.asList(0, 1, 2, 3, 5, 6, 7);

            if (slots.contains(event.getRawSlot())) {
                event.setCancelled(true);
                return;
            }

            ItemStack comparator = Tools.getPetTreat();
            comparator.setAmount(1);


            if (event.getRawSlot() == 8) {
                event.setCancelled(true);
                if (event.getInventory().getItem(4) == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                ItemStack actual = Objects.requireNonNull(event.getInventory().getItem(4));
                ItemStack inContainer = actual.clone();
                inContainer.setAmount(1);

                if (inContainer.equals(comparator)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PARROT_EAT, 100, 0.5F);
                    int amount = Objects.requireNonNull(event.getInventory().getItem(4)).getAmount();
                    int total = player.getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(),
                            "AppreciationLevel"), PersistentDataType.INTEGER);

                    for (int i = 0; i < amount; i++) {
                        total = total + Tools.getRandomNumber(5, 13);
                    }

                    if ((total - 1000) >= 0) {
                        total = total - 1000;
                        MyPlugin.system.petFindingTreasure.add(player);
                        findTreasureForPlayer(player);
                        player.closeInventory();
                    }

                    player.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(),
                            "AppreciationLevel"), PersistentDataType.INTEGER, total);
                    Objects.requireNonNull(event.getInventory().getItem(4)).setAmount(0);

                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 100, 1.5F);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                }

            }

        }
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(Tools.hexColorMSG("#eb3452Contenedor de Golosinas"))) {
            if (event.getInventory().getItem(4) == null) {
                return;
            }

            Player player = (Player) event.getPlayer();

            new BukkitRunnable() {
                @Override
                public void run() {

                    if (event.getInventory().getItem(4) == null) {
                        return;
                    }

                    ItemStack actual = Objects.requireNonNull(event.getInventory().getItem(4));
                        player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 100, 1.5F);
                        player.getWorld().dropItem(player.getLocation(), Objects.requireNonNull(event.getInventory().getItem(4)));

                }
            }.runTaskLater(MyPlugin.getMyPlugin(), 2);

        }
    }


    private void findTreasureForPlayer(Player player) {
        PlayerPet pet = MyPlugin.system.getByName(player.getName());
        List<Block> blocks = getBlocks(player.getLocation().getBlock(), 10);
        int random = Tools.getRandomNumber(0, blocks.size() - 1);


        Block block = blocks.get(random);

        MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().setTarget(block.getLocation());

        new BukkitRunnable() {
            int second = 0;

            @Override
            public void run() {
                if (pet.getNpc().getStoredLocation() == null) {
                    this.cancel();
                    return;
                }
                if (second == 11) {
                    MyPlugin.system.petFindingTreasure.remove(player);
                    pet.getNpc().getNavigator().cancelNavigation();
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () ->
                            player.getWorld().dropItem(MyPlugin.system.getByName(player.getName()).getNpc().getStoredLocation(), Tools.getPetAppreciation()));
                    player.sendMessage(Tools.hexColorMSG(pet.getNpc().getName()+ " #8a64e3-> #f0b330Amo, encontre un tesoro en &c" + block.getX() + " "
                     + block.getY() + " " + block.getZ() + "!"));
                    pet.getNpc().getNavigator().cancelNavigation();
                    Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> runRetrieve(pet), 20L);
                    this.cancel();
                    return;
                }


                second++;
            }


        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);

    }

    public ArrayList<Block> getBlocks(Block start, int radius) {
        List<Material> notAllowed = Arrays.asList(Material.VOID_AIR, Material.AIR, Material.CAVE_AIR, Material.LEGACY_AIR);
        ArrayList<Block> blocks = new ArrayList<>();
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    if(!notAllowed.contains(loc.getBlock().getType())) {
                        blocks.add(loc.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    private void runRetrieve(PlayerPet pet) {
        Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> {
            pet.getNpc().getNavigator().setTarget(pet.getPlayer(), false);
        }, 3 * 20L);
    }

}
