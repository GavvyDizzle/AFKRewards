package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.group.Group;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminValidateRegionCommand extends SubCommand {

    @Override
    public String getName() {
        return "validateRegion";
    }

    @Override
    public String getDescription() {
        return "Tells you what group region(s) you are currently in";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin validateRegion";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;

        Group dolphinGroup = AFKRewards.getInstance().getAfkManager().getDolphinGroup();
        Group pigGroup = AFKRewards.getInstance().getAfkManager().getPigGroup();

        boolean isInDolphin = dolphinGroup.isPlayerInRegion((Player) sender);
        boolean isInPig = pigGroup.isPlayerInRegion((Player) sender);

        if (isInDolphin && isInPig) {
            sender.sendMessage(ChatColor.GREEN + "You are both the DOLPHIN and PIG AFK zones");
        } else if (isInDolphin) {
            sender.sendMessage(ChatColor.GREEN + "You are in the DOLPHIN AFK zone");
        } else if (isInPig) {
            sender.sendMessage(ChatColor.GREEN + "You are in the PIG AFK zone");
        } else {
            sender.sendMessage(ChatColor.RED + "You are not in either AFK zone");
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}