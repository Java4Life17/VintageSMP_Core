package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.simpleyaml.configuration.file.YamlFile;

public class SellingSystem_Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        YamlFile file = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
        try {
            if (!file.exists()) {
                file.createNewFile(true);
                file.createSection("Items");
                file.set("Coins", 100);
                file.save();
            }
            file.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tools.setPlayerCoins(player, file.getInt("Coins"));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        YamlFile file = new YamlFile("plugins/MyPlugin/SellingSystem/" + player.getName() + ".yml");
        try {
            file.load();
            file.set("Coins", Tools.getPlayerCoins(player));
            file.save();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
