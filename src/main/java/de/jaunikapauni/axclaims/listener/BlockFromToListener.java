package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    AxClaims reference;
    public BlockFromToListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e){
        Location from = e.getBlock().getLocation();
        Location to = e.getToBlock().getLocation();

        for(Claim c : reference.getAllClaims()){
            boolean fromInside = c.isInside(from);
            boolean toInside = c.isInside(to);
            if(fromInside != toInside){
                e.setCancelled(true);
                return;
            }
        }
    }
}
