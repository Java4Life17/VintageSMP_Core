package myplugin.myplugin.AdminSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class Pet_Inventories implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        //petstorages <jugador>

        if (command.getName().equalsIgnoreCase("petStorages")) {
            Player player = (Player) sender;
            try {

                if (!player.hasPermission("MyPlugin.PetStorages")) {
                    player.sendMessage(Tools.langText("noPerm"));
                    return false;
                }

                String target = args[0];

                if(Bukkit.getPlayer(target) == null){
                    player.sendMessage(Tools.langText("playerNotFound"));
                    return false;
                }

                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 100, 0.3F);
                player.openInventory(getPetStoragesFor(Objects.requireNonNull(Bukkit.getPlayer(target))));

            }catch (Exception e){
                player.sendMessage(Tools.colorMSG("&c&lUso &f-> &7/ps <jugador>"));
            }
        }
        return true;
    }

    private Inventory getPetStoragesFor(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, Tools.hexColorMSG("#174f2dAlmacenamientos de &c" + player.getName()));
        for(Map.Entry<Integer, Inventory> entry : MyPlugin.system.getByName(player.getName()).getPetStorage().getRawStorages().entrySet()){
            ItemStack stack = new ItemStack(Material.CHEST_MINECART);
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            meta.setDisplayName(Tools.hexColorMSG("#a3e0f0Almacenamiento " + entry.getKey()));
            stack.setItemMeta(meta);
            inventory.addItem(stack);
        }

        for(int i = 0; i < 9; i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, displayPane());
            }
        }

        return inventory;
    }

    private ItemStack displayPane() {
        ItemStack display = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta = display.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        display.setItemMeta(meta);
        return display;
    }
}
