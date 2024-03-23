package me.crackma.pvpcore.gui;

import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiListener implements Listener {
  private PVPCorePlugin plugin;
  public GuiListener(PVPCorePlugin plugin) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }
  @EventHandler
  public void onOpen(InventoryOpenEvent event) {
    plugin.getGuiManager().onOpen(event);
  }
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    plugin.getGuiManager().onClick(event);
  }
  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    plugin.getGuiManager().onClose(event);
  }
}

