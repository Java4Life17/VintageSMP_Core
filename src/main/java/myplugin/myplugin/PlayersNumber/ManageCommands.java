package myplugin.myplugin.PlayersNumber;

import myplugin.myplugin.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //playerNumbers reload
        if(command.getName().equalsIgnoreCase("playerNumbers")){

            Player player = (Player) sender;

            if(!player.hasPermission("VintageSMP.PlayerNumbers")){
                player.sendMessage(Tools.colorMSG("&cNo Permission for this command!"));
            }

            try {
                
            }catch (Exception e){

            }
        }

        return false;
    }
}
