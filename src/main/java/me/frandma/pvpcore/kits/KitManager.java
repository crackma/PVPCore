package me.frandma.pvpcore.kits;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
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

    @Getter
    private Inventory kitInventory = Bukkit.createInventory(null, 54);

    public void init() {
        Bukkit.getLogger().info("kitmanager/init");
        KitDatabase.fetchKits().thenAccept(kits -> {
            for (Kit kit : kits) {
                addKit(kit);
            }
        });
    }

    public void addKit(Kit kit) {
        kitSet.add(kit);
        ItemStack itemStack = new ItemStack(kit.getDisplayItem());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d" + kit.getName() + " §ekit. §7(Click to claim)");
        itemMeta.setLore(null);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        kitInventory.setItem(kit.getInventoryPosition(), itemStack);
    }

    public void deleteKit(Kit kit) {
        kitSet.remove(kit);
        kitInventory.setItem(kit.getInventoryPosition(), null);
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
