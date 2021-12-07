package myplugin.myplugin.Server_Selling;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class On_ConfirmationMenu_Listener implements Listener {

    @EventHandler
    public void onConfirmationListener(InventoryClickEvent e){
        if(e.getView().getTitle().startsWith(Tools.colorMSG("&0&l&oCONFIRMA TU VENTA"))){
            e.setCancelled(true);

            Player player = (Player) e.getWhoClicked();

            if(e.getSlot() == 14){
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.BLOCK_CANDLE_EXTINGUISH, 100, 0.5F);
                return;
            }
            if(e.getSlot() == 12){

                if(player.getInventory().getItemInMainHand().getItemMeta() != null && player.getInventory().getItemInMainHand().getItemMeta().hasEnchants()){
                    player.sendMessage(Tools.langText("No_Enchants_Accepted"));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 100, 0.6F);
                    player.closeInventory();
                    return;
                }

                int amount = player.getInventory().getItemInMainHand().getAmount();
                int total_coins = MyPlugin.server_selling.items_and_prices.get(player.getInventory().getItemInMainHand().getType()) * amount;
                Tools.addPlayerCoins(player, total_coins);
                player.playSound(player.getLocation(), Sound.BLOCK_CANDLE_EXTINGUISH, 100, 1.6F);
                player.closeInventory();
                player.getInventory().getItemInMainHand().setAmount(0);
                player.sendTitle(Tools.colorMSG("&2&l+ " + total_coins + " monedas"), Tools.colorMSG("&b&o&n________________"), 20, 20, 20);
                player.spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 5);
                player.spawnParticle(Particle.TOTEM, player.getLocation(), 5);
            }
        }

    }

}
