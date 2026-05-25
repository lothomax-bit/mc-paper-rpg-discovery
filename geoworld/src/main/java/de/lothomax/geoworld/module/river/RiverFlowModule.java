package de.lothomax.geoworld.module.river;

import de.lothomax.geoworld.config.GeoWorldConfig;
import org.bukkit.Chunk;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.LinkedBlockingQueue;

public class RiverFlowModule {

    private final Plugin plugin;
    private final GeoWorldConfig config;
    private final LinkedBlockingQueue<Chunk> pendingChunks;
    private RiverFlowTask task;

    public RiverFlowModule(Plugin plugin, GeoWorldConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.pendingChunks = new LinkedBlockingQueue<>();
    }

    public void start() {
        if (task == null) {
            task = new RiverFlowTask(this.plugin, this.config, this.pendingChunks);
            // Start as repeating async task, e.g., every 10 ticks
            task.runTaskTimerAsynchronously(plugin, 10L, 10L);
        }
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        pendingChunks.clear();
    }

    public void queueChunk(Chunk chunk) {
        pendingChunks.offer(chunk);
    }
}
