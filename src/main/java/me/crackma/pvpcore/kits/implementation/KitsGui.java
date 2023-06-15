package me.crackma.pvpcore.kits.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.gui.GuiManager;
import me.crackma.pvpcore.kits.Kit;
import me.crackma.pvpcore.kits.KitManager;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitsGui extends Gui {
    @Override
    public Inventory createInventory() {
        return Bukkit.createInventory(null, PVPCorePlugin.getInstance().getConfig().getInt("kit_gui_size"));
    }

    @Override
    public void decorate() {
        KitManager.getKitSet().forEach(kit -> addKit(kit));
        super.decorate();
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
        this.addButton(kit.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                leftConsumer(event -> kit.giveTo(event.getWhoClicked())).
                rightConsumer(event -> GuiManager.openGUI(new KitGui(kit), event.getWhoClicked())));
        super.decorate();
    }
}
