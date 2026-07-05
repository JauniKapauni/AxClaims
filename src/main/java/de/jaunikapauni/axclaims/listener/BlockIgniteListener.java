package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockIgniteListener implements Listener {

    AxClaims reference;
    public BlockIgniteListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e){
        Location loc = e.getBlock().getLocation();
        for(Claim c : reference.getAllClaims()){
            if(!c.isInside(loc)){
                continue;
            }
            e.setCancelled(true);
            return;
        }
    }
}
