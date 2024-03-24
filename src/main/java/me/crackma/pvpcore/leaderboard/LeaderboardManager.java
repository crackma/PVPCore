package me.crackma.pvpcore.leaderboard;

import lombok.Getter;
import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.gui.GuiButton;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.Map.Entry;

public class LeaderboardManager {
  private PVPCorePlugin plugin;
  @Getter
  private LinkedHashMap<UUID, Integer> sortedKillsMap;
  @Getter
  private LeaderboardGui killsGui;
  @Getter
  private LinkedHashMap<UUID, Integer> sortedBestStreaksMap;
  @Getter
  private LeaderboardGui bestStreaksGui;
  @Getter
  private LinkedHashMap<UUID, Integer> sortedGemsMap;
  @Getter
  private LeaderboardGui gemsGui;
  public LeaderboardManager(PVPCorePlugin plugin) {
    this.plugin = plugin;
    sortedKillsMap = new LinkedHashMap<>();
    sortedBestStreaksMap = new LinkedHashMap<>();
    sortedGemsMap = new LinkedHashMap<>();
    killsGui = new LeaderboardGui("Kills Leaderboard");
    bestStreaksGui = new LeaderboardGui("Streaks Leaderboard");
    gemsGui = new LeaderboardGui("Gems Leaderboard");
    new BukkitRunnable() {
      @Override
      public void run() {
        updateLeaderboard();
      }
    }.runTaskTimer(plugin, 0, 6000);
  }
  public void updateLeaderboard() {
    long start = System.currentTimeMillis();
    HashMap<UUID, Integer> unsortedKillsMap = new HashMap<>();
    HashMap<UUID, Integer> unsortedBestStreaksMap = new HashMap<>();
    HashMap<UUID, Integer> unsortedGemsMap = new HashMap<>();
    plugin.getUserDatabase().getAllDocuments().thenAccept(iterable -> {
      Iterator iterator = iterable.iterator();
      while (iterator.hasNext()) {
        Document document = (Document) iterator.next();
        UUID uuid = UUID.fromString(document.getString("_id"));
        unsortedKillsMap.put(uuid, (int)document.getOrDefault("kills", 0));
        unsortedBestStreaksMap.put(uuid, (int)document.getOrDefault("bestStreak", 0));
        unsortedGemsMap.put(uuid, (int)document.getOrDefault("gems", 0));
      }
      sortAndUpdate(killsGui, unsortedKillsMap, sortedKillsMap);
      sortAndUpdate(bestStreaksGui, unsortedBestStreaksMap, sortedBestStreaksMap);
      sortAndUpdate(gemsGui, unsortedGemsMap, sortedGemsMap);
      long end = System.currentTimeMillis();
      Bukkit.getLogger().info("Updated leaderboard in " + (end - start) + " ms.");
    });
  }
  private void sortAndUpdate(LeaderboardGui gui, HashMap<UUID, Integer> unsortedMap, HashMap<UUID, Integer> sortedMap) {
    sortedMap.clear();
    unsortedMap.entrySet()
        .stream()
        .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
        .forEachOrdered(entry -> {
          sortedMap.put(entry.getKey(), entry.getValue());
        });
    int leaderboardPosition = 0;
    gui.getButtonMap().clear();
    for (Entry<UUID, Integer> entry : sortedMap.entrySet()) {
      leaderboardPosition++;
      if (leaderboardPosition > 10) return;
      ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
      SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
      PlayerProfile playerProfile = offlinePlayer.getPlayerProfile();
      skullMeta.setOwnerProfile(playerProfile);
      int inventorySlot = plugin.getConfig().getInt("leaderboardrankincorrespondancetotheinventoryslot." + leaderboardPosition);
      if (offlinePlayer.hasPlayedBefore()) {
        skullMeta.setDisplayName("§e§l#" + leaderboardPosition + " §f" + offlinePlayer.getName() + " §8(" + entry.getValue() + ")");
        skull.setItemMeta(skullMeta);
        gui.putButton(inventorySlot, new GuiButton().creator(skull));
      } else {
        int finalLeaderboardPosition = leaderboardPosition;
        playerProfile.update().thenAccept(updatedPlayerProfile -> {
          skullMeta.setDisplayName("§e§l#" + finalLeaderboardPosition + " §f" + updatedPlayerProfile.getName() + " §8(" + entry.getValue() + ")");
          skull.setItemMeta(skullMeta);
          gui.putButton(inventorySlot, new GuiButton().creator(skull));
        });
      }
    }
  }
}
