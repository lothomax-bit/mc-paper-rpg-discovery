package de.lothomax.geoworld.listener;

import de.lothomax.geoworld.config.GeoWorldConfig;
import de.lothomax.geoworld.module.river.RiverFlowModule;
import org.bukkit.ChunkSnapshot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkPopulateListener implements Listener {

    private final RiverFlowModule riverFlowModule;
    private final GeoWorldConfig config;

    public ChunkPopulateListener(RiverFlowModule riverFlowModule, GeoWorldConfig config) {
        this.riverFlowModule = riverFlowModule;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            ChunkSnapshot snapshot = event.getChunk().getChunkSnapshot(true, true, false);
            int highest = 0;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    highest = Math.max(highest, snapshot.getHighestBlockYAt(x, z));
                }
            }
            if (highest >= config.getMinSpringY()) {
                riverFlowModule.queueChunk(event.getChunk());
            }
        }
    }
}
