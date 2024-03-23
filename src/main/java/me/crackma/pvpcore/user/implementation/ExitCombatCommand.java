package me.crackma.pvpcore.user.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.user.User;
import me.crackma.pvpcore.user.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExitCombatCommand implements CommandExecutor {
  private UserManager userManager;
  public ExitCombatCommand(PVPCorePlugin plugin) {
    userManager = plugin.getUserManager();
    plugin.getCommand("exitcombat").setExecutor(this);
  }
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) return true;
    Player player = (Player) sender;
    User user = userManager.get(player.getUniqueId());
    user.getStats().setCombatTimer(0);
    sender.sendMessage("§eExited combat.");
    return false;
  }
}
