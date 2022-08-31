package com.github.gavvydizzle.afkrewards.commands.playercommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenLeaderboardCommand extends SubCommand {

    @Override
    public String getName() {
        return "leaderboard";
    }

    @Override
    public String getDescription() {
        return "Opens the leaderboard inventory";
    }

    @Override
    public String getSyntax() {
        return "/religion leaderboard";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        AFKRewards.getInstance().getInventoryManager().getLeaderboardInventory().openInventory((Player) sender);
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}
