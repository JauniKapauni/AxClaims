package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class BlockPistonExtendListener implements Listener {

    AxClaims reference;
    public BlockPistonExtendListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e){
        for(Block b : e.getBlocks()){
            Block from = b;
            Block to = b.getRelative(e.getDirection());
            for(Claim c : reference.getAllClaims()){
                if(c.isInside(from.getLocation()) || c.isInside(to.getLocation())){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
