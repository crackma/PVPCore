package me.crackma.pvpcore.shop;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CategoryItem {
    @Getter
    private String name;
    @Getter
    private ItemStack itemStack;
    @Getter
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
}
