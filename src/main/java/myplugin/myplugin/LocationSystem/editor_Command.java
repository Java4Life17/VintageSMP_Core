package myplugin.myplugin.LocationSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class editor_Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (command.getName().equalsIgnoreCase("districtEdit")) {
            Player player = (Player) sender;

            if (!player.hasPermission("MyPlugin.DistricEditor")) {
                player.sendMessage(Tools.colorMSG("&cNo Permission for this command!"));
                return false;
            }

            player.openInventory(Locations_MainGUI.getGUI());
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 0.3F);


        } else if (command.getName().equalsIgnoreCase("takePlayerToDistrict")) {
            if (!(sender instanceof ConsoleCommandSender)) {
                return false;
            }

            //TakePlayerToDistrict player district
            try {

                Player player = Bukkit.getPlayer(args[0]);
                String district = args[1];

                if (!MyPlugin.locations.locationsFile.contains(district)) {
                    player.sendMessage(Tools.langText("District_Null"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("X") || MyPlugin.locations.locationsFile.get(district + ".X") == null) {
                    player.sendMessage(Tools.langText("District_Null_X"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("Y") || MyPlugin.locations.locationsFile.get(district + ".Y") == null) {
                    player.sendMessage(Tools.langText("District_Null_Y"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("Z") || MyPlugin.locations.locationsFile.get(district + ".Z") == null) {
                    player.sendMessage(Tools.langText("District_Null_Z"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("Yaw") || MyPlugin.locations.locationsFile.get(district + ".Yaw") == null) {
                    player.sendMessage(Tools.langText("District_Null_Yaw"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("Pitch") || MyPlugin.locations.locationsFile.get(district + ".Pitch") == null) {
                    player.sendMessage(Tools.langText("District_Null_Pitch"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }
                if (!MyPlugin.locations.locationsFile.getConfigurationSection(district).contains("World") || MyPlugin.locations.locationsFile.get(district + ".World") == null) {
                    player.sendMessage(Tools.langText("District_Null_World"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.5F);
                    return false;
                }


                double x = MyPlugin.locations.locationsFile.getDouble(district + ".X");
                double y = MyPlugin.locations.locationsFile.getDouble(district + ".Y");
                double z = MyPlugin.locations.locationsFile.getDouble(district + ".Z");
                float yaw = MyPlugin.locations.locationsFile.getLong(district + ".Yaw");
                float pitch1 = MyPlugin.locations.locationsFile.getLong(district + ".Pitch");
                World world = Bukkit.getServer().getWorld(MyPlugin.locations.locationsFile.getString(district + ".World"));

                assert player != null;
                player.teleport(new Location(world, x, y, z, yaw, pitch1));
                List<String> messages = MyPlugin.locations.locationsFile.getStringList(district + ".Messages");
                for (String message : messages) {
                    player.sendMessage(Tools.colorMSG(message));
                }
                String soundStringFormat = MyPlugin.locations.locationsFile.getString(district + ".Sound");
                String[] soundStringFormatSplit = soundStringFormat.split(" ");
                Sound sound = Sound.valueOf(soundStringFormatSplit[0]);

                int pitch = Integer.parseInt(soundStringFormatSplit[1]);
                if (pitch > 0 && pitch <= 5) {
                    player.playSound(player.getLocation(), sound, 100, 0.1F);
                } else if (pitch > 0 && pitch <= 10) {
                    player.playSound(player.getLocation(), sound, 100, 0.2F);
                } else if (pitch > 0 && pitch <= 15) {
                    player.playSound(player.getLocation(), sound, 100, 0.3F);
                } else if (pitch > 0 && pitch <= 20) {
                    player.playSound(player.getLocation(), sound, 100, 0.4F);
                } else if (pitch > 0 && pitch <= 25) {
                    player.playSound(player.getLocation(), sound, 100, 0.5F);
                } else if (pitch > 0 && pitch <= 30) {
                    player.playSound(player.getLocation(), sound, 100, 0.6F);
                } else if (pitch > 0 && pitch <= 35) {
                    player.playSound(player.getLocation(), sound, 100, 0.7F);
                } else if (pitch > 0 && pitch <= 40) {
                    player.playSound(player.getLocation(), sound, 100, 0.8F);
                } else if (pitch > 0 && pitch <= 45) {
                    player.playSound(player.getLocation(), sound, 100, 0.9F);
                } else if (pitch > 0 && pitch <= 50) {
                    player.playSound(player.getLocation(), sound, 100, 1.0F);
                } else if (pitch > 0 && pitch <= 55) {
                    player.playSound(player.getLocation(), sound, 100, 1.1F);
                } else if (pitch > 0 && pitch <= 60) {
                    player.playSound(player.getLocation(), sound, 100, 1.2F);
                } else if (pitch > 0 && pitch <= 65) {
                    player.playSound(player.getLocation(), sound, 100, 1.3F);
                } else if (pitch > 0 && pitch <= 70) {
                    player.playSound(player.getLocation(), sound, 100, 1.4F);
                } else if (pitch > 0 && pitch <= 75) {
                    player.playSound(player.getLocation(), sound, 100, 1.5F);
                } else if (pitch > 0 && pitch <= 80) {
                    player.playSound(player.getLocation(), sound, 100, 1.6F);
                } else if (pitch > 0 && pitch <= 85) {
                    player.playSound(player.getLocation(), sound, 100, 1.7F);
                } else if (pitch > 0 && pitch <= 90) {
                    player.playSound(player.getLocation(), sound, 100, 1.8F);
                } else if (pitch > 0 && pitch <= 95) {
                    player.playSound(player.getLocation(), sound, 100, 1.9F);
                } else {
                    if (pitch > 0 && pitch <= 100) {
                        player.playSound(player.getLocation(), sound, 100, 2.0F);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return true;
    }
}
