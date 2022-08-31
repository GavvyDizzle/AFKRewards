package com.github.gavvydizzle.afkrewards.commands.playercommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Opens this help menu";
    }

    @Override
    public String getSyntax() {
        return "/religion help";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        player.sendMessage("-----(Religion Commands)-----");
        ArrayList<SubCommand> subCommands = AFKRewards.getInstance().getPlayerCommandManager().getSubcommands();
        for (SubCommand subCommand : subCommands) {
            player.sendMessage(ChatColor.GOLD + subCommand.getSyntax() + " - " + ChatColor.YELLOW + subCommand.getDescription());
        }
        player.sendMessage("-----(Religion Commands)-----");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}