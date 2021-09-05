package myplugin.myplugin.PlayerStaticItem;

import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class StaticItem_Events implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (!Objects.equals(event.getPlayer().getInventory().getItem(8), Tools.getStaticItem()) || event.getPlayer().getInventory().getItem(8) !=
                Tools.getStaticItem() || event.getPlayer().getInventory().getItem(8) == null) {
            event.getPlayer().getInventory().setItem(8, Tools.getStaticItem());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == Tools.getStaticItem() || Objects.equals(event.getCurrentItem(), Tools.getStaticItem())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(Objects.equals(event.getItem(), Tools.getStaticItem()) || event.getItem() == Tools.getStaticItem()){
            Inventory inventory = Bukkit.createInventory(null, 27, Tools.colorMSG("&bMenu De Jugador"));
            event.getPlayer().openInventory(inventory);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(Tools.getStaticItem()) || event.getItemDrop().getItemStack() == Tools.getStaticItem()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.getDrops().removeIf(stack -> stack.equals(Tools.getStaticItem()) || stack == Tools.getStaticItem());
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event){
        event.getPlayer().getInventory().setItem(8, Tools.getStaticItem());
    }


}
