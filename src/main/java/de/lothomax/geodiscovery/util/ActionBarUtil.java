package de.lothomax.geodiscovery.util;

import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class ActionBarUtil {

    public static void send(Player player, String message) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(message.replace("&", "\u00A7").replace("§", "\u00A7"));
        player.sendActionBar(component);
    }

    public static String buildActionBarText(DiscoveredRegion region, ConfigManager config) {
        String icon = config.getRegionIcon(region.getBiomeKey());
        if (icon == null || icon.isEmpty()) {
            icon = "📍";
        }

        String format = config.getMessage("actionbar-format");
        if (format == null) {
            format = "%icon% %name% (entdeckt von %player%)";
        }

        return format.replace("%icon%", icon)
                .replace("%name%", region.getRegionName() != null ? region.getRegionName() : "Unbekannt")
                .replace("%player%", region.getDiscovererName() != null ? region.getDiscovererName() : "Unbekannt");
    }
}