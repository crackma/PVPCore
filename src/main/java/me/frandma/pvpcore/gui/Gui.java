package me.frandma.pvpcore.gui;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
        int slot = event.getSlot();
        GuiButton button = this.buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    public void onClose(InventoryCloseEvent event) {}

}
