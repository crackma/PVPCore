package me.crackma.pvpcore.shop.implementation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.shop.Category;
import me.crackma.pvpcore.shop.CategoryItem;

public class CategoryGui extends Gui {
    private PVPCorePlugin plugin;
    private final Category category;
    public CategoryGui(PVPCorePlugin plugin, Category category) {
        super();
        this.plugin = plugin;
        this.category = category;
    }
    @Override
    public Inventory createInventory() {
    	return Bukkit.createInventory(null, PVPCorePlugin.getPlugin().getConfig().getInt("shop_gui_size"));
    }
    @Override
    public void decorate() {
        for (CategoryItem item : category.getItems()) {
            addItem(item);
        }
        super.decorate();
    }
    private void addItem(CategoryItem categoryItem) {
        ItemStack itemStack = new ItemStack(categoryItem.getItemStack().getType());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + categoryItem.getName());
        List<String> meta = new ArrayList<>();
        meta.add(categoryItem.getPrice() == 1 ? "§ePrice: §d" + categoryItem.getPrice() + " gem§e." : "§ePrice: §d" + categoryItem.getPrice() + " gems§e.");
        meta.add("§7Left click to buy.");
        meta.add("§7Right click for more options.");
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.addButton(categoryItem.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                leftConsumer(event -> {
                    plugin.getShopManager().giveCategoryItem(categoryItem, event.getWhoClicked(), 1);
                }).rightConsumer(event -> {
                    plugin.getGuiManager().openGUI(new ItemGui(plugin, categoryItem), event.getWhoClicked());
                }));
        super.decorate();
    }
}
