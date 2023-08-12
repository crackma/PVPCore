package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private PVPCorePlugin plugin;
    public EntityDamageListener(PVPCorePlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        User victimUser = plugin.getUserManager().get(victim.getUniqueId());
        if (victimUser == null) return;
        victimUser.restartCombatTimer();
        Player attacker;
        User attackerUser;
        //make last attacker
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent newEvent = (EntityDamageByEntityEvent) event;
            if (newEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) newEvent.getDamager();
                if (!(projectile.getShooter() instanceof Player)) return;
                attacker = (Player) projectile.getShooter();
                attackerUser = plugin.getUserManager().get(attacker.getUniqueId());
                attackerUser.restartCombatTimer();
                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }
            if (newEvent.getDamager() instanceof Player) {
                attacker = (Player) newEvent.getDamager();
                attackerUser = plugin.getUserManager().get(attacker.getUniqueId());
                attackerUser.restartCombatTimer();
                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }
        }
        if (victim.getHealth() - event.getFinalDamage() > 0) return;
        event.setCancelled(true);
        victimUser.kill();
    }
}
