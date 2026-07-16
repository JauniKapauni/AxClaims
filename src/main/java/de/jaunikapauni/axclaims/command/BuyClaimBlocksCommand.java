package de.jaunikapauni.axclaims.command;

import de.jaunikapauni.axclaims.AxClaims;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyClaimBlocksCommand implements CommandExecutor {
    AxClaims reference;
    public BuyClaimBlocksCommand(AxClaims reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command!");
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("axclaims.buyclaimblocks")){
            p.sendMessage("You don't have the permission [axclaims.buyclaimblocks]");
            return true;
        }
        if(args.length != 1){
            return false;
        }
        int claimBlocks;
        try{
            claimBlocks = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            p.sendMessage("The amount of claim blocks has to be a number!");
            return true;
        }
        if(claimBlocks <= 0){
            p.sendMessage("Claim block amount to be bought has to be bigger then 0.");
            return true;
        }
        if(claimBlocks > 100){
            p.sendMessage("You can only buy a maximum 100 blocks at a time.");
            return true;
        }
        int cost = claimBlocks * 20;
        if(reference.getEconomyAPI().has(p.getUniqueId(), cost)){
            reference.getEconomyAPI().withdraw(p.getUniqueId(), cost);
            reference.addClaimBlocks(p.getUniqueId(), claimBlocks);
            p.sendMessage(ChatColor.GREEN + "You bought " + claimBlocks + " claim blocks for " + cost);
        } else {
            p.sendMessage(ChatColor.RED + "You don't have enough money!");
            return true;
        }
        return true;
    }
}
