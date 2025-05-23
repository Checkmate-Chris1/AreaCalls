package me.CheckmateChris1.AreaCalls;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager implements Listener {
    public static HashMap<Player, User> inGameNames = new HashMap<>();

    public void main(String args[]) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //methods
                cancel();
            }
        }.runTaskTimer(new Main(), 1, 1);
    }

    @EventHandler()
    public void onEvent(PlayerMoveEvent e) {
        List<Callpoint> pointsInArea = new ArrayList<>();

        for (Map.Entry<String, Callpoint> entry : Commands.points.entrySet()) {
            Callpoint poi = entry.getValue();
            // Player is in same world
            if(poi.getLoc().getWorld() == e.getTo().getWorld()) {
                // Player is in area
                if(poi.getLoc().getWorld().getNearbyEntities(poi.getLoc(), poi.getRadius(), poi.getRadius(), poi.getRadius()).contains(e.getPlayer())) {
                    pointsInArea.add(poi);
                }
            }
        }
        if(pointsInArea.size() == 1) {
            // Player is verified
            if(inGameNames.get(e.getPlayer()) != null) {
                VoiceChannel channel = Bot.jda.getVoiceChannelById(pointsInArea.get(0).getId().toString());
                // Player is in a call
                GuildVoiceState plrCall = channel.getGuild().getMemberById(inGameNames.get(e.getPlayer()).getId()).getVoiceState();
                if(plrCall.inAudioChannel()) {
                    // Player is not already in target call
                    if(plrCall.getChannel() != channel) {
                        channel.getGuild().moveVoiceMember(channel.getGuild().getMember(inGameNames.get(e.getPlayer())), channel).queue();
                    }
                }
            }
        }
    }
}
