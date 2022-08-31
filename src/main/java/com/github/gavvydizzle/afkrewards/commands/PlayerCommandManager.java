package com.github.gavvydizzle.afkrewards.commands;

import com.github.gavvydizzle.afkrewards.commands.playercommands.HelpCommand;
import com.github.gavvydizzle.afkrewards.commands.playercommands.OpenGroupSelectionCommand;
import com.github.gavvydizzle.afkrewards.commands.playercommands.OpenLeaderboardCommand;
import com.github.gavvydizzle.afkrewards.commands.playercommands.ViewNextTokenTimeCommand;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommandManager implements TabExecutor {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();
    private final ArrayList<String> subcommandStrings = new ArrayList<>();

    public PlayerCommandManager() {
        subcommands.add(new HelpCommand());
        subcommands.add(new OpenGroupSelectionCommand());
        subcommands.add(new OpenLeaderboardCommand());
        subcommands.add(new ViewNextTokenTimeCommand());

        for (SubCommand subCommand : subcommands) {
            subcommandStrings.add(subCommand.getName());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {

                    SubCommand subCommand = subcommands.get(i);
                    subCommand.perform(sender, args);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "Invalid command");
        }
        sender.sendMessage(ChatColor.YELLOW + "Use '/religion help' to see a list of valid commands");

        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], subcommandStrings, subcommandsArguments);

            return subcommandsArguments;
        }
        else if (args.length >= 2) {
            for (SubCommand subcommand : subcommands) {
                if (args[0].equalsIgnoreCase(subcommand.getName())) {
                    return subcommand.getSubcommandArguments((Player) sender, args);
                }
            }
        }

        return null;
    }
}
