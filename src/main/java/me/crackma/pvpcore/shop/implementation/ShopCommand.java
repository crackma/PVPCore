package me.crackma.pvpcore.shop.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    private PVPCorePlugin plugin;
    public ShopCommand(PVPCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("shop").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        plugin.getGuiManager().openGUI(new ShopGui(plugin), (Player) sender);
        return true;
    }
}
