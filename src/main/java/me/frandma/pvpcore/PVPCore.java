package me.frandma.pvpcore;

import me.frandma.pvpcore.commands.TestCommand;
import me.frandma.pvpcore.listeners.PlayerLoginListener;
import me.frandma.pvpcore.user.UserDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class PVPCore extends JavaPlugin {

    private static PVPCore instance;

    private UserDatabase userDatabase;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        instance = this;
        userDatabase = new UserDatabase();
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
        getCommand("test").setExecutor(new TestCommand());
    }

    @Override
    public void onDisable() {
        try {
            userDatabase.getConnection().close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static PVPCore getInstance() {
        return instance;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
}
