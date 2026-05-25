# Jules-Prompt: GeoDiscovery Phase 3 & 4 – Biom-Listener + Struktur-Task

Dieser Prompt baut auf Phase 1 & 2 auf. Das Projektgerüst, DatabaseManager,
RegionCache und DiscoveredRegion müssen bereits existieren.

---

```
Ergänze das bestehende GeoDiscovery Paper-Plugin um Phase 3 und Phase 4.
Das Projektgerüst (pom.xml, GeoDiscovery.java, DatabaseManager, RegionCache,
DiscoveredRegion, plugin.yml, config.yml) existiert bereits aus Phase 1+2.

=== PHASE 3: BIOM-ERKENNUNG ===

Erstelle: de.lothomax.geodiscovery.listener.PlayerMoveListener

Implements Listener, hört auf PlayerMoveEvent.

** Optimierung: Nur bei Block-Koordinatenwechsel auslösen **
```java
// Nur triggern wenn sich die Block-Koordinaten geändert haben
if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
    event.getFrom().getBlockY() == event.getTo().getBlockY() &&
    event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
    return;
}
```

** Ablauf bei jedem Block-Schritt: **

1. Hole aktuelles Biom: `player.getLocation().getBlock().getBiome()`
2. Hole Biom-Key als String: `biome.getKey().toString()`
   Beispiel: "minecraft:desert" oder "geoworld:crystal_peaks"
3. Prüfe ob Biom-Key (ohne Namespace) in `enabled-biomes` aus config.yml steht
   ODER ob der volle Key (mit Namespace) in enabled-biomes steht
   (für Custom-Biome wie "geoworld:abyssal_trench")
4. Wenn nicht in Whitelist: return
5. Prüfe NamingSessionManager: Hat Spieler bereits eine aktive Session? return
6. Hole world UUID: `player.getWorld().getUID().toString()`
7. Rufe RegionCache.findNearestRegion(worldUuid, x, z) auf
8. Wenn Ergebnis present AND Distanz <= region.getRadius():
   - Spieler ist in bekannter Region
   - Zeige Actionbar via ActionBarUtil (nur alle 40 Ticks anzeigen, nicht jeden Schritt)
   - return
9. Wenn kein Ergebnis ODER Distanz > radius aller bekannten Regionen:
   - Spieler hat unbekanntes Gebiet betreten
   - Rufe NamingSessionManager.startSession(player, location, biomeKey, "BIOME") auf

** Actionbar-Cooldown pro Spieler: **
Verwende eine HashMap<UUID, Long> lastActionbarDisplay.
Nur anzeigen wenn System.currentTimeMillis() - lastDisplay > 2000 (2 Sekunden).

---

Erstelle: de.lothomax.geodiscovery.session.NamingSessionManager

Verwaltet alle aktiven Entdeckungs-Sessions (Spieler hat Neuland betreten,
wartet auf /taufe-Eingabe).

NamingSession ist ein inneres Record/Klasse mit:
- UUID playerUuid
- Location discoveryLocation
- String biomeKey
- String regionType  // "BIOME" oder "STRUCTURE"
- long expiresAt     // System.currentTimeMillis() + (naming-timeout * 1000)
- BukkitTask timeoutTask  // Referenz für Canceln

Methoden:

`startSession(Player player, Location loc, String biomeKey, String regionType)`:
1. Falls bereits Session aktiv: Alte zuerst beenden (cancelSession)
2. Erstelle neue NamingSession
3. Sende discovery-prompt Nachricht an Spieler
   (Platzhalter: %seconds% = naming-timeout aus config)
4. Starte BukkitRunnable als delayed task nach `naming-timeout` Sekunden:
   - Sende naming-timeout-msg an Spieler (falls noch online)
   - Entferne Session
5. Speichere in HashMap<UUID, NamingSession>

`hasSession(UUID playerUuid)` → boolean

`getSession(UUID playerUuid)` → Optional<NamingSession>

`completeSession(UUID playerUuid, String chosenName)` → void:
1. Hole Session, return wenn nicht vorhanden
2. Cancele Timeout-Task
3. Erstelle DiscoveredRegion mit allen Daten
4. Rufe DatabaseManager.saveRegion() async auf
5. Rufe RegionCache.addRegion() auf
6. Sende naming-success Nachricht
7. Entferne Session

`cancelSession(UUID playerUuid)` → void:
- Cancele Timeout-Task, entferne Session, keine Nachricht

Außerdem: SessionManager muss auf PlayerQuitEvent hören → cancelSession beim Logout.

---

Erstelle: de.lothomax.geodiscovery.command.TaufeCommand

Implements CommandExecutor für /taufe

1. Prüfe: Ist Sender ein Player? Sonst: Fehlermeldung
2. Prüfe Permission: geodiscovery.player.taufe
3. Prüfe: Hat Spieler aktive NamingSession?
   Wenn nicht: Sende "Du hast gerade keine Entdeckung zu benennen."
4. Prüfe: args.length >= 1
   Wenn nicht: Sende Usage-Hinweis
5. Zusammensetze Namen: String.join(" ", args) (mehrere Wörter erlaubt)
6. Prüfe Mindestlänge: name.length() >= 3
   Wenn nicht: Sende naming-too-short Nachricht
7. Prüfe Maxlänge: name.length() <= 48
   Wenn nicht: Sende Fehlermeldung
8. Rufe NamingSessionManager.completeSession(player.getUniqueId(), name) auf

---

Erstelle: de.lothomax.geodiscovery.util.ActionBarUtil

Methode: static void send(Player player, String message)
- Nutzt Paper API: player.sendActionBar(Component.text(message))
- Parst Legacy-Farbcodes (&a, §a etc.) via LegacyComponentSerializer

Methode: static String buildActionBarText(DiscoveredRegion region, ConfigManager config)
- Holt Icon aus config region-icons Map anhand region.getBiomeKey()
- Fallback-Icon: 📍
- Ersetzt Platzhalter in actionbar-format:
  %icon% → icon
  %name% → region.getRegionName()
  %player% → region.getDiscovererName()

=== PHASE 4: STRUKTUR-ERKENNUNG ===

Erstelle: de.lothomax.geodiscovery.task.StructureCheckTask

Extends BukkitRunnable.
Wird in GeoDiscovery.onEnable() als repeating task gestartet:
`task.runTaskTimer(plugin, 0L, config.getStructureCheckInterval())`
(Default: alle 100 Ticks = 5 Sekunden)

Läuft auf MAIN-THREAD (locateNearestStructure benötigt Sync-Kontext).

Ablauf pro Tick:

1. Iteriere alle online Spieler: `Bukkit.getOnlinePlayers()`
2. Für jeden Spieler:
   a. Hat Spieler aktive NamingSession? → überspringen
   b. Hole `enabled-structures` Liste aus Config
   c. Für jeden Structure-Key in der Liste:
      - Konvertiere String zu StructureType:
        `Registry.STRUCTURE_TYPE.get(NamespacedKey.minecraft(key.toLowerCase()))`
      - Falls StructureType null: überspringen (ungültiger Key in Config)
      - Rufe async aus:
        ```java
        Location structLoc = player.getWorld()
            .locateNearestStructure(
                player.getLocation(),
                structureType,
                config.getRegionRadius() * 2,  // Suchradius = doppelter Region-Radius
                false  // findUnexplored = false, auch bekannte Strukturen prüfen
            );
        ```
      - Wenn structLoc == null: weiter
      - Berechne 2D-Distanz zwischen Spieler und structLoc (nur X/Z)
      - Wenn Distanz > config.getRegionRadius(): weiter
      - Prüfe RegionCache: Gibt es bereits eine Region nahe structLoc?
        `regionCache.findNearestRegion(worldUuid, structLoc.getX(), structLoc.getZ())`
      - Wenn Region vorhanden UND Distanz <= region.getRadius(): weiter (bereits bekannt)
      - Wenn unbekannt: NamingSessionManager.startSession(player, structLoc, key, "STRUCTURE")
      - break (nur erste unbekannte Struktur pro Tick pro Spieler)

** WICHTIG: locateNearestStructure ist teuer! **
Verwende pro Spieler eine Cooldown-Map:
`HashMap<UUID, Long> lastStructureCheck`
Nur prüfen wenn letzte Prüfung > 5000ms zurückliegt (zusätzlich zum Task-Interval).

---

Erstelle: de.lothomax.geodiscovery.command.GeoDiscoveryCommand

Implements CommandExecutor für /geodiscovery

Subcommands:
- `/geodiscovery reload`:
  1. Prüfe Permission: geodiscovery.admin.reload
  2. Rufe ConfigManager.reload() auf
  3. Sende Bestätigung: "§aGeoDiscovery Konfiguration neu geladen."
- `/geodiscovery info`:
  1. Zeige Plugin-Version, Anzahl geladener Regionen aus RegionCache
  2. Zeige ob DB-Verbindung aktiv
- Kein gültiger Subcommand: Zeige Usage

---

=== GeoDiscovery.java AKTUALISIERUNG ===

Ergänze onEnable() um:
```java
// Session Manager
NamingSessionManager sessionManager = new NamingSessionManager(this);

// Listener
getServer().getPluginManager().registerEvents(
    new PlayerMoveListener(regionCache, sessionManager, configManager), this);
getServer().getPluginManager().registerEvents(sessionManager, this);

// Commands
getCommand("taufe").setExecutor(new TaufeCommand(sessionManager, configManager));
getCommand("geodiscovery").setExecutor(new GeoDiscoveryCommand(this, regionCache, configManager));

// Struktur-Task
StructureCheckTask structureTask = new StructureCheckTask(regionCache, sessionManager, configManager);
structureTask.runTaskTimer(this, 20L, configManager.getStructureCheckInterval());
```

onDisable() ergänzen:
```java
// Alle aktiven Sessions canceln
sessionManager.cancelAllSessions();
// Struktur-Task stoppen
if (structureTask != null) structureTask.cancel();
```

=== EDGE CASES die behandelt werden müssen ===

1. Spieler loggt aus während Session aktiv → Session canceln (PlayerQuitEvent)
2. Spieler stirbt während Session aktiv → Session canceln (PlayerDeathEvent)
3. Spieler wechselt Welt während Session → Session canceln (PlayerChangedWorldEvent)
4. Zwei Spieler entdecken gleichzeitig dieselbe Region:
   Wer zuerst /taufe eingibt gewinnt. Zweitem Spieler beim completeSession prüfen
   ob Region inzwischen im Cache ist → Nachricht "Diese Region wurde bereits von
   %player% als '%name%' benannt."
5. Spielername ändert sich: discoverer_name immer beim Login aktualisieren
   (PlayerJoinEvent → async DB-Update falls Name geändert)
6. Config-Reload während aktiver Sessions: Sessions weiterlaufen lassen,
   neue Config nur für neue Sessions verwenden

Gib alle neuen und aktualisierten Dateien vollständig aus.
```
