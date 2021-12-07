package myplugin.myplugin;

import myplugin.myplugin.AdminSystem.AdminSystem;
import myplugin.myplugin.Clan_System.Clan_System;
import myplugin.myplugin.Clan_System.SimpleLogger;
import myplugin.myplugin.Combat.Combat;
import myplugin.myplugin.Conversation.AutoMessage;
import myplugin.myplugin.Conversation.Conversation;
import myplugin.myplugin.Conversation.ConversationManager;
import myplugin.myplugin.DeathRecords.BackCommand;
import myplugin.myplugin.DeathRecords.DeathRecord;
import myplugin.myplugin.Discord_System.DiscordSystem;
import myplugin.myplugin.Discord_System.cheatingCommand;
import myplugin.myplugin.IpControl.IpControl;
import myplugin.myplugin.LivesSystem.LivesSystem;
import myplugin.myplugin.LocationSystem.Locations;
import myplugin.myplugin.Pet_System.Pet_System;
import myplugin.myplugin.PlayersNumber.PlayerNumberGenerator;
import myplugin.myplugin.SellingSystem.Selling_System;
import myplugin.myplugin.Server_Selling.Server_Selling;
import myplugin.myplugin.TeleportRequest.TeleportRequest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;

public final class MyPlugin extends JavaPlugin implements Listener {

    public static PlayerNumberGenerator generator;
    public static HashMap<Player, Integer> playerNumbersMap;
    public static Locations locations;
    public static final YamlFile languageFile = new YamlFile("plugins/MyPlugin/Lang.yml");
    public static LivesSystem livesSystem;
    public static Selling_System selling_system;
    public static Server_Selling server_selling;
    public static DiscordSystem discordSystem;
    public static IpControl ipControl;
    public static Pet_System system;
    private static MyPlugin plugin;
    public static boolean whiteList;
    public static AutoRestart autoRestart;
    public static List<ItemStack> specialItems;
    public static List<Material> minedAllowed = new ArrayList<>();
    public static Combat combat;
    public static NPCRegistry registry;
    public static TeleportRequest teleportRequestSystem;
    public static AdminSystem adminSystem;
    public static HashMap<Block, Material> onEffect = new HashMap<>();
    public static Clan_System clan_system;
    private static SimpleLogger logger;
    private static ConversationManager conversationManager;
    private static AutoMessage autoMessage;
    public static Map<Player, DeathRecord> deathRecordMap;

    @Override
    public void onEnable() {


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            //Bukkit.getPluginManager().registerEvents(this, this);

        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getServer().getPluginManager().registerEvents(this, this);

        whiteList = false;
        plugin = this;

        conversationManager = new ConversationManager();
        autoMessage = new AutoMessage();
        playerNumbersMap = new HashMap<>();
        generator = new PlayerNumberGenerator(this);
        generator.startSystem();


        locations = new Locations(this);
        locations.startSystem();
        loadLanguageFile();

        combat = new Combat();
        combat.start();

        livesSystem = new LivesSystem(this);
        livesSystem.startSystem();

        selling_system = new Selling_System(this);
        try {
            selling_system.startSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }

        server_selling = new Server_Selling(this);
        server_selling._start_selling_system_();
        discordSystem = new DiscordSystem(this);
        try {
            discordSystem.startBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        ipControl = new IpControl(this);
        ipControl.start_System();

        system = new Pet_System(this);
        system.start_system();

        minedAllowed = Arrays.asList(Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.DIORITE,
                Material.GRAVEL, Material.GRANITE, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS);
        Objects.requireNonNull(getCommand("custodian")).setExecutor(new RestartSystem());
        specialItems = Arrays.asList(Tools.getPetTreat());

        getServer().getPluginManager().registerEvents(new MiningEvent(), this);
        getServer().getPluginManager().registerEvents(new chatRankFormat(), this);
        new SpigotExpansion().register();

        Objects.requireNonNull(getCommand("discordCheater")).setExecutor(new cheatingCommand());
        teleportRequestSystem = new TeleportRequest();
        teleportRequestSystem.start();
        adminSystem = new AdminSystem();
        adminSystem.initialize();

        clan_system = new Clan_System();
        clan_system.startSystem();

        avoidGC();
        autoMessage.start();

        registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        deathRecordMap = new HashMap<>();

        getCommand("back").setExecutor(new BackCommand());

    }

    private void avoidGC() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Tools.avoidGC();
            }
        }.runTaskTimerAsynchronously(this, 0, 6000);
    }

    @Override
    public void onDisable() {
        turnEveryEffectBlock();
        for (Map.Entry<Player, Integer> entry : MyPlugin.selling_system.server_balance.entrySet()) {
            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + entry.getKey().getName() + ".yml");
            try {
                yamlFile.load();
                yamlFile.set("Coins", entry.getValue());
                yamlFile.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        livesSystem.saveAllPlayers();
        discordSystem.disableBot();
        system.deleteAllPets();

    }

    private void loadLanguageFile() {
        try {
            if (!languageFile.exists()) {
                this.saveResource("Lang.yml", true);
            }
            languageFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRandomIntegerBetweenRange(double min, double max) {
        return (int) ((int) (Math.random() * ((max - min) + 1)) + min);

    }

    public static MyPlugin getMyPlugin() {
        return plugin;
    }

    public void turnEveryEffectBlock() {
        for (Map.Entry<Block, Material> entry : onEffect.entrySet()) {
            entry.getKey().setType(entry.getValue());
        }
    }

    /**
     * Gets this plugin's Logger instance
     *
     * @return SimpleLogger
     */
    public static SimpleLogger getSimpleLogger() {
        if (logger != null) return logger;
        return new SimpleLogger();
    }

    @EventHandler
    public void citizensEnable(CitizensEnableEvent event) {
        System.out.println("CITIZENS ENABLED");
        registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
    }

    public static ConversationManager getConversationManager() {
        return conversationManager;
    }

    public static AutoMessage getAutoMessage() {
        return autoMessage;
    }
}
