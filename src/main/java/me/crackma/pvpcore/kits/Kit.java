package me.crackma.pvpcore.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public class Kit {
    @Getter
    private final String name;
    @Getter
    private int cooldown, inventorySlot;
    @Getter
    private Material displayItem;
    @Getter
    private ItemStack[] items;
    public Kit(String name, int cooldown, int inventorySlot, Material displayItem, ItemStack[] items) {
        this.name = name;
        this.cooldown = cooldown;
        this.inventorySlot = inventorySlot;
        this.displayItem = displayItem;
        this.items = items;
    }
}
