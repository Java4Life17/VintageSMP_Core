package myplugin.myplugin.IpControl;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.RestartSystem;
import myplugin.myplugin.Tools;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IpControl {

    private MyPlugin plugin;

    public IpControl(MyPlugin plugin) {
        this.plugin = plugin;
    }

    public YamlFile ipFile = new YamlFile("plugins/MyPlugin/IpFile.yml");
    public YamlFile ipBindings = new YamlFile("plugins/MyPlugin/IP_Binding.yml");
    public boolean whitelist;

    public void start_System() {
        whitelist = false;
        loadIPFiles();
        loadIPBindings();
        plugin.getServer().getPluginManager().registerEvents(new JoinEvent(), plugin);
        Objects.requireNonNull(plugin.getCommand("mwhitelist")).setExecutor(new RestartSystem());

    }

    public void loadIPFiles() {
        try {
            if (!ipFile.exists()) {
                plugin.saveResource("IpFile.yml", true);
            }
            ipFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadIPBindings() {
        try {
            if (!ipBindings.exists()) {
                plugin.saveResource("IP_Binding.yml", true);
            }
            ipBindings.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is a method to register every new user.
     *
     * @param ip         - The user's IP to register on the file
     * @param playerName - The name to add to the list of accounts.
     */
    public void createNewIpRegistry(String ip, String playerName) {

        ip = ip.replace(".", "_");
        try {
            ipFile.createSection(ip);
            List<String> accounts = new ArrayList<>();
            accounts.add(playerName);
            ipFile.set(ip + ".Accounts", accounts);
            ipFile.set(ip + ".Bypass", false);
            ipFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAccount(String ip, String playerName) {
        ip = ip.replace(".", "_");
        try {
            List<String> accounts = new ArrayList<>();
            accounts = ipFile.getStringList(ip + ".Accounts");
            accounts.add(playerName);
            ipFile.set(ip + ".Accounts", accounts);
            ipFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAccount(String ip, String playerName) {
        ip = ip.replace(".", "_");
        try {
            List<String> accounts = new ArrayList<>();
            accounts = ipFile.getStringList(ip + ".Accounts");
            if (accounts.contains(playerName)) {
                accounts.remove(playerName);
                if (accounts.size() == 0) {
                    ipFile.set(ip, null);
                } else {
                    ipFile.set(ip + ".Accounts", accounts);
                }
                ipFile.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBypass(String ip, String playerName, boolean state) {
        ip = ip.replace(".", "_");
        try {
            ipFile.set(ip + ".Bypass", state);
            ipFile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAccounts(String ip) {
        ip = ip.replace(".", "_");
        List<String> accounts = new ArrayList<>();
        accounts = ipFile.getStringList(ip + ".Accounts");
        return accounts;
    }

    public boolean ipIsRegistered(String ip) {
        ip = ip.replace(".", "_");
        return ipFile.contains(ip);
    }

    public boolean ipContainsAccount(String ip, String playerName) {
        List<String> accounts = getAccounts(ip);
        return accounts.contains(playerName);
    }

    public boolean accountOnOtherIP(String playerName, String ip){
        ip = ip.replace(".", "_");
        boolean containsIt = false;
        for(String key : ipFile.getKeys(false)){
            List<String> names = ipFile.getStringList(key + ".Accounts");
            if(names.contains(playerName) && !key.equalsIgnoreCase(ip)){
                containsIt = true;
                break;
            }
        }
        return containsIt;
    }

    public String getBind(String ip){
        ip = ip.replace(".", "_");

        return ipBindings.getString(ip);
    }


    public String ipOnOtherAccount(String ip_Token) {
        //dsc.gg/vintagesmp
        StringBuilder message = new StringBuilder();
        message.append(Tools.colorMSG("&4&l&nVINTAGE&f&l&nSMP\n"));
        message.append("\n");
        message.append("\n");
        message.append(Tools.colorMSG("&cParece que esta ip está registrada para otra cuenta ip &7&o(IP: "))
                .append(ip_Token)
                .append(Tools.colorMSG(")\n&csi cree que se trata de un error, comuníquese con el soporte en nuestro discord\n"))
                .append(Tools.colorMSG("dsc.gg/vintagesmp"));

        return message.toString();
    }


    public List<String> getAllBinds(){
        List<String> toReturn = new ArrayList<>();
        for(String key : ipBindings.getKeys(false)){
            toReturn.add(ipBindings.getString(key));
        }
        return toReturn;
    }

    public String getIPFromBind(String toCompare){
        String toReturn = "";
        for(String key : ipBindings.getKeys(false)){
            if(ipBindings.getString(key).equalsIgnoreCase(toCompare)){
                toReturn = key;
                break;
            }
        }
        return toReturn;
    }


    public String accountOtherIP(String ip_Token) {
        //dsc.gg/vintagesmp
        StringBuilder message = new StringBuilder();
        message.append(Tools.colorMSG("&4&l&nVINTAGE&f&l&nSMP\n"));
        message.append("\n");
        message.append("\n");
        message.append(Tools.colorMSG("&cParece que esta cuenta esta registrada a otra IP"))
                .append(Tools.colorMSG("\n&csi cree que se trata de un error, comuníquese con el soporte en nuestro discord\n"))
                .append(Tools.colorMSG("dsc.gg/vintagesmp"));

        return message.toString();
    }


}
