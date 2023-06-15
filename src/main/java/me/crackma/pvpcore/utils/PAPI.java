package me.crackma.pvpcore.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.OfflinePlayer;

public class PAPI extends PlaceholderExpansion {

    private final PVPCorePlugin plugin;

    public PAPI(PVPCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "crackma";
    }

    @Override
    public String getIdentifier() {
        return "pvpcore";
    }

    @Override
    public String getVersion() {
        return "0-0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        User user = UserManager.getUser(player.getUniqueId());
        if (user == null) return null;
        if(params.equalsIgnoreCase("kills")) {
            return user.getStats().getKills() + "";
        }
        if(params.equalsIgnoreCase("deaths")) {
            return user.getStats().getDeaths() + "";
        }
        if(params.equalsIgnoreCase("streak")) {
            return user.getStats().getStreak() + "";
        }
        if(params.equalsIgnoreCase("gems")) {
            return user.getStats().getGems() + "";
        }
        return null;
    }
}
