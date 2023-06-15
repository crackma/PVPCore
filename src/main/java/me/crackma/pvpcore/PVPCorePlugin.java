package me.crackma.pvpcore;

import lombok.Getter;
import me.crackma.pvpcore.enchanting.EnchantListener;
import me.crackma.pvpcore.kits.implementation.CreateKitCommand;
import me.crackma.pvpcore.kits.implementation.KitCommand;
import me.crackma.pvpcore.shop.implementation.CategoryCommand;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.gui.GuiListener;
import me.crackma.pvpcore.kits.KitDatabase;
import me.crackma.pvpcore.kits.KitManager;
import me.crackma.pvpcore.kits.implementation.DeleteKitCommand;
import me.crackma.pvpcore.shop.ShopDatabase;
import me.crackma.pvpcore.shop.ShopManager;
import me.crackma.pvpcore.shop.implementation.ShopCommand;
import me.crackma.pvpcore.user.implementation.EditStatsCommand;
import me.crackma.pvpcore.user.implementation.StatsCommand;
import me.crackma.pvpcore.user.implementation.EntityDamageListener;
import me.crackma.pvpcore.user.implementation.PlayerJoinListener;
import me.crackma.pvpcore.user.implementation.PlayerLoginListener;
import me.crackma.pvpcore.utils.PAPI;
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

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI(this).register();
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new EnchantListener(this), this);

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
        getCommand("category").setTabCompleter(new CategoryCommand());
        getCommand("shop").setExecutor(new ShopCommand());

        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("editstats").setExecutor(new EditStatsCommand());
        getCommand("editstats").setTabCompleter(new EditStatsCommand());
    }
}
