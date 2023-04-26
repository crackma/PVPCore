package me.frandma.pvpcore;

import lombok.Getter;
import me.frandma.pvpcore.kits.commands.CreateKitCommand;
import me.frandma.pvpcore.kits.commands.DeleteKitCommand;
import me.frandma.pvpcore.kits.commands.KitCommand;
import me.frandma.pvpcore.user.commands.EditStatsCommand;
import me.frandma.pvpcore.user.commands.StatsCommand;
import me.frandma.pvpcore.kits.KitDatabase;
import me.frandma.pvpcore.kits.KitManager;
import me.frandma.pvpcore.user.listeners.EntityDamageListener;
import me.frandma.pvpcore.user.listeners.PlayerJoinListener;
import me.frandma.pvpcore.user.listeners.PlayerLoginListener;
import me.frandma.pvpcore.kits.listeners.KitInventoryClickListener;
import me.frandma.pvpcore.user.UserDatabase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVPCore extends JavaPlugin {

    @Getter
    private static PVPCore instance;

    @Getter
    private static UserDatabase userDatabase;

    @Getter
    private static KitDatabase kitDatabase;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        instance = this;

        userDatabase = new UserDatabase();
        kitDatabase = new KitDatabase();
        KitManager.init();

        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("createkit").setExecutor(new CreateKitCommand());
        getCommand("createkit").setTabCompleter(new CreateKitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());
        getCommand("deletekit").setTabCompleter(new DeleteKitCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kit").setTabCompleter(new KitCommand());

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("editstats").setExecutor(new EditStatsCommand());
        getCommand("editstats").setTabCompleter(new EditStatsCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new KitInventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }
}
