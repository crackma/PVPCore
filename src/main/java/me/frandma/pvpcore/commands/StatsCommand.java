package me.frandma.pvpcore.commands;

import me.frandma.pvpcore.PVPCore;
import me.frandma.pvpcore.user.Stats;
import me.frandma.pvpcore.user.User;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Provide some more arguments.");
                return true;
            }

            Player player = (Player) sender;
            User user = UserManager.getUser(player.getUniqueId());
            Stats stats = user.getStats();

            sender.sendMessage(statsMessage(stats));
            return true;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        UUID uuid = player.getUniqueId();
        sender.sendMessage(uuid.toString());

        if (UserManager.exists(uuid)) {
            User user = UserManager.getUser(uuid);

            Stats stats = user.getStats();
            sender.sendMessage(statsMessage(stats));

            return true;
        }
        UserDatabase database = PVPCore.getUserDatabase();

        database.exists(uuid).thenAccept(bool -> {
            if (!bool) {
                sender.sendMessage("§cEnter a valid user.");
                return;
            }
            database.fetchStats(uuid).thenAccept(stats -> sender.sendMessage(statsMessage(stats)));
        });

        return true;
    }
    private String statsMessage(Stats stats) {
        return "Kills: " + stats.getKills() + "\n" +
                "Deaths: " + stats.getDeaths() + "\n" +
                "Streak: " + stats.getStreak() + "\n" +
                "Gems: " + stats.getGems();
    }
}
