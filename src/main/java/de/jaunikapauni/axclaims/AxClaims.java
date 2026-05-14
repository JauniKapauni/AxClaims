package de.jaunikapauni.axclaims;

import de.jaunikapauni.axclaims.command.ClaimCommand;
import de.jaunikapauni.axclaims.listener.BlockBreakListener;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class AxClaims extends JavaPlugin {
    List<Claim> allClaims = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        loadCLaims();
        getCommand("claim").setExecutor(new ClaimCommand(this));
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
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

            Claim claim = new Claim(owner, x, z, radius);
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
}
