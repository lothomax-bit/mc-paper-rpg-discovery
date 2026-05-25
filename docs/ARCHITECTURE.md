# GeoDiscovery – Architektur & Planungsdokument

## Überblick

GeoDiscovery ist ein Minecraft Paper-Plugin (1.21+), das Spielern ermöglicht, geografische Strukturen und Biome dynamisch zu entdecken und zu benennen. Das Plugin erkennt **sowohl natürliche Biome als auch generierte Strukturen** (Hybrid-Ansatz).

---

## Erkennungslogik

Es gibt zwei unabhängige Erkennungs-Trigger:

```
PlayerMoveEvent
    ├─► Biom-Check (Whitelist-Biome)
    │       └─► Distanzprüfung → unentdeckt? → /taufe
    │
    └─► Struktur-Check (BukkitScheduler, alle 5s)
            └─► locateNearestStructure() async → in Radius? → unentdeckt? → /taufe
```

**Wichtig:** Der Struktur-Check läuft NICHT bei jedem Move-Event, sondern als eigener `BukkitRunnable`-Task alle 100 Ticks (5 Sekunden) pro online-Spieler, um Performance zu schonen.

### Biom-Check Logik
1. Prüfe aktuelles Biom gegen `enabled-biomes` Whitelist aus `config.yml`
2. Berechne 2D-Distanz (X/Z) zu allen bekannten Regionen im `RegionCache`
3. Ist der Spieler weiter als `region-radius` von allen bekannten Regionen entfernt → **unentdeckt**
4. Spieler erhält Discovery-Prompt und `/taufe`-Session startet

### Struktur-Check Logik
1. Rufe `world.locateNearestStructure()` asynchron auf für alle `enabled-structures`
2. Ist die nächste Struktur innerhalb von `region-radius` Blöcken des Spielers?
3. Existiert diese Struktur noch nicht als benannte Region in der DB?
4. → **unentdeckt**, Discovery-Prompt

---

## Datenmodell

### Tabelle: `discovered_regions`

| Spalte | Typ | Beschreibung |
|---|---|---|
| `id` | INTEGER PK AUTOINCREMENT | Interne ID |
| `region_name` | VARCHAR NOT NULL | Vom Spieler gewählter Name |
| `discoverer_uuid` | VARCHAR NOT NULL | UUID des Entdeckers |
| `discoverer_name` | VARCHAR NOT NULL | Letzter bekannter Spielername |
| `world_uuid` | VARCHAR NOT NULL | Welt-UUID (Overworld/Nether/End) |
| `center_x` | DOUBLE NOT NULL | X-Koordinate des Entdeckungspunkts |
| `center_z` | DOUBLE NOT NULL | Z-Koordinate des Entdeckungspunkts |
| `radius` | INTEGER DEFAULT 150 | Erkennungsradius in Blöcken |
| `region_type` | VARCHAR DEFAULT 'BIOME' | `BIOME` oder `STRUCTURE` |
| `biome_key` | VARCHAR | z.B. `DESERT`, `VILLAGE`, `ANCIENT_CITY` |
| `timestamp` | DATETIME DEFAULT CURRENT_TIMESTAMP | Zeitpunkt der Entdeckung |

---

## Paketstruktur

```
de.lothomax.geodiscovery
├── GeoDiscovery.java              // Plugin-Hauptklasse
├── database
│   ├── DatabaseManager.java       // HikariCP + SQLite, alle async
│   └── RegionCache.java           // In-Memory Cache aller Regionen
├── model
│   └── DiscoveredRegion.java      // Datenmodell
├── listener
│   └── PlayerMoveListener.java    // Biom-Erkennung
├── task
│   └── StructureCheckTask.java    // Struktur-Check alle 5s
├── command
│   ├── TaufeCommand.java          // /taufe [Name]
│   └── GeoDiscoveryCommand.java   // /geodiscovery reload
├── session
│   └── NamingSessionManager.java  // Pending /taufe Sessions
└── util
    └── ActionBarUtil.java         // Actionbar-Anzeige
```

---

## Actionbar-Anzeige

Beim Betreten einer bekannten Region wird die Actionbar dezent beschriftet:

```
🏜️ Die Große Wüste  (Entdeckt von Steve)
🌊 Tiefer Ozean     (Entdeckt von Alex)
🏘️ Dorf am Fluss    (Entdeckt von Notch)
💀 Uralte Stadt     (Entdeckt von Herobrine)
⛰️ Die Nebelberge   (Entdeckt von Gomme)
```

Das Icon wird aus der `region-icons`-Map in der `config.yml` anhand des `biome_key` geladen. Fallback: `📍`

---

## Befehle & Berechtigungen

| Befehl | Beschreibung | Permission |
|---|---|---|
| `/taufe [Name]` | Benennt eine frisch entdeckte Region | `geodiscovery.player.taufe` |
| `/geodiscovery reload` | Lädt config.yml neu | `geodiscovery.admin.reload` |

---

## Performance-Überlegungen

- **Alle DB-Zugriffe asynchron** via `CompletableFuture` + HikariCP Connection Pool
- **RegionCache im RAM** – kein DB-Query bei jedem Move-Event
- **PlayerMoveEvent-Optimierung** – nur auslösen bei Block-Koordinatenwechsel (nicht bei Mikroschritten)
- **Struktur-Check** – nur alle 5 Sekunden, nicht per Move-Event
- **Cache-Update** – nach jeder neuen Entdeckung sofort, kein Neustart nötig
