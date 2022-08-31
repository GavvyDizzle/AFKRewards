package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminSetScoreCommand extends SubCommand {

    @Override
    public String getName() {
        return "setScore";
    }

    @Override
    public String getDescription() {
        return "Set the score of a player";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin setScore <player>";
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

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
        if (!offlinePlayer.hasPlayedBefore()) {
            sender.sendMessage(ChatColor.RED + "Invalid player");
            return;
        }

        int score;
        try {
            score = Integer.parseInt(args[2]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid score: " + args[2]);
            return;
        }
        if (score < 0) {
            sender.sendMessage(ChatColor.RED + "The score must be positive");
            return;
        }

        try {
            AFKRewards.getInstance().getDatabase().setScore(offlinePlayer, score);
            sender.sendMessage(ChatColor.GREEN + "Successfully set " + offlinePlayer.getName() + "'s score to " + score);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred when executing this command");
            e.printStackTrace();
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