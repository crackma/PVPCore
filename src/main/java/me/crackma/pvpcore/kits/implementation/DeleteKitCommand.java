package me.crackma.pvpcore.kits.implementation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.*;

import me.crackma.pvpcore.PVPCorePlugin;
import me.crackma.pvpcore.kits.Kit;

public class DeleteKitCommand implements CommandExecutor, TabCompleter {
  private PVPCorePlugin plugin;
  public DeleteKitCommand(PVPCorePlugin plugin) {
    this.plugin = plugin;
    PluginCommand pluginCommand = plugin.getCommand("deletekit");
    pluginCommand.setExecutor(this);
    pluginCommand.setTabCompleter(this);
  }
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length < 1) return false;
    Kit kit = plugin.getKitManager().getKit(args[0]);
    if (kit == null) return false;
    plugin.getKitDatabase().deleteKit(kit.getName()).thenAccept(data -> {
      plugin.getKitManager().deleteKit(kit);
      sender.sendMessage("§eDeleted kit §d" + args[0] + "§e.");
    });
    return true;
  }
  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
    if (args.length > 1) return null;
    List<String> completions = new ArrayList<>();
    for (Kit kit : plugin.getKitManager().getKitSet()) {
      completions.add(kit.getName());
    }
    return completions;
  }
}
