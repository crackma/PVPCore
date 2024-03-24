package me.crackma.pvpcore.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {
  private Map<Inventory, Gui> guiMap = new HashMap<>();
  public void openGUI(Gui gui, HumanEntity player) {
    gui.setInventory(gui.createInventory());
    Inventory inventory = gui.getInventory();
    addGui(inventory, gui);
    player.openInventory(inventory);
  }
  public void addGui(Inventory inventory, Gui gui) {
    guiMap.put(inventory, gui);
  }
  public void onOpen(InventoryOpenEvent event) {
    Inventory inventory = event.getInventory();
    if (!guiMap.containsKey(inventory)) return;
    guiMap.get(inventory).onOpen(event);
  }
  public void onClick(InventoryClickEvent event) {
    Inventory inventory = event.getInventory();
    if (!guiMap.containsKey(inventory)) return;
    guiMap.get(inventory).onClick(event);
  }
  public void onClose(InventoryCloseEvent event) {
    Inventory inventory = event.getInventory();
    if (!guiMap.containsKey(inventory)) return;
    guiMap.get(inventory).onClose(event);
    guiMap.remove(inventory);
  }
}
