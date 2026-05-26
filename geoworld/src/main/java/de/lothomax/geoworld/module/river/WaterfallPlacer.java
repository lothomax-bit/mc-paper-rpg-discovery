package de.lothomax.geoworld.module.river;

import de.lothomax.geoworld.config.GeoWorldConfig;
import de.lothomax.geoworld.config.RiverProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WaterfallPlacer {

    private static final Set<Material> CARVEABLE = Set.of(
            Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT,
            Material.ROOTED_DIRT, Material.PODZOL, Material.MUD, Material.CLAY,
            Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE,
            Material.GRAVEL, Material.SAND, Material.SANDSTONE,
            Material.RED_SAND, Material.RED_SANDSTONE,
            Material.SNOW_BLOCK, Material.POWDER_SNOW,
            Material.DEEPSLATE, Material.TUFF, Material.CALCITE,
            Material.MYCELIUM, Material.MOSS_BLOCK, Material.SOUL_SAND,
            Material.SOUL_SOIL, Material.PACKED_ICE, Material.BLUE_ICE,
            Material.TERRACOTTA, Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA,
            Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA,
            Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA,
            Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA,
            Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA,
            Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA,
            Material.BLACK_TERRACOTTA
    );

    private final Random random = new Random();

    public void place(World world, List<RiverNode> nodes, GeoWorldConfig config) {
        if (nodes == null || nodes.isEmpty()) return;

        int waterfallWidth = 1;

        for (RiverNode node : nodes) {
            RiverProfile profile = config.getProfile(node.y());
            int width = profile.width();
            int depth = profile.depth();
            int bankWidth = config.getRiverBankWidth();

            int perpDirX = -node.dirZ();
            int perpDirZ = node.dirX();

            if (node.type() == RiverNodeType.WATERFALL_START) {
                waterfallWidth = width;
            }

            switch (node.type()) {
                case FLOW:
                case MOUTH:
                    carveRiverbed(world, node, width, depth, perpDirX, perpDirZ);
                    placeBanks(world, node, width, bankWidth, perpDirX, perpDirZ);
                    break;
                case CONFLUENCE:
                    placeBanks(world, node, width, bankWidth, perpDirX, perpDirZ);
                    break;
                case WATERFALL_START:
                case WATERFALL:
                case WATERFALL_END:
                    placeWaterfall(world, node, waterfallWidth, perpDirX, perpDirZ);
                    break;
                case LAKE:
                    placeLake(world, node, depth, bankWidth, config);
                    break;
            }
        }
    }

    private void carveRiverbed(World world, RiverNode node, int width, int depth, int perpDirX, int perpDirZ) {
        for (int w = -width; w <= width; w++) {
            int cx = node.x() + perpDirX * w;
            int cz = node.z() + perpDirZ * w;

            int highestY = getHighestSolid(world, cx, cz);
            if (highestY <= 0) continue;

            int bottomY = highestY - depth;

            Block bottomBlock = world.getBlockAt(cx, bottomY, cz);
            if (isCarveableBlock(bottomBlock.getType())) {
                bottomBlock.setType(Material.GRAVEL);
            }

            for (int dy = 1; dy <= depth; dy++) {
                Block waterBlock = world.getBlockAt(cx, bottomY + dy, cz);
                if (isCarveableBlock(waterBlock.getType())) {
                    waterBlock.setType(Material.WATER);
                }
            }
        }
    }

    private void placeBanks(World world, RiverNode node, int width, int bankWidth, int perpDirX, int perpDirZ) {
        for (int side : new int[]{1, -1}) {
            for (int b = 1; b <= bankWidth; b++) {
                int ux = node.x() + perpDirX * (side * (width + b));
                int uz = node.z() + perpDirZ * (side * (width + b));

                int highestY = getHighestSolid(world, ux, uz);
                if (highestY <= 0) continue;

                Block topBlock = world.getBlockAt(ux, highestY, uz);
                if (isCarveableBlock(topBlock.getType())) {
                    Material bankMat = getBankMaterialForLocation(world, ux, highestY, uz);
                    topBlock.setType(bankMat);
                }
            }
        }
    }

    private void placeWaterfall(World world, RiverNode node, int waterfallWidth, int perpDirX, int perpDirZ) {
        for (int w = -waterfallWidth; w <= waterfallWidth; w++) {
            int cx = node.x() + perpDirX * w;
            int cz = node.z() + perpDirZ * w;

            Block b = world.getBlockAt(cx, node.y(), cz);
            if (b.getType().isAir() || isCarveableBlock(b.getType())) {
                b.setType(Material.WATER);
            }
        }
    }

    private void placeLake(World world, RiverNode node, int depth, int bankWidth, GeoWorldConfig config) {
        int minR = config.getMountainLakeMinRadius();
        int maxR = config.getMountainLakeMaxRadius();
        int radius = minR + random.nextInt((maxR - minR) + 1);
        int outerR = radius + bankWidth;

        int lakeX = node.x();
        int lakeZ = node.z();

        for (int dx = -outerR; dx <= outerR; dx++) {
            for (int dz = -outerR; dz <= outerR; dz++) {
                int distSq = dx * dx + dz * dz;
                int bx = lakeX + dx;
                int bz = lakeZ + dz;

                int top = getHighestSolid(world, bx, bz);
                if (top <= 0) continue;

                if (distSq <= radius * radius) {
                    Block bottom = world.getBlockAt(bx, top - depth, bz);
                    if (isCarveableBlock(bottom.getType())) {
                        bottom.setType(Material.GRAVEL);
                    }

                    for (int dy = 0; dy < depth; dy++) {
                        Block b = world.getBlockAt(bx, top - dy, bz);
                        if (isCarveableBlock(b.getType())) {
                            b.setType(Material.WATER);
                        }
                    }
                } else if (distSq <= outerR * outerR) {
                    Block b = world.getBlockAt(bx, top, bz);
                    if (isCarveableBlock(b.getType())) {
                        b.setType(getBankMaterialForLocation(world, bx, top, bz));
                    }
                }
            }
        }

        if (config.isGeodiscoveryHook()) {
            Plugin geo = Bukkit.getPluginManager().getPlugin("GeoDiscovery");
            if (geo != null && geo.isEnabled()) {
                File logFile = new File(geo.getDataFolder().getParentFile(), "GeoWorld/pending_lakes.csv");
                logFile.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(logFile, true)) {
                    writer.write(String.format("%s,%d,%d,%d,%d\n",
                            world.getUID().toString(), lakeX, node.y(), lakeZ, radius));
                } catch (IOException e) {
                    Bukkit.getLogger().warning("GeoWorld could not log pending lake: " + e.getMessage());
                }
            }
        }
    }

    private int getHighestSolid(World world, int x, int z) {
        int y = world.getHighestBlockYAt(x, z) + 5;
        while (y > world.getMinHeight()) {
            Material type = world.getBlockAt(x, y, z).getType();
            if (type.isSolid() && !isLeaves(type)) {
                return y;
            }
            y--;
        }
        return world.getHighestBlockYAt(x, z);
    }

    private boolean isLeaves(Material type) {
        return type == Material.OAK_LEAVES || type == Material.SPRUCE_LEAVES || type == Material.BIRCH_LEAVES
            || type == Material.JUNGLE_LEAVES || type == Material.ACACIA_LEAVES || type == Material.DARK_OAK_LEAVES
            || type == Material.MANGROVE_LEAVES || type == Material.CHERRY_LEAVES || type == Material.AZALEA_LEAVES
            || type == Material.FLOWERING_AZALEA_LEAVES;
    }

    private boolean isCarveableBlock(Material m) {
        return CARVEABLE.contains(m);
    }

    private Material getBankMaterialForLocation(World world, int x, int y, int z) {
        Biome biome = world.getBiome(x, y, z);
        String namespace = biome.getKey().getNamespace().toLowerCase();

        if (namespace.equals("geoworld")) {
            return RiverBiomeHelper.getCustomBankMaterial(biome.getKey().asString());
        }

        return RiverBiomeHelper.getBankMaterial(biome);
    }
}
