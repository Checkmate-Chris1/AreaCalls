package me.CheckmateChris1.AreaCalls;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Random;

public class Bot extends ListenerAdapter {
    public static JDA jda;
    public static Main plugin;
    public static String token;
    public static HashMap<String,User> verifyCodes = new HashMap<>();
    public static final String passChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    public Bot(Main main) {
        this.plugin = main;
        token = plugin.getConfig().getString("bot-token");
        try {
            startBot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void startBot() throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.listening("Nagesy's nonsense"));
        jda.addEventListener(this);
    }

    public void messageReceivedEvent(MessageReceivedEvent e) {
        String[] args = e.getMessage().getContentRaw().split("\\s+");
        if (args[0].equalsIgnoreCase("!verify")) {
            if(!EventManager.inGameNames.containsValue(e.getMember().getUser())) {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + ", check your DMs to verify!").queue();
                e.getMember().getUser().openPrivateChannel().queue((channel) -> {
                    String password = createPassword(passChars, 10);
                    channel.sendMessage("To verify your Minecraft account, type `/verify " + password + "` in-game.").queue();
                    verifyCodes.put(password, e.getMember().getUser());
                });
            } else {
                e.getChannel().sendMessage("You are already verified!").queue();
            }
            return;
        }
        return;
    }

    public static String createPassword(String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }

        return sb.toString();
    }
}
