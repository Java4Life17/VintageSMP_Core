package myplugin.myplugin.IpControl;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;

public class JoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
//        String ipAddress = event.getAddress().getHostAddress();
//        String playerName = event.getName();
//
//        if(MyPlugin.ipControl.whitelist){
//
//            if(!MyPlugin.languageFile.getStringList("Whitelist").contains(playerName)){
//                String builder = Tools.colorMSG("&4&l&nVINTAGE&f&l&nSMP\n\n") +
//                        Tools.colorMSG("&e&lNuestro servidor esta en modo privado por ahora...\n") +
//                        Tools.colorMSG("&e&lVisitanos en discord para mas info.\n&adsc.gg/vintagesmp");
//                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, builder);
//                return;
//            }
//        }
//
//        if(MyPlugin.whiteList){
//            String builder = Tools.colorMSG("&4&l&nVINTAGE&f&l&nSMP\n\n") +
//                    Tools.colorMSG("&a&lNuestro servidor se esta reiniciando y\n") +
//                    Tools.colorMSG("&a&lrecolectando informacion necesaria para iniciar") +
//                    Tools.colorMSG("&a&lcorrectamente. Danos un minuto antes de intentar otravez.");
//            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, builder);
//        }
//
//        //This is going to run if the IP is already registered.
//        if (MyPlugin.ipControl.ipIsRegistered(ipAddress)) {
//            //Check if player is registered to another IP before anything!!!!!!
//            if (MyPlugin.ipControl.accountOnOtherIP(playerName, ipAddress)) {
//                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MyPlugin.ipControl.
//                        accountOtherIP(MyPlugin.ipControl.getBind(ipAddress)));
//            }
//
//            //If the IP does not have that account registered.
//            if (!MyPlugin.ipControl.ipContainsAccount(ipAddress, playerName)) {
//                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MyPlugin.ipControl.
//                        ipOnOtherAccount(MyPlugin.ipControl.getBind(ipAddress)));
//            }
//        } else {
//            if (MyPlugin.ipControl.accountOnOtherIP(playerName, ipAddress)) {
//                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, MyPlugin.ipControl.
//                        accountOtherIP(MyPlugin.ipControl.getBind(ipAddress)));
//                return;
//            }
//
//            List<String> bindings = new ArrayList<>(MyPlugin.ipControl.ipBindings.getKeys(false));
//
//            String bind = Tools.getAlphaNumericString(5);
//            while (bindings.contains(bind)) {
//                bind = Tools.getAlphaNumericString(5);
//            }
//
//            try {
//                String temp = ipAddress;
//                temp = temp.replace(".", "_");
//                MyPlugin.ipControl.ipBindings.set(temp, bind);
//                MyPlugin.ipControl.ipBindings.save();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            StringBuilder builder = new StringBuilder()
//                    .append(Tools.colorMSG("&4&l&nVINTAGE&f&l&nSMP\n\n"))
//                    .append(Tools.colorMSG("&a&lRegistramos con éxito esta cuenta como un nuevo usuario.\n"))
//                    .append(Tools.colorMSG("&a&lVuelva a iniciar sesión para unirse.\n\n\n"))
//                    .append(Tools.colorMSG("&a&ldsc.gg/vintagesmp"));
//
//            MyPlugin.ipControl.createNewIpRegistry(ipAddress, playerName);
//            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, builder.toString());
//
//
//        }

    }

}
