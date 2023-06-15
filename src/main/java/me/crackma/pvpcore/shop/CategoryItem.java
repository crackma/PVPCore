package me.crackma.pvpcore.shop;

import lombok.Getter;
import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
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
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decode(categoryItem[1]));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            this.itemStack = (ItemStack) dataInput.readObject();
            dataInput.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        this.inventorySlot = Integer.parseInt(categoryItem[2]);
        this.price = Integer.parseInt(categoryItem[2]);
    }

    public void giveTo(HumanEntity player) {
        User user = UserManager.getUser(player.getUniqueId());
        if (user.getStats().getGems() < price) {
            player.sendMessage("§cYou cannot afford that.");
            return;
        }
        Stats stats = user.getStats();
        stats.setGems(stats.getGems() - price);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            return;
        }
        player.getInventory().addItem(itemStack);
    }

    public String toString() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(itemStack);
            dataOutput.close();
            Bukkit.broadcastMessage(Base64Coder.encodeLines(outputStream.toByteArray()));
            return name + "," + Base64Coder.encodeLines(outputStream.toByteArray()) + "," + inventorySlot + "," + price;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
