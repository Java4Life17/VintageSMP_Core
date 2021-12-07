package myplugin.myplugin.AdminSystem;

import myplugin.myplugin.MyPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminSystem {

    public List<Player> playersOnStorageSpy;

    public void initialize() {
        playersOnStorageSpy = new ArrayList<>();
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("petStorages")).setExecutor(new Pet_Inventories());
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new Pet_Inventories_Listener(), MyPlugin.getMyPlugin());

    }

}
