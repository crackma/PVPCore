package me.crackma.pvpcore.shop.implementation;

import me.crackma.pvpcore.utils.Utils;
import me.crackma.pvpcore.shop.Category;
import me.crackma.pvpcore.shop.CategoryItem;
import me.crackma.pvpcore.shop.ShopDatabase;
import me.crackma.pvpcore.shop.ShopManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CategoryCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;
        Category category;
        Material material;
        String description;
        switch (args[0].toLowerCase()) {
            case "help":
                sender.sendMessage("§7/category create <name> <displayItem> <inventorySlot> <description>\n" +
                                      "§7/category edit <name> displayItem/inventorySlot/description <new> \n" +
                                      "§7/category addItem <categoryName> <itemName> <inventorySlot> <price> (hold an item) \n" +
                                      "§7/category removeItem <categoryName> <itemName>\n" +
                                      "§7/category remove <name>\n");
                return true;
            //category create <name> <displayItem> <inventorySlot> <description>
            case "create":
                if (args.length < 5) return false;
                if (ShopManager.getCategory(args[1]) != null) {
                    sender.sendMessage("§cA category with that name already exists.");
                    return true;
                }
                material = Material.valueOf(args[2]);
                if (material.equals(Material.AIR)) return false;
                description = convertArray(args, " ", 4);
                try {
                    category = new Category(args[1], description, Material.valueOf(args[2]), Integer.parseInt(args[3]));
                } catch (NumberFormatException exception) {
                    return false;
                }
                ShopManager.addCategory(category);
                ShopDatabase.insertCategory(category);
                sender.sendMessage("§eCreated category §d" + args[1] + "§e.");
                return true;
            //category edit <name> displayItem/inventorySlot/description <new>
            case "edit":
                if (args.length < 4) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                switch (args[2].toLowerCase()) {
                    case "displayitem":
                        material = Material.valueOf(args[2]);
                        if (material.equals(Material.AIR)) return false;
                        category.setDisplayItem(material);
                        sender.sendMessage("§eUpdated §d" + args[1] + "'s§e display item to §d" + args[3] + "§e.");
                        break;
                    case "inventoryslot":
                        try {
                            category.setInventorySlot(Integer.parseInt(args[3]));
                            sender.sendMessage("§eUpdated §d" + args[1] + "'s§e inventory slot to §d" + args[3] + "§e.");
                        } catch (NumberFormatException exception) {
                            return false;
                        }
                        break;
                    case "description":
                        category.setDescription(convertArray(args, " ", 3));
                        sender.sendMessage("§eUpdated §d" + args[1] + "'s§e description.");
                        break;
                }
                return true;
            //category addItem <categoryName> <itemName> <inventorySlot> <price> (hold an item)
            case "additem":
                if (!(sender instanceof Player)) return true;
                if (args.length < 5) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                Player player = (Player) sender;
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack == null) return false;
                try {
                    //have to create a different instance of ItemStack
                    category.addItem(new CategoryItem(args[2], new ItemStack(itemStack), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
                } catch (NumberFormatException exception) {
                    return false;
                }
                sender.sendMessage("§eAdded item to category §d" + args[1] + "§e.");
                ShopDatabase.insertCategory(category);
                return true;
            //category removeItem <categoryName> <itemName>
            case "removeitem":
                if (args.length < 3) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                category.removeItem(args[2]);
                sender.sendMessage("§eRemoved item from category §d" + args[1] + "§e.");
                ShopDatabase.insertCategory(category);
            //category remove <name>
            case "remove":
                if (args.length < 2) return false;
                category = ShopManager.getCategory(args[1]);
                if (category == null) return false;
                ShopDatabase.deleteCategory(category.getName());
                ShopManager.deleteCategory(category);
                sender.sendMessage("§eRemoved category §d" + args[1] + "§e.");
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

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        Category category;
        if (args.length == 1) {
            completions.add("create");
            completions.add("edit");
            completions.add("addItem");
            completions.add("removeItem");
            completions.add("remove");
            return completions;
        }
        switch (args[0].toLowerCase()) {
            //category create <name> <displayItem> <inventorySlot> <description>
            case "create":
                if (args.length == 2) completions.add("<name>");
                if (args.length == 3) return Utils.getMaterialList(args[2]);
                if (args.length == 4) completions.add("<inventorySlot>");
                if (args.length == 5) completions.add("<description>");
                break;
            //category edit <name> displayItem/inventorySlot/description <new>
            case "edit":
                if (args.length == 2) ShopManager.getCategorySet().forEach(category1 -> completions.add(category1.getName()));
                if (args.length == 3) {
                    completions.add("displayItem");
                    completions.add("inventorySlot");
                    completions.add("description");
                }
                if (args.length < 4) break;
                switch (args[2].toLowerCase()) {
                    case "displayitem":
                        return Utils.getMaterialList(args[2]);
                    case "inventoryslot":
                        category = ShopManager.getCategory(args[1]);
                        completions.add(category == null ? "<inventorySlot>" : category.getInventorySlot() + "");
                        break;
                    case "description":
                        category = ShopManager.getCategory(args[1]);
                        completions.add(category == null ? "<description>" : category.getDescription());
                        break;
                    default:
                        break;
                }
                break;
            //category addItem <categoryName> <itemName> <inventorySlot> <price>
            case "additem":
                if (args.length == 2) ShopManager.getCategorySet().forEach(category1 -> completions.add(category1.getName()));
                if (args.length == 3) completions.add("<itemName>");
                if (args.length == 4) completions.add("<inventorySlot>");
                if (args.length == 5) completions.add("<price>");
                break;
            //category removeItem <categoryName> <itemName>
            case "removeitem":
                if (args.length == 2) ShopManager.getCategorySet().forEach(category2 -> completions.add(category2.getName()));
                category = ShopManager.getCategory(args[1]);
                if (category == null) break;
                if (args.length == 3) category.getItems().forEach(item -> completions.add(item.getName()));
                break;
            //category remove <name>
            case "remove":
                if (args.length == 2) ShopManager.getCategorySet().forEach(category3 -> completions.add(category3.getName()));
                break;
        }
        return completions;
    }
}
