package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player_Settings_Menu {

    public static Inventory getOptionsInventory(String playerName) {
        List<Integer> pane_Slots = Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        Inventory inventory = Bukkit.createInventory(null, 54, Tools.colorMSG("&2Configuracion de vendedor."));

        for (int number : pane_Slots) {
            inventory.setItem(number, getPane());
        }


        YamlFile file = new YamlFile("plugins/MyPlugin/SellingSystem/" + playerName + ".yml");
        int ganancia = 0;
        try {
            if (MyPlugin.system.getByName(playerName) != null) {
                file = MyPlugin.system.getByName(playerName).getFile();
            } else {
                file.load();
            }
            ganancia = file.getInt("Ganancia");
            inventory.setItem(4, getGananciaItem(playerName, ganancia));


            List<ItemStack> expired = new ArrayList<>();

            for (String key : file.getConfigurationSection("Items").getKeys(false)) {
                if (file.getBoolean("Items." + key + ".Expired")) {

                    ItemStack toAdd = Tools.itemStackFromBase64(file.getString("Items." + key + ".Base64"));
                    ItemMeta meta = toAdd.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    lore.add(Tools.colorMSG("&4&lCADUCADO"));
                    lore.add(Tools.colorMSG("&8" + key));
                    lore.add(Tools.colorMSG("&7&oClic para quitar de las subastas."));
                    assert meta != null;
                    meta.setLore(lore);
                    toAdd.setItemMeta(meta);

                    expired.add(toAdd);
                }else{
                    ItemStack toAdd = Tools.itemStackFromBase64(file.getString("Items." + key + ".Base64"));
                    ItemMeta meta = toAdd.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(" ");
                    lore.add(Tools.colorMSG("&a&lEN VENTA"));
                    lore.add(Tools.colorMSG("&8" + key));
                    lore.add(Tools.colorMSG("&7&oClic para quitar de las subastas."));
                    assert meta != null;
                    meta.setLore(lore);
                    toAdd.setItemMeta(meta);

                    expired.add(toAdd);
                }
            }

            boolean done = false;

            while (!done) {
                if (expired.size() == 0 || expired.isEmpty() || expired.get(0) == null) {
                    break;
                }

                if (inventory.firstEmpty() != 1) {
                    inventory.addItem(expired.get(0));
                    expired.remove(0);
                } else {
                    done = true;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return inventory;
    }

    private static ItemStack getGananciaItem(String playerName, int ganancia) {


        ItemStack stack = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&e&lGANANCIA"));
        List<String> lore = new ArrayList<>();
        lore.add(Tools.colorMSG("&a&o" + ganancia));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack getPane() {

        ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        stack.setItemMeta(meta);
        return stack;

    }

}
