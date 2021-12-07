package myplugin.myplugin.DeathRecords;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {



        if(command.getName().equalsIgnoreCase("back")){

            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;

            if(!MyPlugin.deathRecordMap.containsKey(player)){
                player.sendMessage(Tools.hexColorMSG("#e02b2bEsa solicitud para volver a su " +
                        "punto de muerte ha expirado, sin embargo, tienes las coordenadas para que puedas caminar. Lo sentimos, piensa más rápido la próxima " +
                        "vez."));
                return false;
            }

            if(Tools.getPlayerCoins(player) < 100){
                if(Tools.getPlayerHearts(player) == 0){
                    player.sendMessage("#e02b2bNo tienes nisiquiera una maldita moneda, porfavor " +
                            "consigue dinero. Si no sabes como miralo en el warp de tutoriales.");
                }else{
                    int missing = 100 - Tools.getPlayerCoins(player);
                    Tools.message(player, "#e02b2bQue sad no puedes pagar ni 100 pobres monedas? " +
                            "Te faltan &7" + missing + " #e02b2bmonedas. Tienes exactamente &7" +
                            MyPlugin.deathRecordMap.get(player).getDelay() + " #e02b2bsegundos para " +
                            "acompletar las &7100 #e02b2bmonedas.");
                }
                return false;
            }

            Tools.removePlayerCoins(player, 100);
            if(MyPlugin.deathRecordMap.get(player).teleportSuccessful()){
                Tools.message(player, "#5cb6d6Fuiste teletransportado con éxito al último lugar en el que moriste.");
            }else {
                Tools.message(player, "#e02b2bNo pudimos llevarte al lugar de tu muerte." +
                        " Quizás el mundo ya no exista o haya ocurrido otro error. Pídale al administrador" +
                        " que revise la consola para solucionar este problema.");
            }
        }

        return false;
    }
}
