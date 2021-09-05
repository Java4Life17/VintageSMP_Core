package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SoundsMenuCreator {
    private final String identifier;
    private HashMap<Integer, Inventory> pages;
    private int total;

    public SoundsMenuCreator(String identifier) {
        this.identifier = identifier;
        this.createPagesForMenu();
    }

    private void createPagesForMenu() {
        this.pages = new HashMap();
        List<Sound> sounds = new ArrayList(Arrays.asList(Sound.values()));
        this.total = sounds.size();
        this.startCreation(this.identifier, sounds);
    }

    private void startCreation(String identifier, List<Sound> sounds) {
        int pageNumber = 1;
        boolean done = false;

        while (true) {
            while (!done) {
                List<Sound> toPass = new ArrayList();
                if (sounds.size() >= 45) {
                    for (int i = 0; i < 45; ++i) {
                        toPass.add(sounds.get(0));
                        sounds.remove(0);
                    }

                    this.createInventory(identifier, toPass, pageNumber);
                    ++pageNumber;
                } else {
                    done = true;
                    toPass.addAll(sounds);
                    this.createInventory(identifier, toPass, pageNumber);
                    ++pageNumber;
                }
            }

            return;
        }
    }

    private void createInventory(String identifier, List<Sound> toPass, int pageNumber) {
        String title = Tools.colorMSG("&4&lSOUNDS &d- &0" + identifier);
        Inventory inventory = Bukkit.createInventory((InventoryHolder) null, 54, title);

        int i;
        for (i = 45; i < 54; ++i) {
            inventory.setItem(i, this.getPane());
        }

        if (this.pages.get(pageNumber - 1) != null) {
            inventory.setItem(45, this.getPreviousArrow(pageNumber));
        }

        if (this.total - pageNumber * 45 > 0) {
            inventory.setItem(53, this.getNextArrow(pageNumber));
        }

        Iterator var8 = toPass.iterator();

        while (var8.hasNext()) {
            Sound sound = (Sound) var8.next();
            inventory.addItem(new ItemStack[]{this.getSoundItem(sound)});
        }

        for (i = 0; i < 45; ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, this.getPane());
            }
        }

        this.pages.put(pageNumber, inventory);
    }

    private ItemStack getSoundItem(Sound sound) {
        ItemStack stack = new ItemStack(Material.MUSIC_DISC_BLOCKS);
        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        List<String> lore = new ArrayList();

        lore.add(Tools.colorMSG("&4Left-Click &d- &7Choose this sound!"));
        lore.add(Tools.colorMSG("&4Right-Click &d- &7Play the sound!"));

        meta.setLore(lore);
        meta.setDisplayName(Tools.colorMSG("&e&l" + sound.name()));
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getPane() {
        ItemStack stack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        meta.setDisplayName(" ");
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getNextArrow(int pageNumber) {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        int page = pageNumber + 1;

        meta.setDisplayName(Tools.colorMSG("&6&lPage " + page + " &7-->"));


        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack getPreviousArrow(int pageNumber) {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();

        assert meta != null;

        int page = pageNumber - 1;

        meta.setDisplayName(Tools.colorMSG("&7<-- &6&lPage " + page));

        stack.setItemMeta(meta);
        return stack;
    }

    public Inventory getSoundPage(int pageNumber) {
        return (Inventory) this.pages.get(pageNumber);
    }
}

