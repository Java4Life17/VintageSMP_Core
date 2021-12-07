package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainMenuEvent implements Listener {

    @EventHandler
    public void onPetMenu(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Tools.colorMSG("&0Menu de mascota!"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null) {
                return;
            }

            if (event.getRawSlot() == 0) {
                player.openInventory(getItemsFound(player));
                return;
            }

            if (event.getRawSlot() == 1) {

                if(!MyPlugin.system.discordMinecraft.contains(player.getName())){
                    player.sendMessage(Tools.colorMSG("&cAntes de usar este sistema, tienes que verificar tu discord a nuestro " +
                            "servidor. Usa &e/discord &cpara hacerlo."));
                    player.closeInventory();
                    return;
                }

                int tier = MyPlugin.system.getByName(player.getName()).getPetTier();

                if (tier == 1 || tier == 2) {
                    player.sendMessage(Tools.colorMSG("&cTu rango no es lo suficientemente alto para usar esta función." +
                            " Para obtener más información sobre cómo desbloquear esta función, mire el canal #Ranks en discord."));
                    player.closeInventory();
                    return;
                }
                if (MyPlugin.system.getByName(player.getName()).getTimeAccordingToTire() == 0 && !MyPlugin.system.getByName(player.getName()).getOfflineMode()) {
                    player.sendMessage(Tools.colorMSG("&cTu mascota no tiene suficiente energía para trabajar un minuto más hoy."));
                    player.closeInventory();
                    return;
                }
                if (MyPlugin.system.getByName(player.getName()).getOfflineMode()) {
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 100, 1.4F);
                    MyPlugin.system.getByName(player.getName()).setOfflineStatus(false);
                } else {
                    player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 100, 1.4F);
                    MyPlugin.system.getByName(player.getName()).setOfflineStatus(true);
                }
                player.openInventory(Menus.getMainInventory(player.getName()));
            }
            if (event.getRawSlot() == 2) {
                player.openInventory(Menus.getStorageSelectionMenu());
            }
            if (event.getRawSlot() == 3) {
                if (player.getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(), "Npc_TenSeconds"), PersistentDataType.INTEGER) == 1) {
                    player.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "Npc_TenSeconds"), PersistentDataType.INTEGER, 0);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 100, 0.5F);
                } else {
                    player.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "Npc_TenSeconds"), PersistentDataType.INTEGER, 1);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 100, 1.6F);
                }

                player.openInventory(Menus.getMainInventory(player.getName()));
            }
            if (event.getRawSlot() == 4) {
                MyPlugin.system.getByName(player.getName()).teleportPet();
                MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().cancelNavigation();
                MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().setTarget(player,false);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 100, 1.2F);
            }

            if (event.getRawSlot() == 5) {
                int tier = MyPlugin.system.getByName(player.getName()).getPetTier();
                if (tier == 1) {
                    player.sendMessage(Tools.langText("RankRequired"));
                    player.closeInventory();
                    return;
                }

                new AnvilGUI.Builder()
                        .onComplete((user, text) -> {
                            user.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Name"), PersistentDataType.STRING, text);
                            String name;

                            switch (tier) {
                                case 3 -> name = Tools.colorMSG(text);
                                case 4 -> name = Tools.hexColorMSG(text);
                                default -> {
                                    name = Tools.hexColorMSG(text);
                                    name = ChatColor.stripColor(text);
                                }
                            }
                            MyPlugin.system.getByName(player.getName()).getNpc().setName(name);
                            return AnvilGUI.Response.close();
                        })
                        .text("Nombre")
                        .itemLeft(new ItemStack(Material.IRON_SWORD))
                        .itemRight(new ItemStack(Material.IRON_SWORD))
                        .onLeftInputClick(user -> user.sendMessage(Tools.colorMSG("&cEscribe el nuevo nombre!")))
                        .onRightInputClick(user -> user.sendMessage(Tools.colorMSG("&cEscribe el nuevo nombre!")))
                        .title("ESCRIBE UN NOMBRE")
                        .plugin(MyPlugin.getMyPlugin())
                        .open(player);
            }
            if (event.getRawSlot() == 6) {
                player.openInventory(Menus.getPetTypes());
            }
            if (event.getRawSlot() == 7) {
                if(MyPlugin.system.petFindingTreasure.contains(player)){
                    player.sendMessage(Tools.colorMSG("&cTu mascota esta buscando un tesoro."));
                    player.closeInventory();
                    return;
                }
                if (!MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.WOLF) &&
                        !MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.BEE)) {
                    player.sendMessage(Tools.colorMSG("&cOpcion deshabilitada en este tipo de mascota."));
                    player.closeInventory();
                    return;
                }


                if(MyPlugin.system.petTracking.contains(player)){
                    player.sendMessage(Tools.colorMSG("&cTu mascota ya esta rastreando a alguien."));
                    player.closeInventory();
                    return;
                }

                if(MyPlugin.system.petTrackCooldown.containsKey(player)){
                    player.sendMessage(Tools.colorMSG("&cDebes esperar &7" + MyPlugin.system.petTrackCooldown.get(player) +
                            " segundos antes de volver a usar esta funcion."));
                    player.closeInventory();
                    return;
                }

                List<Player> players = new ArrayList<>();
                double range = 50;
                for (Entity entity : player.getNearbyEntities(range, range, range)) {
                    if (entity instanceof Player && entity != player) {
                        players.add((Player) entity);
                    }
                }

                if (players.size() == 0) {
                    player.sendMessage(Tools.colorMSG("&cTu mascota no encontró ninguna jugador cerca de ti."));
                    if (MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.WOLF)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_WOLF_WHINE, 100, 1.3F);
                    }
                    if (MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.BEE)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 100, 1.3F);
                    }
                    player.closeInventory();
                    return;
                }

                int random = Tools.getRandomNumber(1, players.size());
                Player target = players.get(random - 1);
                if (MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.WOLF)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 100, 1.3F);
                }
                if (MyPlugin.system.getByName(player.getName()).getNpc().getEntity().getType().equals(EntityType.BEE)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_BEE_STING, 100, 1.3F);
                }
                MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().setTarget(target, false);
                int afterTime = Tools.getRandomNumber(5, 15);
                MyPlugin.system.petTracking.add(player);
                Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> {
                    String petName = MyPlugin.system.getByName(player.getName()).getNpc().getName();
                    player.sendMessage(Tools.hexColorMSG(petName + " #716dbd-> #f0b330Amo, perdí el rastro pero ciertamente fue en esa dirección."));
                    MyPlugin.system.petTracking.remove(player);
                    MyPlugin.system.petTrackCooldown.put(player, 30);
                    MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().cancelNavigation();
                    MyPlugin.system.getByName(player.getName()).getNpc().getNavigator().setTarget(player, false);
                }, afterTime * 20L);

                player.closeInventory();

            }

            if(event.getRawSlot() == 8){

                if(MyPlugin.system.petTracking.contains(player)){
                    player.sendMessage(Tools.colorMSG("&cTu mascota esta olfateando algo!"));
                    player.closeInventory();
                    return;
                }

                if(MyPlugin.system.petFindingTreasure.contains(player)){
                    player.sendMessage(Tools.colorMSG("&cTu mascota esta olfateando algo!"));
                    player.closeInventory();
                    return;
                }


                player.openInventory(Menus.getTreatContainer());
            }

            if(event.getRawSlot() == 13){
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 0.8F);
                MyPlugin.system.getByName(player.getName()).getNpc().destroy();
            }

        } else if (event.getView().getTitle().equals(Tools.colorMSG("&6&lOBJETOS ENCONTRADOS"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null) {
                return;
            }

            List<String> lore = Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getLore();
            if (lore == null) {
                return;
            }
            String key = lore.get(lore.size() - 1);

            int emptySpace = 0;

            for (int i = 0; i < 36; i++) {
                ;
                if (player.getInventory().getItem(i) == null) {
                    emptySpace++;
                }
            }

            if (emptySpace == 0) {
                player.sendMessage(Tools.langText("No_Empty_Slot"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                return;
            }


            PlayerPet pet = MyPlugin.system.getByName(player.getName());
            ItemStack stack = pet.getItemByKey(key);
            pet.removeItemByKey(key);

            player.getInventory().addItem(stack);
            player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_LAY_EGG, 100, 1.8F);
            for (int i = 0; i < 54; i++) {
                event.getInventory().setItem(i, getItemsFound(player).getItem(i));
            }


        } else if (event.getView().getTitle().equals(Tools.colorMSG("&9Almacenamiento de tu mascota"))) {

            if (event.getCurrentItem() == null) {
                return;
            }

            if (Tag.SHULKER_BOXES.getValues().contains(Objects.requireNonNull(event.getCurrentItem()).getType()) ||
                    Tag.SHULKER_BOXES.getValues().contains(Objects.requireNonNull(event.getCursor()).getType())) {
                event.setCancelled(true);
                ((Player) event.getWhoClicked()).sendMessage(Tools.colorMSG("&cEste objeto esta muy pesado para tu mascota. Tu mascota odia este objeto!"));
                ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_TURTLE_LAY_EGG, 100, 1.8F);
                Player player = (Player) event.getWhoClicked();
                player.playSound(player.getLocation().add(0.0, 1.0, 0.0), Sound.BLOCK_ANVIL_PLACE, 100, 0.5F);
            }
        } else if (event.getView().getTitle().equals(Tools.colorMSG("&4MASCOTA NO PRESENTE"))) {


            Player player = (Player) event.getWhoClicked();
            PlayerPet pet = MyPlugin.system.getByName(player.getName());
            event.setCancelled(true);

            if(event.getRawSlot() == 4){


                MyPlugin.system.petSpawning.add(player);
                player.closeInventory();
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                new BukkitRunnable() {
                    double artificial_Y = y + 10;
                    int count = 0;

                    @Override
                    public void run() {
                        if (count == 0) {
                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, new Location(player.getWorld(), x, artificial_Y, z), 5);
                            artificial_Y = artificial_Y - 1.0;
                        }
                        if (artificial_Y == y) {
                            if (count >= 6) {
                                this.cancel();
                                Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                                    MyPlugin.system.petSpawning.remove(player);
                                    player.getWorld().strikeLightningEffect(new Location(player.getWorld(), x, y, z));
                                    pet.spawnPet(new Location(player.getWorld(), x, y, z));
                                    pet.getNpc().getNavigator().setTarget(player, false);
                                });
                            } else {
                                player.getWorld().spawnParticle(Particle.TOTEM, new Location(player.getWorld(), x, y, z), 3);
                                count++;
                            }
                        }

                    }
                }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 7);
            }
        }
    }


    public Inventory getItemsFound(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, Tools.colorMSG("&6&lOBJETOS ENCONTRADOS"));
        PlayerPet pet = MyPlugin.system.getByName(player.getName());
        List<ItemStack> items = pet.getItems();
        for (ItemStack stack : items) {
            inventory.addItem(stack);
        }
        return inventory;
    }


}
