package de.jaunikapauni.axclaims.command;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecenterCommand implements CommandExecutor {

    AxClaims reference;
    public RecenterCommand(AxClaims reference){
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(reference.isWorldDisabled(p.getWorld().getName())){
            p.sendMessage("Claims are disabled in this world!");
            return true;
        }
        if(!p.hasPermission("axclaims.recenter")){
            p.sendMessage(ChatColor.RED + "You don't have the permission! [axclaims.recenter]");
            return true;
        }
        Claim claim = null;
        for(Claim c : reference.getAllClaims()){
            if(c.isInside(p.getLocation())){
                claim = c;
                break;
            }
        }
        if(claim == null){
            p.sendMessage(ChatColor.RED + "You are not inside a claim!");
            return true;
        }
        if(!claim.getOwner().equals(p.getUniqueId())){
            p.sendMessage(ChatColor.RED + "Only the owner can recenter a claim!");
            return true;
        }
        int cost = 2000;
        if(!reference.getEconomyAPI().has(p.getUniqueId(), cost)){
            p.sendMessage(ChatColor.RED + "You don't have enough money!");
            return true;
        }
        reference.getEconomyAPI().withdraw(p.getUniqueId(), cost);
        Location loc = p.getLocation();
        claim.setCenterX(loc.getBlockX());
        claim.setCenterZ(loc.getBlockZ());
        saveToConfig(claim.getId(), claim);
        p.sendMessage(ChatColor.GREEN + "Your claim was recentered for " + cost);
        return true;
    }

    private void saveToConfig(int id, Claim claim) {
        String path = "claims." + id + ".";
        reference.getConfig().set(path + "owner", claim.getOwner().toString());
        reference.getConfig().set(path + "centerX", claim.getCenterX());
        reference.getConfig().set(path + "centerZ", claim.getCenterZ());
        reference.getConfig().set(path + "radius", claim.getRadius());
        List<String> trusted = new ArrayList<>();
        for(UUID uuid : claim.getTrusted()){
            trusted.add(uuid.toString());
        }
        reference.getConfig().set(path + "trusted", trusted);
        reference.saveConfig();
    }
}
