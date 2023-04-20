package me.frandma.pvpcore.listeners;

import me.frandma.pvpcore.user.Stats;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerLoginListener implements Listener {
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        UserManager.addUser(uuid, new Stats(0, 0, 0));
        UserDatabase.createUser(uuid);
        Bukkit.getLogger().info("worked");
    }
}
