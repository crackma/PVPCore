package me.crackma.pvpcore.shop.implementation;

import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.shop.Category;
import me.crackma.pvpcore.shop.CategoryItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CategoryGui extends Gui {

    private final Category category;

    public CategoryGui(Category category) {
        super();
        this.category = category;
    }

    @Override
    public Inventory createInventory() {
        Inventory inventory = Bukkit.createInventory(null, PVPCorePlugin.getInstance().getConfig().getInt("shop_gui_size"));
        return inventory;
    }

    @Override
    public void decorate() {
        for (CategoryItem item : category.getItems()) {
            addItem(item);
        }
        super.decorate();
    }

    private void addItem(CategoryItem item) {
        ItemStack itemStack = new ItemStack(item.getItemStack().getType());
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§d" + item.getName());
        List<String> meta = new ArrayList<>();
        meta.add(item.getPrice() == 1 ? "§ePrice: §d" + item.getPrice() + " gem§e." : "§ePrice: §d" + item.getPrice() + " gems§e.");
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.addButton(item.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                leftConsumer(event -> {
                    item.giveTo(event.getWhoClicked());
                }));
        super.decorate();
    }
}
