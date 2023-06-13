package me.frandma.pvpcore.kits.implementation;

import me.frandma.pvpcore.gui.GuiManager;
import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitManager;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        Kit kit;
        switch(args.length) {
            case 0:
                if (!(sender instanceof Player)) return false;
                player = (Player) sender;
                GuiManager.openGUI(new KitGui(), player);
                return true;
            case 1:
                if (!(sender instanceof Player)) return false;
                player = (Player) sender;
                kit = KitManager.getKit(args[0]);
                if (kit == null) return false;
                kit.giveTo(player);
                if (UserManager.getUser(player.getUniqueId()).getStats().canClaim(kit)) {
                    sender.sendMessage("§eGave §d" + kit.getName() + " §ekit to §d" + player.getName() + "§e.");
                }
                return true;
            case 2:
                if (!sender.hasPermission("pvpcore.kit.others")) {
                    sender.sendMessage("§cYou cannot do that.");
                    return true;
                }
                player = Bukkit.getPlayer(args[1]);
                if (player == null) return false;
                kit = KitManager.getKit(args[0]);
                if (kit == null) return false;
                kit.giveTo(player);
                sender.sendMessage("§eGave §d" + kit.getName() + " §ekit to §d" + player.getName() + "§e.");
                return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length > 1) return null;
        List<String> kitNameList = new ArrayList<>();
        for(Kit kit : KitManager.getKitSet()) {
            kitNameList.add(kit.getName());
        }
        return kitNameList;
    }
}
