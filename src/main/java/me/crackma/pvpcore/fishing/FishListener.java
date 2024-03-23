package me.crackma.pvpcore.fishing;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.Stats;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;

public class FishListener implements Listener {
	private PVPCorePlugin plugin;
	public FishListener(PVPCorePlugin plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	@EventHandler
	public void onFish(PlayerFishEvent event) {
		event.setExpToDrop(0);
		Item item = (Item) event.getCaught();
		item.setItemStack(new ItemStack(Material.DIAMOND));
		item.setPickupDelay(Integer.MAX_VALUE);
		UserManager userManager = plugin.getUserManager();
		Player player = event.getPlayer();
		User user = userManager.get(player.getUniqueId());
		Stats stats = user.getStats();
		stats.setGems(stats.getGems() + 1);
		plugin.getUserDatabase().updateOne(user);
		userManager.updateBoard(user);
		player.sendMessage("§eYou fished up §d1 gem§e!");
		new BukkitRunnable() {
			  @Override
			  public void run() {
				  item.remove();
			  }
			}.runTaskLater(plugin, 100);
	}
}
