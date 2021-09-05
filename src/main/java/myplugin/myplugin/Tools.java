package myplugin.myplugin;

import dev.dbassett.skullcreator.SkullCreator;
import myplugin.myplugin.MyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static String colorMSG(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String langText(String path) {
        String text = MyPlugin.languageFile.getString(path);
        return colorMSG(text);
    }

    public static void saveLangFile() {
        try {
            MyPlugin.languageFile.save();
            MyPlugin.languageFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reloadLangFile() {
        try {
            MyPlugin.languageFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLocationsFile(){
        try {
            MyPlugin.locations.locationsFile.save();
            MyPlugin.locations.locationsFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getPlayerHearts(Player player){
        return MyPlugin.livesSystem.playerLives.get(player);
    }

    public static void updatePlayerHearts(Player player, double hearts){
        MyPlugin.livesSystem.playerLives.put(player, hearts);
        player.setHealthScale(hearts);
        savePlayersHearts(player);
        saveHeartsFile();
    }

    public static void savePlayersHearts(Player player){
        MyPlugin.livesSystem.heartsFile.set(player.getName(), MyPlugin.livesSystem.playerLives.get(player));
        saveHeartsFile();
    }

    private static void saveHeartsFile() {
        try {
            MyPlugin.livesSystem.heartsFile.save();
            MyPlugin.livesSystem.heartsFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ItemStack getStaticItem() {
        // Got this base64 string from minecraft-heads.com
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdiNDE2ZDM2Yjc1NjE3Mzk4ZDdiYjNjZThkYjhhNzRiZDVlMzE0MTgyNTM3NzhjN2E2ZmU4NTQ5ODY3MSJ9fX0=";

        ItemStack stack =  SkullCreator.itemFromBase64(base64);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&bMenu De Jugador"));
        List<String> lore = new ArrayList<>();
        lore.add("\n");
        lore.add(Tools.colorMSG("&7&oInteractua con este objeto para"));
        lore.add(Tools.colorMSG("&7&oabrir el menu de jugador."));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save the item
            dataOutput.writeObject(item);

            // Serialize that item
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack itemStackFromBase64(String data) throws IOException {
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

    public static String getAlphaNumericString(int n) {


        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";


        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public static String getDateAndTimeFormatted(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        return dtf.format(now);
    }

    /*
    The following methods will be added to manage a user's balance on this server
     */

    /**
     * This method will be used to set the wish balance to a player
     * Balance can be loaded from a file or you can set it at any time
     * you'd like.
     * @param player - The player receiving the money.
     * @param balance - The amount of coins added to the player.
     */
    public static void setPlayerCoins(Player player, int balance){
        MyPlugin.selling_system.server_balance.put(player, balance);
    }

    /**
     * Method to add coins to player.
     * @param player - The player who to add coins to.
     * @param coins_to_add - The Coins added.
     */
    public static void addPlayerCoins(Player player, int coins_to_add){
        int total = MyPlugin.selling_system.server_balance.get(player);
        total = total + coins_to_add;
        MyPlugin.selling_system.server_balance.put(player, total);
    }

    /**
     * Method to remove coins from a player
     * @param player - The player who will lose coins.
     * @param coins_to_remove - The total coins removed.
     */
    public static void removePlayerCoins(Player player, int coins_to_remove){
        int total = MyPlugin.selling_system.server_balance.get(player);
        total = total - coins_to_remove;
        MyPlugin.selling_system.server_balance.put(player, total);
    }

    public static void addSoldPlayerCoins(String fileName, int coins){
        YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + fileName + ".yml");
        try {
            yamlFile.load();

            if(!yamlFile.contains("Ganancia")){
                yamlFile.set("Ganancia", 0);
            }

            int balance = yamlFile.getInt("Ganancia");
            int total =  balance + coins;
            yamlFile.set("Ganancia", total);
            yamlFile.save();

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    /**
     * Returns the current coin balance for a player.
     * @param player - Player to get the coins from.
     * @return - The Coins returned
     */
    public static int getPlayerCoins(Player player){
        return MyPlugin.selling_system.server_balance.get(player);
    }




}
