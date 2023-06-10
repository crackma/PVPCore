package me.frandma.pvpcore.shop.implementation;

import me.frandma.pvpcore.PVPCorePlugin;
import me.frandma.pvpcore.gui.Gui;
import me.frandma.pvpcore.gui.GuiButton;
import me.frandma.pvpcore.shop.Category;
import me.frandma.pvpcore.shop.CategoryItem;
import me.frandma.pvpcore.user.User;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CategoryGui extends Gui {

    private Category category;

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
        ItemStack itemStack = item.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + item.getName());
        List<String> meta = new ArrayList<>();
        meta.add(item.getPrice() == 1 ? "§ePrice: §d" + item.getPrice() + " gem§e." : "§ePrice: §d" + item.getPrice() + " gems§e.");
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.addButton(item.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                consumer(event -> {
                    item.giveTo(event.getWhoClicked());
                }));
        super.decorate();
    }
}
