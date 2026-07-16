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

public class ClaimCommand implements CommandExecutor {
    AxClaims reference;
    public ClaimCommand(AxClaims reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axclaims.claim")){
            p.sendMessage("You don't have the permission [axclaims.claim]");
            return true;
        }
        if(args.length != 1){
            return false;
        }
        int radius;
        try{
            radius = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            p.sendMessage("The radius has to be a number!");
            return true;
        }
        if(radius < 2){
            p.sendMessage("Radius must be at least 1.");
            return true;
        }
        int requiredBlocks = (radius * 2 + 1) * (radius * 2 + 1);
        if(reference.getClaimBlocks(p.getUniqueId()) < requiredBlocks){
            p.sendMessage(ChatColor.RED + "You need " + requiredBlocks + " claim blocks!");
            return true;
        }
        Location loc = p.getLocation();
        for(Claim existing : reference.getAllClaims()){
            if(existing.overlaps(loc.getBlockX(), loc.getBlockZ(), radius)){
                p.sendMessage("Overlap! There is already a claim in this region!");
                return true;
            }
        }
        int id = reference.getNextId();
        Claim newClaim = new Claim(id, p.getUniqueId(), loc.getBlockX(), loc.getBlockZ(), radius);
        reference.getAllClaims().add(newClaim);
        saveToConfig(id, newClaim);
        reference.removeClaimBlocks(p.getUniqueId(), requiredBlocks);
        p.sendMessage("Claim " + "#" + id + " was successfully created!");
        return true;
    }

    public void saveToConfig(int id, Claim claim){
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
