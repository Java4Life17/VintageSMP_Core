package myplugin.myplugin.Conversation;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConvCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("conversation")){

            if(!(sender instanceof ConsoleCommandSender)){
                return false;
            }

            if(args.length < 3){
                return false;
            }

            if(Bukkit.getPlayer(args[1]) == null) return false;

            if(args[0].equalsIgnoreCase("send")){
                Player player = Bukkit.getPlayer(args[1]);
                String identifier = args[2];
                MyPlugin.getConversationManager().sendTo(player, identifier);
            }
            if(args[0].equalsIgnoreCase("reload")){
                try {
                    MyPlugin.getConversationManager().getConversationFile().load();
                    Player player = Bukkit.getPlayer(args[1]);
                    assert player != null;
                    player.sendMessage(Tools.colorMSG("&aReloaded conversation file."));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }

        return false;
    }
}
