package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.afk.AFKManager;
import com.github.gavvydizzle.afkrewards.group.GroupType;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminBeginAFKCommand extends SubCommand {

    @Override
    public String getName() {
        return "beginAFK";
    }

    @Override
    public String getDescription() {
        return "Sets the player to AFK mode";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin beginAFK <player> <group>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Invalid player");
            return;
        }

        GroupType groupType;
        try {
            groupType = GroupType.valueOf(args[2].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Group");
            return;
        }

        AFKManager afkManager = AFKRewards.getInstance().getAfkManager();

        switch (groupType) {
            case DOLPHIN:
                if (afkManager.isInDolphinGroup(player)) {
                    afkManager.setDolphinPlayerAFK(player);

                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.GREEN + "Set " + player.getName() + " to AFK " + groupType);
                    }
                }
                else if (sender instanceof Player) {
                    sender.sendMessage(ChatColor.RED + player.getName() + " is not in the group " + groupType);
                }
                break;
            case PIG:
                if (afkManager.isInPigGroup(player)) {
                    afkManager.setPigPlayerAFK(player);

                    if (sender instanceof Player) {
                        sender.sendMessage(ChatColor.GREEN + "Set " + player.getName() + " to AFK " + groupType);
                    }
                }
                else if (sender instanceof Player) {
                    sender.sendMessage(ChatColor.RED + player.getName() + " is not in the group " + groupType);
                }
                break;
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            return null;
        }
        else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], GroupType.values, list);
        }

        return list;
    }

}