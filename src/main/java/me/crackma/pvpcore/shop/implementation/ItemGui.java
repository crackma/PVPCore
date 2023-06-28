package me.crackma.pvpcore.shop.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.shop.Category;
import me.crackma.pvpcore.shop.CategoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemGui extends Gui {
    private PVPCorePlugin plugin;
    private final CategoryItem categoryItem;
    public ItemGui(PVPCorePlugin plugin, CategoryItem categoryItem) {
        super();
        this.plugin = plugin;
        this.categoryItem = categoryItem;
    }
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, 27);
    }
    @Override
    public void decorate() {
        addItem(10, 1);
        addItem(11, 2);
        addItem(12, 4);
        addItem(13, 8);
        addItem(14, 16);
        addItem(15, 32);
        addItem(16, 64);
        super.decorate();
    }
    private void addItem(int inventorySlot, int amount) {
        ItemStack itemStack = new ItemStack(categoryItem.getItemStack().getType());
        itemStack.setAmount(amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + categoryItem.getName());
        List<String> meta = new ArrayList<>();
        int newPrice = categoryItem.getPrice() * amount;
        meta.add(newPrice == 1 ? "§ePrice: §d" + newPrice + " gem§e." : "§ePrice: §d" + newPrice + " gems§e.");
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.addButton(inventorySlot, new GuiButton().
                creator(unused -> itemStack).
                leftConsumer(event -> {
                    plugin.getShopManager().giveCategoryItem(categoryItem, event.getWhoClicked(), amount);
                }));
        super.decorate();
    }
}
