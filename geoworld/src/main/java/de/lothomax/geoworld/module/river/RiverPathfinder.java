package de.lothomax.geoworld.module.river;

import de.lothomax.geoworld.config.GeoWorldConfig;
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
            int bestNy = currentY;
            int bestNz = currentZ;
            int bestDirX = 0;
            int bestDirZ = 0;
            boolean foundLower = false;
            boolean foundWaterConfluence = false;

            // Find lowest neighbor
            for (int[] dir : DIRECTIONS) {
                int nx = currentX + dir[0];
                int nz = currentZ + dir[1];

                int ny = world.getHighestBlockYAt(nx, nz);

                if (ny < bestNy) {
                    bestNx = nx;
                    bestNy = ny;
                    bestNz = nz;
                    bestDirX = dir[0];
                    bestDirZ = dir[1];
                    foundLower = true;
                }
            }

            if (!foundLower) {
                for (int[] dir : DIRECTIONS) {
                    int nx = currentX + dir[0];
                    int nz = currentZ + dir[1];
                    int ny = world.getHighestBlockYAt(nx, nz);

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

            Block neighborBlock = world.getBlockAt(bestNx, bestNy, bestNz);
            if (neighborBlock.getType() == Material.WATER) {
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.CONFLUENCE, bestDirX, bestDirZ));
                currentX = bestNx;
                currentY = bestNy;
                currentZ = bestNz;
                iterations++;
                continue;
            }

            if (bestNy <= config.getSeaLevel()) {
                nodes.add(new RiverNode(bestNx, bestNy, bestNz, RiverNodeType.MOUTH, bestDirX, bestDirZ));
                break;
            }

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
