package myplugin.myplugin;

import me.TechsCode.UltraPermissions.UltraPermissions;
import me.TechsCode.UltraPermissions.storage.objects.Group;
import me.TechsCode.UltraPermissions.storage.objects.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "MyPlugin";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Jdaniel";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(@Nullable OfflinePlayer player, @NotNull String params) {

        if(player == null)
            return null;
        if(params.equals("hexColors")){
            final String[] toReturn = new String[1];

            for(User user : UltraPermissions.getAPI().getUsers()){
                if(user.getName().equals(player.getName())){
                    user.getGroups().stream().toList().get(0).get().flatMap(Group::getPrefix).ifPresent(prefix -> toReturn[0] = prefix);
                    break;
                }
            }
            return Tools.hexColorMSG(toReturn[0]);
        }else if(params.equals("hearts")){
            return String.valueOf(Tools.getPlayerHearts((Player) player) / 2);
        }else if(params.equals("clan")){
            if(MyPlugin.clan_system.manager().getPlayerClan((Player) player) == null){
                return Tools.hexColorMSG("#ff0522Sin Clan");
            }else{
                String clanName = MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanName();
                int clanLevel = MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel();

                String color = getClanColor((Player) player);
                return Tools.hexColorMSG(color + clanName + " #7be3a6" + clanLevel);

            }
        }
       return null;
    }

    private String getClanColor(Player player) {

        String color = "#a600ff";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 0 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 9)
            color = "#807c7c";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 10 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 19)
            color = "#1ce6a6";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 20 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 29)
            color = "#59729e";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 30 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 39)
            color = "#aa60d1";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 40 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 49)
            color = "#686b40";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 50 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 59)
            color = "#996a48";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 60 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 69)
            color = "#cf6317";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 70 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 79)
            color = "#05a846";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 80 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 89)
            color = "#061e45";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 90 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 99)
            color = "#eb5252";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 100 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 109)
            color = "#b52626";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 110 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 119)
            color = "#4a0000";
        if(MyPlugin.clan_system.manager().getPlayerClan((Player) player).getClanLevel() >= 120 && MyPlugin.clan_system
                .manager().getPlayerClan((Player) player).getClanLevel() <= 129)
            color = "#fbff00&l";

        return color;

    }
}
