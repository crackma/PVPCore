package me.crackma.pvpcore.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitTask;

import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import me.crackma.pvpcore.quests.Quest;

@Getter
public class User {
    private UUID uniqueId;
    @Setter
    private OfflinePlayer offlinePlayer;
    @Setter
    private Stats stats;
    @Setter
    private Quest quest;
    @Setter
    private User lastAttacker;
    @Setter
    private BukkitTask bukkitTask;
    private Set<User> allAttackers = new HashSet<>();
    @Setter
    private FastBoard board;
    public User(UUID uniqueId, Stats stats) {
        this.uniqueId = uniqueId;
        this.stats = stats;
    }
    public void addAttacker(User user) {
        if (this == user || allAttackers.contains(user)) return;
        allAttackers.add(user);
    }
}
