package de.lothomax.geodiscovery.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public List<String> getEnabledBiomes() {
        return config.getStringList("enabled-biomes");
    }

    public List<String> getEnabledStructures() {
        return config.getStringList("enabled-structures");
    }

    public int getRegionRadius() {
        return config.getInt("region-radius", 150);
    }

    public int getNamingTimeout() {
        return config.getInt("naming-timeout", 60);
    }

    public long getStructureCheckInterval() {
        return config.getLong("structure-check-interval", 100);
    }

    public String getRegionIcon(String key) {
        String iconPath = "region-icons." + key;
        if (config.contains(iconPath)) {
            return config.getString(iconPath);
        }
        return config.getString("region-icons.DEFAULT", "📍");
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "Message not found: " + key);
    }
}
