package myplugin.myplugin.Conversation;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Conversation {

    private final Player player;
    private final List<String> conversationContent;
    private final int interval;
    private final String identifier;

    public Conversation(Player player, List<String> conversationContent, int interval, String identifier) {
        this.player = player;
        this.conversationContent = conversationContent;
        this.interval = interval;
        this.identifier = identifier;
    }

    public void start(){
        final int[] i = {0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if(conversationContent.size() > i[0]){
                    if(!MyPlugin.getConversationManager().getConversations().containsKey(player)){
                        this.cancel();
                        return;
                    }
                    Tools.message(player, " ");
                    Tools.message(player, conversationContent.get(i[0]));
                    Tools.message(player, " ");
                    if(MyPlugin.getConversationManager().getConversationFile().getConfigurationSection(identifier).contains("Sound")) {
                        Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> player.playSound(player.getLocation(),
                                Sound.valueOf(MyPlugin.getConversationManager().getConversationFile().getString(identifier + ".Sound")), 100, 0.6F));
                    }
                }else{
                    MyPlugin.getConversationManager().getConversations().remove(player);
                    this.cancel();
                    return;
                }
                i[0]++;
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, interval * 20L);
    }

}
