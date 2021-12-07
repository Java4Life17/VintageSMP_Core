package myplugin.myplugin;

import dev.dbassett.skullcreator.SkullCreator;
import myplugin.myplugin.IpControl.IpControl;
import myplugin.myplugin.MyPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static String colorMSG(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String langText(String path) {
        String text = MyPlugin.languageFile.getString(path);
        return hexColorMSG(text);
    }

    public static String langText(String path, HashMap<String, String> valuesToReplace) {
        String text = MyPlugin.languageFile.getString(path);
        for(Map.Entry<String, String> entry : valuesToReplace.entrySet()){
         text = text.replace(entry.getKey(), entry.getValue());
        }
        return hexColorMSG(text);
    }

    public static void message(Player p, String msg){
        if(p == null){
            return;
        }
        p.sendMessage(Tools.hexColorMSG(msg));
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

    public static void saveLocationsFile() {
        try {
            MyPlugin.locations.locationsFile.save();
            MyPlugin.locations.locationsFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getPlayerHearts(Player player) {
        return MyPlugin.livesSystem.playerLives.get(player);
    }

    public static void updatePlayerHearts(Player player, double hearts) {
        MyPlugin.livesSystem.playerLives.put(player, hearts);
        player.setHealthScale(hearts);
        savePlayersHearts(player);
        saveHeartsFile();
    }

    public static void savePlayersHearts(Player player) {
        MyPlugin.livesSystem.heartsFile.set(player.getName(), MyPlugin.livesSystem.playerLives.get(player));
        saveHeartsFile();
    }

    private static void saveHeartsFile() {
        try {
            MyPlugin.livesSystem.heartsFile.save();
            MyPlugin.livesSystem.heartsFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getStaticItem() {
        // Got this base64 string from minecraft-heads.com
        String base64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdiNDE2ZDM2Yjc1NjE3Mzk4ZDdiYjNjZThkYjhhNzRiZDVlMzE0MTgyNTM3NzhjN2E2ZmU4NTQ5ODY3MSJ9fX0=";

        ItemStack stack = SkullCreator.itemFromBase64(base64);
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

    public static ItemStack getHeadFromBase64(String base64) {
        // Got this base64 string from minecraft-heads.com

        ItemStack stack = SkullCreator.itemFromBase64(base64);
        ItemMeta meta = stack.getItemMeta();
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

    public static String getDateAndTimeFormatted() {
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
     *
     * @param player  - The player receiving the money.
     * @param balance - The amount of coins added to the player.
     */
    public static void setPlayerCoins(Player player, int balance) {
        MyPlugin.selling_system.server_balance.put(player, balance);
    }

    /**
     * Method to add coins to player.
     *
     * @param player       - The player who to add coins to.
     * @param coins_to_add - The Coins added.
     */
    public static void addPlayerCoins(Player player, int coins_to_add) {
        int total = MyPlugin.selling_system.server_balance.get(player);
        total = total + coins_to_add;
        MyPlugin.selling_system.server_balance.put(player, total);
    }

    /**
     * Method to remove coins from a player
     *
     * @param player          - The player who will lose coins.
     * @param coins_to_remove - The total coins removed.
     */
    public static void removePlayerCoins(Player player, int coins_to_remove) {
        int total = MyPlugin.selling_system.server_balance.get(player);
        total = total - coins_to_remove;
        MyPlugin.selling_system.server_balance.put(player, total);
    }

    public static void addSoldPlayerCoins(String fileName, int coins) {
        YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + fileName + ".yml");

        try {
            if (MyPlugin.system.getByName(fileName) != null) {
                yamlFile = MyPlugin.system.getByName(fileName).getFile();
            } else {
                yamlFile.load();
            }
            if (!yamlFile.contains("Ganancia")) {
                yamlFile.set("Ganancia", 0);
            }

            int balance = yamlFile.getInt("Ganancia");
            int total = balance + coins;
            yamlFile.set("Ganancia", total);
            yamlFile.save();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Returns the current coin balance for a player.
     *
     * @param player - Player to get the coins from.
     * @return - The Coins returned
     */
    public static int getPlayerCoins(Player player) {
        return MyPlugin.selling_system.server_balance.get(player);
    }

    public static void addNewPriceToItem(Material material, int price) {
        try {
            MyPlugin.server_selling.selling_Items_File.set(material.toString(), price);
            MyPlugin.server_selling.selling_Items_File.save();
            MyPlugin.server_selling.selling_Items_File.load();

            MyPlugin.server_selling.loadItemsAndPrices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) + min;
    }

    public static int getOfflinePlayerHearts(String name) {
        return (int) ((MyPlugin.livesSystem.heartsFile.getDouble(name)) / 2);
    }

    public static int getOfflinePlayerCoins(String playerName) {
        YamlFile file = new YamlFile("plugins/MyPlugin/SellingSystem/" + playerName + ".yml");
        ;
        if (MyPlugin.system.getByName(playerName) != null) {
            file = MyPlugin.system.getByName(playerName).getFile();
        } else {
            try {
                file.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return file.getInt("Coins");
    }

    public static int getOfflinePlayerGanancia(String playerName) {
        YamlFile file = new YamlFile("plugins/MyPlugin/SellingSystem/" + playerName + ".yml");
        ;
        if (MyPlugin.system.getByName(playerName) != null) {
            file = MyPlugin.system.getByName(playerName).getFile();
        } else {
            try {
                file.load();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return file.getInt("Ganancia");
    }

    public static String inventorytoBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static Inventory inventoryfromBase64(String data, String name) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), name);

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String hexColorMSG(String msg) {
        Matcher match = pattern.matcher(msg);
        while (match.find()) {
            String color = msg.substring(match.start(), match.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            match = pattern.matcher(msg);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static ItemStack getPetTreat() {
        ItemStack stack = new ItemStack(Material.BEETROOT);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.colorMSG("&7&oPonga esto en el recipiente de golosinas de su"));
        lore.add(Tools.colorMSG("&7&omascota para aumentar su barra de agradecimiento."));
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(hexColorMSG("#ba0b2b&lGolosina de mascota."));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    //eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E3NTMyOGViYTdjYjVhMDUyMzI2ZjU5ZGRhZjY3YTFjZWJkNGU1NWJiNjgwMWMwM2MzMTQyZTI4ZTEifX19

    public static ItemStack getPetAppreciation() {
        ItemStack stack = getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV" +
                "0L3RleHR1cmUvOWM5NmJlNzg4NmViN2RmNzU1MjVhMzYzZTVmNTQ5NjI2YzIxMzg4ZjBmZGE5ODhhNmU4YmY0ODdhNTMifX19");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.colorMSG("&7&oColoque para abrir este tesoro"));
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(hexColorMSG("&f&lTesoro"));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static boolean ItemMatch(ItemStack item1, ItemStack item2){
        item1.setAmount(1);
        item2.setAmount(1);

        return item1.equals(item2);
    }

    public static void avoidGC(){
        MyPlugin.getSimpleLogger().warning("Evitando el garbage collector", "Nada importante");
    }

    /**
     * Este metodo se toma la tarea de obtener una ubicacion y traducirla a un texto
     * @param p  El juagador con la ubicacion
     * @return  La ubicacion en World x y z yaw pitch
     */
    public static String playerLocationToString(Player p){
        StringBuilder location = new StringBuilder();
        String world = p.getWorld().getName();
        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();
        float yaw = p.getLocation().getYaw();
        float pitch = p.getLocation().getPitch();
        location.append(world).append(" ").append(x).append(" ").append(y).append(" ").append(z).append(" ")
                .append(yaw).append(" ").append(pitch);

        return location.toString();
    }

    /**
     * Obtener una ubicacion por medio de un texto
     * @param stringLocation Ubicacion en format texto
     * @return Ubicacion
     */
    public static Location playerLocationFromString(String stringLocation){
        String[] args = stringLocation.split(" ");
        World world = Bukkit.getServer().getWorld(args[0]);
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = Float.parseFloat(args[4]);
        float pitch = Float.parseFloat(args[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }



}
