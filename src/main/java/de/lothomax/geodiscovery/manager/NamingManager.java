package de.lothomax.geodiscovery.manager;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NamingManager {

    private final GeoDiscovery plugin;
    private final Map<UUID, NamingSession> activeSessions = new ConcurrentHashMap<>();

    public NamingManager(GeoDiscovery plugin) {
        this.plugin = plugin;
    }

    public void startSession(Player player, DiscoveredRegion.Builder regionBuilder) {
        UUID uuid = player.getUniqueId();
        if (activeSessions.containsKey(uuid)) {
            cancelSession(uuid); // Replace old session
        }

        int timeoutSeconds = plugin.getConfigManager().getNamingTimeout();

        // Notify player
        String prompt = plugin.getConfigManager().getMessage("discovery-prompt");
        prompt = prompt.replace("%seconds%", String.valueOf(timeoutSeconds));
        player.sendMessage(prompt);

        BukkitTask task = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (activeSessions.containsKey(uuid)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    p.sendMessage(plugin.getConfigManager().getMessage("naming-timeout-msg"));
                }
                activeSessions.remove(uuid);
            }
        }, timeoutSeconds * 20L); // 20 ticks per second

        activeSessions.put(uuid, new NamingSession(regionBuilder, task));
    }

    public boolean completeSession(Player player, String name) {
        UUID uuid = player.getUniqueId();
        NamingSession session = activeSessions.remove(uuid);

        if (session == null) {
            return false;
        }

        session.getTimeoutTask().cancel();

        DiscoveredRegion region = session.getRegionBuilder().regionName(name).build();

        plugin.getDatabaseManager().saveRegion(region).thenRun(() -> {
            plugin.getRegionCache().addRegion(region);

            String successMsg = plugin.getConfigManager().getMessage("naming-success");
            successMsg = successMsg.replace("%name%", name);
            player.sendMessage(successMsg);
        });

        return true;
    }

    public void cancelSession(UUID uuid) {
        NamingSession session = activeSessions.remove(uuid);
        if (session != null) {
            session.getTimeoutTask().cancel();
        }
    }

    public void cancelAllSessions() {
        for (NamingSession session : activeSessions.values()) {
            session.getTimeoutTask().cancel();
        }
        activeSessions.clear();
    }

    public boolean hasActiveSession(UUID uuid) {
        return activeSessions.containsKey(uuid);
    }

    private static class NamingSession {
        private final DiscoveredRegion.Builder regionBuilder;
        private final BukkitTask timeoutTask;

        public NamingSession(DiscoveredRegion.Builder regionBuilder, BukkitTask timeoutTask) {
            this.regionBuilder = regionBuilder;
            this.timeoutTask = timeoutTask;
        }

        public DiscoveredRegion.Builder getRegionBuilder() {
            return regionBuilder;
        }

        public BukkitTask getTimeoutTask() {
            return timeoutTask;
        }
    }
}
