package me.crackma.pvpcore.leaderboard;

import me.crackma.pvpcore.gui.GuiManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crackma.pvpcore.PVPCorePlugin;

public class LeaderboardCommand implements CommandExecutor {
  private GuiManager guiManager;
  private LeaderboardManager leaderboardManager;
  public LeaderboardCommand(PVPCorePlugin plugin) {
    this.guiManager = plugin.getGuiManager();
    this.leaderboardManager = plugin.getLeaderboardManager();
    plugin.getCommand("leaderboard").setExecutor(this);
  }
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cOnly players can execute this command.");
      return true;
    }
    Player player = (Player) sender;
    guiManager.openGUI(new LeaderboardSelectionGui(guiManager, leaderboardManager), player);
    return true;
  }
}
