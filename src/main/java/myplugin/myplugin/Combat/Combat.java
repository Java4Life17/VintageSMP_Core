package myplugin.myplugin.Combat;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Combat {


    private HashMap<Player, Integer> _combatCoolDown_;

    /*
    Gonna take care to initialize the whole system
     */
    public void start() {
        _combatCoolDown_ = new HashMap<>();
        startCooldownTask();
        MyPlugin.getMyPlugin().getServer().getPluginManager().registerEvents(new onQuit(), MyPlugin.getMyPlugin());
    }

    /*
    This method is going to run every second.
    It will check if the list _combatCooldown_ has any values.
    If it does, it will start  reducing a second from it.
     */
    private void startCooldownTask() {

        Bukkit.getScheduler().runTaskTimerAsynchronously(MyPlugin.getMyPlugin(), () -> {

            if (!_combatCoolDown_.isEmpty()) {
                HashMap<Player, Integer> finalOne = new HashMap<>();
                for (Map.Entry<Player, Integer> entry : _combatCoolDown_.entrySet()) {
                    if((entry.getValue() - 1) > 0){
                        finalOne.put(entry.getKey(), entry.getValue() - 1);
                    }else{
                        entry.getKey().sendMessage(Tools.colorMSG("&aSaliste del modo combate."));
                    }
                    _combatCoolDown_ = finalOne;

                }
            }

        }, 0, 20);

    }

    public HashMap<Player, Integer> getCombatCoolDown(){
        return _combatCoolDown_;
    }


}
