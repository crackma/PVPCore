package me.crackma.pvpcore.kits;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class KitManager {
    private PVPCorePlugin plugin;
    @Getter
    private Set<Kit> kitSet = new HashSet<>();
    public KitManager(PVPCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getKitDatabase().fetchKits().thenAccept(kits -> {
            for (Kit kit : kits) {
                addKit(kit);
            }
        });
    }
    public void addKit(Kit kit) {
        kitSet.add(kit);
    }
    public void giveKit(HumanEntity player, Kit kit) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (!user.getStats().canClaim(kit)) {
            long currentTime = new Date().getTime();
            long cooldownTime = user.getStats().getKitCooldown(kit);
            player.sendMessage("§cYou cannot claim this kit. Time left: " + Utils.formatToDate(cooldownTime - currentTime) + ".");
            return;
        }
        for (ItemStack itemStack : kit.getItems()) {
            if (itemStack == null) continue;
            //hoping this fixes the problem where items' values randomly decrease
            ItemStack itemStack1 = new ItemStack(itemStack);
            //-1 is returned by #firstEmpty() when an inventory is full
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack1);
                continue;
            }
            player.getInventory().addItem(itemStack1);
        }
        user.getStats().addKitCooldown(kit);
        plugin.getUserDatabase().updateOne(user);
    }
    public void deleteKit(Kit kit) {
        kitSet.remove(kit);
    }
    public Kit getKit(String name) {
        name = name.toLowerCase();
        for (Kit kit : kitSet) {
            String kitName = kit.getName().toLowerCase();
            if (kitName.equals(name)) return kit;
        }
        return null;
    }
}
