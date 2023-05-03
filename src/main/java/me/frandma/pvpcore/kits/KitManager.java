package me.frandma.pvpcore.kits;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class KitManager {

    @Getter
        private Set<Kit> kitSet = new HashSet<>();

    public void init() {
        KitDatabase.fetchKits().thenAccept(kits -> {
            for (Kit kit : kits) {
                addKit(kit);
            }
        });
    }

    public void addKit(Kit kit) {
        kitSet.add(kit);
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
