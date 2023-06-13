package me.frandma.pvpcore.kits.implementation;

import me.frandma.pvpcore.PVPCorePlugin;
import me.frandma.pvpcore.gui.Gui;
import me.frandma.pvpcore.gui.GuiButton;
import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitGui extends Gui {
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
        itemMeta.setDisplayName("§d" + kit.getName() + " §ekit. §7(Click to claim)");
        itemMeta.setLore(null);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        this.addButton(kit.getInventorySlot(), new GuiButton().
                creator(unused -> itemStack).
                consumer(event -> kit.giveTo(event.getWhoClicked())));
        super.decorate();
    }
}
