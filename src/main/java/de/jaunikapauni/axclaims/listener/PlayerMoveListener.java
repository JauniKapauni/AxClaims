package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    AxClaims reference;
    public PlayerMoveListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()){
            return;
        }
        Claim previousClaim = null;
        for(Claim claim : reference.getAllClaims()){
            if(claim.isInside(e.getFrom())){
                previousClaim = claim;
            }
        }
        Claim currentClaim = null;
        for(Claim claim : reference.getAllClaims()){
            if(claim.isInside(e.getTo())){
                currentClaim = claim;
            }
        }
        if(currentClaim != null){
            if(previousClaim == null || currentClaim.getId() != previousClaim.getId()){
                String ownerName = Bukkit.getOfflinePlayer(currentClaim.getOwner()).getName();
                p.sendActionBar(ChatColor.GREEN + "Region of " + ownerName);
            }
        }

        if(currentClaim == null && previousClaim != null){
            String ownerName = Bukkit.getOfflinePlayer(previousClaim.getOwner()).getName();
            p.sendActionBar(ChatColor.RED + "Region of " + ownerName);
        }
    }
}
