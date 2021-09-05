package myplugin.myplugin.PlayersNumber;

import myplugin.myplugin.MyPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class onPlayerJoin implements Listener {
    private MyPlugin plugin;

    public onPlayerJoin(MyPlugin plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!plugin.generator.numbersFile.contains(player.getName())){

            List<Integer> numbersTaken = new ArrayList<>();

            for(String key : plugin.generator.numbersFile.getKeys(false)){
                numbersTaken.add(plugin.generator.numbersFile.getInt(key));
            }

            int playerNumber = MyPlugin.getRandomIntegerBetweenRange(1, 10000);

            while(numbersTaken.contains(playerNumber)){
                playerNumber = MyPlugin.getRandomIntegerBetweenRange(1, 10000);
            }



            plugin.generator.numbersFile.set(player.getName(), playerNumber);
            try {
                plugin.generator.numbersFile.save();
                plugin.generator.numbersFile.load();
            }catch (Exception e){
                e.printStackTrace();
            }
            MyPlugin.playerNumbersMap.put(player, playerNumber);
        }else {
            MyPlugin.playerNumbersMap.put(player, plugin.generator.numbersFile.getInt(player.getName()));
        }

    }

    @EventHandler
    public void playerLeaves(PlayerQuitEvent event){
        MyPlugin.playerNumbersMap.remove(event.getPlayer());
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event){
        if(event.getFormat().contains("PlayerNumber")){
            int playerNumber = MyPlugin.playerNumbersMap.get(event.getPlayer());
            String stringNumber = String.format("%05d", playerNumber);
            System.out.println(stringNumber);
            event.setFormat(event.getFormat().replace("PlayerNumber", stringNumber));
        }
    }


}
