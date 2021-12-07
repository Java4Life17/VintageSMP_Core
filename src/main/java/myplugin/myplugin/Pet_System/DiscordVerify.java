package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordVerify implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("discord")){
            Player player = (Player) sender;

            if(MyPlugin.system.discordMinecraft.contains(player.getName())){
                player.sendMessage(Tools.colorMSG("&cLo siento pero esta cuenta ya esta vinculada a un discord. Para establecer un nuevo" +
                        " discord, contacte a los fundadores en nuestro servidor de discord!"));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100 , 0.5F);
                return false;
            }

            String key = Tools.getAlphaNumericString(10);

            TextComponent message = new TextComponent(Tools.colorMSG("&f&l[&6&o" + key + "&f&l]"));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, key));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("CLICK PARA COPIAR || " +
                    "Pega esto en #VerificarMC en nuestro discord!")));
            player.spigot().sendMessage(message);
            MyPlugin.system.discordLink.put(player, key);
        }
        return false;
    }
}
