package me.crackma.pvpcore.kits.implementation;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.kits.Kit;
import me.crackma.pvpcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateKitCommand implements CommandExecutor, TabCompleter {
  private PVPCorePlugin plugin;
  public CreateKitCommand(PVPCorePlugin plugin) {
    this.plugin = plugin;
    PluginCommand pluginCommand = plugin.getCommand("createkit");
    pluginCommand.setExecutor(this);
    pluginCommand.setTabCompleter(this);
  }
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) return true;
    if (args.length < 4) return false;
    Player player = (Player) sender;
    if (plugin.getKitManager().getKit(args[0]) != null) {
      player.sendMessage("§cA kit with that name already exists.");
      return true;
    }
    try {
      Material material = Material.valueOf(args[3]);
      if (material == null) return false;
      Kit kit = new Kit(args[0], Integer.parseInt(args[1]) * 1000 /*milliseconds -> seconds*/, Integer.parseInt(args[2]), material, player.getInventory().getContents().clone());
      plugin.getKitDatabase().insertKit(kit).thenAccept(data -> {
        plugin.getKitManager().addKit(kit);
        player.sendMessage("§eCreated kit §d" + args[0] + "§e.");
      });
    } catch (NumberFormatException exception) {
      return false;
    }
    return true;
  }
  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
    List<String> completions = new ArrayList<>();
    switch (args.length) {
      case 1:
        completions.add("<kit_name>");
        break;
      case 2:
        completions.add("<cooldown>");
        break;
      case 3:
        completions.add("<inventoryPosition>");
        break;
      case 4:
        return Utils.getMaterialList(args[1]);
    }
    return completions;
  }
}
