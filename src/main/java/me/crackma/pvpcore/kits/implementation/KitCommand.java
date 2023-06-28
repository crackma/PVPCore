package me.crackma.pvpcore.kits.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.GuiManager;
import me.crackma.pvpcore.kits.Kit;
import me.crackma.pvpcore.kits.KitManager;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private PVPCorePlugin plugin;
    public KitCommand(PVPCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("kit").setExecutor(this);
        plugin.getCommand("kit").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        Kit kit;
        switch(args.length) {
            case 0:
                if (!(sender instanceof Player)) return false;
                player = (Player) sender;
                plugin.getGuiManager().openGUI(new KitsGui(plugin), player);
                return true;
            case 1:
                if (!(sender instanceof Player)) return false;
                player = (Player) sender;
                kit = plugin.getKitManager().getKit(args[0]);
                if (kit == null) return false;
                plugin.getKitManager().giveKit(player, kit);
                if (plugin.getUserManager().getUser(player.getUniqueId()).getStats().canClaim(kit)) {
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
                kit = plugin.getKitManager().getKit(args[0]);
                if (kit == null) return false;
                plugin.getKitManager().giveKit(player, kit);
                sender.sendMessage("§eGave §d" + kit.getName() + " §ekit to §d" + player.getName() + "§e.");
                return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length > 1) return null;
        List<String> kitNameList = new ArrayList<>();
        for(Kit kit : plugin.getKitManager().getKitSet()) {
            kitNameList.add(kit.getName());
        }
        return kitNameList;
    }
}
