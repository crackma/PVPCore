package me.crackma.pvpcore.user.implementation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.mrmicky.fastboard.FastBoard;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;

public class UserListener implements Listener {
    private PVPCorePlugin plugin;
    public UserListener(PVPCorePlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
    	User user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
    	if (user == null) {
    		user = plugin.getUserDatabase().getOrCreate(event.getPlayer().getUniqueId());
    		plugin.getUserManager().add(user);
    	}
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	User user = plugin.getUserManager().get(player.getUniqueId());
    	FastBoard board = new FastBoard(player);
    	user.setBoard(board);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getUserManager().get(event.getPlayer().getUniqueId());
        user.setBoard(null);
        if (!user.getStats().isInCombat()) return;
        user.kill();
        Bukkit.broadcastMessage("§c" + player.getName() + " has logged out while in combat.");
    }
}