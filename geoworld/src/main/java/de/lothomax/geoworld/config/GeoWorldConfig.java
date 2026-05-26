package de.lothomax.geoworld.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class GeoWorldConfig {

    private final Plugin plugin;
    private FileConfiguration config;
    private File configFile;

    // River flow properties
    private boolean riverFlowEnabled;
    private int minSpringY;
    private int seaLevel;
    private int waterfallThreshold;
    private int maxRiverLength;
    private double mountainLakeChance;
    private int mountainLakeMinRadius;
    private int mountainLakeMaxRadius;
    private double springChance;
    private int springMaxPerChunk;
    private int riverBankWidth;
    private int chunksPerTick;
    private boolean geodiscoveryHook;
    private String geodiscoveryLakeIcon;

    private int width1, depth1;
    private int width2, depth2;
    private int width3, depth3;
    private int width4, depth4;

    public GeoWorldConfig(Plugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "geoworld_config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("geoworld_config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = plugin.getResource("geoworld_config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            config.setDefaults(defConfig);
        }

        loadValues();
    }

    private void loadValues() {
        String path = "modules.river-flow.";
        riverFlowEnabled = config.getBoolean(path + "enabled", true);
        minSpringY = config.getInt(path + "min-spring-y", 200);
        seaLevel = config.getInt(path + "sea-level", 63);
        waterfallThreshold = config.getInt(path + "waterfall-threshold", 4);
        maxRiverLength = config.getInt(path + "max-river-length", 800);
        mountainLakeChance = config.getDouble(path + "mountain-lake-chance", 0.35);
        mountainLakeMinRadius = config.getInt(path + "mountain-lake-min-radius", 2);
        mountainLakeMaxRadius = config.getInt(path + "mountain-lake-max-radius", 3);
        springChance = config.getDouble(path + "spring-chance", 0.08);
        springMaxPerChunk = config.getInt(path + "spring-max-per-chunk", 1);

        width1 = config.getInt(path + "river-width-1", 1);
        depth1 = config.getInt(path + "river-depth-1", 1);
        width2 = config.getInt(path + "river-width-2", 2);
        depth2 = config.getInt(path + "river-depth-2", 2);
        width3 = config.getInt(path + "river-width-3", 4);
        depth3 = config.getInt(path + "river-depth-3", 3);
        width4 = config.getInt(path + "river-width-4", 7);
        depth4 = config.getInt(path + "river-depth-4", 4);

        riverBankWidth = config.getInt(path + "river-bank-width", 2);
        chunksPerTick = config.getInt(path + "chunks-per-tick", 3);
        geodiscoveryHook = config.getBoolean(path + "geodiscovery-hook", true);
        geodiscoveryLakeIcon = config.getString(path + "geodiscovery-lake-icon", "💧");
    }

    public RiverProfile getProfile(int y) {
        if (y > 400) return new RiverProfile(width1, depth1);
        if (y > 250) return new RiverProfile(width2, depth2);
        if (y > 150) return new RiverProfile(width3, depth3);
        return new RiverProfile(width4, depth4);
    }

    public boolean isRiverFlowEnabled() { return riverFlowEnabled; }
    public int getMinSpringY() { return minSpringY; }
    public int getSeaLevel() { return seaLevel; }
    public int getWaterfallThreshold() { return waterfallThreshold; }
    public int getMaxRiverLength() { return maxRiverLength; }
    public double getMountainLakeChance() { return mountainLakeChance; }
    public int getMountainLakeMinRadius() { return mountainLakeMinRadius; }
    public int getMountainLakeMaxRadius() { return mountainLakeMaxRadius; }
    public double getSpringChance() { return springChance; }
    public int getSpringMaxPerChunk() { return springMaxPerChunk; }
    public int getRiverBankWidth() { return riverBankWidth; }
    public int getChunksPerTick() { return chunksPerTick; }
    public boolean isGeodiscoveryHook() { return geodiscoveryHook; }
    public String getGeodiscoveryLakeIcon() { return geodiscoveryLakeIcon; }
}