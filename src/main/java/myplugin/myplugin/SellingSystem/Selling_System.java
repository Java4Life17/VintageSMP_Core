package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.LivesSystem.livesTabCompleter;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Selling_System {

    private MyPlugin plugin;

    public Selling_System(MyPlugin plugin) {
        this.plugin = plugin;
    }

    public HashMap<Integer, Inventory> pages;
    public List<Selling_Item> selling_Items = new ArrayList<>();
    public HashMap<Player, Integer> server_balance = new HashMap<>();
    public Map<String, Integer> topMoney = new HashMap<>();
    public List<Selling_Item> itemsList = new ArrayList<>();

    public void startSystem() throws IOException {
        plugin.getServer().getPluginManager().registerEvents(new SellingSystem_Events(), plugin);
        setUpSells();
        Objects.requireNonNull(plugin.getCommand("Subastas")).setExecutor(new SellingSystem_Commands());
        plugin.getServer().getPluginManager().registerEvents(new Auction_Inventory_Listener(), plugin);
        Objects.requireNonNull(plugin.getCommand("monedas")).setExecutor(new SellingSystem_Commands());
        Objects.requireNonNull(plugin.getCommand("monedas")).setTabCompleter(new livesTabCompleter());
        plugin.getServer().getPluginManager().registerEvents(new Player_Settings_Listener(), plugin);
        startMoneyUpdater();

    }

    public void setUpSells() throws IOException {
        pages = new HashMap<>();
        loadFiles();

        if (selling_Items.size() != 0) {
            Seller_Inventories_Creator.createPages();
        }

    }


    private void loadFiles() throws IOException {

        File[] files = new File("plugins/MyPlugin/SellingSystem").listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + file.getName());
            try {
                yamlFile.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String userName = file.getName();
            userName = userName.replace(".yml", "");

            if(!yamlFile.contains("Items")){
                yamlFile.createSection("Items");
            }

            if (yamlFile.getConfigurationSection("Items").getKeys(false).size() != 0 || yamlFile.contains("Items")) {
                for (String key : yamlFile.getConfigurationSection("Items").getKeys(false)) {

                    String str = yamlFile.getString("Items." + key + ".Expiration_Date");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
                    String toCompare = LocalDateTime.now().format(formatter);
                    LocalDateTime now = LocalDateTime.parse(toCompare, formatter);

                    if (now.isAfter(dateTime)) {
                        yamlFile.set("Items." + key + ".Expired", true);
                        yamlFile.save();
                    }


                    boolean itsExpired = yamlFile.getBoolean("Items." + key + ".Expired");
                    if (!itsExpired) {
                        selling_Items.add(new Selling_Item(userName, yamlFile.getInt("Items." + key + ".Price"), key, yamlFile.getString("Items." + key + ".Base64"), yamlFile.getString("Items." + key + ".Expiration_Date"), yamlFile));
                        itemsList.add(new Selling_Item(userName, yamlFile.getInt("Items." + key + ".Price"), key, yamlFile.getString("Items." + key + ".Base64"), yamlFile.getString("Items." + key + ".Expiration_Date"), yamlFile));
                    }
                }
            }
        }
    }


    public void generateTopSellersList() {

        File[] files = new File("plugins/MyPlugin/SellingSystem").listFiles();

        HashMap<String, Integer> temporal = new HashMap<>();

        assert files != null;
        for (File file : files) {

            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + file.getName());
            try {
                yamlFile.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String userName = file.getName();
            userName = userName.replace(".yml", "");

            temporal.put(userName, yamlFile.getInt("Coins"));
        }

        topMoney = temporal.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


    }

    public String getTopString() {

        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append(Tools.colorMSG("&e&l"));
        builder.append("\n");
        builder.append("\n");
        int count = 1;


        for (Map.Entry<String, Integer> entry : topMoney.entrySet()) {
            builder.append(Tools.colorMSG("&6&l" + count + ".&7&o " + entry.getKey() + " &1- &8&o" + entry.getValue() + "\n"));
            count++;
        }

        builder.append(Tools.colorMSG("&e&l"));
        builder.append("\n");

        return builder.toString();

    }

    public void startMoneyUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, Integer> entry : server_balance.entrySet()) {
                    YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + entry.getKey().getName() + ".yml");
                    try {
                        yamlFile.load();
                        yamlFile.set("Coins", entry.getValue());
                        yamlFile.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                generateTopSellersList();
            }
        }.runTaskTimerAsynchronously(plugin, 0, 6000);
    }

}
