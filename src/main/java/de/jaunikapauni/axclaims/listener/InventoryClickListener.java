package de.jaunikapauni.axclaims.listener;

import de.jaunikapauni.axclaims.AxClaims;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

public class InventoryClickListener implements Listener {
    AxClaims reference;
    public InventoryClickListener(AxClaims reference){
        this.reference = reference;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Claim-Menu")){
            e.setCancelled(true);
            if(e.getCurrentItem() != null){
                Player p = (Player) e.getWhoClicked();
                ItemStack infoItem = e.getInventory().getItem(0);
                if(infoItem != null){
                    NamespacedKey key = new NamespacedKey(reference, "claim_id");
                    ItemMeta meta = infoItem.getItemMeta();
                    if(meta != null && meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
                        int claimid = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                        if(e.getRawSlot() == 8 && e.getCurrentItem().getType() == Material.BARRIER){
                            reference.removeClaim(claimid);
                            p.sendMessage("Your claim was deleted!");
                            p.closeInventory();
                            BukkitTask task = reference.getActiveParticles().remove(p.getUniqueId());
                            if(task != null){
                                task.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}
