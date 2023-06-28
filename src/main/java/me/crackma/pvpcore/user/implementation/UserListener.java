package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {
    private PVPCorePlugin plugin;
    public UserListener(PVPCorePlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        UserDatabase userDatabase = plugin.getUserDatabase();
        userDatabase.insert(uuid).thenCompose(data -> userDatabase.get(uuid).thenAccept(stats -> {
            if (!plugin.getUserManager().exists(uuid)) plugin.getUserManager().addUser(new User(uuid, stats, plugin));
        }));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (!user.getStats().isInCombat()) return;
        user.kill();
        Bukkit.broadcastMessage("§c" + player.getName() + " has logged out while in combat.");
    }
}