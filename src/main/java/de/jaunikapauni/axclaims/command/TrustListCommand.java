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

import java.util.UUID;

public class TrustListCommand implements CommandExecutor {

    AxClaims reference;
    public TrustListCommand(AxClaims reference){
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axclaims.trustlist")){
            p.sendMessage(ChatColor.RED + "You don't have the permission [axclaims.trustlist]");
            return true;
        }
        Claim c = getClaimAt(p);
        if(c == null){
            p.sendMessage("You are not in a claim!");
            return true;
        }
        p.sendMessage(ChatColor.GREEN + "Owner: " + Bukkit.getOfflinePlayer(c.getOwner()).getName());
        if(c.getTrusted().isEmpty()){
            p.sendMessage("No players are trusted in this claim");
        } else {
            p.sendMessage(ChatColor.GREEN + "Trusted players:");
            for(UUID uuid : c.getTrusted()){
                OfflinePlayer trustedPlayer = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.GREEN + "- " +  trustedPlayer.getName());
            }
        }
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
}
