package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.getUser(player.getUniqueId());
        if (!user.getStats().isInCombat()) return;
        user.kill();
        Bukkit.broadcastMessage("§c" + player.getName() + " has logged out while in combat.");
    }
}
