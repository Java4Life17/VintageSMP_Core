package myplugin.myplugin.Pet_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (command.getName().equalsIgnoreCase("pet")) {

            if (MyPlugin.system.petSpawning.contains(((Player) sender))) {
                sender.sendMessage(Tools.colorMSG("&cEspera a que tu mascota aparesca!"));
                return false;
            }

            if (!MyPlugin.system.getByName(sender.getName()).getNpc().isSpawned() || MyPlugin.system.getByName(sender.getName()).getNpc() == null) {
                ((Player) sender).openInventory(Menus.getNotSpawnInventory());
                return false;
            }

            ((Player) sender).openInventory(MyPlugin.system.getByName(sender.getName()).getMainMenu());
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 0.3F);
        }
        if (command.getName().equalsIgnoreCase("opItem")) {
            Player player = (Player) sender;
            if (!player.hasPermission("MyPlugin.OpItems")) {
                player.sendMessage(Tools.langText("NoPerm"));
                return false;
            }

            if (args.length == 0) {
                return false;
            }

            boolean emptySlot = false;

            for (int i = 0; i < 36; i++) {
                if (player.getInventory().getItem(i) == null || Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getType(), Material.AIR) ||
                        Objects.equals(Objects.requireNonNull(player.getInventory().getItem(i)).getType(), Material.VOID_AIR)) {
                    emptySlot = true;
                }
            }

            if (!emptySlot) {
                player.sendMessage(Tools.langText("No_Empty_Slot"));
                player.closeInventory();
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "pettreat", "somethingelse" -> player.getInventory().addItem(Tools.getPetTreat());
            }

        }
        return false;
    }
}
