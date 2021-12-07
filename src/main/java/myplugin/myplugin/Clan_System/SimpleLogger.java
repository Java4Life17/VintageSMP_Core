package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class SimpleLogger {

    public void warning(String errorMessage, String description){
        Bukkit.getLogger().info(ChatColor.YELLOW + "\n\n===================================================================");
        Bukkit.getLogger().info(ChatColor.YELLOW + "WARNING WAS CAUGHT IN " + MyPlugin.getMyPlugin().getName() + "\n");
        Bukkit.getLogger().info(ChatColor.YELLOW + "Description: " + description + "\n\n");
        Bukkit.getLogger().info(ChatColor.YELLOW + errorMessage);
        Bukkit.getLogger().info(ChatColor.YELLOW + "\n\n===================================================================");
    }

    public void error(String errorMessage, String description){
        Bukkit.getLogger().info(ChatColor.RED + "\n\n===================================================================");
        Bukkit.getLogger().info(ChatColor.RED + "ERROR WAS CAUGHT IN " + MyPlugin.getMyPlugin().getName() + "\n");
        Bukkit.getLogger().info(ChatColor.RED + "Description: " + description + "\n\n");
        Bukkit.getLogger().info(ChatColor.RED + errorMessage);
        Bukkit.getLogger().info(ChatColor.RED + "\n\n===================================================================");
    }

    public void fatal(String errorMessage, String description){
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "\n\n===================================================================");
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "FATAL ERROR WAS CAUGHT IN " + MyPlugin.getMyPlugin().getName() + "\n");
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "Description: " + description + "\n\n");
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE + errorMessage);
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE + "\n\n===================================================================");
    }

}
