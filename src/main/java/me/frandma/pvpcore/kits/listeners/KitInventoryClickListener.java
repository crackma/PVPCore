package me.frandma.pvpcore.kits.listeners;

import me.frandma.pvpcore.kits.Kit;
import me.frandma.pvpcore.kits.KitManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class KitInventoryClickListener implements Listener {
    @EventHandler
    public void onKitInventoryClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (event.getClickedInventory() != KitManager.getKitInventory()) return;
        event.setCancelled(true);
        ItemStack clickedItemStack = event.getCurrentItem();
        if (clickedItemStack == null) return;
        String kitName = clickedItemStack.getItemMeta().getDisplayName();
        kitName = kitName.replace("§d", "");
        kitName = kitName.replace(" §ekit. §7(Click to claim)", "");
        Kit kit = KitManager.getKit(kitName);
        if (kit == null) return;
        kit.giveTo(event.getWhoClicked());
        player.sendMessage("§eGave §d" + kit.getName() + " §ekit to §d" + player.getName() + "§e.");
    }
}
