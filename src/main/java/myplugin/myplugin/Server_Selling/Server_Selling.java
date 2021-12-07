package myplugin.myplugin.Server_Selling;

import myplugin.myplugin.MyPlugin;
import org.bukkit.Material;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.HashMap;
import java.util.Objects;

public class Server_Selling {

    private MyPlugin plugin;

    public Server_Selling(MyPlugin plugin) {
        this.plugin = plugin;
    }

    public YamlFile selling_Items_File = new YamlFile("plugins/MyPlugin/SellingItems.yml");
    public HashMap<Material, Integer> items_and_prices = new HashMap<>();

    public void _start_selling_system_() {
        loadNeededFile();
        loadItemsAndPrices();
        Objects.requireNonNull(plugin.getCommand("SellPlayerItem")).setExecutor(new SellingCommand());
        plugin.getServer().getPluginManager().registerEvents(new On_ConfirmationMenu_Listener(), plugin);
        Objects.requireNonNull(plugin.getCommand("setPrice")).setExecutor(new SellingCommand());
    }

    public void loadItemsAndPrices() {
        items_and_prices = new HashMap<>();
        for(String material : selling_Items_File.getKeys(false)){
            items_and_prices.put(Material.getMaterial(material), selling_Items_File.getInt(material));
        }
    }

    private void loadNeededFile() {
        try {
            if (!selling_Items_File .exists()) {
                plugin.saveResource("SellingItems.yml", true);
            }
            selling_Items_File.load();
        } catch (Exception e) {
            e.printStackTrace();
        }


        }
    }


