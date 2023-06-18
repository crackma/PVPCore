package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerLoginListener implements Listener {
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        UserDatabase.insertUser(uuid).thenCompose(data -> UserDatabase.fetchStats(uuid).thenAccept(stats -> {
            Bukkit.getLogger().info(UserManager.exists(uuid) + " " + stats.toString());
            if (!UserManager.exists(uuid)) UserManager.addUser(new User(uuid, stats));
        }));

    }
}
