package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.*;

public class PlayerPet {

    private final Pet_System system;
    private int tier = 1;
    private boolean isEnabled = true;
    private int min = 60;
    private int max = 90;
    private Player player = null;
    int minutes = 0;
    private final MyPlugin plugin;
    private final YamlFile file;
    private int timeAccordingToTire;
    String playerName;
    public final Pet_Storage storage;
    public boolean hasStorage_one;
    public boolean hasStorage_two;
    public boolean hasStorage_three;
    public boolean hasStorage_four;
    public boolean hasStorage_five;
    private NPC npc;


    public PlayerPet(Pet_System system, Player player, MyPlugin plugin) {
        this.system = system;
        this.player = player;
        this.playerName = player.getName();
        this.plugin = plugin;


        file = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
        try {
            file.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (player.hasPermission("MyPlugin.PetTier.2")) {
            min = 45;
            max = 70;
            tier = 2;
            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 0;
        }
        if (player.hasPermission("MyPlugin.PetTier.3")) {
            min = 45;
            max = 70;
            tier = 3;

            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 90;
        }
        if (player.hasPermission("MyPlugin.PetTier.4")) {
            min = 20;
            max = 40;
            tier = 4;
            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 140;
        }

        hasStorage_one = player.hasPermission("AdditionalStorage.1");
        hasStorage_two = player.hasPermission("AdditionalStorage.2");
        hasStorage_three = player.hasPermission("AdditionalStorage.3");
        hasStorage_four = player.hasPermission("AdditionalStorage.4");
        hasStorage_five = player.hasPermission("AdditionalStorage.5");

        minutes = Tools.getRandomNumber(min, max);
        startRunning();
        storage = new Pet_Storage(player.getName(), tier, this);

    }


    public void eliminate() {
        isEnabled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public void startRunning() {
        new BukkitRunnable() {
            int count = 0;
            int seconds = 0;

            @Override
            public void run() {
                if (player == null && !getOfflineMode() && timeAccordingToTire == 0) {
                    eliminate();
                    checkIfCanCancel();
                    this.cancel();
                    return;

                }
                if (!isEnabled) {
                    Bukkit.getLogger().info(Tools.colorMSG("&cSe apago el pet de " + playerName + "!"));
                    checkIfCanCancel();
                    this.cancel();
                    return;
                }
                seconds++;

                if (seconds == 60) {
                    if (Bukkit.getPlayer(playerName) == null) {
                        if (timeAccordingToTire > 0) {
                            timeAccordingToTire--;
                            file.set("TimeLeft", timeAccordingToTire);
                            saveFile();
                        } else {
                            if (file.getInt("TimeLeft") != 0) {
                                file.set("TimeLeft", 0);
                                file.set("Offline", false);
                                saveFile();
                            }
                            MyPlugin.discordSystem.sendPetShuttingDown(playerName);
                            eliminate();
                            this.cancel();
                            return;
                        }
                    }
                    count++;
                    seconds = 0;
                }


                if (count == minutes) {
                    count = 0;
                    minutes = Tools.getRandomNumber(min, max);

                    List<ItemStack> stacks = getItemsAccordingToTier();
                    if (Bukkit.getPlayer(playerName) != null) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 0.3F);
                    }
                    if (player == null && !getOfflineMode() && timeAccordingToTire == 0) {
                        eliminate();
                        checkIfCanCancel();
                        this.cancel();

                        return;

                    }
                    for (ItemStack stack : stacks) {
                        String type = stack.getType().toString();
                        int damage = 0;
                        if (stack.getItemMeta() instanceof Damageable) {
                            {
                                int max = Math.abs(stack.getType().getMaxDurability());
                                if (max > 0) {
                                    damage = Tools.getRandomNumber(1, max);
                                    damage = Math.abs(damage);
                                }
                            }
                        }

                        List<String> keys;
                        if (file.getConfigurationSection("Pet_Items").getKeys(false).stream().toList() == null) {
                            keys = new ArrayList<>();
                        } else {
                            keys = file.getConfigurationSection("Pet_Items").getKeys(false).stream().toList();
                        }
                        String key = Tools.getAlphaNumericString(6);

                        while (keys.contains(key)) {
                            key = Tools.getAlphaNumericString(6);
                        }

                        file.getConfigurationSection("Pet_Items").createSection(key);
                        file.set("Pet_Items." + key + ".Damage", damage);
                        file.set("Pet_Items." + key + ".Type", type);
                        saveFile();
                    }


                    if (Bukkit.getPlayer(playerName) == null) {
                        MyPlugin.discordSystem.sendPetDiscoveryAlert(stacks.size(), playerName, timeAccordingToTire);
                    } else {
                        String message = "";
                        if (stacks.size() == 1) {
                            message = Tools.colorMSG("&aTu mascota ha descubierto &c1 &aobjeto tirado.");
                        } else {
                            player.sendMessage(Tools.colorMSG("&aTu mascota ha descubierto &c" + stacks.size() + " &aobjetos tirados."));
                        }

                        player.sendMessage(message);
                    }
                }


            }

        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    private void checkIfCanCancel() {
        system.pets.remove(this);
    }

    public ItemStack getRandomElement(List<ItemStack> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private List<ItemStack> getItemsAccordingToTier() {

        List<ItemStack> items = new ArrayList<>();

        if (tier == 1) {
            items.add(getRandomElement(system.getLowItems()));
            if (Tools.getRandomNumber(1, 2) == 2) {
                items.add(getRandomElement(system.getLowItems()));
            }
        }
        if (tier == 2) {
            items.add(getRandomElement(system.getMedItems()));
            int max = Tools.getRandomNumber(1, 3);
            if (max == 2) {
                items.add(getRandomElement(system.getLowItems()));
            }
            if (max == 3) {
                items.add(getRandomElement(system.getLowItems()));
                items.add(getRandomElement(system.getMedItems()));
            }
        }
        if (tier == 3) {
            items.add(getRandomElement(system.getMedItems()));
            int max = Tools.getRandomNumber(1, 3);
            if (max == 2) {
                items.add(getRandomElement(system.getMedItems()));
            }
            if (max == 3) {
                items.add(getRandomElement(system.getLowItems()));
                items.add(getRandomElement(system.getMedItems()));
            }
        }
        if (tier == 4) {
            items.add(getRandomElement(system.getHighItems()));
            int max = Tools.getRandomNumber(1, 4);
            if (max == 2) {
                items.add(getRandomElement(system.getMedItems()));
            }
            if (max == 3) {
                items.add(getRandomElement(system.getLowItems()));
                items.add(getRandomElement(system.getMedItems()));
            }
            if (max == 4) {
                items.add(getRandomElement(system.getLowItems()));
                items.add(getRandomElement(system.getMedItems()));
                items.add(getRandomElement(system.getHighItems()));
            }
        }
        return items;
    }

    public Inventory getMainMenu() {
        return Menus.getMainInventory(playerName);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (String key : file.getConfigurationSection("Pet_Items").getKeys(false)) {
            ItemStack stack = new ItemStack(Material.valueOf(file.getString("Pet_Items." + key + ".Type")));
            int damage = file.getInt("Pet_Items." + key + ".Damage");
            ItemMeta meta = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(Tools.colorMSG("&a&o" + key));
            assert meta != null;
            meta.setLore(lore);
            if (damage > 0) {
                ((org.bukkit.inventory.meta.Damageable) meta).setDamage(((org.bukkit.inventory.meta.Damageable) meta).getDamage() + damage);
            }
            stack.setItemMeta(meta);
            items.add(stack);
        }
        return items;
    }

    public void removeItemByKey(String key) {
        key = ChatColor.stripColor(key);
        try {
            file.set("Pet_Items." + key, null);
            file.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemStack getItemByKey(String key) {
        key = ChatColor.stripColor(key);
        ItemStack toReturn = new ItemStack(Material.valueOf(file.getString("Pet_Items." + key + ".Type")));
        int damage = file.getInt("Pet_Items." + key + ".Damage");
        ItemMeta meta = toReturn.getItemMeta();
        if (damage > 0) {
            assert meta != null;
            ((org.bukkit.inventory.meta.Damageable) meta).setDamage(((org.bukkit.inventory.meta.Damageable) meta).getDamage() + damage);
        }
        toReturn.setItemMeta(meta);

        return toReturn;
    }

    public int getPetTier() {
        return tier;
    }

    public boolean getOfflineMode() {
        return file.getBoolean("Offline");
    }

    public void setOfflineStatus(boolean state) {
        file.set("Offline", state);
        saveFile();
    }

    public void saveFile() {
        try {
            file.save();
            file.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTimeAccordingToTire() {
        return timeAccordingToTire;
    }

    public void updateTimeLeft() {
        this.timeAccordingToTire = 0;
        if (player.hasPermission("MyPlugin.PetTier.2")) {
            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 0;
        }
        if (player.hasPermission("MyPlugin.PetTier.3")) {
            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 90;
        }
        if (player.hasPermission("MyPlugin.PetTier.4")) {
            this.timeAccordingToTire = file.contains("TimeLeft") ? file.getInt("TimeLeft") : 4;
        }



    }

    public void reloadFile() {
        try {
            file.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YamlFile getFile() {
        return file;
    }

    public Pet_Storage getPetStorage() {
        return storage;
    }

    public NPC getNpc() {
        return npc;
    }

    public void spawnAtLocation(Location location) {
        npc.spawn(location, SpawnReason.CREATE);
    }

    public void startFollowing() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(npc.isSpawned()) {
                    npc.getNavigator().cancelNavigation();
                    npc.getNavigator().setTarget(player, false);
                }
            }
        }.runTaskLater(plugin, 40);
    }

    public void teleportPet() {
        npc.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
    }

    public void spawnPet(Location location) {

        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "NPC_Type"), PersistentDataType.STRING)) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "NPC_Type"), PersistentDataType.STRING, "WOLF");
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "NPC_Name"), PersistentDataType.STRING, "&a" + player.getName() + " Jr.");

        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "Npc_TenSeconds"), PersistentDataType.INTEGER)) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "Npc_TenSeconds"), PersistentDataType.INTEGER, 0);
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "AppreciationLevel"), PersistentDataType.INTEGER)) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "AppreciationLevel"), PersistentDataType.INTEGER, 0);
        }

        String type = player.getPersistentDataContainer().get(new NamespacedKey(plugin, "NPC_Type"), PersistentDataType.STRING);
        String name = player.getPersistentDataContainer().get(new NamespacedKey(plugin, "NPC_Name"), PersistentDataType.STRING);

        npc = MyPlugin.registry.createNPC(EntityType.valueOf(type), "Null");
        int tier = MyPlugin.system.getByName(player.getName()).getPetTier();
        switch (tier) {
            case 3 -> name = Tools.colorMSG(name);
            case 4 -> name = Tools.hexColorMSG(name);
            default -> {
                name = Tools.hexColorMSG(name);
                name = ChatColor.stripColor(name);
            }
        }

        getNpc().setName(name);

        spawnAtLocation(location);
        startFollowing();
    }

    public void deletePet() {
        npc.destroy();
    }

    public void callSpawnFromTask(Location location) {
        spawnPet(location);
    }
    public String getRawPetName(){
        String name = player.getPersistentDataContainer().get(new NamespacedKey(plugin, "NPC_Name"), PersistentDataType.STRING);;
        return ChatColor.stripColor(name);
    }

}
