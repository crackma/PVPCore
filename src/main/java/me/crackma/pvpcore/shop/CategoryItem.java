package me.crackma.pvpcore.shop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import lombok.Getter;
@Getter
public class CategoryItem {
    private String name;
    private ItemStack itemStack;
    private int inventorySlot, price;
    public CategoryItem(String name, ItemStack itemStack, int inventorySlot, int price) {
        this.name = name;
        this.itemStack = itemStack;
        this.inventorySlot = inventorySlot;
        this.price = price;
    }
    public CategoryItem(String fromString) {
        String[] categoryItem = fromString.split(",");
        this.name = categoryItem[0];
        this.inventorySlot = Integer.parseInt(categoryItem[2]);
        this.price = Integer.parseInt(categoryItem[3]);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(categoryItem[1]));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            this.itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return name + "," + Base64Coder.encodeLines(outputStream.toByteArray()) + "," + inventorySlot + "," + price;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ItemStack prepareForShop(int amount) {
        ItemStack itemStack = this.itemStack.clone();
        itemStack.setAmount(amount);
        int newPrice = price * amount;
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + name);
        List<String> itemLore = new ArrayList<>();
        itemLore.add("§ePrice: §d" + newPrice + (newPrice == 1 ? " gem§e." : " gems§e."));
        itemLore.add("§7Left click to buy.");
        itemLore.add("§7Right click for more options.");
        itemMeta.setLore(itemLore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
