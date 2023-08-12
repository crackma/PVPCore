package me.crackma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.kits.Kit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Stats {
    @Getter
    @Setter
    private int kills, deaths, streak, gems;
    @Getter
    @Setter
    private int combatTimer = 0;
    @Getter
    private int configCombatTimer;
    @Getter
    //kitName, cooldown (currentTime + kitCooldown)
    private Map<String, Long> cooldownMap = new HashMap<>();
    public Stats(int kills, int deaths, int streak, int gems, String cooldowns) {
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.gems = gems;
        if (cooldowns == null) return;
        String[] splitCooldowns = cooldowns.split(";");
        for (String cooldown : splitCooldowns) {
            String[] splitCooldown = cooldown.split(":");
            cooldownMap.put(splitCooldown[0], Long.valueOf(splitCooldown[1]));
        }
        this.configCombatTimer = PVPCorePlugin.getPlugin().getConfig().getInt("combat_timer");
    }
    public Stats(int kills, int deaths, int streak, int gems, Map<String, Long> cooldownMap, PVPCorePlugin plugin) {
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.gems = gems;
        if (cooldownMap == null) return;
        this.cooldownMap = cooldownMap;
        this.configCombatTimer = plugin.getConfig().getInt("combat_timer");
    }
    public boolean isInCombat() {
        return combatTimer > 0;
    }
    public void addKitCooldown(Kit kit) {
        cooldownMap.put(kit.getName(), new Date().getTime() + kit.getCooldown());
    }
    public long getKitCooldown(Kit kit) {
        return cooldownMap.get(kit.getName());
    }
    public boolean canClaim(Kit kit) {
        Long cooldown = cooldownMap.get(kit.getName());
        if (cooldown == null) return true;
        return cooldown < new Date().getTime();
    }
    public String getCooldownMapAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Long> set : cooldownMap.entrySet()) {
            stringBuilder.append(set.getKey() + ":" + set.getValue() + ";");
        }
        return stringBuilder.toString();
    }
    public String toString() {
        return kills + ", " + deaths + ", " + streak + ", " + gems + ", " + combatTimer;
    }
}
