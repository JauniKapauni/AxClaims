package de.jaunikapauni.axclaims.command;

import de.jaunikapauni.axclaims.AxClaims;
import de.jaunikapauni.axclaims.manager.Claim;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MenuCommand implements CommandExecutor {
    AxClaims reference;
    public MenuCommand(AxClaims reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axclaims.menu")){
            p.sendMessage("You don't have the permission! [axclaims.menu]");
            return true;
        }
        Claim currentClaim = null;
        for(Claim claim : reference.getAllClaims()){
            if(claim.isInside(p.getLocation())){
                currentClaim = claim;
                break;
            }
        }
        if(currentClaim == null){
            p.sendMessage("You are not in a claim!");
            return true;
        }
        reference.openClaimGUI(p, currentClaim);
        return true;
    }
}
