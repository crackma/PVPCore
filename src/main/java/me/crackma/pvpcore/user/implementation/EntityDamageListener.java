package me.crackma.pvpcore.user.implementation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;

public class EntityDamageListener implements Listener {
    private PVPCorePlugin plugin;
    public EntityDamageListener(PVPCorePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        UserManager userManager = plugin.getUserManager();
        User victimUser = userManager.get(victim.getUniqueId());
        if (victimUser == null) return;
        if (victim.getHealth() - event.getFinalDamage() > 0) plugin.getUserManager().restartCombatTimer(victimUser);
        Player attacker;
        User attackerUser;
        //make last attacker
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;
                if (!(projectile.getShooter() instanceof Player)) return;
                attacker = (Player) projectile.getShooter();
                attackerUser = userManager.get(attacker.getUniqueId());
                userManager.restartCombatTimer(attackerUser);
                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }
            if (damager instanceof Player) {
                attacker = (Player) damager;
                attackerUser = userManager.get(attacker.getUniqueId());
                userManager.restartCombatTimer(attackerUser);
                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }
        }
        if (victim.getHealth() - event.getFinalDamage() >= 0) return;
        event.setCancelled(true);
        userManager.kill(victimUser);
    }
}
