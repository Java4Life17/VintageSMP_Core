package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.HashMap;


public class Pet_Storage {

    private YamlFile pet_Storage;

    private final HashMap<Integer, Inventory> storages = new HashMap<>();
    private int tier;
    private PlayerPet pet;

    public Pet_Storage(String ownerName, int tier, PlayerPet pet) {
        this.pet = pet;
        this.tier = tier;
        assignStorageFile(ownerName);
        generateStorageFile(tier);
        upgradeRank(ownerName);
    }

    private void assignStorageFile(String ownerName) {
        pet_Storage = new YamlFile("plugins/MyPlugin/Pet_Storage/" + ownerName + ".yml");
    }

    private void generateStorageFile(int tier) {

        try {
            if (!pet_Storage.exists()) {
                pet_Storage.createNewFile(false);
                pet_Storage.save();

                switch (tier) {
                    case 2 -> {
                        storages.put(1, Bukkit.createInventory(null, 36, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                        pet_Storage.createSection("Inventory_1");
                        pet_Storage.set("Inventory_1.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 36, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                        pet_Storage.save();
                    }
                    case 3 -> {
                        storages.put(1, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                        pet_Storage.createSection("Inventory_1");
                        pet_Storage.set("Inventory_1.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                        pet_Storage.save();
                    }
                    case 4 -> {
                        storages.put(1, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                        storages.put(2, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                        pet_Storage.createSection("Inventory_1");
                        pet_Storage.createSection("Inventory_2");
                        pet_Storage.set("Inventory_1.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                        pet_Storage.set("Inventory_2.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                        pet_Storage.save();
                    }
                    default -> {
                        storages.put(1, Bukkit.createInventory(null, 18, Tools.colorMSG("&9Almacenamiento de tu mascota #1")));
                        pet_Storage.createSection("Inventory_1");
                        pet_Storage.set("Inventory_1.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 18, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                        pet_Storage.save();
                    }

                }

                if(pet.hasStorage_one){
                    pet_Storage.createSection("Additional_1");
                    pet_Storage.set("Additional_1.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                    storages.put(11, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                }
                if(pet.hasStorage_two){
                    pet_Storage.createSection("Additional_2");
                    pet_Storage.set("Additional_2.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                    storages.put(12, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                }
                if(pet.hasStorage_three){
                    pet_Storage.createSection("Additional_3");
                    pet_Storage.set("Additional_3.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                    storages.put(13, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                }
                if(pet.hasStorage_four){
                    pet_Storage.createSection("Additional_4");
                    pet_Storage.set("Additional_4.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                    storages.put(14, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                }
                if(pet.hasStorage_five){
                    pet_Storage.createSection("Additional_5");
                    pet_Storage.set("Additional_5.Inventory", Tools.inventorytoBase64(Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota"))));
                    storages.put(15, Bukkit.createInventory(null, 54, Tools.colorMSG("&9Almacenamiento de tu mascota")));
                }

                pet_Storage.save();
                return;
            }

            pet_Storage.load();
            if (pet_Storage.contains("Inventory_1")) {
                storages.put(1, Tools.inventoryfromBase64(pet_Storage.getString("Inventory_1.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if (pet_Storage.contains("Inventory_2")) {
                storages.put(2, Tools.inventoryfromBase64(pet_Storage.getString("Inventory_2.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if(pet_Storage.contains("Additional_1")){
                storages.put(11, Tools.inventoryfromBase64(pet_Storage.getString("Additional_1.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if(pet_Storage.contains("Additional_2")){
                storages.put(12, Tools.inventoryfromBase64(pet_Storage.getString("Additional_2.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if(pet_Storage.contains("Additional_3")){
                storages.put(13, Tools.inventoryfromBase64(pet_Storage.getString("Additional_3.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if(pet_Storage.contains("Additional_4")){
                storages.put(14, Tools.inventoryfromBase64(pet_Storage.getString("Additional_4.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }
            if(pet_Storage.contains("Additional_5")){
                storages.put(15, Tools.inventoryfromBase64(pet_Storage.getString("Additional_5.Inventory"), Tools.colorMSG("&9Almacenamiento de tu mascota")));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile() {
        try {
            pet_Storage.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory(int refrence) {
        if (storages.get(refrence) == null) {
            return null;
        } else {
            return storages.get(refrence);
        }
    }

    public void saveContent() {
        pet_Storage.set("Inventory_1.Inventory", Tools.inventorytoBase64(storages.get(1)));

        if (storages.get(2) != null) {

            pet_Storage.set("Inventory_2.Inventory", Tools.inventorytoBase64(storages.get(2)));
        }
        if(storages.get(11) != null){
            pet_Storage.set("Additional_1.Inventory", Tools.inventorytoBase64(storages.get(11)));
        }
        if(storages.get(12) != null){
            pet_Storage.set("Additional_2.Inventory", Tools.inventorytoBase64(storages.get(12)));
        }
        if(storages.get(13) != null){
            pet_Storage.set("Additional_3.Inventory", Tools.inventorytoBase64(storages.get(13)));
        }
        if(storages.get(14) != null){
            pet_Storage.set("Additional_4.Inventory", Tools.inventorytoBase64(storages.get(14)));
        }
        if(storages.get(15) != null){
            pet_Storage.set("Additional_5.Inventory", Tools.inventorytoBase64(storages.get(15)));
        }

        saveFile();
    }

    public void upgradeRank(String ownerName) {
        int comparator = 18;

        if (tier == 2) {
            comparator = 36;
        }
        if (tier == 3) {
            comparator = 54;
        }
        if (tier == 4) {
            comparator = 54;
        }

        if (storages.get(1).getSize() != comparator) {
            Inventory inventory = Bukkit.createInventory(null, comparator, Tools.colorMSG("&9Almacenamiento de tu mascota"));
            for (int i = 0; i < storages.get(1).getSize(); i++) {
                if (storages.get(1).getItem(i) != null) {
                    inventory.addItem(storages.get(1).getItem(i));
                }
            }

            storages.put(1, inventory);


            if (tier == 4) {
                if (storages.get(2) == null) {
                    Inventory inventory2 = Bukkit.createInventory(null, comparator, Tools.colorMSG("&9Almacenamiento de tu mascota"));
                    storages.put(2, inventory2);
                }
            }

        }

    }

    public HashMap<Integer, Inventory> getRawStorages(){
        return this.storages;
    }


}
