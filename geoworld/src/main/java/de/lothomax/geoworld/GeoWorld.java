package de.lothomax.geoworld;

import de.lothomax.geoworld.config.GeoWorldConfig;
import de.lothomax.geoworld.listener.ChunkPopulateListener;
import de.lothomax.geoworld.module.river.RiverFlowModule;
import org.bukkit.plugin.java.JavaPlugin;

public class GeoWorld extends JavaPlugin {

    private GeoWorldConfig config;
    private RiverFlowModule riverFlowModule;

    @Override
    public void onEnable() {
        getLogger().info("Initializing GeoWorld...");

        // Load configuration
        this.config = new GeoWorldConfig(this);

        // Initialize modules
        if (this.config.isRiverFlowEnabled()) {
            this.riverFlowModule = new RiverFlowModule(this, this.config);
            this.riverFlowModule.start();

            // Register ChunkPopulateListener
            getServer().getPluginManager().registerEvents(new ChunkPopulateListener(this.riverFlowModule, this.config), this);
            getLogger().info("RiverFlowModule enabled and started.");
        }

        getLogger().info("GeoWorld enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling GeoWorld...");

        if (this.riverFlowModule != null) {
            this.riverFlowModule.stop();
        }

        getLogger().info("GeoWorld disabled.");
    }

    public GeoWorldConfig getGeoWorldConfig() {
        return config;
    }
}
