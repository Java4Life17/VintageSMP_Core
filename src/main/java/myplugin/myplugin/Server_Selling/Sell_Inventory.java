package myplugin.myplugin.Server_Selling;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Sell_Inventory{

    public static Inventory getSellConfirmation( Material material, int amount){
        int total = MyPlugin.server_selling.items_and_prices.get(material) * amount;
        int each = MyPlugin.server_selling.items_and_prices.get(material);
        Inventory inventory = Bukkit.createInventory(null, 27, Tools.colorMSG("&0&l&oCONFIRMA TU VENTA &4- &1" + total));

        ItemStack confirmar = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmarMeta = confirmar.getItemMeta();
        assert confirmarMeta != null;
        confirmarMeta.setDisplayName(Tools.colorMSG("&a&lCONFIRMAR"));
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Tools.colorMSG("&7&oAl confirmar estas acceptando vender"));
        lore.add(Tools.colorMSG("&c" + amount + " &7&odel objeto en tu mano a &c" + each));
        lore.add(Tools.colorMSG("&7&ocada uno"));
        confirmarMeta.setLore(lore);
        confirmar.setItemMeta(confirmarMeta);

        ItemStack negar = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta negarMeta = negar.getItemMeta();
        assert negarMeta != null;
        negarMeta.setDisplayName(Tools.colorMSG("&4&lNEGAR"));
        List<String> lore1 = new ArrayList<>();
        lore1.add("");
        lore1.add(Tools.colorMSG("&7&oAl negar, estas cancelando esta compra!"));
        negarMeta.setLore(lore1);
        negar.setItemMeta(negarMeta);

        inventory.setItem(12, confirmar);
        inventory.setItem(14, negar);

        ItemStack display = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta displayMeta = display.getItemMeta();
        assert displayMeta != null;
        displayMeta.setDisplayName(" ");
        display.setItemMeta(displayMeta);


        for(int i = 0; i < 27; i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, display);
            }
        }

        return inventory;

    }

}
