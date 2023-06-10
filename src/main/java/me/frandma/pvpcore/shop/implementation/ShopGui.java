package me.frandma.pvpcore.shop.implementation;

import me.frandma.pvpcore.PVPCorePlugin;
import me.frandma.pvpcore.gui.Gui;
import me.frandma.pvpcore.gui.GuiButton;
import me.frandma.pvpcore.gui.GuiManager;
import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitManager;
import me.frandma.pvpcore.shop.Category;
import me.frandma.pvpcore.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopGui extends Gui {
    
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, PVPCorePlugin.getInstance().getConfig().getInt("shop_gui_size"));
    }

    @Override
    public void decorate() {
        ShopManager.getCategorySet().forEach(category -> addCategory(category));
        super.decorate();
    }

    private void addCategory(Category category) {
        ItemStack itemStack = new ItemStack(category.getDisplayItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + category.getName());
        List<String> meta = new ArrayList<>();
        meta.add(ChatColor.translateAlternateColorCodes('&', category.getDescription()));
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.addButton(category.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                consumer(event -> GuiManager.openGUI(new CategoryGui(category), event.getWhoClicked())));
        super.decorate();
    }
}
