package me.crackma.pvpcore.kits;

import lombok.Getter;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class Kit {

    @Getter
    private final String name;

    @Getter
    private ItemStack[] items;

    @Getter
    private int cooldown, inventorySlot;

    @Getter
    private Material displayItem;


    public Kit(String name, int cooldown, int inventoryPosition, Material displayItem, ItemStack[] items) {
        this.name = name;
        this.cooldown = cooldown;
        this.inventorySlot = inventoryPosition;
        this.displayItem = displayItem;
        this.items = items;
    }
    public Kit(String name, int cooldown, int inventoryPosition, Material displayItem, String base64) {
        this.name = name;
        this.cooldown = cooldown;
        this.inventorySlot = inventoryPosition;
        this.displayItem = displayItem;
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

    public String toBase64() {
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

    public void giveTo(HumanEntity player) {
        User user = UserManager.getUser(player.getUniqueId());
        if (!user.getStats().canClaim(this)) {
            long currentTime = new Date().getTime();
            long cooldownTime = user.getStats().getCooldown(this);
            player.sendMessage("§cYou cannot claim this kit. Time left: " + Utils.formatToDate(cooldownTime - currentTime) + ".");
            return;
        }
        for (ItemStack item : items) {
            if (item == null) continue;
            //-1 is returned by #firstEmpty() when an inventory is full
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                continue;
            }
            player.getInventory().addItem(item);
        }
        user.getStats().addCooldown(this);
    }
}
