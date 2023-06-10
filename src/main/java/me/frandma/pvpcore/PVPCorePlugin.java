package me.frandma.pvpcore;

import lombok.Getter;
import me.frandma.pvpcore.gui.GuiListener;
import me.frandma.pvpcore.kits.KitDatabase;
import me.frandma.pvpcore.kits.KitManager;
import me.frandma.pvpcore.kits.implementation.CreateKitCommand;
import me.frandma.pvpcore.kits.implementation.DeleteKitCommand;
import me.frandma.pvpcore.kits.implementation.KitCommand;
import me.frandma.pvpcore.shop.ShopDatabase;
import me.frandma.pvpcore.shop.ShopManager;
import me.frandma.pvpcore.shop.implementation.CategoryCommand;
import me.frandma.pvpcore.shop.implementation.ShopCommand;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.implementation.EditStatsCommand;
import me.frandma.pvpcore.user.implementation.StatsCommand;
import me.frandma.pvpcore.user.implementation.EntityDamageListener;
import me.frandma.pvpcore.user.implementation.PlayerJoinListener;
import me.frandma.pvpcore.user.implementation.PlayerLoginListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PVPCorePlugin extends JavaPlugin {

    @Getter
    private static PVPCorePlugin instance;

    @Getter
    private static UserDatabase userDatabase;

    @Getter
    private static KitDatabase kitDatabase;

    @Getter
    private static ShopDatabase shopDatabase;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();

        this.saveDefaultConfig();

        instance = this;

        userDatabase = new UserDatabase();
        kitDatabase = new KitDatabase();
        shopDatabase = new ShopDatabase();
        KitManager.init();
        ShopManager.init();

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);

        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }

    private void registerCommands() {
        getCommand("createkit").setExecutor(new CreateKitCommand());
        getCommand("createkit").setTabCompleter(new CreateKitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());
        getCommand("deletekit").setTabCompleter(new DeleteKitCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kit").setTabCompleter(new KitCommand());

        getCommand("category").setExecutor(new CategoryCommand());
        getCommand("shop").setExecutor(new ShopCommand());

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("editstats").setExecutor(new EditStatsCommand());
        getCommand("editstats").setTabCompleter(new EditStatsCommand());
    }
}
