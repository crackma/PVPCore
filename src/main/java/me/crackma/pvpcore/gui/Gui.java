package me.crackma.pvpcore.gui;

import lombok.Getter;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Gui {
    @Getter
    private final Inventory inventory;
    @Getter
    private final Map<Integer, GuiButton> buttonMap = new HashMap<>();
    public Gui() {
        this.inventory = createInventory();
    }
    public abstract Inventory createInventory();
    public void addButton(int slot, GuiButton button) {
        this.buttonMap.put(slot, button);
    }
    public void decorate() {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack itemStack = button.getItemStackCreator().apply(null);
            this.inventory.setItem(slot, itemStack);
        });
    }
    public void onOpen(InventoryOpenEvent event) {
        decorate();
    }
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;
        int slot = event.getSlot();
        GuiButton button = this.buttonMap.get(slot);
        if (button == null) return;
        if (event.getClick() == ClickType.LEFT) {
            if (button.getLeftEventConsumer() == null) return;
            button.getLeftEventConsumer().accept(event);
        } else if (event.getClick() == ClickType.RIGHT) {
            if (button.getRightEventConsumer() == null) return;
            button.getRightEventConsumer().accept(event);
        }
    }
    public void onClose(InventoryCloseEvent event) {}
}
