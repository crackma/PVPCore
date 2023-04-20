package me.frandma.pvpcore.commands;

import me.frandma.pvpcore.user.Stats;
import me.frandma.pvpcore.user.UserDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        Stats playerStats = UserDatabase.getStats(player.getUniqueId());
        player.sendMessage(playerStats.toString());
        return true;
    }
}
