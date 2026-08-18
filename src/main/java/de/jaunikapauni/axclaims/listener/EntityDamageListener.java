package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    AxClaims reference;
    public EntityDamageListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof LivingEntity){
            return;
        }
        for(Claim c : reference.getAllClaims()){
            if(c.isInside(e.getEntity().getLocation())){
                e.setCancelled(true);
                return;
            }
        }
    }
}
