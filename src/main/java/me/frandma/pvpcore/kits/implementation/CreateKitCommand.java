package me.frandma.pvpcore.kits.implementation;

import me.frandma.pvpcore.PVPCorePlugin;
import me.frandma.pvpcore.Utils;
import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitDatabase;
import me.frandma.pvpcore.kits.KitManager;
import org.bukkit.Bukkit;
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
            Material material = Material.valueOf(args[3]);
            if (material == null) return false;
            Kit kit = new Kit(args[0], Integer.parseInt(args[1]) * 1000 /*milliseconds -> seconds*/, Integer.parseInt(args[2]), material, items);
            KitDatabase kitDatabase = PVPCorePlugin.getKitDatabase();
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
                completions.add("<cooldown>");
                break;
            case 3:
                completions.add("<inventoryPosition>");
                break;
            case 4:
                return Utils.getMaterialList(args[1]);
        }
        return completions;
    }
}
