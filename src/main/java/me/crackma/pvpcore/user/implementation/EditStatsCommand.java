package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditStatsCommand implements CommandExecutor, TabCompleter {
    private PVPCorePlugin plugin;
    public EditStatsCommand(PVPCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("editstats").setExecutor(this);
        plugin.getCommand("editstats").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 5) return false;
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        User user = plugin.getUserManager().get(player.getUniqueId());
        if (user == null) return false;
        Stats stats = user.getStats();
        try {
            stats.setKills(Integer.parseInt(args[1]));
            stats.setDeaths(Integer.parseInt(args[2]));
            stats.setStreak(Integer.parseInt(args[3]));
            stats.setGems(Integer.parseInt(args[4]));
            plugin.getUserDatabase().updateOne(user);
            sender.sendMessage("updated stats of " + player.getName() + " to " + stats);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1:
                for (User user : plugin.getUserManager().getUsers()) completions.add(user.getPlayer().getName());
                break;
            case 2:
                completions.add("<kills>");
                break;
            case 3:
                completions.add("<deaths>");
                break;
            case 4:
                completions.add("<streak>");
                break;
            case 5:
                completions.add("<gems>");
        }
        return completions;
    }
}
