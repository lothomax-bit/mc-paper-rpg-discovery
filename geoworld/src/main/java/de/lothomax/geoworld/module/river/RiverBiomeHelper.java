package de.lothomax.geoworld.module.river;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class RiverBiomeHelper {

    public static Material getBankMaterial(Biome biome) {
        if (biome == null || biome.getKey() == null) return Material.GRAVEL;

        String key = biome.getKey().getKey().toUpperCase();

        return switch (key) {
            case "FROZEN_PEAKS", "SNOWY_SLOPES", "SNOWY_PLAINS",
                 "SNOWY_TAIGA", "SNOWY_BEACH", "ICE_SPIKES"
                -> Material.SNOW_BLOCK;
            case "DESERT", "BADLANDS", "ERODED_BADLANDS",
                 "WOODED_BADLANDS", "BEACH"
                -> Material.SAND;
            case "SWAMP", "MANGROVE_SWAMP"
                -> Material.MUD;
            case "MUSHROOM_FIELDS"
                -> Material.MYCELIUM;
            case "STONY_PEAKS", "STONY_SHORE", "WINDSWEPT_GRAVELLY_HILLS",
                 "JAGGED_PEAKS", "WINDSWEPT_HILLS"
                -> Material.GRAVEL;
            case "PLAINS", "SUNFLOWER_PLAINS", "MEADOW",
                 "FOREST", "FLOWER_FOREST", "BIRCH_FOREST",
                 "OLD_GROWTH_BIRCH_FOREST", "DARK_FOREST",
                 "TAIGA", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA",
                 "WINDSWEPT_FOREST", "WINDSWEPT_SAVANNA",
                 "SAVANNA", "SAVANNA_PLATEAU"
                -> Material.GRASS_BLOCK;
            case "JUNGLE", "SPARSE_JUNGLE", "BAMBOO_JUNGLE"
                -> Material.DIRT;
            case "NETHER_WASTES", "CRIMSON_FOREST", "WARPED_FOREST",
                 "SOUL_SAND_VALLEY", "BASALT_DELTAS"
                -> Material.SOUL_SAND;
            default -> Material.GRAVEL;
        };
    }

    public static Material getCustomBankMaterial(String namespaceKey) {
        if (namespaceKey == null) return Material.GRAVEL;

        return switch (namespaceKey.toLowerCase()) {
            case "geoworld:glacial_abyss",
                 "geoworld:aurora_tundra",
                 "geoworld:rime_coast"          -> Material.PACKED_ICE;
            case "geoworld:dune_sea",
                 "geoworld:coral_archipelago",
                 "geoworld:pearl_lagoon",
                 "geoworld:volcanic_island"     -> Material.SAND;
            case "geoworld:red_canyon",
                 "geoworld:ember_plains",
                 "geoworld:volcanic_ashfields"  -> Material.RED_SAND;
            case "geoworld:cursed_bayou",
                 "geoworld:mangrove_labyrinth",
                 "geoworld:peat_moors"          -> Material.MUD;
            case "geoworld:fungal_expanse",
                 "geoworld:fungal_depths"       -> Material.MYCELIUM;
            case "geoworld:salt_flats"          -> Material.CALCITE;
            case "geoworld:ancient_forest",
                 "geoworld:verdant_chasms",
                 "geoworld:dream_forest"        -> Material.MOSS_BLOCK;
            case "geoworld:spore_jungle",
                 "geoworld:bamboo_highlands"    -> Material.DIRT;
            default                             -> Material.GRAVEL;
        };
    }
}
