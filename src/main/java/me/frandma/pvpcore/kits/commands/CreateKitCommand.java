package me.frandma.pvpcore.kits.commands;

import me.frandma.pvpcore.PVPCore;
import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitDatabase;
import me.frandma.pvpcore.kits.KitManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CreateKitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        if (args.length < 3) return false;
        Player player = (Player) sender;
        ItemStack[] items = player.getInventory().getContents();
        if (KitManager.getKit(args[0]) != null) {
            player.sendMessage("§cA kit with that name already exists.");
            return true;
        }
        try {
            Kit kit = new Kit(args[0], Material.valueOf(args[1]), Integer.parseInt(args[2]), items);
            KitDatabase kitDatabase = PVPCore.getKitDatabase();
            kitDatabase.insertKit(kit).thenAccept(data -> {
                KitManager.addKit(kit);
                player.sendMessage("§eCreated kit §d" + args[0] + "§e.");
            });
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1:
                completions.add("<kit_name>");
                break;
            case 2:
                List<String> materialList = new ArrayList<>(Material.values().length);
                for (Material material : Material.values()) {
                    String materialString = material.toString();
                    if (!materialString.contains(args[1])) continue;
                    materialList.add(material.toString());
                }
                return materialList;
            case 3:
                completions.add("<inventoryPosition>");
                break;
        }
        return completions;
    }
}
