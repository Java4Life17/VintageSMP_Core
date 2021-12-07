package myplugin.myplugin.Clan_System;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.rmi.MarshalledObject;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Arena {

    private final String arenaName;
    private int arenaTime;
    private final Clan team1;
    private final Clan team2;
    private int team1_points;
    private int team2_points;
    private Clan lead;
    private final Map<Player, Location> locationMap = new HashMap<>();
    boolean pvpEnabled = false;
    private Location spawn1;
    private Location spawn2;
    private AtomicInteger seconds = new AtomicInteger(0);
    private BossBar bossBar;
    private String name1;
    private String name2;

    public Arena(String arenaName, Clan team1, Clan team2) {
        team1.getOnlineMembers().forEach(player -> locationMap.put(player, player.getLocation()));
        team2.getOnlineMembers().forEach(player -> locationMap.put(player, player.getLocation()));
        this.arenaName = arenaName;
        this.team1 = team1;
        this.team2 = team2;
        team1_points = 0;
        team2_points = 0;
        lead = team1;
        MyPlugin.clan_system.getActiveArenas().add(this);
        try {
            getArenaTime(arenaName);
        } catch (Exception e) {
            exitWithError("No se pudo cargar los minutos de la arena por alguna razon.", e.toString());
            MyPlugin.clan_system.getActiveArenas().remove(this);
            return;
        }

        this.name1 = team1.getClanName();
        this.name2 = team2.getClanName();

        setUpArena();

    }


    private void setUpArena() {
        MyPlugin.clan_system.arenas.set(arenaName  + ".InUse", true);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                   TRY TO TELEPORT TO THE WAITING LOBBY
        String waitingTP = waitingTP();
        if (!waitingTP.equalsIgnoreCase("Successful")) {
            exitWithError("Encontramos un error al intentar teletransportar a los miembros " +
                    "del clan a la arena de combate. Nombre de la arena: Arena " + arenaName + ". Terminaremos esta arena.", waitingTP);
            return;
        }

        iniciarConteon();


        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                  START COUNTDOWN BEFORE THE MATCH STARTS

    }

    private void iniciarConteon() {
        //Crear un ejecutador de servicios para que corra el conteo en otro thread.
        ExecutorService ejecutador = Executors.newFixedThreadPool(2);
        // Tarea que iniciara el conteo de la batalla.
        Callable<Boolean> conteoConExito = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                try {
                    // segundos de conteo
                    int segundos = 20;

                    //Loop para que cuente usando Thread.sleep() para un segundo de delay.
                    while (segundos != 0) {
                        if (team1.getOnlineCount() == 0) {
                            Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                                sendMessageToClanMembers("&cLos miembros del clan &7" + team1.getClanName() + " &cabandonaron la batalla. " +
                                        "Tu clan es el ganador");
                                team2.getOnlineMembers().forEach(player -> takeBackPlayer(player));
                                winEventLeft(team1 ,2);

                            });
                            return false;
                        }
                        if (team2.getOnlineCount() == 0) {
                            Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                                sendMessageToClanMembers("&cLos miembros del clan &7" + team2.getClanName() + " &cabandonaron la batalla. " +
                                        "Tu clan es el ganador");
                                team1.getOnlineMembers().forEach(player -> takeBackPlayer(player));
                                winEventLeft(team2, 1);
                            });
                            return false;
                        }

                        if (segundos == 20)
                            sendMessageToClanMembers("&aLa batalla inicia en &7" + segundos + " &asegundos...");
                        if (segundos == 15)
                            sendMessageToClanMembers("&aLa batalla inicia en &7" + segundos + " &asegundos...");
                        if (segundos == 10)
                            sendMessageToClanMembers("&aLa batalla inicia en &7" + segundos + " &asegundos...");
                        if (segundos == 5) sendMessageToClanMembers("&aIniciando en &7" + segundos + "...");
                        if (segundos == 4) sendMessageToClanMembers("&aIniciando en &7" + segundos + "...");
                        if (segundos == 3) sendMessageToClanMembers("&aIniciando en &7" + segundos + "...");
                        if (segundos == 2) sendMessageToClanMembers("&aIniciando en &7" + segundos + "...");
                        if (segundos == 1) sendMessageToClanMembers("&aIniciando en &7" + segundos + "...");

                        Thread.sleep(1000);
                        segundos--;
                    }

                /*
                Cuando el conteo llegue a 0, los miembros seran teletransportados a la arena para iniciar la partida.
                 */

                    sendMessageToClanMembers("&bLa batalla ha iniciado!");
                    pvpEnabled = true;
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> teleportMembersToSpawn());
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        };

        Future<Boolean> funciono = ejecutador.submit(conteoConExito);

        ejecutador.submit(() -> {
            try {
                if (funciono.get()) {
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), this::startArena);
                    ejecutador.shutdown();
                }
            } catch (
                    Exception e) {
                Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                    sendMessageToClanMembers("&cOcurrio un error al iniciar la batalla... ");
                    takeBackPlayer(team1);
                    takeBackPlayer(team2);
                });

                exitWithError("ocurrio un error mientras iniciabamos la arena " + arenaName, ExceptionUtils.getMessage(e));
            }
        });


    }

    private void startArena() {
        seconds = new AtomicInteger(60 * 2);
        bossBar = Bukkit.createBossBar(Tools.colorMSG("&b&l" + secondsToMinutes(seconds.get())), BarColor.WHITE, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
        bossBar.removeFlag(BarFlag.CREATE_FOG);
        team1.getOnlineMembers().forEach(player -> {
            bossBar.addPlayer(player);
            MyPlugin.system.getByName(player.getName()).deletePet();
        });
        team2.getOnlineMembers().forEach(player -> {
            bossBar.addPlayer(player);
            MyPlugin.system.getByName(player.getName()).deletePet();
        });
        new BukkitRunnable() {
            @Override
            public void run() {

                if (team1.getOnlineCount() == 0) {
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        sendMessageToClanMembers("&cLos miembros del clan &7" + team1.getClanName() + " &cabandonaron la batalla. " +
                                "Tu clan es el ganador");
                        team2.getOnlineMembers().forEach(player -> takeBackPlayer(player));
                        winEventLeft(team1, 2);
                    });
                    bossBar.removeAll();
                    this.cancel();
                    return;
                }
                if (team2.getOnlineCount() == 0) {
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        sendMessageToClanMembers("&cLos miembros del clan &7" + team2.getClanName() + " &cabandonaron la batalla. " +
                                "Tu clan es el ganador");
                        team1.getOnlineMembers().forEach(player -> takeBackPlayer(player));
                        winEventLeft(team2, 1);
                    });
                    bossBar.removeAll();
                    this.cancel();
                    return;
                }

                Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                    bossBar.setTitle(Tools.colorMSG("&b" + secondsToMinutes(seconds.getAndDecrement())));
                    sendPlayersLeadAB();
                });

                if (seconds.get() == 0) {
                    this.cancel();
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> {
                        winEvent(getLeadingClan());
                        bossBar.removeAll();
                    });
                }

            }
        }.runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), 0, 20);
    }

    private void winEvent(@Nullable Clan leadingClan) {

        if (leadingClan == null) {
            team1.getOnlineMembers().forEach(this::takeBackPlayer);
            team2.getOnlineMembers().forEach(this::takeBackPlayer);
            sendMessageToClanMembers("&6x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            sendMessageToClanMembers(" ");
            sendMessageToClanMembers("&aLos clanes empataron y no hay hubo ningun ganador!");
            sendMessageToClanMembers(" ");
            sendMessageToClanMembers("&6x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
        } else {
            int random = getBiggestPoints() * 2;
            team1.getOnlineMembers().forEach(this::takeBackPlayer);
            team2.getOnlineMembers().forEach(this::takeBackPlayer);
            sendMessageToClanMembers("&6x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            sendMessageToClanMembers(" ");
            sendMessageToClanMembers("&dClan Ganador: &e" + leadingClan.getClanName() + " &7[&a" + getPointsForTeam(leadingClan) + "&7 kills]");
            if (getLoosingClan() == null) {
                sendMessageToClanMembers("&dClan Perdedor: &e" + getOppositeTeam(leadingClan) + " &7[&a" + getPointsForTeam(Objects.requireNonNull(getOpposite(leadingClan))) + "&7 kills]");
            }else {
                sendMessageToClanMembers("&dClan Perdedor: &e" + getLoosingClan().getClanName() + " &7[&a" + getPointsForTeam(getLoosingClan()) + "&7 kills]");
            }
            sendMessageToClanMembers("&dNiveles Ganados: &e " + random);
            sendMessageToClanMembers(" ");
            sendMessageToClanMembers("&6x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            leadingClan.setClanLevel(leadingClan.getClanLevel() + random);
            getLoosingClan().setClanLevel(getLoosingClan().getClanLevel() - random);

            if(getLeadingClan() != null){
            getLeadingClan().getOnlineMembers().forEach(player -> {
                Tools.message(player, "&aGanaste la batalla contra el clan &c" + getLoosingClan().getClanName() + " &ay se le dieron &c" +
                        random + " &apuntos a tu clan.");
            });
        }
            if(getLoosingClan() != null) {
                getLoosingClan().getOnlineMembers().forEach(player -> {
                    Tools.message(player, "&cPerdiste la batalla contra el clan &7" + leadingClan.getClanName() + " &cy se le quitaron &7" +
                            random + " &cpuntos a tu clan.");
                });
            }
        }
        MyPlugin.clan_system.arenas.set(arenaName  + ".InUse", false);
        MyPlugin.clan_system.getActiveArenas().remove(this);
    }

    public void winEventLeft(Clan winner, int numberOfOpposite){
        int random = getBiggestPoints() * 2;
        for(Player player : winner.getOnlineMembers()){
            Tools.message(player, "&aGanaste la batalla y se le dieron &c" +
                    random + " &apuntos a tu clan.");
            winner.setClanLevel(random);
        }

        String name = numberOfOpposite == 1 ? name1 : name2;
        MyPlugin.clan_system.manager().getOfflineClan(name).setClanLevel(MyPlugin.clan_system.manager().getOfflineClan(name).getClanLevel() - random);
        MyPlugin.clan_system.arenas.set(arenaName  + ".InUse", false);
        MyPlugin.clan_system.getActiveArenas().remove(this);
    }

    private int getPointsForTeam(Clan clan) {
        return clan.equals(team1) ? team1_points : team2_points;
    }

    private Clan getOppositeTeam(Clan lead) {
        return team1.equals(lead) ? team2 : team1;
    }

    private String secondsToMinutes(int seconds) {

        int secondsLeft;

        secondsLeft = seconds % 60;
        int minutes = seconds / 60;

        String temp = String.format("%02d", secondsLeft);

        return minutes + ":" + temp;

    }


    private void teleportMembersToSpawn() {
        Location loc1 = Tools.playerLocationFromString(MyPlugin.clan_system.arenas.getString(arenaName + ".Team_One_Spawn"));
        spawn1 = loc1;
        team1.getOnlineMembers().forEach(player -> {
            player.teleport(loc1);
        });
        Location loc2 = Tools.playerLocationFromString(MyPlugin.clan_system.arenas.getString(arenaName + ".Team_Two_Spawn"));
        spawn2 = loc2;
        team2.getOnlineMembers().forEach(player -> {
            player.teleport(loc2);
        });
    }


    private String waitingTP() {
        try {
            Location waitingLobby = Tools.playerLocationFromString(MyPlugin.clan_system.arenas.getString(arenaName + ".Team_Waiting_Lobby"));
            team1.getOnlineMembers().forEach(player -> {
                Tools.message(player, " ");
                Tools.message(player, "#4989adFuiste teletransportado al lobby de espera");
                Tools.message(player, " ");
                player.teleport(waitingLobby);
            });
            team2.getOnlineMembers().forEach(player -> {
                Tools.message(player, " ");
                Tools.message(player, "#4989adFuiste teletransportado al lobby de espera");
                Tools.message(player, " ");
                player.teleport(waitingLobby);
            });
        } catch (Exception e) {
            return e.toString();
        }
        return "Successful";
    }

    public void exitWithError(String description, String error) {
        MyPlugin.clan_system.arenas.set(arenaName  + ".InUse", false);
        MyPlugin.clan_system.getActiveArenas().remove(this);
        MyPlugin.getSimpleLogger().error(error, description);
        team1.getOnlineMembers().forEach(player -> {
            Tools.message(player, " ");
            Tools.message(player, "&cOcurrio un error interno durante la batalla.");
            Tools.message(player, " ");
        });
        team2.getOnlineMembers().forEach(player -> {
            Tools.message(player, " ");
            Tools.message(player, "#&cOcurrio un error interno durante la batalla.");
            Tools.message(player, " ");
        });
    }


    public Map<Player, Location> getLocationMap() {
        return locationMap;
    }

    private void getArenaTime(String arenaName) {
        if (MyPlugin.clan_system.arenas.getConfigurationSection(arenaName).contains("Time")) {
            this.arenaTime = MyPlugin.clan_system.arenas.getInt(arenaName + ".Time");
        }
    }

    private void sendMessageToClanMembers(String msg) {
        team1.getOnlineMembers().forEach(player -> {
            if (player == null) {
                return;
            }
            Tools.message(player, msg);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 100, 0.8F);
        });
        team2.getOnlineMembers().forEach(player -> {
            if (player == null) {
                return;
            }
            Tools.message(player, msg);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 100, 0.8F);
        });
    }

    public void takeBackPlayer(Player player) {
        if (player == null) return;
        if (!getLocationMap().containsKey(player)) {
            Tools.message(player, "&cNo tienes ubicacion a cual regresarte...");
            player.teleport(new Location(Bukkit.getServer().getWorld("VintageSMP"), 309.00000, 64.00000, 209.00000));
        }
        player.teleport(getLocationMap().get(player));
    }

    public void takeBackPlayer(Clan clan) {
        clan.getOnlineMembers().forEach(player -> player.teleport(getLocationMap().get(player)));
    }

    public boolean checkForPlayer(Player player) {
        if (team1.getOnlineMembers().contains(player)) return true;
        return team2.getOnlineMembers().contains(player);
    }

    private Clan getLeadingClan() {
        if (team1_points > team2_points) return team1;
        if (team2_points > team1_points) return team2;
        return null;
    }

    private void sendPlayersLeadAB() {
        StringBuilder builder = new StringBuilder();

        builder.append("&f&l[&4&l&k|||&r ").append("#7775bf&l").append(team1.getClanName()).append("&8: &a").append(team1_points).append("             ")
                .append("#7775bf&l").append(team2.getClanName()).append("&f: &a").append(team2_points).append(" &4&l&k|||&r&f&l]");
        for (Player player : team1.getOnlineMembers()) {
            if (player == null) return;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Tools.hexColorMSG(builder.toString())));
        }
        for (Player player : team2.getOnlineMembers()) {
            if (player == null) return;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Tools.hexColorMSG(builder.toString())));
        }
    }

    public boolean playerIsInArena(Player player) {
        if (team1.getOnlineMembers().contains(player)) return true;
        return team2.getOnlineMembers().contains(player);
    }

    public void increasePoints(Player player) {
        if (player == null) {
            return;
        }
        if (team1.getOnlineMembers().contains(player)) team1_points++;
        if (team2.getOnlineMembers().contains(player)) team2_points++;
    }

    public Location getPlayerSpawn(Player player) {
        if (player == null) return null;
        if (team1.getOnlineMembers().contains(player)) return spawn1;
        if (team2.getOnlineMembers().contains(player)) return spawn2;
        return null;
    }

    private Clan getLoosingClan() {
        if (team1_points > team2_points) return team2;
        if (team2_points > team1_points) return team1;
        return null;
    }

    public int getSecondsLeft() {
        return seconds.get();
    }

    public int getBiggestPoints() {
        return Math.max(team1_points, team2_points);
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public Clan getOpposite(Clan clan) {
        if(clan.equals(team1)) return team1;
        if(clan.equals(team2)) return team2;
        return null;
    }

}