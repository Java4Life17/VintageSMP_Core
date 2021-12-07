package myplugin.myplugin.Conversation;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.HashMap;
import java.util.Map;

public class ConversationManager {

    private final Map<Player, Conversation> conversations = new HashMap<>();
    private final YamlFile conversationFile = new YamlFile("plugins/MyPlugin/Conversation.yml");

    public ConversationManager() {
        loadConversationFile();
        MyPlugin.getMyPlugin().getCommand("conversation").setExecutor(new ConvCommand());
    }

    private void loadConversationFile() {
        if(!conversationFile.exists()){
            MyPlugin.getMyPlugin().saveResource("Conversation.yml", false);
        }
        try {
            conversationFile.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendTo(Player player, String identifier){
        if(conversations.containsKey(player)){
            player.sendMessage(Tools.colorMSG("&cYa estas conversando con un NPC, espera a que la conversation termine."));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 50, 0.4F);
            return;
        }
        if(!conversationFile.contains(identifier)){
            player.sendMessage(Tools.colorMSG("&cEsta conversacion ya no existe. Habla con un administrador para que sea reparado."));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 50, 0.4F);
            return;
        }
        Conversation conversation = new Conversation(player, conversationFile.getStringList(identifier + ".Content"), conversationFile.getInt(identifier + ".Interval"), identifier);
        conversation.start();
        conversations.put(player, conversation);
    }

    public void removeFrom(Player player){
        if(!conversations.containsKey(player)){
            return;
        }
        conversations.remove(player);
    }

    public YamlFile getConversationFile() {
        return conversationFile;
    }

    public Map<Player, Conversation> getConversations() {
        return conversations;
    }
}
