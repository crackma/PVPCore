package me.crackma.pvpcore.kits.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.kits.Kit;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitsGui extends Gui {
    private PVPCorePlugin plugin;
    public KitsGui(PVPCorePlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public Inventory createInventory() {
    	return Bukkit.createInventory(null, PVPCorePlugin.getPlugin().getConfig().getInt("kit_gui_size"));
    }
    private void addKit(Kit kit) {
        ItemStack itemStack = new ItemStack(kit.getDisplayItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + kit.getName() + " §ekit.");
        List<String> lore = new ArrayList<>();
        lore.add("§eCooldown: §d" + Utils.formatToDate(kit.getCooldown()) + "§e.");
        lore.add("§7Left click to claim.");
        lore.add("§7Right click to view contents.");
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        addButton(kit.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                leftConsumer(event -> plugin.getKitManager().giveKit(event.getWhoClicked(), kit)).
                rightConsumer(event -> plugin.getGuiManager().openGUI(new KitGui(kit), event.getWhoClicked())));
        super.decorate();
    }
    @Override
    public void decorate() {
        plugin.getKitManager().getKitSet().forEach(this::addKit);
        super.decorate();
    }
}
