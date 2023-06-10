package me.frandma.pvpcore.shop;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<CategoryItem> items = new ArrayList<>();

    public Category(String name, String description, Material displayItem, int inventorySlot) {
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.inventorySlot = inventorySlot;
    }

    public Category(String name, String description, Material displayItem, int inventorySlot, String base64) {
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.inventorySlot = inventorySlot;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            CategoryItem[] items = new CategoryItem[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (CategoryItem) dataInput.readObject();
            }
            dataInput.close();
            this.items = Arrays.asList(items);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            this.items = null;
        }
    }

    public String toBase64() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(getItems().length);

            for (CategoryItem item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addItem(CategoryItem item) {
        items.add(item);
    }

    public CategoryItem[] getItems() {
        return items.toArray(new CategoryItem[items.size()]);
    }

    public void removeItem(String name) {
        name = name.toLowerCase();
        for (CategoryItem item : items) {
            String categoryName = item.getName().toLowerCase();
            if (categoryName.equals(name)) items.remove(item);
        }
    }
}
