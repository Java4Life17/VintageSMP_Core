package myplugin.myplugin.Discord_System;


import myplugin.myplugin.MyPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordSystem {

    private MyPlugin plugin;

    public DiscordSystem(MyPlugin plugin) {
        this.plugin = plugin;
    }

    public JDA bot;

    public void startBot() throws LoginException {

        bot = JDABuilder.createDefault("NzM2MzQ2Njg2NzM3MjE5Njg0.XxteSg.pA8mtfRLEGNKIwialILGyn8LQWQ").build();
        bot.getPresence().setActivity(Activity.watching("VintageSMP"));
        bot.getPresence().setPresence(OnlineStatus.IDLE, true);

        bot.addEventListener(new MessageListener());
        bot.addEventListener(new DiscordToMinecraft());
        plugin.getServer().getPluginManager().registerEvents(new Minecraft_Chat(), plugin);

    }

    public void disableBot() {
        bot.removeEventListener(new MessageListener());
        bot.removeEventListener(new DiscordToMinecraft());
    }

    public JDA getBot() {
        return bot;
    }

    public void sendPetDiscoveryAlert(int totalFound, String playerName, int timeLeft) {

        String userID = MyPlugin.system.discordMinecraft.getString(playerName);

        //********************************************************************

        getBot().retrieveUserById(userID).queue(user -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**", null)
                    .setColor(Color.MAGENTA);

            if (totalFound == 1) {
                builder.setDescription("Este es un mensaje para informarle que su mascota ha encontrado un nuevo objeto tirado.");
            } else {
                builder.setDescription("Este es un mensaje para informarle que su mascota ha encontrado *" + totalFound + "* objetos tirados.");
            }
            builder.addBlankField(false);
            builder.addField("***INFO***", "Mientras no estabas conectado, dejaste a tu mascota deambulando por" +
                    " el mundo y él comenzó a buscar algunas cosas que te podrían gustar. Su mascota solo tiene " + Integer.toString(timeLeft + 1) + " minuto(s) " +
                    "antes de que se canse y se tome un descanso. Recuerda darle todo el amor posible para que pueda encontrar " +
                    "un objeto legendario para ti.\n", false);
            builder.addBlankField(false);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/employee-avatar-png-image-with-transparent-background-png-arts-518374.png?raw=true");
            builder.setImage("https://www.minecraft.net/content/dam/archive/85bc841d6455c227eaebef2bdc00f73f-sticker_Wolf.png");
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessageEmbeds(builder.build()).queue();
            });
        });

    }

    public void sendPetShuttingDown(String playerName){
        String userID = MyPlugin.system.discordMinecraft.getString(playerName);

        getBot().retrieveUserById(userID).queue(user -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**VintageSMP**", null)
                    .setColor(Color.MAGENTA);


            builder.setDescription("Este es un mensaje de su mascota para usted.");
            builder.addBlankField(false);
            builder.addField("***MENSAJE***", "Hola amo, he estado buscando objetos en todo el mundo tal como" +
                    " me dejaste antes de irte. Después de toda esa búsqueda, me cansé y decidí buscar un árbol cercano y " +
                    "descansar debajo de él. Sin embargo, no se preocupe, tan pronto como regrese a visitarme, volveré al " +
                    "trabajo pero no tengo suficiente energía para buscar por mí mismo hoy.\n", false);
            builder.addBlankField(false);
            builder.setFooter("Programado por [RE-16] Daniel#5071", "https://github.com/proxytimeout/VintageSMP_Core/blob/master/src/main/java/myplugin/employee-avatar-png-image-with-transparent-background-png-arts-518374.png?raw=true");
            builder.setImage("https://thumbs.gfycat.com/ZestyCluelessEyas-max-1mb.gif");
            user.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessageEmbeds(builder.build()).queue();
            });
        });

    }

}
