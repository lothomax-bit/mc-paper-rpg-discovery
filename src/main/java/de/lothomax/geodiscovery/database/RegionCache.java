package de.lothomax.geodiscovery.database;

import de.lothomax.geodiscovery.model.DiscoveredRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class RegionCache {

    private final Map<String, List<DiscoveredRegion>> cache = new ConcurrentHashMap<>();
    private final Logger logger;

    public RegionCache(Logger logger) {
        this.logger = logger;
    }

    public void loadFromDatabase(DatabaseManager db) {
        db.getAllRegions().thenAccept(regions -> {
            cache.clear();
            for (DiscoveredRegion region : regions) {
                String worldUuid = region.getWorldUuid().toString();
                cache.computeIfAbsent(worldUuid, k -> new ArrayList<>()).add(region);
            }
            logger.info("Loaded " + regions.size() + " regions into cache.");
        }).exceptionally(e -> {
            logger.severe("Failed to load regions into cache: " + e.getMessage());
            return null;
        });
    }

    public void addRegion(DiscoveredRegion region) {
        String worldUuid = region.getWorldUuid().toString();
        cache.computeIfAbsent(worldUuid, k -> new ArrayList<>()).add(region);
    }

    public Optional<DiscoveredRegion> findNearestRegion(String worldUuid, double x, double z) {
        List<DiscoveredRegion> regionsInWorld = cache.get(worldUuid);
        if (regionsInWorld == null || regionsInWorld.isEmpty()) {
            return Optional.empty();
        }

        DiscoveredRegion nearest = null;
        double minDistanceSq = Double.MAX_VALUE;

        for (DiscoveredRegion region : regionsInWorld) {
            double dx = region.getCenterX() - x;
            double dz = region.getCenterZ() - z;
            double distanceSq = dx * dx + dz * dz;

            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                nearest = region;
            }
        }

        return Optional.ofNullable(nearest);
    }

    public void clear() {
        cache.clear();
    }
}
