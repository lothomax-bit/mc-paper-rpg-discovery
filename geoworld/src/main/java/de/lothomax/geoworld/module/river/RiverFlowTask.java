package de.lothomax.geoworld.module.river;

import de.lothomax.geoworld.config.GeoWorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class RiverFlowTask extends BukkitRunnable {

    private final Plugin plugin;
    private final GeoWorldConfig config;
    private final LinkedBlockingQueue<Chunk> pendingChunks;
    private final Random random;
    private final RiverPathfinder pathfinder;
    private final WaterfallPlacer placer;

    private static final Set<String> ALLOWED_BIOMES = Set.of(
            "minecraft:jagged_peaks", "minecraft:frozen_peaks", "minecraft:stony_peaks",
            "minecraft:windswept_hills", "minecraft:windswept_gravelly_hills", "minecraft:snowy_slopes"
    );
    private static final Set<String> ALLOWED_CUSTOM_BIOMES = Set.of(
            "geoworld:crystal_peaks", "geoworld:skyreach_plateau",
            "geoworld:obsidian_spires", "geoworld:floating_isles"
    );

    public RiverFlowTask(Plugin plugin, GeoWorldConfig config, LinkedBlockingQueue<Chunk> pendingChunks) {
        this.plugin = plugin;
        this.config = config;
        this.pendingChunks = pendingChunks;
        this.random = new Random();
        this.pathfinder = new RiverPathfinder(config);
        this.placer = new WaterfallPlacer();
    }

    @Override
    public void run() {
        int chunksProcessed = 0;
        int limit = config.getChunksPerTick();

        while (chunksProcessed < limit && !pendingChunks.isEmpty()) {
            Chunk chunk = pendingChunks.poll();
            if (chunk == null) continue;

            try {
                if (!chunk.isLoaded()) continue;

                ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, true, false);
                World world = chunk.getWorld();

                processChunk(chunk, snapshot, world);
                chunksProcessed++;
            } catch (Exception e) {
                plugin.getLogger().warning("Error processing chunk for river flow: " + e.getMessage());
            }
        }
    }

    private void processChunk(Chunk chunk, ChunkSnapshot snapshot, World world) {
        int springsThisChunk = 0;
        int maxSprings = config.getSpringMaxPerChunk();
        int minY = config.getMinSpringY();
        int maxY = world.getMaxHeight();

        int startX = chunk.getX() << 4;
        int startZ = chunk.getZ() << 4;

        outer:
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int highestY = snapshot.getHighestBlockYAt(x, z);
                if (highestY < minY) continue;

                for (int y = minY; y <= Math.min(maxY - 2, highestY); y++) {
                    Material type = snapshot.getBlockType(x, y, z);

                    if (type == Material.WATER) {
                        Material above = snapshot.getBlockType(x, y + 1, z);
                        if (above.isAir()) {
                            // FIX: cache biome lookup – only one call per block
                            Biome biome = snapshot.getBiome(x, y, z);
                            NamespacedKey key = biome.getKey();
                            // FIX: use lowercase to match the Set entries
                            String fullKey = key.getNamespace().toLowerCase() + ":" + key.getKey().toLowerCase();

                            boolean isAllowed = ALLOWED_BIOMES.contains(fullKey)
                                    || ALLOWED_CUSTOM_BIOMES.contains(fullKey);

                            if (isAllowed) {
                                if (springsThisChunk >= maxSprings) {
                                    break outer; // FIX: break outer loop, not just inner
                                }

                                if (random.nextDouble() < config.getSpringChance()) {
                                    springsThisChunk++;

                                    final int blockX = startX + x;
                                    final int blockY = y;
                                    final int blockZ = startZ + z;

                                    Bukkit.getScheduler().runTask(plugin, () -> {
                                        List<RiverNode> nodes = pathfinder.trace(world, blockX, blockY, blockZ);
                                        placer.place(world, nodes, config);
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
