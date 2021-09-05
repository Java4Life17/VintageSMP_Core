package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Selling_Item {
    private String seller_Name;
    private int item_Price;
    private String item_ID;
    private String encodedBase64;
    private String expDate;
    private YamlFile file;

    public Selling_Item(String seller_Name, int item_Price, String item_ID, String encodedBase64, String expDate, YamlFile file) {
        this.seller_Name = seller_Name;
        this.item_Price = item_Price;
        this.item_ID = item_ID;
        this.encodedBase64 = encodedBase64;
        this.expDate = expDate;
        this.file = file;
    }

    public ItemStack getAsDisplay() throws IOException {

        ItemStack sellingStack = itemStackFromBase64(encodedBase64);
        ItemMeta meta = sellingStack.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(Tools.colorMSG("&6-------------------------"));
        lore.add("");
        lore.add(Tools.colorMSG("&c&lVendedor&b: &7&o" + getSeller_Name()));
        lore.add(Tools.colorMSG("&c&lPrecio&b: &7&o" + getItem_Price()));
        lore.add(Tools.colorMSG("&c&lID&b: &7&o" + getItem_ID()));
        lore.add(Tools.colorMSG("&c&lFecha de Vencimiento&b: &7&o" + getExpDate()));
        lore.add("");
        lore.add(Tools.colorMSG("&6-------------------------"));

        assert meta != null;
        meta.setLore(lore);
        sellingStack.setItemMeta(meta);

        return sellingStack;
    }


    private ItemStack itemStackFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public void deleteSellingItem(){

        YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + getSeller_Name() + ".yml");

        try {
            yamlFile.load();
            yamlFile.set("Items." + getItem_ID() , null);
            yamlFile.save();
            MyPlugin.selling_system.itemsList.remove(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSeller_Name() {
        return seller_Name;
    }

    public int getItem_Price() {
        return item_Price;
    }

    public String getItem_ID() {
        return item_ID;
    }

    public String getEncodedBase64() {
        return encodedBase64;
    }

    public String getExpDate() {
        return expDate;
    }
    public ItemStack getRawItem() throws IOException {
        return itemStackFromBase64(encodedBase64);
    }


}
