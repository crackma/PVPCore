package me.crackma.pvpcore;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import me.crackma.pvpcore.enchanting.EnchantListener;
import me.crackma.pvpcore.gui.GuiListener;
import me.crackma.pvpcore.gui.GuiManager;
import me.crackma.pvpcore.kits.KitDatabase;
import me.crackma.pvpcore.kits.KitManager;
import me.crackma.pvpcore.kits.implementation.CreateKitCommand;
import me.crackma.pvpcore.kits.implementation.DeleteKitCommand;
import me.crackma.pvpcore.kits.implementation.KitCommand;
import me.crackma.pvpcore.shop.ShopDatabase;
import me.crackma.pvpcore.shop.ShopManager;
import me.crackma.pvpcore.shop.implementation.CategoryCommand;
import me.crackma.pvpcore.shop.implementation.ShopCommand;
import me.crackma.pvpcore.user.UserDatabase;
import me.crackma.pvpcore.user.UserManager;
import me.crackma.pvpcore.user.implementation.CommandListener;
import me.crackma.pvpcore.user.implementation.EditStatsCommand;
import me.crackma.pvpcore.user.implementation.EntityDamageListener;
import me.crackma.pvpcore.user.implementation.UserListener;

public final class PVPCorePlugin extends JavaPlugin {
	@Getter
	private static PVPCorePlugin plugin;
    @Getter
    private GuiManager guiManager;
    @Getter
    private UserDatabase userDatabase;
    @Getter
    private UserManager userManager;
    @Getter
    private KitDatabase kitDatabase;
    @Getter
    private ShopDatabase shopDatabase;
    @Getter
    private KitManager kitManager;
    @Getter
    private ShopManager shopManager;
    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        this.saveDefaultConfig();
        plugin = this;
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        MongoDatabase mongoDatabase = MongoClients.create(getConfig().getString("connection_uri")).getDatabase(getConfig().getString("database"));
        guiManager = new GuiManager();
        userDatabase = new UserDatabase(this, mongoDatabase);
        userManager = new UserManager(this);
        kitDatabase = new KitDatabase(this, mongoDatabase);
        shopDatabase = new ShopDatabase(this, mongoDatabase);
        kitManager = new KitManager(this);
        shopManager = new ShopManager(this);
        new EnchantListener(this);
        new GuiListener(this);
        new CreateKitCommand(this);
        new DeleteKitCommand(this);
        new KitCommand(this);
        new CategoryCommand(this);
        new ShopCommand(this);
        new CommandListener(this);
        new EditStatsCommand(this);
        new EntityDamageListener(this);
        new UserListener(this);
    }
}
