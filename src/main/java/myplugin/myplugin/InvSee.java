package myplugin.myplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvSee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("inventario")) {
            Player player = (Player) sender;
            if(!player.hasPermission("Inventario.Abrir")){
                player.sendMessage(Tools.colorMSG("&cSin permisos!"));
                return false;
            }

            try {
                String name = args[0];
                if(Bukkit.getPlayer(name) == null){
                    player.sendMessage(Tools.langText("playerNotFound"));
                    return false;
                }

                player.openInventory(getSpyInvOf(name));


            }catch (Exception e){
                e.printStackTrace();
                player.sendMessage(Tools.colorMSG("&cUso: /inventario <jugador>"));
            }

        }

        return false;
    }

    private Inventory getSpyInvOf(String name) {

        Inventory inventory = Bukkit.createInventory(null, 54 , Tools.colorMSG("&dMenu Espia de &4" + name));
        Player player = Bukkit.getPlayer(name);
        for(int i  = 0; i < Objects.requireNonNull(player).getInventory().getSize(); i++){
            if(player.getInventory().getItem(i) != null)
            inventory.addItem(player.getInventory().getItem(i));
        }

        return inventory;
    }
}
