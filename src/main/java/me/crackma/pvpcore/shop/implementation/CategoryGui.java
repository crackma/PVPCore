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
    	return Bukkit.createInventory(null, (PVPCorePlugin.getInstance().getConfig().getInt("kit_gui_rows")) * 9);
    }
    @Override
    public void decorate() {
        for (CategoryItem categoryItem : category.getItems()) {
            putButton(categoryItem.getInventorySlot(), new GuiButton().
                creator(categoryItem.prepareForShop(1)).
                leftConsumer(event -> {
                    plugin.getShopManager().giveCategoryItem(categoryItem, event.getWhoClicked(), 1);
                }).rightConsumer(event -> {
                    plugin.getGuiManager().openGUI(new ItemGui(plugin, categoryItem), event.getWhoClicked());
                }));
        }
        super.decorate();
    }
}
