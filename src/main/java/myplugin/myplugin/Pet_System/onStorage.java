package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class onStorage implements Listener {


    private MyPlugin plugin;

    public onStorage(MyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTheStorage(InventoryClickEvent event) {

        if (event.getView().getTitle().equals(Tools.hexColorMSG("#699c5c&nAlmacenamientos de mascota"))) {

            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }

            Player player = (Player) event.getWhoClicked();

            PlayerPet pet = MyPlugin.system.getByName(player.getName());


            if (event.getRawSlot() == 0) {
                if (event.getRawSlot() == 0) {
                    if (pet.getPetTier() != 1) {
                        player.sendMessage(Tools.langText("RankRequired"));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }

                    player.openInventory(pet.getPetStorage().getInventory(1));

                } //
            } else if (event.getRawSlot() == 1) {
                if (pet.getPetTier() != 2) {
                    player.sendMessage(Tools.langText("RankRequired"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(1));
            } else if (event.getRawSlot() == 2) {
                if (pet.getPetTier() != 3 && pet.getPetTier() != 4) {
                    player.sendMessage(Tools.langText("RankRequired"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(1));
            } else if (event.getRawSlot() == 3) {
                if (pet.getPetTier() != 4) {
                    player.sendMessage(Tools.langText("RankRequired"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(2));
            } else if (event.getRawSlot() == 4) {
                if (!pet.hasStorage_one) {
                    player.sendMessage(Tools.langText("noAdditionalStorage"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(11));
            } else if (event.getRawSlot() == 5) {
                if (!pet.hasStorage_two) {
                    player.sendMessage(Tools.langText("noAdditionalStorage"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(12));
            } else if (event.getRawSlot() == 6) {
                if (!pet.hasStorage_three) {
                    player.sendMessage(Tools.langText("noAdditionalStorage"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(13));
            } else if (event.getRawSlot() == 7) {
                if (!pet.hasStorage_four) {
                    player.sendMessage(Tools.langText("noAdditionalStorage"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(14));
            } else if (event.getRawSlot() == 8) {
                if (!pet.hasStorage_five) {
                    player.sendMessage(Tools.langText("noAdditionalStorage"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return;
                }
                player.openInventory(pet.getPetStorage().getInventory(15));
            }

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(Tools.colorMSG("&9Almacenamiento de tu mascota"))) {

            if(event.getPlayer() == null){
                return;
            }

            if(MyPlugin.adminSystem.playersOnStorageSpy.contains(event.getPlayer())){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MyPlugin.adminSystem.playersOnStorageSpy.remove(event.getPlayer());
                    }
                }.runTaskLater(plugin, 2);
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().openInventory(Menus.getStorageSelectionMenu());
                }
            }.runTaskLater(plugin, 2);
            return;
        }

        if (event.getView().
                getTitle().
                equals(Tools.colorMSG("&6&lOBJETOS ENCONTRADOS"))) {
            if(event.getPlayer() == null){
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {

                    event.getPlayer().openInventory(Menus.getMainInventory(event.getPlayer().getName()));
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void onTreatUse(PlayerInteractEvent event){
        if(event.getItem() == null){
            return;
        }
        ItemStack stack = event.getItem();
        ItemStack copy = stack.clone();
        copy.setAmount(1);
        if(copy.equals(Tools.getPetTreat())){
            event.setCancelled(true);
        }
    }

}
