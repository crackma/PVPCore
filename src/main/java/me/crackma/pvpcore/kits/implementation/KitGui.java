package me.crackma.pvpcore.kits.implementation;

import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitGui extends Gui {
  private final Kit kit;
  public KitGui(Kit kit) {
    this.kit = kit;
  }
  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 27, "Kit " + kit.getName());
  }
  @Override
  public void decorate() {
    int slot = 0;
    for (ItemStack itemStack : kit.getItems()) {
      putButton(slot, new GuiButton().
          creator(itemStack));
      slot++;
    }
    super.decorate();
  }
}