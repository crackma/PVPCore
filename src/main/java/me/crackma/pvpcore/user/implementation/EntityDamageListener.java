package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        User victimUser = UserManager.getUser(victim.getUniqueId());
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
                attackerUser = UserManager.getUser(attacker.getUniqueId());

                victimUser.setLastAttacker(attackerUser);
                victimUser.addAttacker(attackerUser);
            }

            if (!(newEvent.getDamager() instanceof Player)) return;

            attacker = (Player) newEvent.getDamager();
            attackerUser = UserManager.getUser(attacker.getUniqueId());

            attackerUser.restartCombatTimer();

            victimUser.setLastAttacker(attackerUser);
            victimUser.addAttacker(attackerUser);
        }

        if (victim.getHealth() - event.getFinalDamage() > 0) return;
        //after death
        event.setCancelled(true);
        victimUser.kill();
    }
}
