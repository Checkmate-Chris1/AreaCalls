package me.CheckmateChris1.AreaCalls;

import org.bukkit.Location;

import java.math.BigInteger;

public class Callpoint {
    private Location loc;
    private int radius;
    private String name;
    private BigInteger channelId;

    public Callpoint(Location loc, int radius, BigInteger channelId) {
        this.loc = loc;
        this.radius = radius;
        this.channelId = channelId;
    }

    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; }

    public Location getLoc() { return loc; }
    public void setLoc(Location loc) {this.loc = loc;}

    public BigInteger getId() { return channelId; }
    public void setId(BigInteger id) { this.channelId = id; }
}
