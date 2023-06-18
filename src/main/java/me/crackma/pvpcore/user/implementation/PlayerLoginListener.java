package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerLoginListener implements Listener {
    private PVPCorePlugin plugin;
    public PlayerLoginListener(PVPCorePlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        UserDatabase userDatabase = PVPCorePlugin.getUserDatabase();
        userDatabase.insertUser(uuid).thenCompose(data -> userDatabase.fetchStats(uuid).thenAccept(stats -> {
            if (!UserManager.exists(uuid)) UserManager.addUser(new User(uuid, stats, plugin));
        }));

    }
}
