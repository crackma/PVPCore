package me.crackma.pvpcore.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiListener implements Listener {
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        GuiManager.onOpen(event);
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        GuiManager.onClick(event);
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        GuiManager.onClose(event);
    }
}
