package me.crackma.pvpcore.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class Kit {
  private final String name;
  private int cooldown, inventorySlot;
  private Material displayItem;
  private ItemStack[] items;
  public Kit(String name, int cooldown, int inventorySlot, Material displayItem, ItemStack[] items) {
    this.name = name;
    this.cooldown = cooldown;
    this.inventorySlot = inventorySlot;
    this.displayItem = displayItem;
    this.items = items;
  }
}
