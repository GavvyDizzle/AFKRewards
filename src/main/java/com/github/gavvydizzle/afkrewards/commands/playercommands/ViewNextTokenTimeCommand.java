package com.github.gavvydizzle.afkrewards.commands.playercommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.mittenmc.serverutils.Numbers;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ViewNextTokenTimeCommand extends SubCommand {

    @Override
    public String getName() {
        return "nextToken";
    }

    @Override
    public String getDescription() {
        return "Tells you how long until your next Zen Token";
    }

    @Override
    public String getSyntax() {
        return "/religion nextToken";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        String msg = AFKRewards.getInstance().getAfkManager().getNextTokenMessage();
        if (!msg.trim().isEmpty()) {
            sender.sendMessage(msg.replace("{time}", Numbers.getTimeFormatted(AFKRewards.getInstance().getAfkManager().getSecondsUntilNextToken((Player) sender), "0s")));
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}