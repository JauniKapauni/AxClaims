package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    AxClaims reference;
    public PlayerInteractListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if(e.getClickedBlock() == null){
            return;
        }
        Player p = e.getPlayer();
        Location loc = e.getClickedBlock().getLocation();
        for(Claim claim : reference.getAllClaims()){
            if(!claim.isInside(loc)){
                continue;
            }
            if(claim.isTrusted(p.getUniqueId())){
                return;
            }
            e.setCancelled(true);
            p.sendMessage("This region doesn't belong to you!");
            return;
        }
    }
}
