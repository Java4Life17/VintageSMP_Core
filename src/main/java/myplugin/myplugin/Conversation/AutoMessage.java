package myplugin.myplugin.Conversation;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoMessage {

    private final YamlFile messages;
    private BukkitTask runnable;
    private int interval;
    private AtomicInteger count = new AtomicInteger(0);

    public AutoMessage() {
        this.messages = new YamlFile("plugins/MyPlugin/AutoMessages.yml");
        Objects.requireNonNull(MyPlugin.getMyPlugin().getCommand("automessage")).setExecutor(new AutoMessage_Command());
        loadFile();
        interval = messages.getInt("Interval");
    }

    public void start(){
        initializeRunnable();
    }

    private void initializeRunnable() {
        List<String> keys = new ArrayList<>(messages.getConfigurationSection("Messages").getKeys(false));
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(count.get() == interval) {
                    int random = Tools.getRandomNumber(1, keys.size());
                    String key = keys.get(random - 1);
                    List<String> lines = messages.getStringList("Messages." + key);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        lines.forEach(line -> Tools.message(player, line));
                    });
                    count.set(0);
                }

                count.getAndIncrement();

            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);
    }

    public void reload(){
        loadFile();
        this.interval = messages.getInt("Interval");
        List<String> keys = new ArrayList<>(messages.getKeys(false));
        count.set(0);
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(count.get() == interval) {
                    int random = Tools.getRandomNumber(1, keys.size());
                    String key = keys.get(random - 1);
                    List<String> lines = messages.getStringList("Messages." + key);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        lines.forEach(line -> Tools.message(player, line));
                    });
                    count.set(0);
                }

                count.getAndIncrement();

            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);
    }

    private void loadFile() {
        try {
            if(!messages.exists()){
                messages.createNewFile(true);
                messages.createSection("Messages");
                List<String> messagesList = new ArrayList<>();
                messagesList.add("LisGirl: Hola");
                messages.set("Interval", 30);
                messages.set("Messages.Default", messagesList);
                messages.save();
            }
            messages.load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
