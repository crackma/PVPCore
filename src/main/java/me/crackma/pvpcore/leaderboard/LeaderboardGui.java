package me.crackma.pvpcore.leaderboard;

import me.crackma.pvpcore.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
public class LeaderboardGui extends Gui {
  public LeaderboardGui() { }
  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 27);
  }
}
