package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketFillListener implements Listener {

    AxClaims reference;
    public PlayerBucketFillListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e){
        Player p = e.getPlayer();
        Location loc = e.getBlock().getLocation();

        for (Claim claim : reference.getAllClaims()){
            if (!claim.isInside(loc)) {
                continue;
            }
            if(claim.getOwner().equals(p.getUniqueId())){
                return;
            }
            e.setCancelled(true);
            p.sendMessage("This region doesn't belong to you!");
            return;
        }
    }
}
