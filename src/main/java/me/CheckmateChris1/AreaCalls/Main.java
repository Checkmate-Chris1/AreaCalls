package me.CheckmateChris1.AreaCalls;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {
    public FileConfiguration dataConfig = null;
    public File dataFile = null;

    public void main(String[] args) {

    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new EventManager(),this);
        this.getServer().getPluginCommand("CallPoint").setExecutor(new Commands());
        this.getServer().getPluginCommand("Verify").setExecutor(new Commands());
        this.getServer().getPluginCommand("Unlink").setExecutor(new Commands());
        new Bot(this);

        this.saveDefaultConfig();
        if (this.getConfig().contains("verify")) {
            this.loadVerifies();
        }
        if(this.getConfig().contains("poi")) {
            this.loadPoints();
        }
    }
    public void onDisable() {
        if (!EventManager.inGameNames.isEmpty()) {
            this.saveVerifies();
        }
        if(!Commands.points.isEmpty()) {
            this.savePoints();
        }
    }

    // Verification saving/loading
    public void loadVerifies() {
        for (String key : this.getConfig().getConfigurationSection("verify").getKeys(false)) {
            User plrUser = Bot.jda.getUserById(this.getConfig().getConfigurationSection("verify").getString(key));
            Player plr = Bukkit.getPlayer(UUID.fromString(key));
            EventManager.inGameNames.put(plr, plrUser);
        }
    }
    public void saveVerifies() {
        // Clear current verifies to remove deleted links
        this.getConfig().set("verify", null);

        for(Map.Entry<Player,User> entry : EventManager.inGameNames.entrySet()) {
            if(entry.getKey() != null && entry.getValue() != null) {
                this.getConfig().set("verify." + entry.getKey().getUniqueId(), entry.getValue().getId());
            }
        };
        this.saveConfig();
    }

    // Callpoint saving/loading
    public void savePoints() {
        // Clear current saves to make sure deleted points do not stay
        this.getConfig().set("poi", null);

        for(Map.Entry<String, Callpoint> entry : Commands.points.entrySet()) {
            String entryDirectory = "poi." + entry.getKey();
            // Save Range Information
            this.getConfig().set(entryDirectory + ".radius", entry.getValue().getRadius());
            // Save Channel ID Information
            this.getConfig().set(entryDirectory + ".id", entry.getValue().getId());
            // Save Location Information
            Location entryLoc = entry.getValue().getLoc();
            this.getConfig().set(entryDirectory + ".coordinates.world", entryLoc.getWorld().getUID().toString());
            this.getConfig().set(entryDirectory + ".coordinates.x", entryLoc.getBlockX());
            this.getConfig().set(entryDirectory + ".coordinates.y", entryLoc.getBlockY());
            this.getConfig().set(entryDirectory + ".coordinates.z", entryLoc.getBlockZ());
        };
        this.saveConfig();
    }
    public void loadPoints() {
        for (String pointName : this.getConfig().getConfigurationSection("poi").getKeys(false)) {
            String mainDirectory = "poi." + pointName;

            int radius = Integer.parseInt(this.getConfig().getString(mainDirectory + ".radius"));
            BigInteger id = BigInteger.valueOf(this.getConfig().getLong(mainDirectory + ".id"));

            // Converting raw information to Location type
            World world = Bukkit.getWorld(UUID.fromString(this.getConfig().getString(mainDirectory + ".coordinates.world")));
            int x = Integer.parseInt(this.getConfig().getString(mainDirectory + ".coordinates.x"));
            int y = Integer.parseInt(this.getConfig().getString(mainDirectory + ".coordinates.y"));
            int z = Integer.parseInt(this.getConfig().getString(mainDirectory + ".coordinates.z"));
            Location loc = new Location(world,x,y,z);

            Commands.points.put(pointName,new Callpoint(loc,radius,id));
        }
    }
}
