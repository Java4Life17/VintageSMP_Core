package myplugin.myplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoRestart {
    private String dateInFile;

    public void start() {
        this.dateInFile = MyPlugin.languageFile.getString("RestartDate");
        new BukkitRunnable() {
            @Override
            public void run() {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(dateInFile, formatter);
                String toCompare = LocalDateTime.now().format(formatter);
                LocalDateTime now = LocalDateTime.parse(toCompare, formatter);

                if (now.isAfter(dateTime)) {
                    MyPlugin.languageFile.set("RestartDate", now.plusDays(1).toString());
                    try {
                        MyPlugin.languageFile.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        MyPlugin.whiteList = true;
                        for (Player player : MyPlugin.getMyPlugin().getServer().getOnlinePlayers()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player + " El servidor " +
                                    "se esta reiniciando.");
                        }
                    });
                    Bukkit.getScheduler().runTaskLater(MyPlugin.getMyPlugin(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                            "restart"), 30 * 20L);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 60);
    }

}
