package de.lothomax.geodiscovery.listener;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.database.RegionCache;
import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import de.lothomax.geodiscovery.session.NamingSessionManager;
import de.lothomax.geodiscovery.util.ActionBarUtil;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final RegionCache regionCache;
    private final NamingSessionManager sessionManager;
    private final ConfigManager configManager;
    private final Map<UUID, Long> lastActionbarDisplay = new HashMap<>();

    public PlayerMoveListener(RegionCache regionCache, NamingSessionManager sessionManager, ConfigManager configManager) {
        this.regionCache = regionCache;
        this.sessionManager = sessionManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        // Only trigger when block coordinates have changed
        if (from.getBlockX() == to.getBlockX() &&
            from.getBlockY() == to.getBlockY() &&
            from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();

        // 1. Get current biome
        Biome biome = to.getBlock().getBiome();

        // 2. Get biome key as string
        String fullKey = biome.getKey().toString(); // e.g., "minecraft:desert"
        String keyWithoutNamespace = biome.getKey().getKey(); // e.g., "desert"

        // 3. Check if biome is in enabled-biomes
        boolean isEnabled = configManager.getEnabledBiomes().contains(keyWithoutNamespace) ||
                            configManager.getEnabledBiomes().contains(fullKey);

        // 4. Return if not enabled
        if (!isEnabled) {
            return;
        }

        // 5. Check if player has an active session
        if (sessionManager.hasSession(player.getUniqueId())) {
            return;
        }

        // 6. Get world UUID
        String worldUuid = player.getWorld().getUID().toString();

        // 7. Call findNearestRegion
        Optional<DiscoveredRegion> nearestRegion = regionCache.findNearestRegion(worldUuid, to.getX(), to.getZ());

        // 8. If present and distance <= radius
        if (nearestRegion.isPresent()) {
            DiscoveredRegion region = nearestRegion.get();
            double dx = region.getCenterX() - to.getX();
            double dz = region.getCenterZ() - to.getZ();
            double distanceSq = dx * dx + dz * dz;

            if (distanceSq <= region.getRadius() * region.getRadius()) {
                // Player is in known region
                long now = System.currentTimeMillis();
                long lastDisplay = lastActionbarDisplay.getOrDefault(player.getUniqueId(), 0L);

                // Show actionbar via ActionBarUtil (2000ms cooldown)
                if (now - lastDisplay > 2000) {
                    String actionbarText = ActionBarUtil.buildActionBarText(region, configManager);
                    ActionBarUtil.send(player, actionbarText);
                    lastActionbarDisplay.put(player.getUniqueId(), now);
                }
                return;
            }
        }

        // 9. If no result or distance > radius
        // Player entered unknown territory
        sessionManager.startSession(player, to, fullKey, "BIOME");
    }
}