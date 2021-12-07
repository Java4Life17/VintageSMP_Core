package myplugin.myplugin.Discord_System;



import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import me.TechsCode.UltraPermissions.storage.objects.User;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Minecraft_Chat implements Listener {
    @EventHandler
    public void onMinecraftChat(AsyncPlayerChatEvent event) {
        try {

            if(event.isCancelled()){
                return;
            }

            Player player = event.getPlayer();

            String id = "887910732111446017";

            String message = event.getMessage();

            final String[] toReturn = new String[1];

            for(User user : UltraPermissions.getAPI().getUsers()){
                if(user.getName().equals(player.getName())){
                    user.getGroups().stream().toList().get(0).get().flatMap(Group::getPrefix).ifPresent(prefix -> toReturn[0] = prefix);
                    break;
                }
            }


            TextChannel channel = MyPlugin.discordSystem.getBot().getTextChannelById(id);
            assert channel != null;
            channel.sendMessage("***" + event.getPlayer().getName() + "***: " + message + "").queue();


            String _Stringformat = event.getFormat();
            _Stringformat = _Stringformat.replace("%2$s", "");

            String stringBuilder = Tools.hexColorMSG("\n#4db9e3⭐#f55353Rango &e➠ ") + Tools.hexColorMSG(toReturn[0]) + "\n" +
                    Tools.hexColorMSG("#4db9e3⭐#f55353Monedas &e➠ &7") + Tools.getPlayerCoins(player) + "\n" +
                    Tools.hexColorMSG("#4db9e3⭐#f55353Corazones &e➠ &7") + Tools.getPlayerHearts(player) + "\n";
            if(MyPlugin.clan_system.manager().getPlayerClan(player) != null){
                stringBuilder = stringBuilder + "#4db9e3⭐#f55353Clan &e➠ &7" + MyPlugin.clan_system.manager().getPlayerClan(player).getClanName() + "\n";
                stringBuilder = stringBuilder + "#4db9e3⭐#f55353Nivel &e➠ &7" + MyPlugin.clan_system.manager().getPlayerClan(player).getClanLevel() + "\n";
                stringBuilder = stringBuilder + "#4db9e3⭐#f55353Miembros &e➠ &7" + MyPlugin.clan_system.manager().getPlayerClan(player).getClanMembers().size()+ "\n";
            }




            ComponentBuilder builder = new ComponentBuilder();
            TextComponent format = new TextComponent(TextComponent.fromLegacyText(Tools.hexColorMSG("#fcba03[#5e463d⛏#fcba03] ")));
            format.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(Tools.hexColorMSG(stringBuilder)))));
            TextComponent finalMessage = new TextComponent(TextComponent.fromLegacyText(_Stringformat + event.getMessage()));
            finalMessage.retain(ComponentBuilder.FormatRetention.EVENTS);
            builder.append(format).append(finalMessage).reset();
            for(Player online : Bukkit.getOnlinePlayers()){
                online.spigot().sendMessage(builder.create());
            }

            event.setCancelled(true);

        }catch (Exception e){
            System.out.println("No se pudo procesar el mensaje a discord.");
        }
    }
}
