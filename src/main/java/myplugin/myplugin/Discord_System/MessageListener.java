package myplugin.myplugin.Discord_System;


import myplugin.myplugin.IpControl.IpControlTools;
import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        //lista-online
        if (event.getChannel().getId().equals("891118493263601664")) {

            StringBuilder stringBuilder = new StringBuilder();
            if (Bukkit.getOnlinePlayers().size() == 0) {
                stringBuilder.append("*No hay jugadores conectados.*");
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    stringBuilder.append("🙍‍ ").append(player.getName()).append("\n");
                }
            }

            event.getMessage().delete().queue();
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**");
            builder.setColor(Color.green);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
            builder.addBlankField(false);
            builder.addField("***Jugadores Online***", stringBuilder.toString(), false);
            builder.addBlankField(false);
            event.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessageEmbeds(builder.build()).queue();
            });
        }
        //lista-jugadores
        if (event.getChannel().getId().equals("891121002334003210")) {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 1;

            if (MyPlugin.generator.numbersFile.getKeys(false).size() == 0) {
                stringBuilder.append("No hay ningun jugador registrado!");
            } else {
                for (String key : MyPlugin.generator.numbersFile.getKeys(false)) {
                    if (i == 1) {
                        stringBuilder.append("\n☁ ").append(key).append("\n");
                    } else {
                        stringBuilder.append("☁ ").append(key).append("\n");
                    }
                    i++;
                }
            }
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**");
            builder.setColor(Color.YELLOW);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
            builder.addBlankField(false);
            builder.addField("***Jugadores de VintageSMP\n***", stringBuilder.toString(), false);
            builder.addBlankField(false);
            event.getAuthor().openPrivateChannel().queue(channel -> {
                channel.sendMessageEmbeds(builder.build()).queue();
            });
            event.getMessage().delete().queue();
        }
        //ip reset
        if (event.getChannel().getId().equals("891121592657137704")) {
            String[] args = event.getMessage().getContentDisplay().split(" ");
            String key;
            if (args.length == 1) {
                key = event.getMessage().getContentDisplay();
            } else {
                key = args[0];
            }

            String registeredIp = IpControlTools.getIpByBind(key);
            registeredIp = registeredIp.replace("_", ".");

            if (!IpControlTools.bindExists(key)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("La IP con el key ***" + key + "***  no ha existido nunca en nuestros registros. Asegúrese de encontrar la clave válida según un jugador antes de volver a intentarlo.\n", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
            } else {
                IpControlTools.resetIpAccordingToKey(key);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.GREEN);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("La IP con el key ***" + key + "*** se reseteo con exito!\n", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
            }


            StringBuilder message = new StringBuilder();
            message.append(Tools.colorMSG("&4&lVINTAGE&f&lSMP")).append("\n").append("\n");
            message.append(Tools.colorMSG("&cFue echado de nuestro servidor porque un administrador decidió restablecer" +
                    " su IP en nuestros registros. Esto no alterará nada con respecto a su conexión a Internet, solo manipulará " +
                    "nuestros archivos y la IP que se asignó a esta cuenta.\n\n\n&adsc.gg/vintagesmp"));

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getAddress().getAddress().getHostAddress().equalsIgnoreCase(registeredIp)) {
                    Bukkit.getScheduler().runTask(MyPlugin.getMyPlugin(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kick " + player.getName()
                            + " &bTu IP esta siendo resteada!"));
                    break;
                }
            }

            event.getMessage().delete().queue();

        }
        //ip stats-de-jugador
        if (event.getChannel().getId().equals("891159813311037450")) {

            String[] args = event.getMessage().getContentDisplay().split(" ");
            String name;
            if (args.length == 1) {
                name = event.getMessage().getContentDisplay();
            } else {
                name = args[0];
            }

            String key = "";
            int corazones = 0;
            int monedas = 0;
            int ganancia = 0;
            String cuentas = "";

            if (!IpControlTools.playerByNameIsRegistered(name)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**Un jugador con el nombre** ***" + name + "***  **nunca ha existido en nuestro servidor.\n**", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
            } else {
                key = IpControlTools.getBindByName(name);
                corazones = Tools.getOfflinePlayerHearts(name);
                monedas = Tools.getOfflinePlayerGanancia(name);
                ganancia = Tools.getOfflinePlayerGanancia(name);
                cuentas = IpControlTools.getAccountsByBind(key);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.PINK);
                builder.addBlankField(false);
                builder.addField("**CORAZONES : **", "*" + corazones + "♥*", false);
                builder.addField("**MONEDAS : **", "*" + monedas + "* 💸", false);
                builder.addField("**GANANCIA :**", "*" + ganancia + "* 💰", false);
                builder.addField("**CUENTAS :**", cuentas, false);
                builder.addField("**IP KEY : **", "*" + key + "*", false);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });

            }
            event.getMessage().delete().queue();

        }

        //ip +cuenta
        if (event.getChannel().getId().equals("891121957016330241")) {

            if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                return;
            }

            String[] args = event.getMessage().getContentDisplay().split(" ");
            String key;
            key = args[0];
            if (args.length == 1) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**No estas usando el format correcto! Escribe el nombre para agregar.\n** ***<llave> <nombre>***", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
                event.getMessage().delete().queue();
                return;
            }

            if (!IpControlTools.bindExists(key)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**Una IP con la llave ** ***" + key + "***  **nunca ha existido en nuestro servidor.\n**", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
                event.getMessage().delete().queue();
                return;
            }

            List<String> accounts = IpControlTools.getRawAccountWithBind(key);
            accounts.add(args[1]);
            IpControlTools.updateAccountsByIP(accounts, IpControlTools.getIpByBind(key));


            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**");
            builder.setColor(Color.GREEN);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
            builder.addField("**Con exito se agrego la cuenta ** ***" + args[1] + "***  **a la IP con la llave \n** ***" + key + "*** **!**\n\n\n**Administrador:** *" + event.getAuthor().getName() + "*", "", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();

            event.getMessage().delete().queue();

        }
        //ip -cuenta
        if (event.getChannel().getId().equals("891122008782409779")) {
            if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                return;
            }
            String[] args = event.getMessage().getContentDisplay().split(" ");
            String key;
            key = args[0];
            if (args.length == 1) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**No estas usando el format correcto! Escribe el nombre para eliminar.\n** ***<llave> <nombre>***", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
                event.getMessage().delete().queue();
                return;
            }

            if (!IpControlTools.bindExists(key)) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**Una IP con la llave ** ***" + key + "***  **nunca ha existido en nuestro servidor.\n**", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
                event.getMessage().delete().queue();
                return;
            }

            List<String> accounts = IpControlTools.getRawAccountWithBind(key);

            if (!accounts.contains(args[1])) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("**VintageSMP**");
                builder.setColor(Color.RED);
                builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
                builder.addField("**Una cuenta con el nombre ** ***" + args[1] + "***  ** nunca ha existido en esta IP.\n**", "", false);
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessageEmbeds(builder.build()).queue();
                });
                event.getMessage().delete().queue();
                return;
            }

            accounts.remove(args[1]);
            IpControlTools.updateAccountsByIP(accounts, IpControlTools.getIpByBind(key));

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**");
            builder.setColor(Color.GREEN);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/0.png?raw=true");
            builder.addField("**Con existo se elimino la cuenta ** ***" + args[1] + "***  **a la IP con la llave \n** ***" + key + "*** **!**\n\n\n**Administrador:** *" + event.getAuthor().getName() + "*", "", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();

            event.getMessage().delete().queue();

        } else if (event.getChannel().getId().equals("890053307182231613")) {
            boolean found = false;
            String playerName = "";
            for (Map.Entry<Player, String> entry : MyPlugin.system.discordLink.entrySet()) {
                if (entry.getValue().equals(event.getMessage().getContentDisplay())) {
                    MyPlugin.system.discordMinecraft.set(entry.getKey().getName(), event.getAuthor().getId());
                    try {
                        MyPlugin.system.discordMinecraft.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    found = true;
                    playerName = entry.getKey().getName();
                    break;
                }
            }

            if(found){
                String finalPlayerName = playerName;
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessage("**Con exito se vinculo este discord al jugador** ***" + finalPlayerName +
                            "*** **!**").queue();
                });
            }else{
                event.getAuthor().openPrivateChannel().queue(channel ->
                {
                    channel.sendMessage("**No encontramos se pudo vincular con ningun jugador. Intentalo de nuevo. Asegurate de que el jugador este en linea mientras" +
                            " verificas aqui.**").queue();
                });
            }
            event.getMessage().delete().queue();
        }
    }

}
