package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collectors;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (command.getName().equalsIgnoreCase("openCM")) {
            if (!(sender instanceof ConsoleCommandSender)) {
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                return false;
            }

            target.openInventory(MyPlugin.clan_system.getClanToolsMenu());

        }

        if (command.getName().equalsIgnoreCase("clans")) {
            Player player = (Player) sender;
            try {
                if (args[0].equalsIgnoreCase("invitar")) {
                    String target = args[1];
                    if (Bukkit.getPlayer(target) == null) {
                        player.sendMessage(Tools.langText("playerNotFound"));
                        return false;
                    }
                    if (target.equals(player.getName())) {
                        player.sendMessage(Tools.langText("clanInviteSelf"));
                        return false;
                    }

                    if (MyPlugin.clan_system.manager().getPlayerClan(Bukkit.getPlayer(target)) != null) {
                        player.sendMessage(Tools.langText("targetInAClan"));
                        return false;
                    }

                    TextComponent msg = new TextComponent(Tools.colorMSG("&bEl jugador &d" + player.getName() + "&b te invito al clan " +
                            MyPlugin.clan_system.manager().getPlayerClan(player).getClanName() + " &b. Que quieres hacer?"));
                    TextComponent accept = new TextComponent(Tools.colorMSG(" &8&l[&a&lACCEPTAR&8&l]"));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans aceptar"));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.hexColorMSG("&aClic para acceptar"))));

                    TextComponent deny = new TextComponent(Tools.colorMSG(" &8&l[&4&lRECHAZAR&8&l]"));
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clans rechazar"));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Tools.hexColorMSG("&cClic para rechazar"))));

                    ComponentBuilder builder = new ComponentBuilder();
                    builder.append(msg).append(accept).append(deny);

                    Objects.requireNonNull(Bukkit.getPlayer(target)).spigot().sendMessage(builder.create());
                    MyPlugin.clan_system.invitePending.put(Bukkit.getPlayer(target), MyPlugin.clan_system.manager().getPlayerClan(player));


                }

                if (args[0].equalsIgnoreCase("aceptar")) {
                    if (MyPlugin.clan_system.invitePending.containsKey(player)) {
                        MyPlugin.clan_system.invitePending.get(player).addMember(player.getName());
                        MyPlugin.clan_system.invitePending.remove(player);
                    }
                }
                if (args[0].equalsIgnoreCase("rechazar")) {
                    if (MyPlugin.clan_system.invitePending.containsKey(player)) {
                        String toSend = Tools.langText("deniedClan");
                        toSend = toSend.replace("%player%", player.getName());
                        if (Bukkit.getPlayer(MyPlugin.clan_system.invitePending.get(player).getClanOwner()) != null) {
                            Objects.requireNonNull(Bukkit.getPlayer(MyPlugin.clan_system.invitePending.get(player).getClanOwner())).sendMessage(toSend);
                        }
                        MyPlugin.clan_system.invitePending.remove(player);
                    }
                }
                if (args[0].equalsIgnoreCase("fight")) {
                    if (args[1].equalsIgnoreCase("accept")) {
                        if (MyPlugin.clan_system.getFightRequest().containsKey(player)) {
                            if (MyPlugin.clan_system.getNextArena().equalsIgnoreCase("NULL")) {
                                MyPlugin.clan_system.getFightRequest().get(player).sendMessage(Tools.colorMSG("&cNo hay ninguna arena disponible. Espera a " +
                                        "que se desocupe una."));
                                player.sendMessage(Tools.colorMSG("&cNo hay ninguna arena disponible. Espera a " +
                                        "que se desocupe una."));
                                return false;
                            }


                            if(!MyPlugin.clan_system.getFightRequest().containsKey(player)){
                                player.sendMessage("&cEl lider de ese clan ya no esta en linea o cancelo la peticion de guerra,");
                                return false;
                            }
                            if(MyPlugin.clan_system.getFightRequest().get(player) == null){
                                player.sendMessage("&cEl lider de ese clan ya no esta en linea o cancelo la peticion de guerra,");
                                return false;
                            }

                            new Arena(MyPlugin.clan_system.getNextArena(), MyPlugin.clan_system.manager().getPlayerClan(player),
                                    MyPlugin.clan_system.manager().getPlayerClan(MyPlugin.clan_system.getFightRequest().get(player)));
                            MyPlugin.clan_system.getFightRequest().get(player).sendMessage(Tools.colorMSG("&aEl jugador &c" + player.getName() + " &aacepto tu peticion a pelear."));
                            player.sendMessage(Tools.colorMSG("&aAceptaste la peticion de pelear."));
                        }

                        MyPlugin.clan_system.getPending().remove(player);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("deny")) {
                        if (MyPlugin.clan_system.getPending().get(player) == null) {
                            player.sendMessage(Tools.colorMSG("&cParese que el lider de este clan ya no esta en linea."));
                        }

                        MyPlugin.clan_system.getFightRequest().get(player).sendMessage(Tools.colorMSG("&cEl jugador &7" + player.getName() + " &crechazo tu peticion para pelear."));
                        player.sendMessage(Tools.colorMSG("&cRechazaste la peticion para una guerra de clanes."));
                        MyPlugin.clan_system.getPending().remove(player);
                        return true;
                    }


                } if(args[0].equalsIgnoreCase("cancel")){
                    if(!MyPlugin.clan_system.getFightRequest().containsValue(player)) return false;
                    Tools.message(player, "&cLa peticion de pelea de clanes fue cancelada.");
                    MyPlugin.clan_system.manager().getPlayerClan(player).getOnlineMembers().forEach(member -> {
                        if(!member.equals(player))
                        Tools.message(member, "&cEl lider de tu clan cancelo la peticion de pelea.");
                    });
                    Bukkit.getLogger().info("Worked canceled");
                    MyPlugin.clan_system.getFightRequest().remove(MyPlugin.clan_system.getFightRequest().
                            keySet().stream().filter(target -> (MyPlugin.clan_system.getFightRequest().get(target).equals(player))).collect(Collectors.toList()).get(0));
                    return true;

                }


            } catch (Exception e) {
                player.sendMessage(Tools.colorMSG("&4&lUso -> &7/clans invitar <jugador>"));
            }
        }
        if (command.getName().equalsIgnoreCase("clanEditor")) {
            Player player = (Player) sender;

            if (!player.hasPermission("clanEditor.Editor")) return false;
            try {
                if (args[0].equalsIgnoreCase("edit")) {
                    String arenaName = args[1];

                    if (!MyPlugin.clan_system.arenas.contains(arenaName)) {
                        player.sendMessage("&cLa arena " + arenaName + " no existe!");
                        return false;
                    }

                    player.openInventory(getEditor(arenaName));
                } if(args[0].equalsIgnoreCase("new")){
                    String arenaName = args[1];
                    if(MyPlugin.clan_system.arenas.getKeys(false).stream().toList().contains(arenaName)){
                        player.sendMessage(Tools.colorMSG("&cEsa arena ya existe"));
                        return false;
                    }

                    MyPlugin.clan_system.arenas.createSection(arenaName);
                    MyPlugin.clan_system.arenas.save();

                }
            } catch (Exception exception) {
                Tools.message(player, "&c&lUso -> &7clanEditor eidt/new (Arena Name)");
            }
        }


      /*  if(command.getName().equalsIgnoreCase("fight")){
            Player player = (Player) sender;
            if(!MyPlugin.clan_system.manager().getPlayerClan(player).getClanOwner().equals(player.getName())){
               Tools.message(player, "&cTienes que ser el linder del clan para hacer esto...");
               return false;
            }
            try {
             String clanName = args[0];

             AtomicBoolean successful = new AtomicBoolean(true);
             MyPlugin.clan_system.getLoadedClans().forEach(clan -> {
                 if(MyPlugin.clan_system.manager().getPlayerClan(player).equals(clan)){
                     Tools.message(player, "&cNo puedes desafiar a tu propio clan.");
                     successful.set(false);
                 }
             });
             if(!successful.get()) return false;
             if(MyPlugin.clan_system.manager().getExistingClans().contains(clanName)){
                 player.sendMessage("&cNo existe ningun clan con el nombre &l" + clanName);
                 return false;
             }

             if(!MyPlugin.clan_system.manager().getOnlineClans().contains(clanName)){
                 player.sendMessage("&cEl lider de este clan no esta conectado! Solo el lider puede aceptar el reto.");
                 return false;
             }
             if(MyPlugin.clan_system.manager().getClanByName(clanName).getClanLevel() < 1){
                 player.sendMessage("&cEl nivel de este clan es muy bajo para una batalla. Su nivel tien " +
                         "que ser mayor de 1.");
                 return false;
             }



            }catch (Exception e){
                Tools.message(player, "&c&lUso -> &7/fight <nombreDeClan>");
            }

        }
*/
        return false;
    }

    private Inventory getEditor(String arenaName) {
        Inventory inventory = Bukkit.createInventory(null, 27, Tools.colorMSG("&0Editor: " + arenaName));

        ItemStack spawn1 = new ItemStack(Material.GOLDEN_SWORD);
        ItemStack spawn2 = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta spawn1Meta = spawn1.getItemMeta();
        ItemMeta spawn2Meta = spawn2.getItemMeta();
        assert spawn1Meta != null;
        spawn1Meta.setDisplayName(Tools.colorMSG("&aSpawn Team 1"));
        assert spawn2Meta != null;
        spawn2Meta.setDisplayName(Tools.colorMSG("&aSpawn Team 2"));
        spawn1.setItemMeta(spawn1Meta);
        spawn2.setItemMeta(spawn2Meta);

        ItemStack waiting = new ItemStack(Material.CLOCK);
        ItemMeta waitingMeta = waiting.getItemMeta();
        assert waitingMeta != null;
        waitingMeta.setDisplayName(Tools.colorMSG("&cSpawn Waiting Lobby"));
        waiting.setItemMeta(waitingMeta);

        inventory.setItem(10, spawn1);
        inventory.setItem(13, spawn2);
        inventory.setItem(16, waiting);

        return inventory;
    }
}
