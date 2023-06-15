package me.crackma.pvpcore.user;

import lombok.Getter;
import lombok.Setter;
import me.crackma.pvpcore.kits.Kit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Stats {

    @Getter
    @Setter
    private int kills, deaths, streak, gems;

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
    }
    public Stats(int kills, int deaths, int streak, int gems, Map<String, Long> cooldownMap) {
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.gems = gems;
        if (cooldownMap == null) return;
        this.cooldownMap = cooldownMap;
    }

    public void addCooldown(Kit kit) {
        cooldownMap.put(kit.getName(), new Date().getTime() + kit.getCooldown());
    }

    public long getCooldown(Kit kit) {
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
        return kills + ", " + deaths + ", " + streak + ", " + gems;
    }


}
