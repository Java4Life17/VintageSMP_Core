package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

public class Menus {

    public static Inventory getMainInventory(String playerName) {
        Inventory inventory = Bukkit.createInventory(null, 18, Tools.colorMSG("&0Menu de mascota!"));

        //*************************************************************************************************************
        ItemStack backpack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3" +
                "JhZnQubmV0L3RleHR1cmUvMjMwOGJmNWNjM2U5ZGVjYWYwNzcwYzNmZGFkMWUwNDIxMjFjZjM5Y2MyNTA1YmJiODY2ZTE4YzZkMjNjY2QwYyJ9fX0=");
        List<String> backLore = new ArrayList<>();
        backLore.add(" ");
        backLore.add(Tools.colorMSG("&eDale clic para ver los objetos de las ultimas 24 horas."));
        backLore.add(Tools.colorMSG("&c&oADVERTENCIA: Se da reset cada 24 horas."));
        backpack = createItemStack(backpack, "&aObjetos recogidos", backLore);
        inventory.setItem(0, backpack);
        //*************************************************************************************************************
        ItemStack offline = new ItemStack(Material.CLOCK);
        List<String> offLore = new ArrayList<>();
        offLore.add("");
        if (MyPlugin.system.getByName(playerName).getOfflineMode()) {
            offLore.add(Tools.colorMSG("&6&lEstado: &a&n&o" + MyPlugin.system.getByName(playerName).getOfflineMode()));
        } else {
            offLore.add(Tools.colorMSG("&6&lEstado: &c&n&o" + MyPlugin.system.getByName(playerName).getOfflineMode()));
        }
        offLore.add(Tools.colorMSG("&6&lTiempo: &7&o" + MyPlugin.system.getByName(playerName).getTimeAccordingToTire()));
        offLore.add(" ");
        offLore.add(Tools.colorMSG("&7&oOpcion para que tu pet se quede buscando"));
        offLore.add(Tools.colorMSG("&7&oaun cuando estes desconctado."));
        offline = createItemStack(offline, "&aActivar OfflineMode", offLore);
        inventory.setItem(1, offline);
        //*************************************************************************************************************
        ItemStack storage = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J" +
                "hZnQubmV0L3RleHR1cmUvMjRiOTUzYjJjMGU5NTI1NzRmMWVkMjljODFlODJlNTNiY2RiMWJhNjgzMjU5YzIwZGFlZWY3ZDU1NGEyYTc5OCJ9fX0=");
        List<String> storage_lore = new ArrayList<>();
        storage_lore.add(Tools.colorMSG("\n&2Este es un cofre que su mascota lleva para "));
        storage_lore.add(Tools.colorMSG("&2almacenar artículos que posiblemente pueda necesitar"));
        storage_lore.add(Tools.colorMSG("&2en el futuro. Puede elegir qué elementos colocar aquí"));
        storage_lore.add(Tools.colorMSG("&2y él tendrá la tarea de guardarlos por usted."));
        storage = createItemStack(storage, "&9Cofre de tu mascota", storage_lore);
        inventory.setItem(2, storage);
        //*************************************************************************************************************
        ItemStack tenSeconds = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5l" +
                "Y3JhZnQubmV0L3RleHR1cmUvMzhiZThhYmQ2NmQwOWE1OGNlMTJkMzc3NTQ0ZDcyNmQyNWNhZDdlOTc5ZThjMjQ4MTg2NmJlOTRkM2IzMmYifX19");
        String status;

        if (Bukkit.getPlayer(playerName).getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(), "Npc_TenSeconds"), PersistentDataType.INTEGER) == 1) {
            status = Tools.colorMSG("&aActivado");
        } else {
            status = Tools.colorMSG("&cDesactivado");
        }
        List<String> tenLore = new ArrayList<>();
        tenLore.add("");
        tenLore.add(Tools.hexColorMSG("#ffff00&oCuando esta opción está activada, su"));
        tenLore.add(Tools.hexColorMSG("#ffff00&omascota podrá teletransportarse hacia ti"));
        tenLore.add(Tools.hexColorMSG("#ffff00&oen caso de que estés demasiado lejos de él o se pierda"));
        tenLore.add(Tools.hexColorMSG("#ffff00&oSi no desea que esto suceda cada 2 minutos, puede"));
        tenLore.add(Tools.hexColorMSG("#ffff00&oteletransportarlo manualmente con"));
        tenLore.add(Tools.hexColorMSG("#ffff00&ola siguiente opción."));
        tenLore.add("");
        tenLore.add(Tools.colorMSG("&1&lEstado: " + status));
        inventory.setItem(3, createItemStack(tenSeconds, Tools.hexColorMSG("#61cf5fTeletransportación automática"), tenLore));
        //*************************************************************************************************************

        ItemStack teleportPet = new ItemStack(Material.ENDER_PEARL);
        List<String> teleportLore = new ArrayList<>();
        teleportLore.add("");
        teleportLore.add(Tools.hexColorMSG("#49b4c4Esta opción hará que tu mascota se "));
        teleportLore.add(Tools.hexColorMSG("#49b4c4teletransporte a ti manualmente a ti."));
        inventory.setItem(4, createItemStack(teleportPet, Tools.hexColorMSG("#9f71bfTeletransportar Mascota"), teleportLore));
        //*************************************************************************************************************
        ItemStack changeName = new ItemStack(Material.NAME_TAG);
        List<String> changeNameLore = new ArrayList<>();
        changeNameLore.add("");
        changeNameLore.add(Tools.hexColorMSG("#8eab97&oEsta es una opción para"));
        changeNameLore.add(Tools.hexColorMSG("#8eab97&ocambiar el nombre de su mascota."));
        changeNameLore.add("");
        changeNameLore.add(Tools.hexColorMSG("&dCambiar Nombre&7: #2b45ba&oTitan"));
        changeNameLore.add(Tools.hexColorMSG("&dCambiar Nombre con colores&8(&b& 0-9 a-f&8) &7: #e3dadb&lGOD"));
        changeNameLore.add(Tools.hexColorMSG("&dCambiar Nombre con colores&8(&bTodos&8) &7: #a886db&o&lLEGEND"));

        inventory.setItem(5, createItemStack(changeName, Tools.colorMSG("&6Cambiar Nombre"), changeNameLore));
        //*************************************************************************************************************

        ItemStack type = new ItemStack(Material.TURTLE_EGG);
        List<String> typeLore = new ArrayList<>();
        typeLore.add("");
        typeLore.add(Tools.hexColorMSG("#ad9ad9Selecciona esta opción para"));
        typeLore.add(Tools.hexColorMSG("#ad9ad9elegir el tipo de mob que será tu mascota."));
        inventory.setItem(6, createItemStack(type, Tools.hexColorMSG("#ba9068Tipo de Mascota"), typeLore));
        inventory.setItem(7, getFindPlayer());
        Player player = Bukkit.getPlayer(playerName);
        assert player != null;
        inventory.setItem(8, getTreatOption(player.getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(),
                "AppreciationLevel"), PersistentDataType.INTEGER)));
        inventory.setItem(9, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(10, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(11, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(12, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(13, despawnPet());
        inventory.setItem(14, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(15, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(16, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(17, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));

        return inventory;

    }

    private static ItemStack despawnPet() {
        ItemStack stack = new ItemStack(Material.BARRIER);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Tools.colorMSG("&eUtilice esta opción para hacer desaparecer a su mascota. "));
        return createItemStack(stack, "&4Desaparecer", lore);
    }
    public static Inventory getNotSpawnInventory(){
        Inventory inventory = Bukkit.createInventory(null, 9 , Tools.colorMSG("&4MASCOTA NO PRESENTE"));
        inventory.setItem(0, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(1, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(2, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(3, new ItemStack(createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>())));
        inventory.setItem(4, respawnItem());
        inventory.setItem(5, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(6, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(7, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        inventory.setItem(8, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        return inventory;
    }

    private static ItemStack respawnItem() {
        ItemStack stack = new ItemStack(Material.ENDER_PEARL);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(Tools.colorMSG("&eUtilice esta opción para hacer aparecer a su mascota. "));
        return createItemStack(stack, "&aAparecer", lore);
    }

    public static Inventory getStorageSelectionMenu() {
        Inventory inventory = Bukkit.createInventory(null, 9, Tools.hexColorMSG("#699c5c&nAlmacenamientos de mascota"));

        inventory.setItem(0, getKnightLevel());
        inventory.setItem(1, getTitantLevel());
        inventory.setItem(2, getGodLevel());
        inventory.setItem(3, getLegendLevel());
        inventory.setItem(4, getAdditionOne());
        inventory.setItem(5, getAdditionTwo());
        inventory.setItem(6, getAdditionThree());
        inventory.setItem(7, getAdditionFour());
        inventory.setItem(8, getAdditionFive());

        return inventory;
    }

    private static ItemStack getKnightLevel() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvNzY1OGNjYzczNDU1NTllOTMyMWY0OWVlMWFmNjc1MjJlNzA4ZGNhODkzMmIwYTcyMWNjMzQxMzA3MzFlYjU5OCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#25b387&lRango requerido&f: #E6A450&oKnight"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o18 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 1"), lore);

    }


    private static ItemStack getTitantLevel() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvMzM3Yjk2YTQ1YThmMGM2ZmQ3ZGZhYjJhODVlMWEzODRkYWNhNGI4MDZlYjg5MmE4N2UyN2Y1MmE5ZjkxYzA4NCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#25b387&lRango requerido&f: #2b45ba&oTitan"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o36 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 2"), lore);
    }

    private static ItemStack getGodLevel() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvODQ0NDk4YTBmZTI3ODk1NmUzZDA0MTM1ZWY0YjEzNDNkMDU0OGE3ZTIwOGM2MWIxZmI2ZjNiNGRiYzI0MGRhOCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#25b387&lRango requerido&f: #e3dadb&lGOD &7o #a886db&o&lLEGEND"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 3"), lore);
    }

    private static ItemStack getLegendLevel() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvMzFmN2NkZmVhMmQyMWNkNWY2ZWJiZjQ4NDgxNzYxYzZjYmRmMzZkMDBmZTY0MDgzNjg2ZTlhZWFhM2YxZjIxNyJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#25b387&lRango requerido&f: #a886db&o&lLEGEND"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 4"), lore);
    }

    private static ItemStack getAdditionOne() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvMWIwZTA2MDAyODg1MTE2MTUyYmFkNGI2NjI4NmYxZjMxN2Y1OTljZDYwYWNkMWI5MDhiYTZhNWM1MDhiZjVlMSJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#ddbdfcExtra #1"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 5"), lore);
    }

    private static ItemStack getAdditionTwo() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvYzJlZDI5ODg1ODk1MWI0M2QyNDQ4ZGI1YzY1NmU3MDA0ZWI5MWUzMDc2ZTVmYzg4ZDI0ZTUyOWU4ODQ5ZTJjMCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#ddbdfcExtra #2"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 6"), lore);
    }

    private static ItemStack getAdditionThree() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvZTUyOTcwZTEyNDE1M2FiZDI4YjVkNjQwMDNiOGZkODc5OTlkYmM3YTVjZGU4OTk5NjFlZTAxY2NkM2Q0YWI5OCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#ddbdfcExtra #3"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 7"), lore);
    }

    private static ItemStack getAdditionFour() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvOWM5NmJlNzg4NmViN2RmNzU1MjVhMzYzZTVmNTQ5NjI2YzIxMzg4ZjBmZGE5ODhhNmU4YmY0ODdhNTMifX19");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#ddbdfcExtra #4"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 8"), lore);
    }

    private static ItemStack getAdditionFive() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvNTg4MTQ5ZTU2Y2RlMDJlZWU2MjA5ODI4ODRiODkzNjM0NzU2YmU5MTQxZjJlMjc3N2I1MWNiMGE2NmMzMWRjZSJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#ddbdfcExtra #5"));
        lore.add(Tools.hexColorMSG("#25b387&lTamaño&f: #D4E157&o54 slots"));

        return createItemStack(stack, Tools.hexColorMSG("#ffd4ec&l&nAlmacenamiento 9"), lore);
    }


    private static ItemStack createItemStack(ItemStack stack, String displayName, List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG(displayName));
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    /*
    This is a method that will get a menu containing the type of pets a user can choose.
     */
    public static Inventory getPetTypes() {
        Inventory inventory = Bukkit.createInventory(null, 54, Tools.hexColorMSG("#25524b&lTIPOS DE MASCOTAS"));
        int[] whitePanes = {0, 1, 7, 8, 11, 15, 21, 23, 30, 32, 38, 42, 45, 46, 52, 53};
        //***********************************************************************************************************************************************************
        for (int i : whitePanes) {
            inventory.setItem(i, createItemStack(new ItemStack(Material.WHITE_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        }
        //***********************************************************************************************************************************************************
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, createItemStack(new ItemStack(Material.RED_STAINED_GLASS_PANE), " ", new ArrayList<>()));
            }
        }

        inventory.setItem(22, getWolfType());
        inventory.setItem(31, getPigType());
        inventory.setItem(18, getSkeletonType());
        inventory.setItem(27, getVexType());
        inventory.setItem(26, getBeeType());
        inventory.setItem(35, getAjolote());
        inventory.setItem(4, getWither());
        inventory.setItem(49, getEnderMan());


        return inventory;
    }

    private static ItemStack getPigType() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvOGM2MWZiODgyNWQ4M2RjNjE0YzkyZWJiZjRlNzRjMThhODA2ODdkNWIyYjk2YjNjZDZhNjA5MGVlZjViIn19fQ==");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en cerdo"));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        lore.add(Tools.hexColorMSG("&7- #2b45ba&oTitan"));
        lore.add(Tools.hexColorMSG("&7- #E6A450&oKnight"));
        return createItemStack(stack, Tools.hexColorMSG("#d98fd0&lCerdo"), lore);
    }

    private static ItemStack getWolfType() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvMmIyZjk3Yjk1MGE4MDMxNTAxOGViYmEyZTU4ZWViNTQ3MThlMjY5MDY2MmNlOWRkZmRjZWRlYzU1NGMyNyJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en lobo"));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        lore.add(Tools.hexColorMSG("&7- #2b45ba&oTitan"));
        lore.add(Tools.hexColorMSG("&7- #E6A450&oKnight"));
        return createItemStack(stack, Tools.hexColorMSG("#b0b0b0&lLobo"), lore);
    }

    private static ItemStack getSkeletonType() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvNzI1ZjQ1ODQ2MmU1ZTY0NGNlYTc5MjU0YTVmY2NkZTdkMGQ4M2YyZDQwOTJhYTQ4N2NiZDZkNWEzZDQ0M2Q3OCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en un esqueleto."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        lore.add(Tools.hexColorMSG("&7- #2b45ba&oTitan"));
        return createItemStack(stack, Tools.hexColorMSG("#807e7d&lEsqueleto"), lore);
    }

    private static ItemStack getVexType() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvYzJlYzVhNTE2NjE3ZmYxNTczY2QyZjlkNWYzOTY5ZjU2ZDU1NzVjNGZmNGVmZWZhYmQyYTE4ZGM3YWI5OGNkIn19fQ==");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en un Vex."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        lore.add(Tools.hexColorMSG("&7- #2b45ba&oTitan"));
        return createItemStack(stack, Tools.hexColorMSG("#9ba5b3&lVex"), lore);
    }

    private static ItemStack getBeeType() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvYjY4NTdlOTg3NmEzNjQ0ZGViYmYxZmQ3MzQ1YTQ4Zjk5OTcwNWUwYTk5M2ExMzA0OTI4ZmQwNmMxYjNmMWY5NCJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en una abeja."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        return createItemStack(stack, Tools.hexColorMSG("#edf2a2&lAbeja"), lore);
    }

    private static ItemStack getAjolote() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvN2Y4MGNjMTQ5MmU0NDY2OGNjY2RiNDAxNzhjM2E2Njg5ZThkZmMwZDIzNGU5ODU1M2ZiN2RlYmMyNmZjYWVhYyJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en un ajolote."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        lore.add(Tools.hexColorMSG("&7- #e3dadbGOD"));
        return createItemStack(stack, Tools.hexColorMSG("#c667db&lAjolote"), lore);
    }

    private static ItemStack getWither() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en un creeper."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        return createItemStack(stack, Tools.hexColorMSG("#1ebd5b&lCreeper"), lore);
    }

    private static ItemStack getEnderMan() {
        ItemStack stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZ" +
                "nQubmV0L3RleHR1cmUvYjYxMWI4MWI3ODRhNzc0NTlhOWY5ZDI1MDk0YjhiMmUzZDg5MDI4ZTFlN2JiYWJlOTE2NjVjZDJkYzY2NiJ9fX0=");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.hexColorMSG("#e6b3e0&oConvierte a tu mascota en un enderman."));
        lore.add("");
        lore.add("");
        lore.add(Tools.hexColorMSG(" #d9b69a&l&n________RANGOS________"));
        lore.add("");
        lore.add(Tools.hexColorMSG("&7- #a886db&oLEGEND"));
        return createItemStack(stack, Tools.hexColorMSG("#7a169c&lEnderman"), lore);
    }

    private static ItemStack getFindPlayer() {
        ItemStack stack = new ItemStack(Material.COMPASS);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.colorMSG("&aEsta es una opción para que tu mascota encuentre al"));
        lore.add(Tools.colorMSG("&aal jugador más cercano. &cSolo funciona si tu mascota es un lobo o una abeja."));
        return createItemStack(stack, Tools.hexColorMSG("#d97f43Olfatear"), lore);
    }

    private static ItemStack getTreatOption(int appLevel) {
        ItemStack stack = new ItemStack(Material.BONE);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Tools.colorMSG("&6Nivel de agradecimiento: &7" + appLevel + "&8/&71000"));
        lore.add("");
        //lore.add(Tools.colorMSG("&7"));
        lore.add(Tools.colorMSG("&7Haga clic en esta opción para darle a su mascota algunas "));
        lore.add(Tools.colorMSG("&7golosinas y aumentar su nivel de apreciación. Una vez que su"));
        lore.add(Tools.colorMSG("&7barra de aprecio esté en el punto más alto, su mascota mostrará su"));
        lore.add(Tools.colorMSG("&7aprecio haciendo todo lo posible para buscar una manera"));
        lore.add(Tools.colorMSG("&7de mostrar su aprecio hacia usted."));

        return createItemStack(stack, Tools.hexColorMSG("#0bbab4&lRecompensa a tu mascota"), lore);
    }


    public static Inventory getTreatContainer() {
        Inventory inventory = Bukkit.createInventory(null, 9, Tools.hexColorMSG("#eb3452Contenedor de Golosinas"));
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, createItemStack(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " ", new ArrayList<>()));
        }

        inventory.setItem(4, null);
        inventory.setItem(8, createItemStack(new ItemStack(Material.LIME_STAINED_GLASS_PANE), "&a&lCONFIRMAR", new ArrayList<>()));

        return inventory;
    }


}
