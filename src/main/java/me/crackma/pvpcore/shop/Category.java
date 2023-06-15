package me.crackma.pvpcore.shop;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Category {

    @Getter
    private final String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Material displayItem;
    @Getter
    @Setter
    private int inventorySlot;

    @Getter
    private Set<CategoryItem> items = new HashSet<>();

    public Category(String name, String description, Material displayItem, int inventorySlot) {
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.inventorySlot = inventorySlot;
    }

    public Category(String name, String description, Material displayItem, int inventorySlot, String items) {
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.inventorySlot = inventorySlot;
        String[] itemsArray = items.split(";");
        for (String string : itemsArray) this.items.add(new CategoryItem(string));
    }

    public String getItemsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        items.forEach(item -> stringBuilder.append(item.toString() + ";"));
        Bukkit.getLogger().info(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public void addItem(CategoryItem item) {
        items.add(item);
    }

    public void removeItem(String name) {
        name = name.toLowerCase();
        for (CategoryItem item : items) {
            String categoryName = item.getName().toLowerCase();
            if (categoryName.equals(name)) items.remove(item);
        }
    }
}
