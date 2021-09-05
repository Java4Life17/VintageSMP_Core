package myplugin.myplugin.PlayersNumber;

import myplugin.myplugin.MyPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;

public class PlayerNumberGenerator{

    private MyPlugin plugin;
    public PlayerNumberGenerator(MyPlugin plugin){this.plugin = plugin;}

    public YamlFile numbersFile;


    /*
    Start a system that will take care of generating a number for
    each player so that when they join, they have a number that belongs
    to their player name and the data will be saved to their name.
     */
    public void startSystem(){
        numbersFile = new YamlFile("plugins/MyPlugin/PlayersNumber.yml");
        generateNumbersFile();
        plugin.getServer().getPluginManager().registerEvents(new onPlayerJoin(plugin), plugin);


    }

    private void generateNumbersFile() {
        try {
            if(!numbersFile.exists()){
                plugin.saveResource("PlayersNumber.yml", true);
            }
            numbersFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
