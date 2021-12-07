package myplugin.myplugin.Conversation;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AutoMessage_Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("automessage")){

            if(!(sender instanceof Player player)) return false;
            if(!player.hasPermission("automessage.reload")){
                return false;
            }

            if(args[0].equalsIgnoreCase("reload")){
                MyPlugin.getAutoMessage().reload();
                Tools.message(player, "&aSuccessfully reloaded the automatic message");
            }


        }

        return false;
    }
}
