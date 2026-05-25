package de.lothomax.geodiscovery.listener;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    private final GeoDiscovery plugin;

    public PlayerMoveListener(GeoDiscovery plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null || (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        String worldUuid = player.getWorld().getUID().toString();

        // Check if player entered a known region
        Optional<DiscoveredRegion> nearestRegion = plugin.getRegionCache().findNearestRegion(worldUuid, to.getX(), to.getZ());

        if (nearestRegion.isPresent()) {
            DiscoveredRegion region = nearestRegion.get();
            double dx = region.getCenterX() - to.getX();
            double dz = region.getCenterZ() - to.getZ();
            double distanceSq = dx * dx + dz * dz;

            if (distanceSq <= region.getRadius() * region.getRadius()) {
                sendActionBar(player, region);
                return; // Player is inside a known region
            }
        }

        // Check if player is discovering a new biome
        if (plugin.getNamingManager().hasActiveSession(player.getUniqueId())) {
            return; // Player is already naming a region
        }

        Biome biome = to.getBlock().getBiome();
        String biomeName = biome.name();

        if (plugin.getConfigManager().getEnabledBiomes().contains(biomeName)) {
            // Check if there's an existing region of this biome nearby to prevent rapid re-discovery.
            // Simplified check: if not in any region, start discovery

            DiscoveredRegion.Builder builder = new DiscoveredRegion.Builder()
                    .discovererUuid(player.getUniqueId())
                    .discovererName(player.getName())
                    .worldUuid(player.getWorld().getUID())
                    .centerX(to.getX())
                    .centerZ(to.getZ())
                    .radius(plugin.getConfigManager().getRegionRadius())
                    .regionType("BIOME")
                    .biomeKey(biomeName);

            plugin.getNamingManager().startSession(player, builder);
        }
    }

    private void sendActionBar(Player player, DiscoveredRegion region) {
        String format = plugin.getConfigManager().getMessage("actionbar-format");
        String icon = plugin.getConfigManager().getRegionIcon(region.getBiomeKey());

        String message = format.replace("%icon%", icon)
                .replace("%name%", region.getRegionName())
                .replace("%player%", region.getDiscovererName());

        // Parse legacy color codes for Adventure component
        Component component = LegacyComponentSerializer.legacySection().deserialize(message.replace("§", "\u00A7"));
        player.sendActionBar(component);
    }
}
