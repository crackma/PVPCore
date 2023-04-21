package me.frandma.pvpcore.listeners;

import me.frandma.pvpcore.PVPCore;
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

        UserDatabase database = PVPCore.getUserDatabase();

        database.insertOrIgnoreUser(uuid);

        database.fetchStats(uuid).thenAccept(stats -> {
            if (!UserManager.exists(uuid)) UserManager.addUser(uuid, stats);
        });
    }
}
