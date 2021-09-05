package myplugin.myplugin.PlayerStaticItem;

import myplugin.myplugin.MyPlugin;

public class StaticItem{
    private MyPlugin plugin;
    public StaticItem(MyPlugin plugin){
        this.plugin = plugin;
    }

    public void startSystem(){

        plugin.getServer().getPluginManager().registerEvents(new StaticItem_Events(), plugin);
    }

}
