package myplugin.myplugin;

import me.TechsCode.UltraPermissions.UltraPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatRankFormat implements Listener {

    @EventHandler
    public void onChatFormat(AsyncPlayerChatEvent event){
        String format = event.getFormat();
        format = Tools.hexColorMSG(format);
        event.setFormat(format);

    }



}
