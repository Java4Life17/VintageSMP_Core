package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clan {

    private final String clanName;
    private String clanOwner;
    private int clanLevel;
    private List<String> clanMembers;
    private String clanDescription;
    private YamlFile clanFile;

    /**
     * This is the method called whenever a new ClanObjets is created. With the value passed on, this constructor
     * will load every parameter needed for the clan to operate. This clan will only be loaded when a clan member
     *  is connected to the server.
     * @param clanName The name that will define the whole clan.
     */
    public Clan(String clanName) {
        this.clanName = clanName;
        startClanTasks();
    }



    /**
     * This method will start generating everything neede for the clan after the clan name has been given.
     */
    private void startClanTasks() {
        loadClanFile();
        this.clanOwner = clanFile.getString("clanOwner");
        this.clanLevel = clanFile.getInt("clanLevel");
        this.clanMembers = clanFile.getStringList("clanMembers");
        this.clanDescription = clanFile.getString("clanDescription");
    }

    /**
     * Gets the name belonging to this clan
     *
     * @return The name of the clan
     */
    public String getClanName() {
        return clanName;
    }

    /**
     * Get the clan owner in form of string
     *
     * @return The clanOwner's name
     */
    public String getClanOwner() {
        return clanOwner;
    }

    /**
     * Get the clan leve in form of integer
     *
     * @return The clan level
     */
    public int getClanLevel() {
        return clanLevel;
    }

    /**
     * Get a list of the clan members
     *
     * @return The clan members
     */
    public List<String> getClanMembers() {
        return clanMembers;
    }

    /**
     * Get the clan's description text
     * @return The clan's description.
     */
    public String getClanDescription() {
        return clanDescription;
    }

    /**
     * Get the file containing all the clan information.
     * @return The clan's file
     */
    public YamlFile getClanFile() {
        return clanFile;
    }


    /**
     * Generated the clan file.
     */
    private void loadClanFile() {
        clanFile = new YamlFile("plugins/MyPlugin/Clans/" + clanName + ".yml");
        try {
            if(!clanFile.exists()){
                return;
            }else{
                clanFile.load();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update the clan's current level
     * @param level New level
     */
    public void setClanLevel(int level){
        this.clanLevel = level;
        getClanFile().set("clanLevel", level);
        try {
            getClanFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMember(String playerName){
        getClanMembers().add(playerName);
        getClanFile().set("clanMembers", getClanMembers());
        saveClan();
        if(Bukkit.getPlayer(playerName) != null){
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).getPersistentDataContainer().set(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"), PersistentDataType.STRING, getClanName());
            String toSend = Tools.langText("JoinedClan");
            toSend = toSend.replace("%clan%", getClanName());
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).sendMessage(toSend);
        }

        if(Bukkit.getPlayer(getClanOwner()) != null){
            String toSend = Tools.langText("targetJoinedClan");
            toSend = toSend.replace("%player%", playerName);
            Objects.requireNonNull(Bukkit.getPlayer(getClanOwner())).sendMessage(toSend);
        }

    }
    public void removeMember(String playerName){
        getClanMembers().remove(playerName);
        getClanFile().set("clanMembers", getClanMembers());
        saveClan();
        if(Bukkit.getPlayer(playerName) != null){
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).getPersistentDataContainer().remove(new NamespacedKey(MyPlugin.getMyPlugin(), "Clan"));
            String toSend = Tools.langText("youWereRemoved");
            toSend = toSend.replace("%clan%", getClanName());
            Objects.requireNonNull(Bukkit.getPlayer(playerName)).sendMessage(toSend);
        }
    }

    /**
     * This method is used to save the Clan's data on the files
     */
    public void saveClan(){
        try {
            getClanFile().save();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Get the count of how many playes of this clan are online.
     * @return The count
     */
    public int getOnlineCount(){
        int count = 0;
        for(String member : getClanMembers()){
            if(Bukkit.getPlayer(member) != null)
                count++;
        }
        return count;
    }

    public void messageToMembers(String message){
        for(String m : getClanMembers()){
            if(Bukkit.getPlayer(m) != null){
                Objects.requireNonNull(Bukkit.getPlayer(m)).sendMessage(message);
            }
        }
    }

    public List<Player> getOnlineMembers(){
        List<Player> toReturn = new ArrayList<>();
        for(String m : getClanMembers()){
            if(Bukkit.getPlayer(m) != null){
                toReturn.add(Bukkit.getPlayer(m));
            }
        }
        return toReturn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clan clan = (Clan) o;
        return clan.getClanName().equals(this.getClanName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(clanName, clanOwner, clanLevel, clanMembers, clanDescription, clanFile);
    }
}
