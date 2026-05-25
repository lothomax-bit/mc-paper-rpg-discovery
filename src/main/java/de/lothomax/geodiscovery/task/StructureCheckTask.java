package de.lothomax.geodiscovery.task;

import de.lothomax.geodiscovery.database.RegionCache;
import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import de.lothomax.geodiscovery.session.NamingSessionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class StructureCheckTask extends BukkitRunnable {

    private final RegionCache regionCache;
    private final NamingSessionManager sessionManager;
    private final ConfigManager configManager;
    private final Map<UUID, Long> lastStructureCheck = new HashMap<>();

    public StructureCheckTask(RegionCache regionCache, NamingSessionManager sessionManager, ConfigManager configManager) {
        this.regionCache = regionCache;
        this.sessionManager = sessionManager;
        this.configManager = configManager;
    }

    @Override
    public void run() {
        List<String> enabledStructures = configManager.getEnabledStructures();
        if (enabledStructures == null || enabledStructures.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();
        int regionRadius = configManager.getRegionRadius();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // 2a. Skip if active session
            if (sessionManager.hasSession(player.getUniqueId())) {
                continue;
            }

            // Check cooldown
            long lastCheck = lastStructureCheck.getOrDefault(player.getUniqueId(), 0L);
            if (now - lastCheck < 5000) {
                continue;
            }
            lastStructureCheck.put(player.getUniqueId(), now);

            Location playerLoc = player.getLocation();
            String worldUuid = player.getWorld().getUID().toString();

            // 2c. Iterate structure keys
            for (String key : enabledStructures) {
                StructureType structureType = Registry.STRUCTURE_TYPE.get(NamespacedKey.minecraft(key.toLowerCase()));
                if (structureType == null) {
                    continue; // invalid key in config
                }

                org.bukkit.util.StructureSearchResult searchResult = player.getWorld().locateNearestStructure(
                        playerLoc,
                        structureType,
                        regionRadius * 2,
                        false
                );

                if (searchResult == null) {
                    continue;
                }

                Location structLoc = searchResult.getLocation();

                double dx = structLoc.getX() - playerLoc.getX();
                double dz = structLoc.getZ() - playerLoc.getZ();
                double distanceSq = dx * dx + dz * dz;

                if (distanceSq > regionRadius * regionRadius) {
                    continue;
                }

                Optional<DiscoveredRegion> existingRegion = regionCache.findNearestRegion(worldUuid, structLoc.getX(), structLoc.getZ());
                if (existingRegion.isPresent()) {
                    DiscoveredRegion region = existingRegion.get();
                    double rdx = region.getCenterX() - structLoc.getX();
                    double rdz = region.getCenterZ() - structLoc.getZ();
                    double rDistSq = rdx * rdx + rdz * rdz;

                    if (rDistSq <= region.getRadius() * region.getRadius()) {
                        continue; // Already known
                    }
                }

                // Start session for structure
                sessionManager.startSession(player, structLoc, "minecraft:" + key.toLowerCase(), "STRUCTURE");
                break; // only first unknown structure per tick per player
            }
        }
    }
}