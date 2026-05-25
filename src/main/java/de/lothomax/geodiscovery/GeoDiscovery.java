package de.lothomax.geodiscovery;

import de.lothomax.geodiscovery.command.GeoDiscoveryCommand;
import de.lothomax.geodiscovery.command.TaufeCommand;
import de.lothomax.geodiscovery.database.DatabaseManager;
import de.lothomax.geodiscovery.database.RegionCache;
import de.lothomax.geodiscovery.listener.PlayerMoveListener;
import de.lothomax.geodiscovery.manager.ConfigManager;
import de.lothomax.geodiscovery.manager.NamingManager;
import de.lothomax.geodiscovery.task.StructureCheckTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class GeoDiscovery extends JavaPlugin {

    private DatabaseManager databaseManager;
    private RegionCache regionCache;
    private ConfigManager configManager;
    private NamingManager namingManager;
    private BukkitTask structureCheckTask;

    @Override
    public void onEnable() {
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(getDataFolder(), getLogger());
        this.regionCache = new RegionCache(getLogger());
        this.namingManager = new NamingManager(this);

        // Setup DB and Cache
        this.databaseManager.initialize();
        this.regionCache.loadFromDatabase(this.databaseManager);

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);

        // Register Commands
        getCommand("taufe").setExecutor(new TaufeCommand(this));
        getCommand("geodiscovery").setExecutor(new GeoDiscoveryCommand(this));

        // Start Tasks
        long interval = configManager.getStructureCheckInterval();
        this.structureCheckTask = Bukkit.getScheduler().runTaskTimer(
                this,
                new StructureCheckTask(this),
                interval,
                interval
        );

        getLogger().info("GeoDiscovery has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.namingManager != null) {
            this.namingManager.cancelAllSessions();
        }

        if (this.structureCheckTask != null) {
            this.structureCheckTask.cancel();
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

    public NamingManager getNamingManager() {
        return namingManager;
    }
}
