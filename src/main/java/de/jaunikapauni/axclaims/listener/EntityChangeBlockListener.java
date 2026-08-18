package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlockListener implements Listener {

    AxClaims reference;
    public EntityChangeBlockListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e){
        Block b = e.getBlock();
        Location loc = b.getLocation();
        for(Claim c : reference.getAllClaims()){
            if(!c.isInside(loc)){
                continue;
            }
            if(e.getEntity() instanceof FallingBlock){
                return;
            }
            e.setCancelled(true);
            return;
        }
    }
}
