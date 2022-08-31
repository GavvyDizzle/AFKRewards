package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.group.GroupType;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminSetGroupRegionCommand extends SubCommand {

    @Override
    public String getName() {
        return "setRegion";
    }

    @Override
    public String getDescription() {
        return "Change a Group's region";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin setRegion <group> <region>";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        if (args.length < 3) {
            sender.sendMessage(getColoredSyntax());
            return;
        }

        GroupType groupType;
        try {
            groupType = GroupType.valueOf(args[1].toUpperCase());
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid Group");
            return;
        }

        switch (groupType) {
            case DOLPHIN:
                AFKRewards.getInstance().getAfkManager().getDolphinGroup().updateRegion((Player) sender, args[2]);
                break;
            case PIG:
                AFKRewards.getInstance().getAfkManager().getPigGroup().updateRegion((Player) sender, args[2]);
                break;
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], GroupType.values, list);
        }

        return list;
    }

}