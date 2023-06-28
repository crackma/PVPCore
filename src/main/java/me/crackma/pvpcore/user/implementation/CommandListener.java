package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {
    private PVPCorePlugin plugin;
    private List<String> blockedCommands;
    public CommandListener(PVPCorePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        blockedCommands = plugin.getConfig().getStringList("blocked_commands");
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        User user = plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (!user.getStats().isInCombat()) return;
        for (String blockedCommand : blockedCommands) {
            if (!event.getMessage().startsWith(blockedCommand)) continue;
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot do that while in combat.");
            return;
        }
    }
}
