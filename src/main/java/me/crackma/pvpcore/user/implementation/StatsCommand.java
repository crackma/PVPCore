package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.UserManager;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return false;
            }

            Player player = (Player) sender;
            User user = UserManager.getUser(player.getUniqueId());
            Stats stats = user.getStats();

            sender.sendMessage(statsMessage(stats));
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        UUID uuid = player.getUniqueId();

        if (UserManager.exists(uuid)) {
            User user = UserManager.getUser(uuid);

            Stats stats = user.getStats();
            sender.sendMessage(statsMessage(stats));

            return true;
        }
        UserDatabase database = PVPCorePlugin.getUserDatabase();

        database.exists(uuid).thenAccept(bool -> {
            if (!bool) {
                sender.sendMessage("§cEnter a valid user.");
                return;
            }
            database.fetchStats(uuid).thenAccept(stats -> sender.sendMessage(statsMessage(stats)));
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1:
                for (Map.Entry<UUID, User> set : UserManager.getUserMap().entrySet()) completions.add(set.getValue().getPlayer().getName());
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
