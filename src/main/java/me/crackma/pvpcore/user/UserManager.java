package me.crackma.pvpcore.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import net.md_5.bungee.api.ChatColor;

public class UserManager {
	private PVPCorePlugin plugin;
    @Getter
    private HashMap<UUID, User> userMap = new HashMap<>();
    private String title;
    private List<String> lines;
    public UserManager(PVPCorePlugin plugin) {
    	this.plugin = plugin;
    	title = plugin.getConfig().getString("scoreboard.title");
    	lines = plugin.getConfig().getStringList("scoreboard.lines");
    }
    public void kill(User user) {
    	if (!(user.getOfflinePlayer() instanceof Player)) return;
    	Bukkit.getPluginManager().callEvent(new UserDeathEvent(user, user.getLastAttacker()));
    	HumanEntity victim = (Player) user.getOfflinePlayer();
        victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 10, 2);
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        victim.setFoodLevel(20);
        victim.setSaturation(20);
        victim.setAbsorptionAmount(0);
        victim.setFallDistance(0);
        victim.setFireTicks(0);
        if (user.getBukkitTask() != null) user.getBukkitTask().cancel();
        Stats userStats = user.getStats();
        userStats.setCombatTimer(0);
        World world = victim.getWorld();
        for (ItemStack item : victim.getInventory().getContents()) {
            if (item == null) continue;
            world.dropItemNaturally(victim.getLocation(), item);
        }
        victim.getInventory().clear();
        victim.getInventory().setArmorContents(null);
        Location loc = victim.getWorld().getSpawnLocation();
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        victim.teleport(loc);
        for (PotionEffect effect : victim.getActivePotionEffects()) {
            victim.removePotionEffect(effect.getType());
        }
        victim.sendMessage(user.getLastAttacker() == null ? "§eYou died." : "§eYou were killed by §d" + user.getLastAttacker().getOfflinePlayer().getName() + "§e.");
        userStats.setDeaths(userStats.getDeaths() + 1);
        userStats.setStreak(0);
        updateBoard(user);
        UserDatabase userDatabase = plugin.getUserDatabase();
        userDatabase.updateOne(user);
        User lastAttacker = user.getLastAttacker();
        if (lastAttacker == null || lastAttacker == user) return;
        Stats attackerStats = lastAttacker.getStats();
        attackerStats.setKills(attackerStats.getKills() + 1);
        attackerStats.setStreak(attackerStats.getStreak() + 1);
        attackerStats.setGems(attackerStats.getGems() + 3);
        updateBoard(lastAttacker);
        if (!(lastAttacker instanceof HumanEntity)) ((HumanEntity)lastAttacker.getOfflinePlayer()).sendMessage("§eYou killed §d" + victim.getName() + " §eand got §d3 gems§e.");
        if (attackerStats.getStreak() % 5 == 0) Bukkit.broadcastMessage("§d" + lastAttacker.getOfflinePlayer().getName() + " §eis on a streak of §d" + attackerStats.getStreak() + "§e.");
        userDatabase.updateOne(lastAttacker);
        Set<User> allAttackers = user.getAllAttackers();
        if (allAttackers != null) {
            for (User assistantUser : allAttackers) {
                if (assistantUser == lastAttacker) continue;
                assistantUser.getStats().setGems(assistantUser.getStats().getGems() + 1);
                updateBoard(assistantUser);
                if (assistantUser.getOfflinePlayer() instanceof HumanEntity) ((HumanEntity) assistantUser.getOfflinePlayer()).sendMessage("§eYou assisted the kill of §d" + victim.getName() + " §eand got §d1 gem§e.");
                userDatabase.updateOne(assistantUser);
            }
        }
        user.setLastAttacker(null);
        allAttackers.clear();
    }
    public void restartCombatTimer(User user) {
        if (Stats.getConfigCombatTimer() < 1) return;
        Stats stats = user.getStats();
        if (!stats.isInCombat()) {
        	Bukkit.getPlayer(user.getUniqueId()).sendMessage("§7You are now in combat.");
        }
        stats.setCombatTimer(Stats.getConfigCombatTimer());
        BukkitTask bukkitTask = user.getBukkitTask();
        if (bukkitTask != null) bukkitTask.cancel();
        OfflinePlayer offlinePlayer = user.getOfflinePlayer();
        user.setBukkitTask(new BukkitRunnable() {
            @Override
            public void run() {
                if (!stats.isInCombat()) this.cancel();
                stats.setCombatTimer(stats.getCombatTimer() - 1);
                updateBoard(user);
                if (stats.isInCombat()) return;
                if (offlinePlayer instanceof HumanEntity) ((HumanEntity)offlinePlayer).sendMessage("§7You are no longer in combat.");
                this.cancel();
            }
        }.runTaskTimer(plugin, 0, 20));
    }
    public void updateBoard(User user) {
    	List<String> translatedLines = new ArrayList<>();
    	lines.forEach(line -> translatedLines.add(ChatColor.translateAlternateColorCodes('&', line)));
    	if (user.getBoard() == null) return;
    	List<String> linesWithStats = new ArrayList<>();
    	Stats stats = user.getStats();
    	for (String line : translatedLines) {
    		line = line.replace("%kills%", stats.getKills() + "")
    				.replace("%deaths%", stats.getDeaths() + "")
    				.replace("%streak%", stats.getStreak() + "")
    				.replace("%gems%", stats.getGems() + "")
    				.replace("%timer%", stats.getCombatTimer() + "");
    		linesWithStats.add(line);
    	}
    	try {
    		user.getBoard().updateTitle(ChatColor.translateAlternateColorCodes('&', title));
    		user.getBoard().updateLines(linesWithStats);
    	} catch (IllegalArgumentException exception) {
    		Bukkit.getLogger().info(String.join(", ", linesWithStats));
    	}
    }
    public void add(User user) {
        userMap.put(user.getUniqueId(), user);
    }
    public User get(UUID uniqueId) {
        for (Map.Entry<UUID, User> entry : userMap.entrySet()) {
            if (!entry.getKey().equals(uniqueId)) continue;
            return entry.getValue();
        }
        return null;
    }
}
