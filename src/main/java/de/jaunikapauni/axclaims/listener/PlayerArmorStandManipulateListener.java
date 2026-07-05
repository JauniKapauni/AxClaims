package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerArmorStandManipulateListener implements Listener {

    AxClaims reference;
    public PlayerArmorStandManipulateListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onArmorStandManipulate(PlayerArmorStandManipulateEvent e){
        Player p = e.getPlayer();
        Location loc = e.getRightClicked().getLocation();
        for(Claim c : reference.getAllClaims()){
            if(!c.isInside(loc)){
                continue;
            }
            if(c.getOwner().equals(p.getUniqueId())){
                return;
            }
            e.setCancelled(true);
            p.sendMessage("This region doesn't belong to you!");
            return;
        }
    }
}
