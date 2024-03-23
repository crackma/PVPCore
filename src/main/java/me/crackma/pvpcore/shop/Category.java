package me.crackma.pvpcore.shop;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Category {
    private final String name;
    @Setter
    private String description;
    @Setter
    private Material displayItem;
    @Setter
    private int inventorySlot;
    private Set<CategoryItem> items = new HashSet<>();
    public Category(String name, String description, Material displayItem, int inventorySlot) {
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.inventorySlot = inventorySlot;
    }
    public Category(String name, String description, String displayItem, int inventorySlot, String items) {
        this.name = name;
        this.description = description;
        this.displayItem = Material.valueOf(displayItem);
        this.inventorySlot = inventorySlot;
        if (items.trim().isEmpty()) return;
        String[] itemsArray = items.split(";");
        for (String string : itemsArray) {
            this.items.add(new CategoryItem(string));
        }
    }
    public String getItemsAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        items.forEach(item -> stringBuilder.append(item.toString() + ";"));
        return stringBuilder.toString();
    }
    public void addItem(CategoryItem item) {
        items.add(item);
    }
    public void removeItem(String name) {
        name = name.toLowerCase();
        for (Iterator<CategoryItem> iterator = items.iterator(); iterator.hasNext();) {
            CategoryItem categoryItem = iterator.next();
            String categoryItemName = categoryItem.getName().toLowerCase();
            if (categoryItemName.equals(name)) {
                items.remove(categoryItem);
                return;
            }
        }
    }
}
