package me.crackma.pvpcore.shop.implementation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.shop.Category;

public class ShopGui extends Gui {
    private PVPCorePlugin plugin;
    public ShopGui(PVPCorePlugin plugin) {
        super();
        this.plugin = plugin;
    }
    @Override
    public Inventory createInventory() {
    	return Bukkit.createInventory(null, (PVPCorePlugin.getInstance().getConfig().getInt("kit_gui_rows")) * 9);
    }
    @Override
    public void decorate() {
        plugin.getShopManager().getCategorySet().forEach(category -> addCategory(category));
        super.decorate();
    }
    private void addCategory(Category category) {
        ItemStack itemStack = new ItemStack(category.getDisplayItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + category.getName());
        List<String> meta = new ArrayList<>();
        meta.add(ChatColor.translateAlternateColorCodes('&',"§e" + category.getDescription()));
        itemMeta.setLore(meta);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(itemMeta);
        this.putButton(category.getInventorySlot(), new GuiButton().
                creator(itemStack).
                leftConsumer(event -> plugin.getGuiManager().openGUI(new CategoryGui(plugin, category), event.getWhoClicked())));
        super.decorate();
    }
}
