package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Locations {

    private MyPlugin plugin;

    public Locations(MyPlugin plugin){
        this.plugin = plugin;
    }

    public YamlFile locationsFile;


    // Method to initiate the locations system
    public void startSystem(){

        locationsFile = new YamlFile("plugins/MyPlugin/Locations.yml");
        createLocationsFile();
        Objects.requireNonNull(plugin.getCommand("districtEdit")).setExecutor(new editor_Command());
        Objects.requireNonNull(plugin.getCommand("takePlayerToDistrict")).setExecutor(new editor_Command());
        plugin.getServer().getPluginManager().registerEvents(new Locations_MainGUI_Listener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new Locations_List_Listener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new onSoundsMenu(plugin), plugin);



    }

    /*
    Create a locations file where all the locations
    will be saved to, along with the information saved
    to that location.
     */

    private void createLocationsFile() {

        try {
            if(!locationsFile.exists()){
                plugin.saveResource("Locations.yml", true);
            }
            locationsFile.load();

        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

}
