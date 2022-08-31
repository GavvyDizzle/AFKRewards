package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminEndAFKCommand extends SubCommand {

    @Override
    public String getName() {
        return "endAFK";
    }

    @Override
    public String getDescription() {
        return "Removed the player from AFK mode";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin endAFK <player>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player");
            return;
        }

        if (AFKRewards.getInstance().getAfkManager().removePlayerFromAFK(player)) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.GREEN + "Removed " + player.getName() + " from AFK");
            }
        }
        else if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + player.getName() + " is not AFK");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        if (args.length == 2) {
            return null;
        }
        return new ArrayList<>();
    }

}