package myplugin.myplugin;

import myplugin.myplugin.LivesSystem.LivesSystem;
import myplugin.myplugin.LocationSystem.Locations;
import myplugin.myplugin.PlayerStaticItem.StaticItem;
import myplugin.myplugin.PlayersNumber.PlayerNumberGenerator;
import myplugin.myplugin.SellingSystem.Selling_System;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class MyPlugin extends JavaPlugin {

    public PlayerNumberGenerator generator;
    public static HashMap<Player, Integer> playerNumbersMap;
    public static Locations locations;
    public static final YamlFile languageFile = new YamlFile("plugins/MyPlugin/Lang.yml");
    public static LivesSystem livesSystem;
    public static StaticItem staticItem;
    public static Selling_System selling_system;

    @Override
    public void onEnable() {

        playerNumbersMap = new HashMap<>();
        generator = new PlayerNumberGenerator(this);
        generator.startSystem();


        locations = new Locations(this);
        locations.startSystem();
        loadLanguageFile();

        livesSystem = new LivesSystem(this);
        livesSystem.startSystem();

        staticItem = new StaticItem(this);
        staticItem.startSystem();

        selling_system = new Selling_System(this);
        try {
            selling_system.startSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDisable() {

        for (Map.Entry<Player, Integer> entry : MyPlugin.selling_system.server_balance.entrySet()) {
            YamlFile yamlFile = new YamlFile("plugins/MyPlugin/SellingSystem/" + entry.getKey().getName() + ".yml");
            try {
                yamlFile.load();
                yamlFile.set("Coins", entry.getValue());
                yamlFile.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       livesSystem.saveAllPlayers();
    }

    private void loadLanguageFile() {
        try {
            if(!languageFile.exists()){
                this.saveResource("Lang.yml",true);
            }
            languageFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int getRandomIntegerBetweenRange(double min, double max){
        return (int) ((int)(Math.random()*((max-min)+1))+min);

    }
}
