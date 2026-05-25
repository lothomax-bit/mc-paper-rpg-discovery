package de.lothomax.geodiscovery;

import de.lothomax.geodiscovery.command.GeoDiscoveryCommand;
import de.lothomax.geodiscovery.command.TaufeCommand;
import de.lothomax.geodiscovery.database.DatabaseManager;
import de.lothomax.geodiscovery.database.RegionCache;
import de.lothomax.geodiscovery.listener.PlayerMoveListener;
import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.session.NamingSessionManager;
import de.lothomax.geodiscovery.task.StructureCheckTask;
import org.bukkit.plugin.java.JavaPlugin;

public class GeoDiscovery extends JavaPlugin {

    private DatabaseManager databaseManager;
    private RegionCache regionCache;
    private ConfigManager configManager;
    private NamingSessionManager sessionManager;
    private StructureCheckTask structureTask;

    @Override
    public void onEnable() {
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(getDataFolder(), getLogger());
        this.regionCache = new RegionCache(getLogger());

        // Setup DB and Cache
        this.databaseManager.initialize();
        this.regionCache.loadFromDatabase(this.databaseManager);

        // Session Manager
        this.sessionManager = new NamingSessionManager(this);

        // Register Listeners
        getServer().getPluginManager().registerEvents(
            new PlayerMoveListener(regionCache, sessionManager, configManager), this);
        getServer().getPluginManager().registerEvents(sessionManager, this);

        // Register Commands
        getCommand("taufe").setExecutor(new TaufeCommand(sessionManager, configManager));
        getCommand("geodiscovery").setExecutor(new GeoDiscoveryCommand(this, regionCache, configManager));

        // Start Tasks
        this.structureTask = new StructureCheckTask(regionCache, sessionManager, configManager);
        this.structureTask.runTaskTimer(this, 20L, configManager.getStructureCheckInterval());

        getLogger().info("GeoDiscovery has been enabled!");
    }

    @Override
    public void onDisable() {
        // Alle aktiven Sessions canceln
        if (this.sessionManager != null) {
            this.sessionManager.cancelAllSessions();
        }

        // Struktur-Task stoppen
        if (this.structureTask != null) {
            this.structureTask.cancel();
        }

        if (this.databaseManager != null) {
            this.databaseManager.close();
        }

        getLogger().info("GeoDiscovery has been disabled!");
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public RegionCache getRegionCache() {
        return regionCache;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public NamingSessionManager getSessionManager() {
        return sessionManager;
    }
}
