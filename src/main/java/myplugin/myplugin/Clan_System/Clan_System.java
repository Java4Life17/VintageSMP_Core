package myplugin.myplugin.Clan_System;

import myplugin.myplugin.LocationSystem.onClanEditor;
import myplugin.myplugin.MyPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.*;

public class Clan_System {

    private List<Clan> loadedClans;
    private Clan_Manager clanManager;
    private Inventory clanTools;
    private List<Integer> paneSltos;
    private Inventory editClan;
    public HashMap<Player, Clan> invitePending;
    public final YamlFile arenas = new YamlFile("plugins/MyPlugin/Arenas.yml");
    private List<Arena> activeArenas;
    private Map<Player, Location> pending = new HashMap<>();
    //                 TARGET | CHALLANGER
    private Map<Player, Player> fightRequest = new HashMap<>();

    public Map<Player, Player> getFightRequest() {
        return fightRequest;
    }

    public Map<Player, Location> getPending() {
        return pending;
    }

    public void startSystem() {
        loadedClans = new ArrayList<>();
        clanManager = new Clan_Manager(this);
        editClan = clanManager.getClanSettings();
        clanTools = clanManager.getClanTools();
        paneSltos = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26);
        invitePending = new HashMap<>();
        clanToolsAnimation();
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("openCM")).setExecutor(new Commands());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new onClanMainMenu(), MyPlugin.getMyPlugin());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new playerJoin(), MyPlugin.getMyPlugin());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new onClanEditor(), MyPlugin.getMyPlugin());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new onClanBattleChoose(), MyPlugin.getMyPlugin());
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("clans")).setExecutor(new Commands());
        activeArenas = new ArrayList<>();
        loadArenasFile();
        MyPlugin.getMyPlugin().getCommand("clanEditor").setExecutor(new Commands());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new ArenaEvents(), MyPlugin.getMyPlugin());
    }

    private void loadArenasFile() {
        try {
            if (!arenas.exists()) {
                arenas.createNewFile(true);
                arenas.save();
                arenas.createSection("Biomes");
                arenas.set("Biomes.Time", 10);
                arenas.set("Biomes.InUse", false);
                arenas.save();
            }
            arenas.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get every loaded clan on the server
     *
     * @return get loaded clans
     */
    public List<Clan> getLoadedClans() {
        return loadedClans;
    }

    /**
     * Get the current clan manager being used. Object instance to avoid GC getting rid of it.
     *
     * @return Current clan manager
     */
    public Clan_Manager manager() {
        if (clanManager == null) {
            clanManager = new Clan_Manager(this);
        }

        return clanManager;
    }


    /**
     * Animation for the clan Tools Menu. Very pretty
     */
    private void clanToolsAnimation() {
        List<Material> materials = Arrays.asList(Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE,
                Material.MAGENTA_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
                Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE);
        final int[] track = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (int i : paneSltos) {
                        Objects.requireNonNull(getClanToolsMenu().getItem(i)).setType(materials.get(track[0]));
                    }
                    for (int i = 0; i < 27; i++) {
                        if (i != 10 && i != 12 && i != 14 && i != 16) {
                            Objects.requireNonNull(editClan.getItem(i)).setType(materials.get(track[0]));
                        }
                    }
                } catch (Exception e) {
                    track[0] = 0;
                    for (int i : paneSltos) {
                        Objects.requireNonNull(getClanToolsMenu().getItem(i)).setType(materials.get(track[0]));
                    }
                }
                track[0]++;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 5);
    }

    /**
     * Get the Clan Tools Inventory
     *
     * @return Clan Tools Menu
     */
    public Inventory getClanToolsMenu() {
        return clanTools;
    }

    public Inventory getEditClan() {
        return editClan;

    }

    /**
     * Method to get the active PvP Arenas
     *
     * @return Active Arenas
     */
    public List<Arena> getActiveArenas() {
        return activeArenas;
    }

    public String getNextArena() {
        String toReturn = "NULL";
        for (String key : arenas.getKeys(false)) {
            if (arenas.getConfigurationSection(key).contains("InUse")) {
                if (!arenas.getBoolean(key + ".InUse")) {
                    toReturn = key;
                    break;
                }
            }
        }

        return toReturn;

    }

}
