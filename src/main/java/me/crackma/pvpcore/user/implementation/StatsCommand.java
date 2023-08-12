package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsCommand implements CommandExecutor, TabCompleter {
    private PVPCorePlugin plugin;
    public StatsCommand(PVPCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("stats").setExecutor(this);
        plugin.getCommand("stats").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player player = (Player) sender;
            User user = plugin.getUserManager().get(player.getUniqueId());
            Stats stats = user.getStats();
            sender.sendMessage(statsMessage(stats));
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        UUID uuid = player.getUniqueId();
        if (plugin.getUserManager().get(uuid) != null) {
            User user = plugin.getUserManager().get(uuid);

            Stats stats = user.getStats();
            sender.sendMessage(statsMessage(stats));

            return true;
        }
        plugin.getUserDatabase().exists(uuid).thenAccept(bool -> {
            if (!bool) {
                sender.sendMessage("§cEnter a valid user.");
                return;
            }
            plugin.getUserDatabase().get(uuid).thenAccept(stats -> sender.sendMessage(statsMessage(stats)));
        });
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
    private String statsMessage(Stats stats) {
        return "Kills: " + stats.getKills() + "\n" +
               "Deaths: " + stats.getDeaths() + "\n" +
               "Streak: " + stats.getStreak() + "\n" +
               "Gems: " + stats.getGems();
    }
}
