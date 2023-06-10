package me.frandma.pvpcore.shop;

import lombok.Getter;
import me.frandma.pvpcore.user.Stats;
import me.frandma.pvpcore.user.User;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class CategoryItem {

    @Getter
    private String name;
    @Getter
    private ItemStack itemStack;
    @Getter
    private int inventorySlot, price;

    public CategoryItem(ItemStack itemStack, String name, int inventorySlot, int price) {
        this.itemStack = itemStack;
        this.name = name;
        this.inventorySlot = inventorySlot;
        this.price = price;
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
}
