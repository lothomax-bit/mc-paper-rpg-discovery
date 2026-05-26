package de.lothomax.geoworld.module.river;

import de.lothomax.geoworld.config.GeoWorldConfig;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RiverPathfinder {

    private final GeoWorldConfig config;
    private final Random random;

    private static final int[][] DIRECTIONS = {
            {0, -1}, // N
            {0, 1},  // S
            {1, 0},  // E
            {-1, 0}  // W
    };

    public RiverPathfinder(GeoWorldConfig config) {
        this.config = config;
        this.random = new Random();
    }

    public List<RiverNode> trace(World world, int startX, int startY, int startZ) {
        List<RiverNode> nodes = new ArrayList<>();

        int currentX = startX;
        int currentY = startY;
        int currentZ = startZ;

        int iterations = 0;
        int maxIterations = config.getMaxRiverLength();

        while (iterations < maxIterations) {
            int bestNx = currentX;
            // FIX: use WORLD_SURFACE so water bodies don't inflate neighbour height
            int bestNy = world.getHighestBlockYAt(currentX, currentZ, HeightMap.WORLD_SURFACE);
            int bestNz = currentZ;
            int bestDirX = 0;
            int bestDirZ = 0;
            boolean foundLower = false;
            boolean foundWaterConfluence = false;

            // Find lowest neighbour using WORLD_SURFACE heightmap
            for (int[] dir : DIRECTIONS) {
                int nx = currentX + dir[0];
                int nz = currentZ + dir[1];
                // FIX: WORLD_SURFACE excludes fluids from height calculation
                int ny = world.getHighestBlockYAt(nx, nz, HeightMap.WORLD_SURFACE);

                if (ny < bestNy) {
                    bestNx = nx;
                    bestNy = ny;
                    bestNz = nz;
                    bestDirX = dir[0];
                    bestDirZ = dir[1];
                    foundLower = true;
                }
            }

            // If no lower neighbour found, check for water confluence at same height
            if (!foundLower) {
                for (int[] dir : DIRECTIONS) {
                    int nx = currentX + dir[0];
                    int nz = currentZ + dir[1];
                    int ny = world.getHighestBlockYAt(nx, nz, HeightMap.WORLD_SURFACE);

                    if (ny == currentY) {
                        Block b = world.getBlockAt(nx, ny, nz);
                        if (b.getType() == Material.WATER) {
                            bestNx = nx;
                            bestNy = ny;
                            bestNz = nz;
                            bestDirX = dir[0];
                            bestDirZ = dir[1];
                            foundWaterConfluence = true;
                            break;
                        }
                    }
                }
            }

            if (!foundLower && !foundWaterConfluence) {
                break;
            }

            int deltaY = currentY - bestNy;

            // FIX: Check confluence BEFORE sea-level and waterfall checks so a river
            // joining an existing watercourse doesn't get wrongly terminated or carved.
            Block neighborBlock = world.getBlockAt(bestNx, bestNy, bestNz);
            if (neighborBlock.getType() == Material.WATER) {
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.CONFLUENCE, bestDirX, bestDirZ));
                currentX = bestNx;
                currentY = bestNy;
                currentZ = bestNz;
                iterations++;
                continue;
            }

            // Reached the ocean / sea level
            if (bestNy <= config.getSeaLevel()) {
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.MOUTH, bestDirX, bestDirZ));
                break;
            }

            // Waterfall
            if (deltaY > config.getWaterfallThreshold()) {
                nodes.add(new RiverNode(currentX, currentY, currentZ, RiverNodeType.WATERFALL_START, bestDirX, bestDirZ));
                for (int y = currentY - 1; y > bestNy; y--) {
                    nodes.add(new RiverNode(currentX, y, currentZ, RiverNodeType.WATERFALL, bestDirX, bestDirZ));
                }
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.WATERFALL_END, bestDirX, bestDirZ));

                if (random.nextDouble() < config.getMountainLakeChance()) {
                    nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.LAKE, bestDirX, bestDirZ));
                }
            } else {
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.FLOW, bestDirX, bestDirZ));
            }

            currentX = bestNx;
            currentY = bestNy;
            currentZ = bestNz;
            iterations++;
        }

        return nodes;
    }
}
