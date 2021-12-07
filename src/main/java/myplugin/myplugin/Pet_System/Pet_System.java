package myplugin.myplugin.Pet_System;

import myplugin.myplugin.LivesSystem.livesTabCompleter;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Server_Selling.SellingCommand;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.*;

public class Pet_System {
    private MyPlugin plugin;

    public Pet_System(MyPlugin plugin) {
        this.plugin = plugin;
    }

    private List<ItemStack> _LOW_;
    private List<ItemStack> _MEDIUM_;
    private List<ItemStack> _HIGH_;
    public List<PlayerPet> pets = new ArrayList<>();
    public HashMap<Player, String> discordLink = new HashMap<>();
    public YamlFile discordMinecraft = new YamlFile("plugins/MyPlugin/DiscordMinecraft.yml");
    public HashMap<Player, Integer> petTrackCooldown = new HashMap<>();
    public List<Player> petTracking = new ArrayList<>();
    public List<Player> petFindingTreasure = new ArrayList<>();
    public List<Player> petSpawning = new ArrayList<>();
    public YamlFile treasureFile;
    public List<String> keys = new ArrayList<>();

    public void start_system() {
        _LOW_ = new ArrayList<>();
        _MEDIUM_ = new ArrayList<>();
        _HIGH_ = new ArrayList<>();
        generateLowQualityItems();
        generateMidQualityItems();
        generateHighQualityItems();
        plugin.getServer().getPluginManager().registerEvents(new JoinEventPet(plugin, this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MainMenuEvent(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new onStorage(plugin), plugin);
        Objects.requireNonNull(plugin.getCommand("pet")).setExecutor(new Commands());
        Objects.requireNonNull(plugin.getCommand("discord")).setExecutor(new DiscordVerify());
        loadIpLinkFile();
        proximityCheck();
        plugin.getServer().getPluginManager().registerEvents(new onPetInteract(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new onPetInteract.onPetType_Menu(), plugin);
        startPetTrackerCooldown();
        Objects.requireNonNull(plugin.getCommand("opItem")).setExecutor(new Commands());
        Objects.requireNonNull(plugin.getCommand("opItem")).setTabCompleter(new livesTabCompleter());
        plugin.getServer().getPluginManager().registerEvents(new onTreatContainer(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new onGamemodeChange(), plugin);
        rainbowPetName();
        generateTreasurePetFile();
        Objects.requireNonNull(plugin.getCommand("treasureRewards")).setExecutor(new TreasureRewards());
        Objects.requireNonNull(plugin.getCommand("treasureRewards")).setTabCompleter(new TreasureRewards_TabCompleter());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new treasurePlace(), MyPlugin.getMyPlugin());
        loadKeys();
    }

    public void loadKeys() {
        List<String> keysTemp = treasureFile.getKeys(false).stream().toList();
        for(String key : keysTemp){
            int weight = treasureFile.getInt(key + ".Weight");
            for(int i = 0; i < weight; i++){
               keys.add(key);
            }
        }

    }

    public void generateTreasurePetFile() {
        treasureFile = new YamlFile("plugins/MyPlugin/TreasureFile.yml");
        try {
            if(!treasureFile.exists()){
               MyPlugin.getMyPlugin().saveResource("TreasureFile.yml", true);
            }
            treasureFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startPetTrackerCooldown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (petTrackCooldown.size() != 0) {
                    for (Map.Entry<Player, Integer> entry : petTrackCooldown.entrySet()) {
                        if (entry.getValue() <= 0) {
                            petTrackCooldown.remove(entry.getKey());
                        } else {
                            petTrackCooldown.put(entry.getKey(), (entry.getValue() - 1));
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    private void loadIpLinkFile() {
        try {
            if (!discordMinecraft.exists()) {
                plugin.saveResource("DiscordMinecraft.yml", true);
            }
            discordMinecraft.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateLowQualityItems() {

        put_Low_Item(Material.STONE_SHOVEL);
        put_Low_Item(Material.STONE_PICKAXE);
        put_Low_Item(Material.STONE_AXE);
        put_Low_Item(Material.STONE_HOE);
        put_Low_Item(Material.STONE_SWORD);
        put_Low_Item(Material.ALLIUM);
        put_Low_Item(Material.AZURE_BLUET);
        put_Low_Item(Material.CHEST);
        put_Low_Item(Material.LEATHER_LEGGINGS);
        put_Low_Item(Material.POPPY);
        put_Low_Item(Material.WHEAT_SEEDS);
        put_Low_Item(Material.LEATHER_CHESTPLATE);
        put_Low_Item(Material.COAL);
        put_Low_Item(Material.STRING);
        put_Low_Item(Material.DIAMOND);
        put_Low_Item(Material.SHEARS);
        put_Low_Item(Material.NAME_TAG);
        put_Low_Item(Material.LEAD);
        put_Low_Item(Material.FISHING_ROD);
        put_Low_Item(Material.WATER_BUCKET);

    }

    private void generateMidQualityItems() {
        put_Mid_Item(Material.BAKED_POTATO);
        put_Mid_Item(Material.COOKED_PORKCHOP);
        put_Mid_Item(Material.IRON_PICKAXE);
        put_Mid_Item(Material.EMERALD);
        put_Mid_Item(Material.SLIME_BALL);
        put_Mid_Item(Material.IRON_HOE);
        put_Mid_Item(Material.GOLDEN_PICKAXE);
        put_Mid_Item(Material.IRON_SHOVEL);
        put_Mid_Item(Material.IRON_AXE);
        put_Mid_Item(Material.HEART_OF_THE_SEA);
        put_Mid_Item(Material.GOLDEN_APPLE);
        put_Mid_Item(Material.COOKED_RABBIT);
        put_Mid_Item(Material.CAKE);
        put_Mid_Item(Material.FLINT_AND_STEEL);
        put_Mid_Item(Material.TURTLE_EGG);
        put_Mid_Item(Material.IRON_HELMET);
        put_Mid_Item(Material.IRON_LEGGINGS);
        put_Mid_Item(Material.IRON_CHESTPLATE);
        put_Mid_Item(Material.IRON_BOOTS);
        put_Mid_Item(Material.BRICK);
        put_Mid_Item(Material.SHULKER_SHELL);
        put_Mid_Item(Material.SADDLE);
    }

    private void generateHighQualityItems() {
        put_High_Item(Material.DIAMOND_CHESTPLATE);
        put_High_Item(Material.DIAMOND_LEGGINGS);
        put_High_Item(Material.DIAMOND_BOOTS);
        put_High_Item(Material.DIAMOND_HELMET);
        put_High_Item(Material.DIAMOND_SWORD);
        put_High_Item(Material.DIAMOND_PICKAXE);
        put_High_Item(Material.DIAMOND_AXE);
        put_High_Item(Material.DIAMOND_SHOVEL);
        put_High_Item(Material.DIAMOND_SHOVEL);
        put_High_Item(Material.ENCHANTED_GOLDEN_APPLE);
        put_High_Item(Material.NETHERITE_INGOT);
        put_High_Item(Material.SHULKER_BOX);
        put_High_Item(Material.ENDER_CHEST);
        put_High_Item(Material.END_CRYSTAL);
        put_High_Item(Material.DRAGON_HEAD);
        put_High_Item(Material.TNT);
        put_High_Item(Material.DIAMOND);
        put_High_Item(Material.EMERALD);
        put_High_Item(Material.ENDER_PEARL);
        put_High_Item(Material.TRIDENT);
    }

    private void put_High_Item(Material material) {
        _HIGH_.add(new ItemStack(material));
    }

    private void put_Mid_Item(Material material) {
        _MEDIUM_.add(new ItemStack(material));
    }

    private void put_Low_Item(Material material) {
        _LOW_.add(new ItemStack(material));
    }

    public List<ItemStack> getHighItems() {
        return _HIGH_;
    }

    public List<ItemStack> getMedItems() {
        return _MEDIUM_;
    }

    public List<ItemStack> getLowItems() {
        return _LOW_;
    }


    public PlayerPet getByName(String name) {
        PlayerPet pet = null;
        for (PlayerPet playerPet : pets) {
            if (playerPet.getPlayer().getName().equalsIgnoreCase(name)) {
                pet = playerPet;
                break;
            }
        }
        return pet;
    }

    public void proximityCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (PlayerPet pet : pets) {
                    if (pet.getNpc().isSpawned()) {
                        if (pet.getPlayer().getPersistentDataContainer().get(new NamespacedKey(plugin, "Npc_TenSeconds"), PersistentDataType.INTEGER) == 1) {
                            pet.teleportPet();
                        }
                    }
                }
            }

        }.runTaskTimer(plugin, 0, 2400);
    }


    public void deleteAllPets() {
        /*
        for(World world : MyPlugin.getMyPlugin().getServer().getWorlds()){
            for(Entity entity : world.getEntities()){
                if(CitizensAPI.getNPCRegistry().isNPC(entity)){
                    char[] characters = new char[CitizensAPI.getNPCRegistry().getNPC(entity).getName().length()];
                    if(characters[characters.length - 1] == '⚫'){
                        CitizensAPI.getNPCRegistry().getNPC(entity).destroy();
                    }
                }
            }
        }

         */
        for (PlayerPet pet : pets) {
            pet.getNpc().destroy();
        }

    }


    public void rainbowPetName() {

        List<String> hexColors = Arrays.asList("#eb3434", "#eb5934", "#eb7434", "#ebb434", "#ebe834", "#aeeb34", "#7aeb34", "#7aeb34", "#34eb5f", "#34eb5f", "#34eba8", "#34ebd0", "#34aeeb", "#3480eb", "#3459eb",
                "#3d34eb", "#6234eb", "#6534eb", "#a534eb", "#c634eb", "#eb34dc", "#eb34b1", "#eb3489", "#eb346b", "#eb3453", "#eb3434");
        final int[] track = {0};

        new BukkitRunnable() {
            @Override
            public void run() {

                for (PlayerPet pet : pets) {
                    if (pet.getNpc().isSpawned()) {
                        if (pet.getPlayer() != null) {
                            if (pet.getPlayer().hasPermission("Pet.RainbowName")) {
                                if (pet.getRawPetName().startsWith("[U]")) {
                                    String name = pet.getRawPetName();
                                    name = name.replace("[U]", "");
                                    name = hexColors.get(track[0]) + name;
                                    name = Tools.hexColorMSG(name);
                                    String finalName = name;
                                    Bukkit.getScheduler().runTask(plugin, () -> pet.getNpc().setName(finalName));
                                }
                            }
                        }
                    }
                }

                if (track[0] < hexColors.size() && (track[0] + 1) != hexColors.size()) {
                    track[0]++;
                }else{
                    track[0] = 0;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 5);
    }


}
