package myplugin.myplugin.Discord_System;

import myplugin.myplugin.Tools;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordToMinecraft extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {

            if (!event.getChannel().getId().equals("887910732111446017")) {
                return;
            }
            if(event.getAuthor().getId().equals("736346686737219684")){
                return;
            }

            String author = event.getAuthor().getName();
            String message = event.getMessage().getContentDisplay();

            Bukkit.getServer().broadcastMessage(Tools.colorMSG("&8&l&o[&b&l&oDISCORD&8&l&o] &7&o" + author + "&8: &o" + message));



        }catch (Exception e){
            System.out.println("No se pudo procesar el mensaje a Minecraft");
        }
    }
}
