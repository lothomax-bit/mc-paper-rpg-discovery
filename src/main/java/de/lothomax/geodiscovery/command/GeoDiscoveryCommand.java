package de.lothomax.geodiscovery.command;

import de.lothomax.geodiscovery.GeoDiscovery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GeoDiscoveryCommand implements CommandExecutor {

    private final GeoDiscovery plugin;

    public GeoDiscoveryCommand(GeoDiscovery plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("geodiscovery.admin.reload")) {
            sender.sendMessage("§cDazu hast du keine Rechte.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().loadConfig();
            sender.sendMessage("§aGeoDiscovery Konfiguration neu geladen.");
            return true;
        }

        sender.sendMessage("§cNutzung: /geodiscovery reload");
        return true;
    }
}
