package me.frandma.pvpcore.kits.implementation;

import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitDatabase;
import me.frandma.pvpcore.kits.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;
        Kit kit = KitManager.getKit(args[0]);
        if (kit == null) return false;
        KitDatabase.deleteKit(kit.getName()).thenAccept(data -> {
            KitManager.deleteKit(kit);
            sender.sendMessage("§eDeleted kit §d" + args[0] + "§e.");
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length > 1) return null;
        List<String> completions = new ArrayList<>();
        for (Kit kit : KitManager.getKitSet()) {
            completions.add(kit.getName());
        }
        return completions;
    }
}
