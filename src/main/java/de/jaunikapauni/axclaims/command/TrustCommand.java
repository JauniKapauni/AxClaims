package de.jaunikapauni.axclaims.command;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrustCommand implements CommandExecutor {

    AxClaims reference;
    public TrustCommand(AxClaims reference){
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axclaims.trust")){
            p.sendMessage(ChatColor.RED + "You don't have the permission [axclaims.trust]");
            return true;
        }
        if(args.length != 1){
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(target.getUniqueId() == null){
            p.sendMessage("Player not found!");
            return true;
        }
        Claim c = getClaimAt(p);
        if(c == null){
            p.sendMessage("You are not in a claim");
            return true;
        }
        if(!c.getOwner().equals(p.getUniqueId())){
            p.sendMessage("Only the owner can trust players!");
            return true;
        }
        c.addTrusted(target.getUniqueId());
        saveToConfig(c.getId(), c);
        p.sendMessage(target.getName() + " is now trusted!");
        return true;
    }

    public Claim getClaimAt(Player p){
        for(Claim c : reference.getAllClaims()){
            if(c.isInside(p.getLocation())){
                return c;
            }
        }
        return null;
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
