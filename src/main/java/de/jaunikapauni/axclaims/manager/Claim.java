package de.jaunikapauni.axclaims.manager;

import org.bukkit.Location;

import java.util.UUID;

public class Claim {
    UUID owner;
    int centerX;
    int centerZ;
    int radius;

    public Claim(UUID owner, int centerX, int centerZ, int radius){
        this.owner = owner;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.radius = radius;
    }

    public boolean isInside(Location loc){
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        return x <= centerX + radius && x >= centerX - radius && z <= centerZ + radius && z >= centerZ - radius;
    }

    public boolean overlaps(int otherX, int otherZ, int otherRadius){
        int distanceX = Math.abs(this.centerX - otherX);
        int distanceZ = Math.abs(this.centerZ - otherZ);

        return distanceX <= (this.radius + otherRadius) && distanceZ <= (this.radius + otherRadius);
    }

    public UUID getOwner(){
        return owner;
    }

    public int getCenterX(){
        return centerX;
    }

    public int getCenterZ(){
        return centerZ;
    }

    public int getRadius(){
        return radius;
    }
}
