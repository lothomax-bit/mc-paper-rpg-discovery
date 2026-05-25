package de.lothomax.geodiscovery.session;

import de.lothomax.geodiscovery.GeoDiscovery;
import de.lothomax.geodiscovery.model.DiscoveredRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class NamingSessionManager implements Listener {

    private final GeoDiscovery plugin;
    private final Map<UUID, NamingSession> activeSessions = new ConcurrentHashMap<>();

    public NamingSessionManager(GeoDiscovery plugin) {
        this.plugin = plugin;
    }

    public void startSession(Player player, Location loc, String biomeKey, String regionType) {
        UUID uuid = player.getUniqueId();

        if (activeSessions.containsKey(uuid)) {
            cancelSession(uuid);
        }

        int timeoutSeconds = plugin.getConfigManager().getNamingTimeout();

        String prompt = plugin.getConfigManager().getMessage("discovery-prompt");
        if (prompt != null) {
            prompt = prompt.replace("%seconds%", String.valueOf(timeoutSeconds));
            player.sendMessage(prompt);
        }

        long expiresAt = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        BukkitTask timeoutTask = Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (activeSessions.containsKey(uuid)) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null && p.isOnline()) {
                    String timeoutMsg = plugin.getConfigManager().getMessage("naming-timeout-msg");
                    if (timeoutMsg != null) {
                        p.sendMessage(timeoutMsg);
                    }
                }
                activeSessions.remove(uuid);
            }
        }, timeoutSeconds * 20L); // 20 ticks per second

        NamingSession session = new NamingSession(uuid, loc, biomeKey, regionType, expiresAt, timeoutTask);
        activeSessions.put(uuid, session);
    }

    public boolean hasSession(UUID playerUuid) {
        return activeSessions.containsKey(playerUuid);
    }

    public Optional<NamingSession> getSession(UUID playerUuid) {
        return Optional.ofNullable(activeSessions.get(playerUuid));
    }

    public void completeSession(UUID playerUuid, String chosenName) {
        NamingSession session = activeSessions.remove(playerUuid);
        if (session == null) {
            return;
        }

        session.timeoutTask().cancel();

        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) {
            return; // Player went offline right when typing
        }

        String worldUuidStr = session.discoveryLocation().getWorld().getUID().toString();

        // Edge Case 4: check if it's already discovered concurrently
        Optional<DiscoveredRegion> existingRegion = plugin.getRegionCache().findNearestRegion(worldUuidStr, session.discoveryLocation().getX(), session.discoveryLocation().getZ());
        if (existingRegion.isPresent()) {
            DiscoveredRegion r = existingRegion.get();
            double dx = r.getCenterX() - session.discoveryLocation().getX();
            double dz = r.getCenterZ() - session.discoveryLocation().getZ();
            if (dx * dx + dz * dz <= r.getRadius() * r.getRadius()) {
                String errorMsg = plugin.getConfigManager().getMessage("region-already-discovered");
                if (errorMsg != null) {
                    errorMsg = errorMsg.replace("%player%", r.getDiscovererName())
                                       .replace("%name%", r.getRegionName());
                    player.sendMessage(errorMsg);
                } else {
                     player.sendMessage("Diese Region wurde bereits von " + r.getDiscovererName() + " als '" + r.getRegionName() + "' benannt.");
                }
                return;
            }
        }

        DiscoveredRegion region = new DiscoveredRegion.Builder()
                .regionName(chosenName)
                .discovererUuid(playerUuid)
                .discovererName(player.getName())
                .worldUuid(session.discoveryLocation().getWorld().getUID())
                .centerX(session.discoveryLocation().getX())
                .centerZ(session.discoveryLocation().getZ())
                .radius(plugin.getConfigManager().getRegionRadius())
                .regionType(session.regionType())
                .biomeKey(session.biomeKey())
                .build();

        plugin.getDatabaseManager().saveRegion(region).thenRun(() -> {
            plugin.getRegionCache().addRegion(region);

            String successMsg = plugin.getConfigManager().getMessage("naming-success");
            if (successMsg != null) {
                successMsg = successMsg.replace("%name%", chosenName);
                player.sendMessage(successMsg);
            }
        });
    }

    public void cancelSession(UUID playerUuid) {
        NamingSession session = activeSessions.remove(playerUuid);
        if (session != null) {
            session.timeoutTask().cancel();
        }
    }

    public void cancelAllSessions() {
        for (NamingSession session : activeSessions.values()) {
            session.timeoutTask().cancel();
        }
        activeSessions.clear();
    }

    // Edge Case Listeners
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cancelSession(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        cancelSession(event.getEntity().getUniqueId());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        cancelSession(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getDatabaseManager().updateDiscovererName(player.getUniqueId(), player.getName());
    }

    public record NamingSession(
            UUID playerUuid,
            Location discoveryLocation,
            String biomeKey,
            String regionType,
            long expiresAt,
            BukkitTask timeoutTask
    ) {}
}