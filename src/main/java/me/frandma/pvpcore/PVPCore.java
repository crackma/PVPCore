package me.frandma.pvpcore;

import lombok.Getter;
import me.frandma.pvpcore.commands.EditStatsCommand;
import me.frandma.pvpcore.commands.StatsCommand;
import me.frandma.pvpcore.listeners.EntityDamageListener;
import me.frandma.pvpcore.listeners.PlayerJoinListener;
import me.frandma.pvpcore.listeners.PlayerLoginListener;
import me.frandma.pvpcore.user.UserDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVPCore extends JavaPlugin {

    @Getter
    private static PVPCore instance;

    @Getter
    private static UserDatabase userDatabase;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        instance = this;
        userDatabase = new UserDatabase();

        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("stats").setExecutor(new StatsCommand());

        getCommand("editstats").setExecutor(new EditStatsCommand());
        getCommand("editstats").setTabCompleter(new EditStatsCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }
}
