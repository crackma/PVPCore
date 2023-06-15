package me.crackma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    @Getter
    private UUID uniqueId;

    @Getter
    @Setter
    private Stats stats;

    @Getter
    @Setter
    private User lastAttacker;

    @Getter
    private List<User> allAttackers = new ArrayList<>();

    public User(UUID uniqueId, Stats stats) {
        this.uniqueId = uniqueId;
        this.stats = stats;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    public void addAttacker(User user) {
        if (this == user || allAttackers.contains(user)) return;
        allAttackers.add(user);
    }

    public void clearAttackers() {
        allAttackers.clear();
    }
}
