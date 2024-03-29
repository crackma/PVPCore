package me.crackma.pvpcore.leaderboard;

import me.crackma.pvpcore.gui.Gui;
import me.crackma.pvpcore.gui.GuiButton;
import me.crackma.pvpcore.gui.GuiManager;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class LeaderboardSelectionGui extends Gui {
  private GuiManager guiManager;
  private LeaderboardManager leaderboardManager;
  public LeaderboardSelectionGui(GuiManager guiManager, LeaderboardManager leaderboardManager) {
    this.guiManager = guiManager;
    this.leaderboardManager = leaderboardManager;
  }
  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 27, "Leaderboard");
  }
  @Override
  public void decorate() {
    putButton(11, new GuiButton()
        .creator(Utils.itemStackCreator(Material.IRON_SWORD, "§dKills Leaderboard"))
        .leftConsumer(event -> guiManager.openGUI(leaderboardManager.getKillsGui(), event.getWhoClicked())));
    putButton(13, new GuiButton()
        .creator(Utils.itemStackCreator(Material.SPECTRAL_ARROW, "§dStreak Leaderboard"))
        .leftConsumer(event -> guiManager.openGUI(leaderboardManager.getBestStreaksGui(), event.getWhoClicked())));
    putButton(15, new GuiButton()
        .creator(Utils.itemStackCreator(Material.AMETHYST_SHARD, "§dGems Leaderboard"))
        .leftConsumer(event -> guiManager.openGUI(leaderboardManager.getGemsGui(), event.getWhoClicked())));
    super.decorate();
  }
}
