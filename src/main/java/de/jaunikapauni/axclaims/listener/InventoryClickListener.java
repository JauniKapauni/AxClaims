package de.jaunikapauni.axclaims.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Claim-Menu")){
            e.setCancelled(true);
        }
    }
}
