package me.frandma.pvpcore.listeners;

import me.frandma.pvpcore.PVPCore;
import me.frandma.pvpcore.user.Stats;
import me.frandma.pvpcore.user.User;
import me.frandma.pvpcore.user.UserDatabase;
import me.frandma.pvpcore.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        User victimUser = UserManager.getUser(victim.getUniqueId());

        Player attacker;
        User attackerUser;

        //make last attacker
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) event;

            if (newEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) newEvent.getDamager();
                if (!(projectile.getShooter() instanceof Player)) return;
                attacker = (Player) projectile.getShooter();
                attackerUser = UserManager.getUser(attacker.getUniqueId());

                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }

            if (!(newEvent.getDamager() instanceof Player)) return;

            attacker = (Player) newEvent.getDamager();
            attackerUser = UserManager.getUser(attacker.getUniqueId());

            victimUser.setLastAttacker(attackerUser);
            victimUser.addAttacker(attackerUser);
        }

        if (victim.getHealth() - event.getFinalDamage() > 0) return;
        //after death
        event.setCancelled(true);
        victim.playSound(victim.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
        victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        victim.setFoodLevel(20);
        victim.setSaturation(20);
        victim.setAbsorptionAmount(0);

        for (ItemStack item : victim.getInventory().getContents()) {
            if (item == null) continue;
            victim.getWorld().dropItemNaturally(victim.getLocation(), item);
        }

        victim.getInventory().clear();
        victim.getInventory().setArmorContents(null);

        victim.teleport(victim.getWorld().getSpawnLocation());

        for (PotionEffect effect : victim.getActivePotionEffects()) {
            victim.removePotionEffect(effect.getType());
        }

        UserDatabase userDatabase = PVPCore.getUserDatabase();

        attackerUser = victimUser.getLastAttacker();
        attacker = attackerUser.getPlayer();

        Stats victimStats = victimUser.getStats();
        victim.sendMessage(attackerUser == null ? "§eYou died." : "§eYou were killed by §d" + attackerUser.getPlayer().getName() + "§e.");
        victimUser.setStats(new Stats(victimStats.getKills(), victimStats.getDeaths() + 1, 0, victimStats.getGems()));
        userDatabase.updateStats(victimUser);

        if (attackerUser == victimUser || attackerUser == null) return;

        Stats attackerStats = attackerUser.getStats();
        attackerUser.setStats(new Stats(attackerStats.getKills() + 1, attackerStats.getDeaths(), attackerStats.getStreak() + 1, attackerStats.getGems() + 3));
        attacker.sendMessage("§eYou killed §d" + victim.getName() + " §eand got §d3 gems§e.");
        if (attackerStats.getStreak() % 5 == 0) Bukkit.broadcastMessage("§d" + attacker.getName() + " §eis on a streak of §d" + attackerStats.getStreak() + "§e.");
        userDatabase.updateStats(attackerUser);

        List<User> attackerList = victimUser.getAllAttackers();
        if (attackerList == null) return;
        for (User assistantUser : attackerList) {
            if (assistantUser == victimUser.getLastAttacker()) continue;
            assistantUser.getStats().setGems(assistantUser.getStats().getGems() + 1);
            assistantUser.getPlayer().sendMessage("§eYou assisted the kill of §d" + victim.getName() + " §eand got §d1 gem§e.");
            userDatabase.updateStats(assistantUser);
        }
        victimUser.clearAttackers();
    }
}
