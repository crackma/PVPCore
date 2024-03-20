package me.crackma.pvpcore.shop;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ShopManager {
	@Getter
    private PVPCorePlugin plugin;
    @Getter
    private Set<Category> categorySet = new HashSet<>();
    public ShopManager(PVPCorePlugin plugin) {
    	this.plugin = plugin;
        plugin.getShopDatabase().fetchCategories().thenAccept(categories -> {
            categorySet = categories;
        });
    }
    public void addCategory(Category category) {
        categorySet.add(category);
    }
    public void giveCategoryItem(CategoryItem categoryItem, HumanEntity player, int amount) {
        User user = plugin.getUserManager().get(player.getUniqueId());
        if (user.getStats().getGems() < categoryItem.getPrice()) {
            player.sendMessage("§cYou cannot afford that.");
            return;
        }
        Stats stats = user.getStats();
        int price = categoryItem.getPrice() * amount;
        stats.setGems(stats.getGems() - price);
        plugin.getUserManager().updateBoard(user);
        plugin.getUserDatabase().updateOne(user);
        String gemOrGems = price == 1 ? "gem" : "gems";
        player.sendMessage("§eYou bought §d" + amount + "§e of §d" + categoryItem.getName() + " §efor §d" + price + " " + gemOrGems + "§e.");
        ItemStack itemStack = new ItemStack(categoryItem.getItemStack());
        itemStack.setAmount(amount);
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            return;
        }
        player.getInventory().addItem(itemStack);
    }
    public Category getCategory(String name) {
        name = name.toLowerCase();
        for (Category category : categorySet) {
            String categoryName = category.getName().toLowerCase();
            if (categoryName.equals(name)) return category;
        }
        return null;
    }
    public boolean exists(Category category) {
        return categorySet.contains(category);
    }
    public void deleteCategory(Category category) {
        categorySet.remove(category);
    }
}
