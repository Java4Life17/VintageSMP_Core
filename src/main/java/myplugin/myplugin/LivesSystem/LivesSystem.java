package myplugin.myplugin.LivesSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LivesSystem {
    private MyPlugin plugin;
    public LivesSystem(MyPlugin plugin){
        this.plugin = plugin;
    }

    public  HashMap<Player, Double> playerLives;
    public YamlFile heartsFile;
    public void startSystem(){
        playerLives = new HashMap<>();
        heartsFile = new YamlFile("plugins/MyPlugin/HeartsFile.yml");
        createHeartsFile();
        plugin.getServer().getPluginManager().registerEvents(new Events(), plugin);
        Objects.requireNonNull(plugin.getCommand("corazones")).setExecutor(new LivesCommands());
        Objects.requireNonNull(plugin.getCommand("corazones")).setTabCompleter(new livesTabCompleter());
    }

    public void saveAllPlayers(){
        for(Map.Entry<Player, Double> entry : playerLives.entrySet()){
            Tools.savePlayersHearts(entry.getKey());
        }
    }

    private void createHeartsFile() {
        try {
            if(!heartsFile.exists()){
                plugin.saveResource("HeartsFile.yml", true);
            }
            heartsFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
