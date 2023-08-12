package me.crackma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;
import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.mrmicky.fastboard.FastBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    @Getter
    private UUID uniqueId;
    @Getter
    @Setter
    private Stats stats;
    @Getter
    @Setter
    private User lastAttacker;
    private BukkitTask bukkitTask;
    @Getter
    private List<User> allAttackers = new ArrayList<>();
    @Getter
    @Setter
    private FastBoard board;
    public User(UUID uniqueId, Stats stats) {
        this.uniqueId = uniqueId;
        this.stats = stats;
    }
    public void restartCombatTimer() {
        if (stats.getConfigCombatTimer() < 1) return;
        if (!stats.isInCombat()) {
        	Bukkit.getPlayer(uniqueId).sendMessage("§7You are now in combat.");
        }
        stats.setCombatTimer(stats.getConfigCombatTimer());
        if (bukkitTask != null) bukkitTask.cancel();
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) this.cancel();
                if (!stats.isInCombat()) this.cancel();
                stats.setCombatTimer(stats.getCombatTimer() - 1);
                if (stats.isInCombat()) return;
                getPlayer().sendMessage("§7You are no longer in combat.");
                this.cancel();
            }
        }.runTaskTimer(PVPCorePlugin.getPlugin(), 0, 20);
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }
    public void addAttacker(User user) {
        if (this == user || allAttackers.contains(user)) return;
        allAttackers.add(user);
    }
    public void kill() {
        Player victim = getPlayer();
        victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        victim.setFoodLevel(20);
        victim.setSaturation(20);
        victim.setAbsorptionAmount(0);
        victim.setFireTicks(0);
        if (bukkitTask != null) bukkitTask.cancel();
        stats.setCombatTimer(0);
        for (ItemStack item : victim.getInventory().getContents()) {
            if (item == null) continue;
            victim.getWorld().dropItemNaturally(victim.getLocation(), item);
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
        PVPCorePlugin plugin = PVPCorePlugin.getPlugin();
        UserDatabase userDatabase = plugin.getUserDatabase();
        Stats victimStats = getStats();
        victim.sendMessage(lastAttacker == null ? "§eYou died." : "§eYou were killed by §d" + lastAttacker.getPlayer().getName() + "§e.");
        setStats(new Stats(victimStats.getKills(), victimStats.getDeaths() + 1, 0, victimStats.getGems(), victimStats.getCooldownMap(), plugin));
        userDatabase.updateOne(this);
        if (lastAttacker == null) return;
        Player attacker = lastAttacker.getPlayer();
        Stats attackerStats = lastAttacker.getStats();
        lastAttacker.setStats(new Stats(attackerStats.getKills() + 1, attackerStats.getDeaths(), attackerStats.getStreak() + 1, attackerStats.getGems() + 3, attackerStats.getCooldownMap(), plugin));
        attackerStats = lastAttacker.getStats();
        attacker.sendMessage("§eYou killed §d" + victim.getName() + " §eand got §d3 gems§e.");
        if (attackerStats.getStreak() % 5 == 0) Bukkit.broadcastMessage("§d" + attacker.getName() + " §eis on a streak of §d" + attackerStats.getStreak() + "§e.");
        userDatabase.updateOne(lastAttacker);
        if (allAttackers == null) return;
        for (User assistantUser : allAttackers) {
            if (assistantUser == lastAttacker) continue;
            assistantUser.getStats().setGems(assistantUser.getStats().getGems() + 1);
            assistantUser.getPlayer().sendMessage("§eYou assisted the kill of §d" + victim.getName() + " §eand got §d1 gem§e.");
            userDatabase.updateOne(assistantUser);
        }
        allAttackers.clear();
    }
}
