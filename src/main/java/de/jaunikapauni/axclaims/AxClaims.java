package de.jaunikapauni.axclaims;

import de.jaunikapauni.axclaims.command.BuyClaimBlocksCommand;
import de.jaunikapauni.axclaims.command.ClaimCommand;
import de.jaunikapauni.axclaims.command.MenuCommand;
import de.jaunikapauni.axclaims.listener.*;
import de.jaunikapauni.axclaims.manager.Claim;
import de.jaunikapauni.axeconomy.AxEconomy;
import de.jaunikapauni.axeconomy.api.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class AxClaims extends JavaPlugin {
    List<Claim> allClaims = new ArrayList<>();
    Map<UUID, BukkitTask> activeParticles = new HashMap<>();
    EconomyAPI economyAPI;
    public EconomyAPI getEconomyAPI(){
        return economyAPI;
    }
    File claimBlocksFile;
    FileConfiguration claimBlocksConfig;

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
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getLogger().info("");
        getLogger().info("----------------------------------------");
        getLogger().info("Name: " + getName());
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info(String.join("Authors: " + ", ", getDescription().getAuthors()));
        getLogger().info("----------------------------------------");
        getLogger().info("");
        if(Bukkit.getPluginManager().getPlugin("AxEconomy") != null){
            AxEconomy axEconomy = (AxEconomy) Bukkit.getPluginManager().getPlugin("AxEconomy");
            if(axEconomy == null){
                throw new IllegalStateException("AxEconomy is missing!");
            }
            economyAPI = axEconomy.getEconomyAPI();
        }
        getCommand("buyclaimblocks").setExecutor(new BuyClaimBlocksCommand(this));
        claimBlocksFile = new File(getDataFolder(), "claimblocks.yml");
        if(!claimBlocksFile.exists()){
            claimBlocksFile.getParentFile().mkdirs();
            saveResource("claimblocks.yml", false);
        }
        claimBlocksConfig = YamlConfiguration.loadConfiguration(claimBlocksFile);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerBucketEmptyListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerBucketFillListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockFromToListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPistonExtendListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockPistonRetractListener(this), this);
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

        NamespacedKey key = new NamespacedKey(this, "claim_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, claim.getId());

        ownerInfo.setItemMeta(meta);
        gui.setItem(0, ownerInfo);

        if(claim.getOwner().equals(p.getUniqueId())){
            ItemStack deleteItem = new ItemStack(Material.BARRIER);
            ItemMeta meta1 = deleteItem.getItemMeta();
            meta1.setDisplayName("Delete claim");
            deleteItem.setItemMeta(meta1);
            gui.setItem(8, deleteItem);
        }
        p.openInventory(gui);
    }

    public void removeClaim(int id){
        for(int i = 0; i < allClaims.size(); i++){
            Claim c = allClaims.get(i);
            if(c.getId() == id){
                allClaims.remove(i);
                getConfig().set("claims." + id, null);
                saveConfig();
                break;
            }
        }
    }
    public int getClaimBlocks(UUID uuid){
        return claimBlocksConfig.getInt("players." + uuid, 0);
    }

    public void addClaimBlocks(UUID uuid, int amount){
        claimBlocksConfig.set("players." + uuid, getClaimBlocks(uuid) + amount);
        saveClaimBlocks();
    }

    public void removeClaimBlocks(UUID uuid, int amount){
        claimBlocksConfig.set("players." + uuid, getClaimBlocks(uuid) - amount);
        saveClaimBlocks();
    }

    public void saveClaimBlocks(){
        try{
            claimBlocksConfig.save(claimBlocksFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
