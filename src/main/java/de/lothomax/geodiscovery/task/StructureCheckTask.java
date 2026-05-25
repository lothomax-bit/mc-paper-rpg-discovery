package de.lothomax.geodiscovery.task;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.Chunk;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;

import java.util.Optional;

public class StructureCheckTask implements Runnable {

    private final GeoDiscovery plugin;

    public StructureCheckTask(GeoDiscovery plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getNamingManager().hasActiveSession(player.getUniqueId())) {
                continue;
            }

            Location loc = player.getLocation();
            String worldUuid = loc.getWorld().getUID().toString();

            // First check if already in a known region
            Optional<DiscoveredRegion> nearestRegion = plugin.getRegionCache().findNearestRegion(worldUuid, loc.getX(), loc.getZ());
            if (nearestRegion.isPresent()) {
                DiscoveredRegion region = nearestRegion.get();
                double dx = region.getCenterX() - loc.getX();
                double dz = region.getCenterZ() - loc.getZ();
                if (dx * dx + dz * dz <= region.getRadius() * region.getRadius()) {
                    continue; // Already in a discovered region
                }
            }

            // Check for structures using Chunk API for better performance
            Chunk currentChunk = loc.getChunk();
            boolean found = false;

            for (GeneratedStructure genStructure : currentChunk.getStructures()) {
                if (genStructure.getBoundingBox().contains(loc.getX(), loc.getY(), loc.getZ())) {
                    Structure structure = genStructure.getStructure();
                    String structureKey = structure.getKey().getKey().toUpperCase();

                    if (plugin.getConfigManager().getEnabledStructures().contains(structureKey)) {
                        DiscoveredRegion.Builder builder = new DiscoveredRegion.Builder()
                                .discovererUuid(player.getUniqueId())
                                .discovererName(player.getName())
                                .worldUuid(loc.getWorld().getUID())
                                .centerX(loc.getX())
                                .centerZ(loc.getZ())
                                .radius(plugin.getConfigManager().getRegionRadius())
                                .regionType("STRUCTURE")
                                .biomeKey(structureKey);

                        plugin.getNamingManager().startSession(player, builder);
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                continue;
            }
        }
    }
}
