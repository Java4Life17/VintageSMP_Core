package myplugin.myplugin.IpControl;

import myplugin.myplugin.MyPlugin;

import java.io.IOException;
import java.util.List;

public class IpControlTools {

    public static void resetIpAccordingToKey(String bind) {
        for (String key : MyPlugin.ipControl.ipBindings.getKeys(false)) {
            if (MyPlugin.ipControl.ipBindings.getString(key).equals(bind)) {
                MyPlugin.ipControl.ipFile.set(key, null);
                break;
            }
        }
        try {
            MyPlugin.ipControl.ipFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean bindExists(String bind) {
        boolean exists = false;
        for (String key : MyPlugin.ipControl.ipBindings.getKeys(false)) {
            if (MyPlugin.ipControl.ipBindings.getString(key).equals(bind)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static String getBindByIP(String ip) {
        String bind = "";
        for (String key : MyPlugin.ipControl.ipBindings.getKeys(false)) {
            if (key.equals(ip)) {
                bind = MyPlugin.ipControl.ipBindings.getString(key);
                break;
            }
        }
        return bind;
    }

    public static String getBindByName(String playerName) {
        String ip = "";

        for (String key : MyPlugin.ipControl.ipFile.getKeys(false)) {
            List<String> accounts = MyPlugin.ipControl.ipFile.getStringList(key + ".Accounts");
            if (accounts.contains(playerName)) {
                ip = key;
                break;
            }
        }

        return getBindByIP(ip);

    }

    public static boolean playerByNameIsRegistered(String playerName) {
        boolean registered = false;
        for (String key : MyPlugin.ipControl.ipFile.getKeys(false)) {
            List<String> accounts = MyPlugin.ipControl.ipFile.getStringList(key + ".Accounts");
            if (accounts.contains(playerName)) {
                registered = true;
                break;
            }
        }
        return registered;
    }

    public static String getAccountsByBind(String bind){
        List<String> accounts = MyPlugin.ipControl.ipFile.getStringList(getIpByBind(bind) + ".Accounts");
        StringBuilder builder = new StringBuilder();

        for(String cuenta : accounts){
            builder.append("- ").append(cuenta).append(" -").append("\n");
        }

        return builder.toString();

    }
    public static String getIpByBind(String bind){
        String ip = "Null";

        for(String key : MyPlugin.ipControl.ipBindings.getKeys(false)){
            if(bind.equals(MyPlugin.ipControl.ipBindings.getString(key))){
                ip = key;
                break;
            }
        }
        return ip;
    }

    public static List<String> getRawAccountWithBind(String bind){
        return MyPlugin.ipControl.ipFile.getStringList(getIpByBind(bind) + ".Accounts");
    }

    public static void updateAccountsByIP(List<String> accounts, String ip){
        if(accounts.size() == 0){
            resetIpAccordingToKey(getBindByIP(ip));
        }else{
            MyPlugin.ipControl.ipFile.set(ip + ".Accounts", accounts);
            try {
                MyPlugin.ipControl.ipFile.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
