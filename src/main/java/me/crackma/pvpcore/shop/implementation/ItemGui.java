package me.crackma.pvpcore.shop.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.shop.CategoryItem;
import me.crackma.pvpcore.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ItemGui extends Gui {
    private ShopManager shopManager;
    private final CategoryItem categoryItem;
    public ItemGui(PVPCorePlugin plugin, CategoryItem categoryItem) {
        super();
        this.shopManager = plugin.getShopManager();
        this.categoryItem = categoryItem;
    }
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, 27, categoryItem.getName());
    }
    @Override
    public void decorate() {
        int slot = 10;
        int amount = 1;
        while (amount < 64) {
            int finalAmount = amount;
            putButton(slot, new GuiButton().
                creator(categoryItem.prepareForShop(amount)).
                leftConsumer(event -> shopManager.giveCategoryItem(categoryItem, event.getWhoClicked(), finalAmount)));
            slot++;
            amount = amount * 2;
        }
        super.decorate();
    }

}
