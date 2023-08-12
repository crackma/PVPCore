package me.crackma.pvpcore.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import net.md_5.bungee.api.ChatColor;

public class UserManager {
    @Getter
    private Set<User> users = new HashSet<>();
    public UserManager(PVPCorePlugin plugin) {
    	String title = plugin.getConfig().getString("scoreboard.title");
    	List<String> lines = plugin.getConfig().getStringList("scoreboard.lines");
    	List<String> translatedLines = new ArrayList<>();
    	lines.forEach(line -> translatedLines.add(ChatColor.translateAlternateColorCodes('&', line)));
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (User user : users) {
            	if (user.getBoard() == null) continue;
            	List<String> linesWithStats = new ArrayList<>();
            	Stats stats = user.getStats();
            	for (String line : translatedLines) {
            		line = line.replace("%kills%", stats.getKills() + "");
            		line = line.replace("%deaths%", stats.getDeaths() + "");
            		line = line.replace("%streak%", stats.getStreak() + "");
            		line = line.replace("%gems%", stats.getGems() + "");
            		line = line.replace("%timer%", stats.getCombatTimer() + "");
            		linesWithStats.add(line);
            	}
            	try {
            		user.getBoard().updateTitle(ChatColor.translateAlternateColorCodes('&', title));
            		user.getBoard().updateLines(linesWithStats);
            	} catch (IllegalArgumentException exception) {
            		Bukkit.getLogger().info(String.join(", ", linesWithStats));
            	}
            }
        }, 0, 20);
    }
    public void add(User user) {
        users.add(user);
    }
    public User get(UUID uniqueId) {
        for (User user : users) {
            if (!user.getUniqueId().equals(uniqueId)) continue;
            return user;
        }
        return null;
    }
}
