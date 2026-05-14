package de.jaunikapauni.axclaims;

import de.jaunikapauni.axclaims.command.ClaimCommand;
import de.jaunikapauni.axclaims.command.MenuCommand;
import de.jaunikapauni.axclaims.listener.*;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class AxClaims extends JavaPlugin {
    List<Claim> allClaims = new ArrayList<>();
    Map<UUID, BukkitTask> activeParticles = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadCLaims();
        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("menu").setExecutor(new MenuCommand(this));
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new PVPListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadCLaims(){
        if(getConfig().getConfigurationSection("claims") == null) return;
        for(String id : getConfig().getConfigurationSection("claims").getKeys(false)){
            String path = "claims." + id + ".";
            UUID owner = UUID.fromString(getConfig().getString(path + "owner"));
            int x = getConfig().getInt(path + "centerX");
            int z = getConfig().getInt(path + "centerZ");
            int radius = getConfig().getInt(path + "radius");

            Claim claim = new Claim(Integer.parseInt(id), owner, x, z, radius);
            allClaims.add(claim);
        }
        getLogger().info(allClaims.size() + " all claims were loaded!");
    }

    public int getNextId(){
        int id = getConfig().getInt("next-id", 1);
        getConfig().set("next-id", id + 1);
        saveConfig();
        return id;
    }

    public List<Claim> getAllClaims(){
        return allClaims;
    }

    public Map<UUID, BukkitTask> getActiveParticles(){
        return activeParticles;
    }

    public void openClaimGUI(Player p, Claim claim){
        Inventory gui = Bukkit.createInventory(null, 9, "Claim-Menu");

        ItemStack ownerInfo = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = ownerInfo.getItemMeta();
        meta.setDisplayName("Owner: " + Bukkit.getOfflinePlayer(claim.getOwner()).getName());
        ownerInfo.setItemMeta(meta);
        gui.setItem(0, ownerInfo);
        p.openInventory(gui);
    }
}
