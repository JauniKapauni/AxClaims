package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

public class EntityExplodeListener implements Listener {

    AxClaims reference;
    public EntityExplodeListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        Iterator<Block> iterator = e.blockList().iterator();
        while (iterator.hasNext()){
            Block block = iterator.next();
            for(Claim c : reference.getAllClaims()){
                if(c.isInside(block.getLocation())){
                    iterator.remove();
                    break;
                }
            }
        }
    }
}
