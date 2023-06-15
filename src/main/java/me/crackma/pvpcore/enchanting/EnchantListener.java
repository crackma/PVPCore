package me.crackma.pvpcore.enchanting;

import me.crackma.pvpcore.PVPCorePlugin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class EnchantListener implements Listener {

    private PVPCorePlugin plugin;

    public EnchantListener(PVPCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack cursorItemStack = event.getCursor();
        if (cursorItemStack.getType() != Material.ENCHANTED_BOOK) return;
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) cursorItemStack.getItemMeta();
        //no books with multiple or no enchants
        if (bookMeta.getStoredEnchants().size() != 1) return;

        //loop through the single enchantment
        for (Map.Entry<Enchantment, Integer> set : bookMeta.getStoredEnchants().entrySet()) {
            Enchantment enchantment = set.getKey();
            ItemStack clickedItemStack = event.getCurrentItem();
            if (clickedItemStack.containsEnchantment(enchantment)) return;
            if (!enchantment.canEnchantItem(clickedItemStack)) return;

            clickedItemStack.addEnchantment(enchantment, set.getValue());

            event.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));

            event.setCancelled(true);
        }
    }

}


