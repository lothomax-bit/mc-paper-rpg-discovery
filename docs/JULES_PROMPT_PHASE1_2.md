# Jules-Prompt: Phase 1 & 2 – Projektgerüst + Datenstruktur

Dieser Prompt ist für Google Jules optimiert. Einfach 1:1 einfügen.

---

```
Erstelle ein vollständiges Minecraft Paper-Plugin-Projekt (Java 17+, Paper API 1.21+)
mit dem Namen "GeoDiscovery". Verwende Maven als Build-System.

=== PHASE 1: Projektgerüst ===

1. Erstelle eine vollständige pom.xml mit:
   - groupId: de.lothomax
   - artifactId: geodiscovery
   - version: 1.0.0-SNAPSHOT
   - Paper-API Dependency: io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT
   - SQLite JDBC: org.xerial:sqlite-jdbc:3.45.3.0
   - HikariCP: com.zaxxer:HikariCP:5.1.0
   - Java 17 Compiler-Target
   - Maven Shade Plugin zum Einbetten von HikariCP und SQLite in die finale JAR

2. Erstelle die Plugin-Hauptklasse: de.lothomax.geodiscovery.GeoDiscovery
   - Extends JavaPlugin
   - onEnable(): Initialisiert DatabaseManager, RegionCache, ConfigManager,
     registriert PlayerMoveListener, StructureCheckTask, TaufeCommand, GeoDiscoveryCommand
   - onDisable(): Schließt HikariCP DataSource sauber, bricht alle pending NamingSessions ab

3. Erstelle DatabaseManager (de.lothomax.geodiscovery.database.DatabaseManager):
   - HikariCP-Pool mit SQLite (Datei: plugins/GeoDiscovery/locations.db)
   - Alle DB-Operationen MÜSSEN asynchron laufen (CompletableFuture)
   - Methoden:
     * initialize() – erstellt Tabellen falls nicht vorhanden
     * saveRegion(DiscoveredRegion region) → CompletableFuture<Void>
     * getAllRegions(String worldUuid) → CompletableFuture<List<DiscoveredRegion>>

4. Erstelle RegionCache (de.lothomax.geodiscovery.database.RegionCache):
   - Hält alle bekannten Regionen im RAM (Map<String worldUuid, List<DiscoveredRegion>>)
   - loadFromDatabase(DatabaseManager db) – befüllt Cache beim Plugin-Start async
   - addRegion(DiscoveredRegion region) – fügt neue Entdeckung sofort hinzu
   - findNearestRegion(String worldUuid, double x, double z) → Optional<DiscoveredRegion>
     Berechnet 2D-Distanz (nur X/Z, kein Y) zu allen Regionen der Welt

=== PHASE 2: Datenstruktur ===

5. Erstelle Datenmodell: de.lothomax.geodiscovery.model.DiscoveredRegion
   Felder:
   - int id
   - String regionName
   - UUID discovererUuid
   - String discovererName
   - UUID worldUuid
   - double centerX
   - double centerZ
   - int radius
   - String regionType  // "BIOME" oder "STRUCTURE"
   - String biomeKey    // z.B. "DESERT", "VILLAGE", "ANCIENT_CITY"
   - LocalDateTime timestamp
   Vollständige Getter/Setter, Builder-Pattern, toString()

6. SQLite-Tabelle discovered_regions mit GENAU diesen Spalten:
   - id INTEGER PRIMARY KEY AUTOINCREMENT
   - region_name VARCHAR NOT NULL
   - discoverer_uuid VARCHAR NOT NULL
   - discoverer_name VARCHAR NOT NULL
   - world_uuid VARCHAR NOT NULL
   - center_x DOUBLE NOT NULL
   - center_z DOUBLE NOT NULL
   - radius INTEGER NOT NULL DEFAULT 150
   - region_type VARCHAR NOT NULL DEFAULT 'BIOME'
   - biome_key VARCHAR
   - timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP

7. Erstelle plugin.yml:
   name: GeoDiscovery
   main: de.lothomax.geodiscovery.GeoDiscovery
   version: 1.0.0
   api-version: '1.21'
   author: lothomax
   commands:
     taufe:
       description: Benennt eine entdeckte Region
       usage: /taufe <Name>
     geodiscovery:
       description: Admin-Befehl
       usage: /geodiscovery reload
   permissions:
     geodiscovery.player.taufe:
       default: true
     geodiscovery.admin.reload:
       default: op

8. Erstelle config.yml:

enabled-biomes:
  - JAGGED_PEAKS
  - FROZEN_PEAKS
  - STONY_PEAKS
  - WINDSWEPT_HILLS
  - WINDSWEPT_GRAVELLY_HILLS
  - DESERT
  - SAVANNA
  - SAVANNA_PLATEAU
  - WINDSWEPT_SAVANNA
  - BADLANDS
  - ERODED_BADLANDS
  - JUNGLE
  - BAMBOO_JUNGLE
  - SPARSE_JUNGLE
  - DARK_FOREST
  - OLD_GROWTH_PINE_TAIGA
  - OLD_GROWTH_SPRUCE_TAIGA
  - ICE_SPIKES
  - SNOWY_SLOPES
  - FROZEN_RIVER
  - SNOWY_BEACH
  - SWAMP
  - MANGROVE_SWAMP
  - OCEAN
  - DEEP_OCEAN
  - WARM_OCEAN
  - LUKEWARM_OCEAN
  - DEEP_LUKEWARM_OCEAN
  - COLD_OCEAN
  - FROZEN_OCEAN
  - DEEP_DARK
  - LUSH_CAVES
  - DRIPSTONE_CAVES
  - BASALT_DELTAS
  - CRIMSON_FOREST
  - WARPED_FOREST
  - SOUL_SAND_VALLEY
  - NETHER_WASTES
  - END_HIGHLANDS
  - END_MIDLANDS
  - SMALL_END_ISLANDS
  - END_BARRENS

enabled-structures:
  - VILLAGE
  - STRONGHOLD
  - DESERT_PYRAMID
  - JUNGLE_TEMPLE
  - SWAMP_HUT
  - IGLOO
  - PILLAGER_OUTPOST
  - WOODLAND_MANSION
  - OCEAN_MONUMENT
  - OCEAN_RUIN
  - SHIPWRECK
  - BURIED_TREASURE
  - NETHER_FORTRESS
  - BASTION_REMNANT
  - END_CITY
  - ANCIENT_CITY
  - TRAIL_RUINS
  - RUINED_PORTAL

region-radius: 150
naming-timeout: 60
structure-check-interval: 100

region-icons:
  DEFAULT: "📍"
  DESERT: "🏜️"
  BADLANDS: "🏜️"
  ERODED_BADLANDS: "🏜️"
  OCEAN: "🌊"
  DEEP_OCEAN: "🌊"
  WARM_OCEAN: "🌊"
  LUKEWARM_OCEAN: "🌊"
  DEEP_LUKEWARM_OCEAN: "🌊"
  COLD_OCEAN: "🌊"
  FROZEN_OCEAN: "❄️"
  JAGGED_PEAKS: "⛰️"
  FROZEN_PEAKS: "🏔️"
  STONY_PEAKS: "⛰️"
  WINDSWEPT_HILLS: "⛰️"
  ICE_SPIKES: "❄️"
  SNOWY_SLOPES: "❄️"
  JUNGLE: "🌴"
  BAMBOO_JUNGLE: "🌴"
  SPARSE_JUNGLE: "🌴"
  DARK_FOREST: "🌲"
  OLD_GROWTH_PINE_TAIGA: "🌲"
  OLD_GROWTH_SPRUCE_TAIGA: "🌲"
  SWAMP: "🌿"
  MANGROVE_SWAMP: "🌿"
  DEEP_DARK: "💀"
  LUSH_CAVES: "🍄"
  DRIPSTONE_CAVES: "🪨"
  BASALT_DELTAS: "🔥"
  CRIMSON_FOREST: "🔥"
  WARPED_FOREST: "🔥"
  SOUL_SAND_VALLEY: "🔥"
  NETHER_WASTES: "🔥"
  END_HIGHLANDS: "🌟"
  END_MIDLANDS: "🌟"
  SMALL_END_ISLANDS: "🌟"
  END_BARRENS: "🌟"
  VILLAGE: "🏘️"
  STRONGHOLD: "🏰"
  DESERT_PYRAMID: "🏜️"
  JUNGLE_TEMPLE: "🌴"
  SWAMP_HUT: "🌿"
  IGLOO: "❄️"
  PILLAGER_OUTPOST: "⚔️"
  WOODLAND_MANSION: "🏚️"
  OCEAN_MONUMENT: "🌊"
  OCEAN_RUIN: "🌊"
  SHIPWRECK: "⚓"
  BURIED_TREASURE: "💎"
  NETHER_FORTRESS: "🔥"
  BASTION_REMNANT: "🔥"
  END_CITY: "🌟"
  ANCIENT_CITY: "💀"
  TRAIL_RUINS: "🏛️"
  RUINED_PORTAL: "🌀"

messages:
  actionbar-format: "%icon% §f%name% §7(Entdeckt von §e%player%§7)"
  discovery-prompt: "§6§lNeuland entdeckt! §7Nutze §b/taufe [Name]§7 um diesen Ort zu benennen. §7(§e%seconds%s§7)"
  naming-success: "§a§l✔ Region benannt! §f\"%name%\" §7wurde gespeichert."
  naming-timeout-msg: "§c§lZeit abgelaufen! §7Die Region wurde nicht benannt."
  naming-too-short: "§cDer Name muss mindestens 3 Zeichen lang sein."

Gib mir die vollständige Projektstruktur mit allen Dateien, fertig zum Kompilieren mit mvn package.
```
