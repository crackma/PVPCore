package me.crackma.pvpcore.kits;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    public Kit(String name, int cooldown, int inventorySlot, String displayItem, String base64, PVPCorePlugin plugin) {
        this.name = name;
        this.cooldown = cooldown;
        this.inventorySlot = inventorySlot;
        this.displayItem = Material.valueOf(displayItem);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            this.items = items;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            this.items = null;
        }
    }
    public String getItemsAsBase64() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
