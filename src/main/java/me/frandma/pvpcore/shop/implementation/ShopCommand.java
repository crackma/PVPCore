package me.frandma.pvpcore.shop.implementation;

import me.frandma.pvpcore.gui.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        GuiManager.openGUI(new ShopGui(), (Player) sender);
        return true;
    }
}
