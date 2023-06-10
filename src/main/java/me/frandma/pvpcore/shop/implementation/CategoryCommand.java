package me.frandma.pvpcore.shop.implementation;

import me.frandma.pvpcore.shop.Category;
import me.frandma.pvpcore.shop.CategoryItem;
import me.frandma.pvpcore.shop.ShopDatabase;
import me.frandma.pvpcore.shop.ShopManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;

public class CategoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;
        Category category;
        switch (args[0].toLowerCase()) {
            case "help":
                sender.sendMessage("§e/category create <name> <displayItem> <inventorySlot> <description>\n" +
                                      "§d/category edit <name> displayItem/inventorySlot/description <new> \n" +
                                      "§e/category addItem <categoryName> <itemName> <inventorySlot> <price> (hold an item) \n" +
                                      "§d/category removeItem <categoryName> <itemName>\n" +
                                      "§e/category remove <name>\n");
                return true;
            case "create":
                if (args.length < 5) return false;
                if (ShopManager.getCategory(args[1]) != null) {
                    sender.sendMessage("§cA category with that name already exists.");
                    return true;
                }
                String description = convertArray(args, " ", 4);
                category = new Category(args[1], description, Material.valueOf(args[2]), Integer.parseInt(args[3]));
                ShopManager.addCategory(category);
                ShopDatabase.insertCategory(category);
                sender.sendMessage("§eCreated category §d" + args[1] + "§e.");
                return true;
            case "edit":
                if (args.length < 4) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                return true;
            case "additem":
                if (!(sender instanceof Player)) return true;
                if (args.length < 5) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                Player player = (Player) sender;
                category.addItem(new CategoryItem(player.getInventory().getItemInMainHand(), args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4])));
                return true;
            case "removeitem":
                if (args.length < 3) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                category.removeItem(args[2]);
            case "remove":

                return true;
            default:
                return false;
        }

    }

    public String convertArray(String[] stringArray, String delimiter, int exclude) {
        StringBuilder stringBuilder = new StringBuilder();
        int current = 0;
        for (String string : stringArray) {
            current++;
            if (current <= exclude) continue;
            stringBuilder.append(string).append(delimiter);
        }
        return stringBuilder.toString();
    }
}
