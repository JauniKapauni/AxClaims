package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerMoveListener implements Listener {
    AxClaims reference;

    public PlayerMoveListener(AxClaims reference) {
        this.reference = reference;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        Claim previousClaim = null;
        Claim currentClaim = null;
        for (Claim claim : reference.getAllClaims()) {
            if (claim.isInside(e.getFrom())) {
                previousClaim = claim;
            }
            if (claim.isInside(e.getTo())) {
                currentClaim = claim;
            }
        }
        if (currentClaim != null) {
            if (previousClaim == null || currentClaim.getId() != previousClaim.getId()) {
                String ownerName = Bukkit.getOfflinePlayer(currentClaim.getOwner()).getName();
                BukkitTask task = reference.getActiveParticles().remove(p.getUniqueId());
                if (task != null) {
                    task.cancel();
                }
                p.sendActionBar(ChatColor.GREEN + "Entering claim of " + ownerName);
                int minX = currentClaim.getCenterX() - currentClaim.getRadius();
                int minZ = currentClaim.getCenterZ() - currentClaim.getRadius();
                int maxX = currentClaim.getCenterX() + currentClaim.getRadius();
                int maxZ = currentClaim.getCenterZ() + currentClaim.getRadius();
                reference.getActiveParticles().put(p.getUniqueId(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int a = minX; a <= maxX; a++) {
                            p.spawnParticle(Particle.FLAME, a, p.getLocation().getY() + 2, minZ, 1, 0, 0, 0, 0);
                        }
                        for (int b = minX; b <= maxX + 1; b++) {
                            p.spawnParticle(Particle.FLAME, b, p.getLocation().getY() + 2, maxZ + 1, 1, 0, 0, 0, 0);
                        }
                        for (int c = minZ; c <= maxZ; c++) {
                            p.spawnParticle(Particle.FLAME, minX, p.getLocation().getY() + 2, c, 1, 0, 0, 0, 0);
                        }
                        for (int d = minZ; d <= maxZ + 1; d++) {
                            p.spawnParticle(Particle.FLAME, maxX + 1, p.getLocation().getY() + 2, d, 1, 0, 0, 0, 0);
                        }
                    }
                }.runTaskTimer(reference, 0, 20));
            }
        }

        if (currentClaim == null && previousClaim != null) {
            String ownerName = Bukkit.getOfflinePlayer(previousClaim.getOwner()).getName();
            BukkitTask task = reference.getActiveParticles().remove(p.getUniqueId());
            if (task != null) {
                task.cancel();
            }
            p.sendActionBar(ChatColor.RED + "Leaving claim of " + ownerName);
        }
    }
}
