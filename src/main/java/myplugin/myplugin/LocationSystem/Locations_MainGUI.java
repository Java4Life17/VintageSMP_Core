package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Locations_MainGUI {

    public static Inventory getGUI() {

        Inventory inventory = Bukkit.createInventory(null, 27, Tools.colorMSG("&0&lDISTRICT GUI"));

        ItemStack listItem = new ItemStack(Material.CLOCK, 1);
        ItemMeta listMeta = listItem.getItemMeta();
        assert listMeta != null;
        listMeta.setDisplayName(Tools.colorMSG("&4&lDistricts List"));
        List<String> listLore = new ArrayList<>();
        listLore.add(Tools.colorMSG("\n&7&oClick this option to see a\n&7&olist of all the " +
                "available districts!"));
        listMeta.setLore(listLore);
        listItem.setItemMeta(listMeta);
        inventory.setItem(11, listItem);

        ItemStack createItem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta createMeta = createItem.getItemMeta();
        assert createMeta != null;
        createMeta.setDisplayName(Tools.colorMSG("&4&lCreate a District"));
        List<String> createLore = new ArrayList<>();
        createLore.add(Tools.colorMSG("\n&7&oClick this option to create a\n&7&onew district location"));
        createMeta.setLore(createLore);
        createItem.setItemMeta(createMeta);
        inventory.setItem(13, createItem);

        ItemStack editItem = new ItemStack(Material.BOOK);
        ItemMeta editMeta = editItem.getItemMeta();
        assert editMeta != null;
        editMeta.setDisplayName(Tools.colorMSG("&4&lDistrict Editor"));
        List<String> editLore = new ArrayList<>();
        editLore.add(Tools.colorMSG("\n&7&oClick this option to edit a\n&7&oexisting district locations"));
        editMeta.setLore(editLore);
        editItem.setItemMeta(editMeta);
        inventory.setItem(15, editItem);

        ItemStack display = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta displayMeta = display.getItemMeta();
        assert displayMeta != null;
        displayMeta.setDisplayName(" ");
        display.setItemMeta(displayMeta);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, display);
            }
        }

        return inventory;
    }

    public static Inventory getListToEdit() {
        Inventory inventory = Bukkit.createInventory(null, 54, Tools.colorMSG("&0&lDISTRICT EDIT GUI"));


        ItemStack stack1 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta1 = stack1.getItemMeta();
        assert meta1 != null;
        meta1.setDisplayName(" ");
        stack1.setItemMeta(meta1);

        for (String key : MyPlugin.locations.locationsFile.getKeys(false)) {
            ItemStack identifier = new ItemStack(Material.IRON_INGOT);
            ItemMeta identifierMeta = identifier.getItemMeta();
            assert identifierMeta != null;
            identifierMeta.setDisplayName(Tools.colorMSG("&a" + key + "&r"));
            List<String> lore = new ArrayList<>();
            lore.add(Tools.colorMSG("&7Click to edit this district!"));
            identifierMeta.setLore(lore);
            identifier.setItemMeta(identifierMeta);
            inventory.addItem(identifier);
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, stack1);
            }
        }


        return inventory;
    }

    public static Inventory getSpecificDisEditor(String key) {
        Inventory inventory = Bukkit.createInventory(null, 9, Tools.colorMSG("&0&lEditor&f: &7" + key));

        ItemStack location = new ItemStack(Material.COMPASS);
        ItemMeta meta = location.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&4&lLOCATION"));
        List<String> lore = new ArrayList<>();
        lore.add(Tools.colorMSG("&7Click to set the location to your\n&7current location!"));
        meta.setLore(lore);
        location.setItemMeta(meta);
        inventory.setItem(3, location);

        ItemStack sound = new ItemStack(Material.MUSIC_DISC_11);
        ItemMeta soundMeta = sound.getItemMeta();
        assert soundMeta != null;
        soundMeta.setDisplayName(Tools.colorMSG("&4&lSOUNDS"));
        List<String> lore1 = new ArrayList<>();
        lore1.add(Tools.colorMSG("&7Click to set the sound for this location!"));
        soundMeta.setLore(lore1);
        sound.setItemMeta(soundMeta);
        inventory.setItem(5, sound);

        ItemStack delete = new ItemStack(Material.REDSTONE);
        ItemMeta deleteMeta = delete.getItemMeta();
        assert deleteMeta != null;
        deleteMeta.setDisplayName(Tools.colorMSG("&c&lDELETE"));
        delete.setItemMeta(deleteMeta);
        inventory.setItem(7, delete);


        ItemStack stack1 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta1 = stack1.getItemMeta();
        assert meta1 != null;
        meta1.setDisplayName(" ");
        stack1.setItemMeta(meta1);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, stack1);
            }
        }

        return inventory;
    }



}
