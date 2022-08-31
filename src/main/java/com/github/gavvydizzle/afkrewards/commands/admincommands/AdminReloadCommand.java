package com.github.gavvydizzle.afkrewards.commands.admincommands;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.configs.GUIConfig;
import com.github.mittenmc.serverutils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads this plugin";
    }

    @Override
    public String getSyntax() {
        return "/afkadmin reload";
    }

    @Override
    public String getColoredSyntax() {
        return ChatColor.YELLOW + "Usage: " + getSyntax();
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        AFKRewards.getInstance().reloadConfig();
        GUIConfig.reload();

        try {
            AFKRewards.getInstance().getAfkManager().reload();
            AFKRewards.getInstance().getInventoryManager().reloadAllGUIs();
            sender.sendMessage(ChatColor.GREEN + "[AFKRewards] Reloaded");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "[AFKRewards] Failed to reload. Check the console for errors");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return new ArrayList<>();
    }

}
