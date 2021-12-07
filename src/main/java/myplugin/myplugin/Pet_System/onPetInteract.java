package myplugin.myplugin.Pet_System;


import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.citizensnpcs.api.event.SpawnReason;
import org.bukkit.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class onPetInteract implements Listener {

    @EventHandler
    public void petInteraction(net.citizensnpcs.api.event.NPCRightClickEvent event) {
        Player p = event.getClicker();
        if (!MyPlugin.system.getByName(p.getName()).getNpc().equals(event.getNPC())) {
            return;
        }

        p.openInventory(Menus.getMainInventory(p.getName()));

    }


    public static class onPetType_Menu implements Listener {

        @EventHandler
        public void onPetSelector(InventoryClickEvent event) {
            if (event.getView().getTitle().equals(Tools.hexColorMSG("#25524b&lTIPOS DE MASCOTAS"))) {
                Player p = (Player) event.getWhoClicked();
                PlayerPet pet = MyPlugin.system.getByName(p.getName());
                event.setCancelled(true);

                if (event.getCurrentItem() == null) {
                    return;
                }
                EntityType type = EntityType.WOLF;

                if (event.getSlot() == 4) {
                    if (pet.getPetTier() != 4) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.CREEPER)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "CREEPER");
                    type = EntityType.CREEPER;
                    p.playSound(p.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 100, 1.1F);
                    p.closeInventory();
                }
                if (event.getSlot() == 18) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4 && pet.getPetTier() != 2) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.SKELETON)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "SKELETON");
                    type = EntityType.SKELETON;
                    p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_HURT, 100, 1.1F);
                    p.closeInventory();
                }
                if (event.getSlot() == 22) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4 && pet.getPetTier() != 2 && pet.getPetTier() != 1) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.WOLF)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "WOLF");
                    type = EntityType.WOLF;
                    p.playSound(p.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 100, 1.1F);
                    p.closeInventory();
                }
                if (event.getSlot() == 26) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.BEE)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "BEE");
                    type = EntityType.BEE;
                    p.playSound(p.getLocation(), Sound.ENTITY_BEE_HURT, 100, 1.1F);
                    p.closeInventory();
                }
                if (event.getSlot() == 27) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4 && pet.getPetTier() != 2) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.VEX)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "VEX");
                    type = EntityType.VEX;
                    p.playSound(p.getLocation(), Sound.ENTITY_VEX_CHARGE, 100, 1.1F);
                    p.closeInventory();
                }
                if (event.getSlot() == 31) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4 && pet.getPetTier() != 2 && pet.getPetTier() != 1) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.PIG)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "PIG");
                    type = EntityType.PIG;
                    p.playSound(p.getLocation(), Sound.ENTITY_PIG_DEATH, 100, 1.1F);
                    p.closeInventory();
                }

                if (event.getSlot() == 35) {
                    if (pet.getPetTier() != 3 && pet.getPetTier() != 4) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.AXOLOTL)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "AXOLOTL");
                    type = EntityType.AXOLOTL;
                    p.playSound(p.getLocation(), Sound.ENTITY_AXOLOTL_SPLASH, 100, 1.1F);
                    p.closeInventory();
                }

                if (event.getSlot() == 49) {
                    if (pet.getPetTier() != 4) {
                        p.sendMessage(Tools.langText("RankRequired"));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    if (pet.getNpc().getEntity().getType().equals(EntityType.ENDERMAN)) {
                        p.sendMessage(Tools.colorMSG("&cYa tienes este tipo de mascota."));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                        return;
                    }
                    p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "NPC_Type"), PersistentDataType.STRING, "ENDERMAN");
                    type = EntityType.ENDERMAN;
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1.1F);
                    p.closeInventory();
                }

                double x = pet.getNpc().getStoredLocation().getX();
                double y = pet.getNpc().getStoredLocation().getY();
                double z = pet.getNpc().getStoredLocation().getZ();
                pet.deletePet();
                MyPlugin.system.petSpawning.add(p);
                new BukkitRunnable() {
                    double artificial_Y = y + 10;
                    int count = 0;

                    @Override
                    public void run() {
                        if (count == 0) {
                            p.getWorld().spawnParticle(Particle.SWEEP_ATTACK, new Location(p.getWorld(), x, artificial_Y, z), 5);
                            artificial_Y = artificial_Y - 1.0;
                        }
                        if (artificial_Y == y) {
                            if (count >= 6) {
                                this.cancel();
                                Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                                    p.getWorld().strikeLightningEffect(new Location(p.getWorld(), x, y, z));
                                    pet.spawnPet(new Location(p.getWorld(), x, y, z));
                                    pet.getNpc().getNavigator().setTarget(p, false);
                                    MyPlugin.system.petSpawning.remove(p);
                                });
                                return;
                            } else {
                                p.getWorld().spawnParticle(Particle.TOTEM, new Location(p.getWorld(), x, y, z), 3);
                                count++;
                            }
                        }

                    }
                }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 7);
            }
        }

        @EventHandler
        public void teleport(PlayerChangedWorldEvent e) {

            if(MyPlugin.clan_system.manager().playerInArena(e.getPlayer())){
                return;
            }

            if(MyPlugin.system.petSpawning.contains(e.getPlayer())){
                return;
            }

            if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
                return;
            }

            MyPlugin.system.petSpawning.add(e.getPlayer());

            MyPlugin.system.getByName(e.getPlayer().getName()).deletePet();
            new BukkitRunnable() {
                @Override
                public void run() {
                    MyPlugin.system.petSpawning.remove(e.getPlayer());
                    if(MyPlugin.system.getByName(e.getPlayer().getName()) == null) return;
                    MyPlugin.system.getByName(e.getPlayer().getName()).spawnPet(e.getPlayer().getLocation());
                }
            }.runTaskLater(MyPlugin.getMyPlugin(), 100);
        }

        @EventHandler
        public void onEntityTeleport(EntityPortalEvent event) {
            if (event.getEntity().hasMetadata("NPC")) {
                event.setCancelled(true);
            }
        }


    }

    @EventHandler
    public void entityTeleport(EntityTeleportEvent event){
        if(!event.getEntity().hasMetadata("NPC")) return;

        for(PlayerPet pet : MyPlugin.system.pets){
            if(MyPlugin.clan_system.manager().playerInArena(pet.getPlayer())){
                event.setCancelled(true);
            }
        }

    }

}
