package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Clan_Manager {
    private Clan_System system;

    public Clan_Manager(Clan_System system) {
        this.system = system;
    }

    /**
     * Method to add a clan to the loaded clans
     *
     * @param name The name of the clan
     */
    public void addClan(String name) {
        Clan clan = new Clan(name);
        addClan(clan);
    }

    /**
     * Add a clan to the existing list
     *
     * @param clan The clan to be added
     */
    public void addClan(Clan clan) {
        if (!system.getLoadedClans().contains(clan)) {
            system.getLoadedClans().add(clan);
        }
    }

    /**
     * Remove a clan from the loaded clans.
     *
     * @param clan The clan to remove
     */
    public void removeClan(Clan clan) {
        system.getLoadedClans().remove(clan);
    }

    /**
     * Check if the loaded clans has the target clan
     *
     * @param clan The clan target in STRING form
     * @return If the clan is loaded
     */
    public boolean clanIsLoaded(String clan) {
        for (Clan target : system.getLoadedClans()) {
            if (target.getClanName().equals(clan))
                return true;
        }
        return false;
    }

    /**
     * Check if the loaded clans has the target clan
     *
     * @param clan The clan target in CLAN form
     * @return If the clan is loaded
     */
    public boolean clanIsLoaded(Clan clan) {
        return system.getLoadedClans().contains(clan);
    }

    /**
     * Check if a clan exists in our database. This is to avoid null clans.
     *
     * @param clanName The clan name
     * @return If the clan exists
     */
    public boolean clanExists(String clanName) {
        YamlFile file = new YamlFile("plugins/MyPlugin/Clans/" + clanName + ".yml");

        return file.exists();
    }

    public void createNewClan(String clanName, String clanOwner) {
        try {

            YamlFile clan = new YamlFile("plugins/MyPlugin/Clans/" + clanName + ".yml");

            clan.createNewFile(true);
            clan.save();
            clan.set("clanOwner", clanOwner);
            clan.set("clanLevel", 0);
            clan.set("clanMembers", createNewMembersList(clanOwner));
            clan.set("clanDescription", "Sin descripcion.");
            clan.save();

            Player p = Bukkit.getPlayer(clanOwner);
            assert p != null;
            p.getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING, clanName);
            system.getLoadedClans().add(new Clan(clanName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private @NotNull List<String> createNewMembersList(String clanOwner) {
        List<String> toReturn = new ArrayList<>();
        toReturn.add(clanOwner);
        return toReturn;
    }

    /**
     * Delete an existing clan. If the clan exists
     *
     * @param clan The clan to delete
     */

    public void deleteClan(@NotNull Clan clan) {
        List<String> members = new ArrayList<>(clan.getClanMembers());
        for (String member : members) {
            if (Bukkit.getPlayer(member) != null) {
                Objects.requireNonNull(Bukkit.getPlayer(member)).sendMessage(Tools.langText("clanDeleted"));
                Objects.requireNonNull(Bukkit.getPlayer(member)).getPersistentDataContainer().remove(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"));
            }
        }

        try {
            clan.getClanFile().deleteFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyPlugin.clan_system.getLoadedClans().remove(clan);
        system.getLoadedClans().remove(clan);


    }

    /**
     * Get a clan according to its name, if the clan exists.
     *
     * @param name The name of the clan
     * @return The clan
     */
    public Clan getClanByName(String name) {
        for (Clan clan : system.getLoadedClans()) {
            if (clan.getClanName().equals(name)) {
                return clan;
            }
        }
        return null;
    }

    public Clan getOfflineClan(String name) {
        return new Clan(name);
    }

    /**
     * Gets the plan of a certain player. If he doesn't have one
     * his clan will be null.
     *
     * @param player The target player
     * @return The clan
     */
    public Clan getPlayerClan(Player player) {

        if (player == null) {
            return null;
        }

        if (!player.getPersistentDataContainer().has(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING)) {
            return null;
        }

        String clanName = player.getPersistentDataContainer().get(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING);

        return getClanByName(clanName);
    }

    /**
     * Update the level of a certain clan.
     *
     * @param clan  The target clan
     * @param level The new level
     */
    public void updateClanLevel(Clan clan, int level) {
        clan.setClanLevel(level);
    }

    /**
     * Get the clan tools main menu
     *
     * @return The Menu
     */
    public Inventory getClanTools() {
        Inventory inventory = Bukkit.createInventory(null, 27, Tools.hexColorMSG("#353445Seleccione una opción"));
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, purplePane());
        }

        ItemStack newClan = new ItemStack(Material.PAPER);
        ItemMeta newClanMeta = newClan.getItemMeta();
        assert newClanMeta != null;
        newClanMeta.setDisplayName(Tools.hexColorMSG("#36a864Crear tu clan"));
        newClan.setItemMeta(newClanMeta);
        inventory.setItem(12, newClan);

        ItemStack editClan = new ItemStack(Material.FEATHER);
        ItemMeta editClanMeta = editClan.getItemMeta();
        assert editClanMeta != null;
        editClanMeta.setDisplayName(Tools.hexColorMSG("#d8f051Editar tu clan"));
        editClan.setItemMeta(editClanMeta);
        inventory.setItem(14, editClan);
        return inventory;
    }

    public void getClanEditor() {

    }

    /**
     * Check if the name of a clan being created is valid
     *
     * @param name Clan name
     * @return Valid or Invalid
     */
    public boolean nameIsValid(String name) {
        List<Character> allowed = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
                , 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'
                , 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7'
                , '8', '9');

        for (char character : name.toCharArray()) {
            if (Character.isWhitespace(character)) {
                return false;
            }
            if (!allowed.contains(character)) {
                return false;
            }
        }

        return true;

    }

    //NOTHING MUCH
    private ItemStack purplePane() {
        ItemStack stack = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        stack.setItemMeta(meta);
        return stack;
    }


    public Inventory getClanSettings() {
        Inventory inventory = Bukkit.createInventory(null, 27, Tools.hexColorMSG("#06452aEdita tu clan"));
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, purplePane());
        }

        ItemStack addMembers = new ItemStack(Material.SCUTE);
        ItemMeta addMeta = addMembers.getItemMeta();
        assert addMeta != null;
        addMeta.setDisplayName(Tools.colorMSG("&aInvitar a clan"));
        List<String> addLore = new ArrayList<>();
        addLore.add(Tools.colorMSG("&7Invita a cualquier jugador conectado a tu clan."));
        addMeta.setLore(addLore);
        addMembers.setItemMeta(addMeta);
        inventory.setItem(10, addMembers);

        ItemStack removeMembers = new ItemStack(Material.RED_DYE);
        ItemMeta removeMeta = removeMembers.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(Tools.colorMSG("&cEliminar a miembro"));
        List<String> removeLore = new ArrayList<>();
        removeLore.add(Tools.colorMSG("&7Elimina miembros de tu clan."));
        removeMeta.setLore(removeLore);
        removeMembers.setItemMeta(removeMeta);
        inventory.setItem(12, removeMembers);

        ItemStack fightClan = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta fightMeta = fightClan.getItemMeta();
        assert fightMeta != null;
        fightMeta.setDisplayName(Tools.colorMSG("&6Desafía a otro clan"));
        List<String> fightLore = new ArrayList<>();
        fightLore.add(Tools.colorMSG("&7Reta a cualquier otro clan en línea a un duelo."));
        fightMeta.setLore(fightLore);
        fightMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        fightClan.setItemMeta(fightMeta);
        inventory.setItem(14, fightClan);

        ItemStack delete = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = delete.getItemMeta();
        assert deleteMeta != null;
        deleteMeta.setDisplayName(Tools.colorMSG("&4Elimina tu clan."));
        List<String> deleteLore = new ArrayList<>();
        deleteLore.add(Tools.colorMSG("&7Si usa esta opción, no habrá vuelta atrás."));
        deleteLore.add(Tools.colorMSG("&7Todos los datos de tu clan se perderán."));
        deleteMeta.setLore(deleteLore);
        delete.setItemMeta(deleteMeta);
        inventory.setItem(16, delete);

        return inventory;
    }

    public List<String> getExistingClans() {
        List<String> toReturn = new ArrayList<>();
        File file = new File("plugins/MyPlugin/Clans");
        Arrays.stream(Objects.requireNonNull(file.listFiles())).toList().forEach(file1 -> {
            String name = file1.getName();
            name = name.replace(".yml", "");
            toReturn.add(name);
        });
        return toReturn;
    }

    public List<String> getOnlineClans() {
        List<String> toReturn = new ArrayList<>();
        MyPlugin.clan_system.getLoadedClans().forEach(clan -> {
            toReturn.add(clan.getClanName());
        });
        return toReturn;
    }

    /**
     * Method to choose a clan who you want to fight against
     *
     * @return The inventory
     */
    public Inventory getClanBattle(Inventory inventory) {
        Inventory previous = inventory == null ? Bukkit.createInventory(null, 27, Tools.colorMSG("&6&lELIJE UN CLAN")) : inventory;
        if (previous.getItem(9) == null){
            updateContent(previous);
            return previous;
        }
        ItemStack stack = previous.getItem(17);
        assert stack != null;
        ItemMeta stackMeta = stack.getItemMeta();
        assert stackMeta != null;
        String name = stackMeta.getDisplayName();
        name = ChatColor.stripColor(name);

        if (previous.getItem(17) != null) updateContent(previous, ChatColor.stripColor(name));
        return previous;
    }

    private void updateContent(Inventory previous) {
        ItemStack pink = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta meta = pink.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        ItemStack green = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        pink.setItemMeta(meta);
        green.setItemMeta(meta);
        int[] pinkSlots = {0, 2, 4, 6, 8, 19, 21, 23, 25};
        int[] greenSlots = {1, 3, 5, 7, 20, 22, 24};
        Arrays.stream(pinkSlots).forEach(pinkSlot -> previous.setItem(pinkSlot, pink));
        Arrays.stream(greenSlots).forEach(greenSlot -> previous.setItem(greenSlot, green));

        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = previousPage.getItemMeta();
        assert arrowMeta != null;
        arrowMeta.setDisplayName(Tools.colorMSG("&e&l⇐⇐⇐"));
        previousPage.setItemMeta(arrowMeta);
        previous.setItem(18, previousPage);
        arrowMeta.setDisplayName(Tools.colorMSG("&e&l⇒⇒⇒"));
        previousPage.setItemMeta(arrowMeta);
        previous.setItem(26, previousPage);

        if (getOnlineClans().size() < 9) {
            getOnlineClans().forEach(s -> {
                ItemStack stack = new ItemStack(Material.PAPER);
                ItemMeta instanceMeta = stack.getItemMeta();
                assert instanceMeta != null;
                instanceMeta.setDisplayName(Tools.colorMSG("&c&l" + s));
                stack.setItemMeta(instanceMeta);
                previous.addItem(stack);
            });


            try {
                if(Objects.requireNonNull(previous.getItem(9)).getType().equals(Material.PAPER)){
                    Objects.requireNonNull(previous.getItem(10)).setType(Material.BOOK);
                }
                if(Objects.requireNonNull(previous.getItem(11)).getType().equals(Material.PAPER)){
                    Objects.requireNonNull(previous.getItem(12)).setType(Material.BOOK);
                }
                if(Objects.requireNonNull(previous.getItem(13)).getType().equals(Material.PAPER)){
                    Objects.requireNonNull(previous.getItem(14)).setType(Material.BOOK);
                }
                if(Objects.requireNonNull(previous.getItem(15)).getType().equals(Material.PAPER)){
                    Objects.requireNonNull(previous.getItem(16)).setType(Material.BOOK);
                }
            }catch (Exception e){
                MyPlugin.getSimpleLogger().error("Error Clan_Manager:399", "Item in inventory was null");
            }

            return;
        }

        getOnlineClans().stream().limit(9).forEach(s -> {
            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta instanceMeta = stack.getItemMeta();
            assert instanceMeta != null;
            instanceMeta.setDisplayName(Tools.colorMSG("&c&l" + s));
            stack.setItemMeta(instanceMeta);
            previous.addItem(stack);
        });
        if(Objects.requireNonNull(previous.getItem(9)).getType().equals(Material.PAPER)){
            Objects.requireNonNull(previous.getItem(10)).setType(Material.BOOK);
        }
        if(Objects.requireNonNull(previous.getItem(11)).getType().equals(Material.PAPER)){
            Objects.requireNonNull(previous.getItem(12)).setType(Material.BOOK);
        }
        if(Objects.requireNonNull(previous.getItem(13)).getType().equals(Material.PAPER)){
            Objects.requireNonNull(previous.getItem(14)).setType(Material.BOOK);
        }
        if(Objects.requireNonNull(previous.getItem(15)).getType().equals(Material.PAPER)){
            Objects.requireNonNull(previous.getItem(16)).setType(Material.BOOK);
        }


    }

    private void updateContent(Inventory previous, String clanName) {
        int slot = 0;
        boolean stillExist = false;
        for (int i = 0; i < getOnlineClans().size(); i++) {
            if (getOnlineClans().get(i).equals(clanName)) {
                slot = i;
                stillExist = true;
                break;
            }
        }
        if(!stillExist){
            updateContent(previous);
            return;
        }
        if(slot == getOnlineClans().size() - 1){
            return;
        }

        int[] shifting = {10,11,12,13,14,15,16,17};
        Arrays.stream(shifting).forEach(shifter -> previous.setItem(shifter - 1, previous.getItem(shifter)));
        ItemStack newEntry;
        if(previous.getItem(16).getType().equals(Material.PAPER)){
             newEntry = new ItemStack(Material.BOOK);
        }else{
             newEntry = new ItemStack(Material.PAPER);
        }
        ItemMeta newEntryMeta = newEntry.getItemMeta();
        assert newEntryMeta != null;
        newEntryMeta.setDisplayName(Tools.colorMSG("&c&l" + getOnlineClans().get(slot + 1)));
        newEntry.setItemMeta(newEntryMeta);
        previous.setItem(17, newEntry);
    }

    public void getPreviousInventory(Inventory inv){
        int slot = 0;
        ItemStack stack = inv.getItem(9);
        assert stack != null;
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        String name  = meta.getDisplayName();
        name = ChatColor.stripColor(name);

        for(int i = 0; i < getOnlineClans().size(); i++){
            if(getOnlineClans().get(i).equals(name)) {
                slot = i;
                break;
            }
        }
        if(slot == 0){
            return;
        }

        for(int i = 17; i > 9; i--){
            inv.setItem(i , inv.getItem(i - 1));
        }

        ItemStack newEntry;
        if(Objects.requireNonNull(inv.getItem(10)).getType().equals(Material.PAPER)){
            newEntry = new ItemStack(Material.BOOK);
        }else{
            newEntry = new ItemStack(Material.PAPER);
        }
        ItemMeta newEntryMeta = newEntry.getItemMeta();
        assert newEntryMeta != null;
        newEntryMeta.setDisplayName(Tools.colorMSG("&c&l" + getOnlineClans().get(slot - 1)));
        newEntry.setItemMeta(newEntryMeta);
        inv.setItem(9, newEntry);
    }

    public boolean playerInArena(Player player){
        if(player == null) return false;

        AtomicBoolean toReturn = new AtomicBoolean(false);

        if(MyPlugin.clan_system.getActiveArenas().size() > 0) {
            MyPlugin.clan_system.getActiveArenas().forEach(arena -> {
                if(arena.playerIsInArena(player)) toReturn.set(true);
            });
        }

        return toReturn.get();
    }

    public Arena getPlayerArena(Player player){
        if(player == null){
            return null;
        }

        if(!playerInArena(player)){
            return null;
        }

        Arena[] toReturn = {null};

        MyPlugin.clan_system.getActiveArenas().forEach(arena -> {
            if(arena.playerIsInArena(player)) toReturn[0] = arena;
        });

        return toReturn[0];

    }


}
