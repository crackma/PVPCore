package me.frandma.pvpcore.user.implementation;

import me.frandma.pvpcore.user.User;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerLoginListener implements Listener {
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        UserDatabase.insertOrIgnoreUser(uuid);

        UserDatabase.fetchStats(uuid).thenAccept(stats -> {
            if (!UserManager.exists(uuid)) UserManager.addUser(new User(uuid, stats));
        });
    }
}
