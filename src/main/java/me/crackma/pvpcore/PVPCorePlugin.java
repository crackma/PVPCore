package me.crackma.pvpcore;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import me.crackma.pvpcore.enchanting.EnchantListener;
import me.crackma.pvpcore.fishing.FishListener;
import me.crackma.pvpcore.gui.GuiListener;
import me.crackma.pvpcore.gui.GuiManager;
import me.crackma.pvpcore.kits.KitDatabase;
import me.crackma.pvpcore.kits.KitManager;
import me.crackma.pvpcore.kits.implementation.CreateKitCommand;
import me.crackma.pvpcore.kits.implementation.DeleteKitCommand;
import me.crackma.pvpcore.kits.implementation.KitCommand;
import me.crackma.pvpcore.leaderboard.LeaderboardCommand;
import me.crackma.pvpcore.leaderboard.LeaderboardManager;
import me.crackma.pvpcore.quests.QuestManager;
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
import org.bukkit.scoreboard.Team;

@Getter
public final class PVPCorePlugin extends JavaPlugin {
	@Getter
	private static PVPCorePlugin instance;
	private UserDatabase userDatabase;
	private KitDatabase kitDatabase;
	private ShopDatabase shopDatabase;
	private KitManager kitManager;
	private QuestManager questManager;
	private ShopManager shopManager;
	private UserManager userManager;
	private GuiManager guiManager;
	private LeaderboardManager leaderboardManager;
    @Override
    public void onEnable() {
    	instance = this;
        getDataFolder().mkdirs();
        this.saveDefaultConfig();
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        MongoDatabase mongoDatabase = MongoClients.create(getConfig().getString("connection_uri")).getDatabase(getConfig().getString("database"));
        userDatabase = new UserDatabase(this, mongoDatabase);
        kitDatabase = new KitDatabase(this, mongoDatabase);
        shopDatabase = new ShopDatabase(this, mongoDatabase);
        kitManager = new KitManager(this);
        questManager = new QuestManager();
        shopManager = new ShopManager(this);
        userManager = new UserManager(this);
        guiManager = new GuiManager();
        leaderboardManager = new LeaderboardManager(this);
        new FishListener(this);
        new EnchantListener(this);
        new GuiListener(this);
        new CreateKitCommand(this);
        new DeleteKitCommand(this);
        new KitCommand(this);
        new LeaderboardCommand(this);
        new CategoryCommand(this);
        new ShopCommand(this);
        new CommandListener(this);
        new EditStatsCommand(this);
        new EntityDamageListener(this);
        new UserListener(this);
    }
}
