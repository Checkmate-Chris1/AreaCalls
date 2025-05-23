package me.CheckmateChris1.AreaCalls;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandExecutor {
    public static HashMap<String, Callpoint> points = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(label.equalsIgnoreCase("CallPoint")) {
            if(!(sender instanceof Player)) return true;
            Player plr = (Player) sender;
            if(args.length < 1) {
                sendUsage(plr);
                return true;
            }
            if(args[0].equalsIgnoreCase("add")) {
                if(args.length < 4) {
                    sendUsage(plr);
                    return true;
                }
                int radius = 0;
                BigInteger channelId = null;
                try {
                    radius = Integer.parseInt(args[2]);
                    channelId = new BigInteger(args[3]);
                    if (Bot.jda.getVoiceChannelById(args[3]) != null && Bot.jda.getGuilds().contains(Bot.jda.getVoiceChannelById(args[3]).getGuild())) {
                        points.put(args[1], new Callpoint(plr.getLocation(), radius, channelId));
                        plr.sendMessage(ChatColor.GREEN + "Created point '" + args[1] + "' with radius " + ChatColor.GOLD + args[2] + ChatColor.GREEN + " connected to " + ChatColor.GOLD + Bot.jda.getVoiceChannelById(args[3]).getName() + ChatColor.GREEN + "!");
                    } else {
                        plr.sendMessage(ChatColor.RED + "That channel ID doesn't exist / isn't in the server!");
                    }
                } catch(NumberFormatException ex) {
                    ex.printStackTrace();
                    plr.sendMessage(ChatColor.RED + "The Radius/ID" + " is not a valid integer!");
                }

                return true;
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(args.length < 2) {
                    sendUsage(plr);
                    return true;
                }
                if(points.containsKey(args[1])) {
                    plr.sendMessage(ChatColor.GREEN + "Removed point '" + args[1] + "'!");
                    points.remove(args[1]);
                } else {
                    plr.sendMessage(ChatColor.RED + "The point " + ChatColor.GOLD + "'" + args[1] + "'" + ChatColor.RED + " does not exist!");
                }
            } else if(args[0].equalsIgnoreCase("list")) {
                if(points.isEmpty()) {
                    plr.sendMessage(ChatColor.RED + "There are no active call points!");
                    plr.sendMessage(ChatColor.YELLOW + "Make one with /callpoint add!");
                    return true;
                }
                int i = 1;
                for (Map.Entry<String, Callpoint> entry : points.entrySet()) {
                    // Format: 1) Name: Example, Radius: 50, ID: 5082394838
                    plr.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6" + i + ") Name: &a" + entry.getKey() + "&6, Radius: &a" + entry.getValue().getRadius() + "&6, ID: &a" + entry.getValue().getId()));
                    i++;
                }
            } else {
                sendUsage(plr);
            }

        } else if(label.equalsIgnoreCase("verify")) {
            if(!(sender instanceof Player)) return true;
            Player plr = (Player) sender;
            if(args.length < 1) {
                plr.sendMessage("Usage: /verify <code>");
                return true;
            }
            if(Bot.verifyCodes.containsKey(args[0])) {
                plr.sendMessage(ChatColor.GREEN + "Confirmed verification! Your connected Discord account is: " + ((User)Bot.verifyCodes.get(args[0])).getAsTag());
                plr.sendMessage(ChatColor.GREEN + "You can unlink your account anytime by typing /unlink!");
                EventManager.inGameNames.put(plr,Bot.verifyCodes.get(args[0]));
                Bot.verifyCodes.remove(args[0]);
            } else {
                plr.sendMessage(ChatColor.RED + "That verification code is invalid / does not exist.");
                plr.sendMessage(ChatColor.YELLOW + "Do !verify on Discord to link your Minecraft account.");
            }
        } else if (label.equalsIgnoreCase("unlink")) {
            if(!(sender instanceof Player)) return true;
            Player plr = (Player) sender;

            // Player is verified
            if(EventManager.inGameNames.containsKey(plr)) {
                EventManager.inGameNames.remove(plr);
                plr.sendMessage(ChatColor.GREEN + "Successfully unlinked your account!");
            } else {
                plr.sendMessage(ChatColor.RED + "You are already unverified!");
            }
        }
        return false;
    }

    public void sendUsage(Player plr) {
        plr.sendMessage("/callpoint <add|remove|list> <name> <radius> <voiceChannelId>");
    }
}
