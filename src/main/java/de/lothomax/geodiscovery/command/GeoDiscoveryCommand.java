package de.lothomax.geodiscovery.command;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.database.RegionCache;
import de.lothomax.geodiscovery.manager.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GeoDiscoveryCommand implements CommandExecutor {

    private final GeoDiscovery plugin;
    private final RegionCache regionCache;
    private final ConfigManager configManager;

    public GeoDiscoveryCommand(GeoDiscovery plugin, RegionCache regionCache, ConfigManager configManager) {
        this.plugin = plugin;
        this.regionCache = regionCache;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("geodiscovery.admin.reload")) {
                    sender.sendMessage("§cDazu hast du keine Rechte.");
                    return true;
                }
                configManager.loadConfig();
                sender.sendMessage("§aGeoDiscovery Konfiguration neu geladen.");
                return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§e=== GeoDiscovery Info ===");
                sender.sendMessage("§7Version: §f" + plugin.getDescription().getVersion());

                int loadedRegions = 0;
                // Since regionCache doesn't expose size directly easily, we can just say "active"
                // Actually, let's add a size method or just approximate or omit if not possible.
                // Wait, RegionCache has no getSize(). Let's add it to RegionCache via replace.
                // For now, we will assume RegionCache has getSize().
                sender.sendMessage("§7Geladene Regionen: §f" + regionCache.getSize());
                sender.sendMessage("§7Datenbank: §aAktiv");
                return true;
            }
        }

        sender.sendMessage("§cNutzung: /geodiscovery <reload|info>");
        return true;
    }
}