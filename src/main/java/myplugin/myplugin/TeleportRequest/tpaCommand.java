package myplugin.myplugin.TeleportRequest;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.tools.Tool;

public class tpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (command.getName().equalsIgnoreCase("tprequest")) {
            Player who = (Player) sender;
            try {

                if (Bukkit.getPlayer(args[0]) == null) {
                    who.sendMessage(Tools.langText("playerNotFound"));
                    return false;
                }

                Player to = Bukkit.getPlayer(args[0]);

                if(to == who){
                    who.sendMessage(Tools.colorMSG("&cNo puedes envitarte peticion de TP a ti mismo!"));
                    return false;
                }
                assert to != null;
                to.sendMessage(" ");
                TextComponent message = new TextComponent(Tools.hexColorMSG(" #d6ff0aEl jugador &c" + who.getName() + " #d6ff0apidio teletransportarse " +
                        "a ti. Da clic en este mensaje para sugerir el comando /tpaccept. Tienes 30 segundos para aceptar."));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpaccept"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Clic para escribir /tpaccept")));
                to.spigot().sendMessage(message);
                to.sendMessage(" ");

                who.sendMessage(" ");
                who.sendMessage(Tools.hexColorMSG("#d6ff0aEnviaste una solicitud de teleporción a &7" + args[0] + "#d6ff0a!"));
                MyPlugin.teleportRequestSystem.getOnRequest().put(to, who);
                MyPlugin.teleportRequestSystem.getCooldown().put(to, 30);
                who.sendMessage(" ");

            } catch (Exception e) {
                who.sendMessage(" ");
                who.sendMessage(ChatColor.RED + "Uso no valido. Uso correcto -> " + ChatColor.GRAY + "tpa/tprequest <jugador>");
                who.sendMessage(" ");
            }
        }else if(command.getName().equalsIgnoreCase("tpaccept")){
            Player player = (Player) sender;
            if(!MyPlugin.teleportRequestSystem.getOnRequest().containsKey(player)) {
                player.sendMessage(Tools.colorMSG("&cNadie ha solicitado teletransportarse a ti."));
                return false;
            }

            player.sendMessage(" ");
            player.sendMessage(Tools.colorMSG("&aAcceptaste la peticion de teletransportacion de &c" + MyPlugin.teleportRequestSystem.getOnRequest().get(player).getName()
            + "&a!"));
            player.sendMessage(" ");
            MyPlugin.teleportRequestSystem.getOnRequest().get(player).sendMessage(" ");
            MyPlugin.teleportRequestSystem.getOnRequest().get(player).sendMessage(Tools.colorMSG("&c" + player.getName() + " &aacepto tu peticion para teletransportarte."));
            MyPlugin.teleportRequestSystem.getOnRequest().get(player).sendMessage(" ");
            MyPlugin.teleportRequestSystem.getOnRequest().get(player).teleport(player);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 0.5F);
            MyPlugin.teleportRequestSystem.getOnRequest().remove(player);
            MyPlugin.teleportRequestSystem.getCooldown().remove(player);

        }




        return true;
    }
}
