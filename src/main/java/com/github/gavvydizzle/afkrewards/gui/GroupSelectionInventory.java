package com.github.gavvydizzle.afkrewards.gui;

import com.github.gavvydizzle.afkrewards.AFKRewards;
import com.github.gavvydizzle.afkrewards.configs.GUIConfig;
import com.github.mittenmc.serverutils.ColoredItems;
import com.github.mittenmc.serverutils.Colors;
import com.github.mittenmc.serverutils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GroupSelectionInventory implements ClickableGUI {

    private Inventory notInGroupInventory, inGroupInventory;
    private int dolphinSlot, pigSlot;

    public void reload() {
        FileConfiguration config = GUIConfig.get();
        config.addDefault("selectionInventory.noGroup.name", "Select Religion");
        config.addDefault("selectionInventory.noGroup.rows", 3);
        config.addDefault("selectionInventory.noGroup.filler", "black");
        config.addDefault("selectionInventory.noGroup.items.dolphin.slot", 11);
        config.addDefault("selectionInventory.noGroup.items.dolphin.material", "DIRT");
        config.addDefault("selectionInventory.noGroup.items.dolphin.name", "Dolphins");
        config.addDefault("selectionInventory.noGroup.items.dolphin.lore", new ArrayList<>());
        config.addDefault("selectionInventory.noGroup.items.pig.slot", 15);
        config.addDefault("selectionInventory.noGroup.items.pig.material", "DIRT");
        config.addDefault("selectionInventory.noGroup.items.pig.name", "Pigs");
        config.addDefault("selectionInventory.noGroup.items.pig.lore", new ArrayList<>());

        config.addDefault("selectionInventory.inGroup.name", "Religion Information");
        config.addDefault("selectionInventory.inGroup.rows", 3);
        config.addDefault("selectionInventory.inGroup.filler", "white");
        config.addDefault("selectionInventory.inGroup.items.dolphin.slot", 11);
        config.addDefault("selectionInventory.inGroup.items.dolphin.material", "DIRT");
        config.addDefault("selectionInventory.inGroup.items.dolphin.name", "Dolphins");
        config.addDefault("selectionInventory.inGroup.items.dolphin.lore", new ArrayList<>());
        config.addDefault("selectionInventory.inGroup.items.pig.slot", 15);
        config.addDefault("selectionInventory.inGroup.items.pig.material", "DIRT");
        config.addDefault("selectionInventory.inGroup.items.pig.name", "Pigs");
        config.addDefault("selectionInventory.inGroup.items.pig.lore", new ArrayList<>());

        String inventoryName = Colors.conv(config.getString("selectionInventory.noGroup.name"));
        int inventorySize = config.getInt("selectionInventory.noGroup.rows") * 9;
        ItemStack filler = ColoredItems.getGlassByName(config.getString("selectionInventory.noGroup.filler"));

        notInGroupInventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        for (int i = 0; i < inventorySize; i++) {
            notInGroupInventory.setItem(i, filler);
        }

        dolphinSlot = config.getInt("selectionInventory.noGroup.items.dolphin.slot");
        ItemStack dolphinItem = new ItemStack(ConfigUtils.getMaterial(config.getString("selectionInventory.noGroup.items.dolphin.material")));
        ItemMeta meta =  dolphinItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Colors.conv(config.getString("selectionInventory.noGroup.items.dolphin.name")));
        meta.setLore(Colors.conv(config.getStringList("selectionInventory.noGroup.items.dolphin.lore")));
        dolphinItem.setItemMeta(meta);
        try {
            notInGroupInventory.setItem(dolphinSlot, dolphinItem);
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The slot at gui.yml selectionInventory.noGroup.items.dolphin.slot is out of bounds");
        }

        pigSlot = config.getInt("selectionInventory.noGroup.items.pig.slot");
        ItemStack pigItem = new ItemStack(ConfigUtils.getMaterial(config.getString("selectionInventory.noGroup.items.pig.material")));
        meta =  pigItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Colors.conv(config.getString("selectionInventory.noGroup.items.pig.name")));
        meta.setLore(Colors.conv(config.getStringList("selectionInventory.noGroup.items.pig.lore")));
        pigItem.setItemMeta(meta);
        try {
            notInGroupInventory.setItem(pigSlot, pigItem);
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The slot at gui.yml selectionInventory.noGroup.items.pig.slot is out of bounds");
        }


        inventoryName = Colors.conv(config.getString("selectionInventory.inGroup.name"));
        inventorySize = config.getInt("selectionInventory.inGroup.rows") * 9;
        filler = ColoredItems.getGlassByName(config.getString("selectionInventory.inGroup.filler"));

        inGroupInventory = Bukkit.createInventory(null, inventorySize, inventoryName);
        for (int i = 0; i < inventorySize; i++) {
            inGroupInventory.setItem(i, filler);
        }

        int dolphinSlot2 = config.getInt("selectionInventory.noGroup.items.dolphin.slot");
        ItemStack dolphinItem2 = new ItemStack(ConfigUtils.getMaterial(config.getString("selectionInventory.inGroup.items.dolphin.material")));
        meta =  dolphinItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Colors.conv(config.getString("selectionInventory.inGroup.items.dolphin.name")));
        meta.setLore(Colors.conv(config.getStringList("selectionInventory.inGroup.items.dolphin.lore")));
        dolphinItem2.setItemMeta(meta);
        try {
            inGroupInventory.setItem(dolphinSlot2, dolphinItem2);
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The slot at gui.yml selectionInventory.inGroup.items.dolphin.slot is out of bounds");
        }

        int pigSlot2 = config.getInt("selectionInventory.noGroup.items.pig.slot");
        ItemStack pigItem2 = new ItemStack(ConfigUtils.getMaterial(config.getString("selectionInventory.inGroup.items.pig.material")));
        meta =  pigItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Colors.conv(config.getString("selectionInventory.inGroup.items.pig.name")));
        meta.setLore(Colors.conv(config.getStringList("selectionInventory.inGroup.items.pig.lore")));
        pigItem2.setItemMeta(meta);
        try {
            inGroupInventory.setItem(pigSlot2, pigItem2);
        } catch (Exception e) {
            AFKRewards.getInstance().getLogger().warning("The slot at gui.yml selectionInventory.inGroup.items.pig.slot is out of bounds");
        }
    }

    @Override
    public void openInventory(Player player) {
        if (AFKRewards.getInstance().getAfkManager().isInGroup(player)) {
            player.openInventory(inGroupInventory);
        }
        else {
            player.openInventory(notInGroupInventory);
        }
        AFKRewards.getInstance().getInventoryManager().setClickableGUI(player, this);
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        if (e.getSlot() != dolphinSlot && e.getSlot() != pigSlot) return;
        if (AFKRewards.getInstance().getAfkManager().isInGroup((Player) e.getWhoClicked())) return;

        if (e.getSlot() == dolphinSlot) {
            AFKRewards.getInstance().getAfkManager().getDolphinGroup().onPlayerJoinGroup((Player) e.getWhoClicked());
        }
        else if (e.getSlot() == pigSlot) {
            AFKRewards.getInstance().getAfkManager().getPigGroup().onPlayerJoinGroup((Player) e.getWhoClicked());
        }

        e.getWhoClicked().closeInventory();
    }
}
