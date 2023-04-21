package me.frandma.pvpcore;

import lombok.Getter;
import me.frandma.pvpcore.commands.StatsCommand;
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

        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        getCommand("stats").setExecutor(new StatsCommand());
    }

    @Override
    public void onDisable() {

    }
}
