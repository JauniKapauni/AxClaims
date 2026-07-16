package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class PlayerQuitListener implements Listener {

    AxClaims reference;
    public PlayerQuitListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        BukkitTask task = reference.getActiveParticles().remove(e.getPlayer().getUniqueId());
        if(task != null){
            task.cancel();
        }
    }
}
