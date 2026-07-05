package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PVPListener implements Listener {
    AxClaims reference;
    public PVPListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player)){
            return;
        }
        Player attacker = (Player) e.getDamager();
        if(!(e.getEntity() instanceof Player)){
            return;
        }
        Player victim = (Player) e.getEntity();

        for(Claim claim : reference.getAllClaims()){
            if(claim.isInside(victim.getLocation())){
                e.setCancelled(true);
                attacker.sendMessage("PVP is deactivated for this region!");
                return;
            }
        }
    }
}
