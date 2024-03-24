package me.crackma.pvpcore.leaderboard;

import me.crackma.pvpcore.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
public class LeaderboardGui extends Gui {
  private String title;
  public LeaderboardGui(String title) {
    this.title = title;
  }
  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 27, title);
  }
}
