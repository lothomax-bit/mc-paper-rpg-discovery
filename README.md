# mc-paper-rpg-discovery

Ein modulares RPG-Erkundungsprojekt für **Paper 26.1+**. Das Projekt besteht aus drei unabhängigen Komponenten, die zusammenspielen: einem Datapack für Custom-Biome und Weltgenerierung, einem Java-Plugin für dynamische Flussgenerierung, und MythicMobs-Konfigurationen für biom-spezifische Kreaturen.

---

## 📦 Projektstruktur

```
mc-paper-rpg-discovery/
├── geoworld/                  # Java Plugin (Maven)
│   └── src/main/java/de/lothomax/geoworld/
│       ├── config/            # GeoWorldConfig, RiverProfile
│       └── module/river/      # RiverFlowTask, RiverPathfinder, WaterfallPlacer, ...
├── geoworld-datapack/         # Vanilla Datapack (Custom Biomes + Worldgen)
├── plugins/                   # MythicMobs YML-Konfigurationen
│   └── MythicMobs/Mobs/
├── docs/                      # Architektur-Dokumente & Jules-Prompts
├── generate_mobs.py           # Python-Hilfsskript für MythicMobs-YMLs
└── pom.xml                    # Maven Root-POM
```

---

## 🌍 Komponente 1 – GeoWorld Datapack

Ein Vanilla-Datapack, das die Weltgenerierung des Overworld-Bioms erweitert. Es registriert **50 Custom-Biome** (Namespace `geoworld:`) und integriert diese in die Vanilla-Biom-Verteilung mittels angepasster Noise-Parameter.

**Custom-Biome-Kategorien:**

| Kategorie | Anzahl | Beispiele |
|---|---|---|
| ⛰️ Gebirge & Höhenlagen | 4 | `crystal_peaks`, `skyreach_plateau`, `obsidian_spires`, `floating_isles` |
| 🌊 Ozeane & Küste | 8 | `pearl_lagoon`, `abyssal_trench`, `coral_archipelago`, `sunken_city`, ... |
| 🌲 Wälder & Ebenen | 9 | `ancient_forest`, `whispering_pines`, `haunted_grove`, `golden_woodland`, ... |
| 🏜️ Wüste & Trocken | 3 | `salt_flats`, `red_canyon`, `dune_sea` |
| ❄️ Eis & Kälte | 3 | `glacial_abyss`, `aurora_tundra`, `rime_coast` |
| 🪵 Sümpfe & Feuchtgebiete | 3 | `cursed_bayou`, `mangrove_labyrinth`, `peat_moors` |
| 💥 Vulkan & Feuer | 4 | `lava_falls`, `ember_plains`, `volcanic_ashfields`, `volcanic_island` |
| 🪫 Höhlen & Untergrund | 4 | `crystal_caverns`, `magma_depths`, `echo_chambers`, `fungal_depths` |
| 🔥 Nether (Erweitert) | 3 | `nether_garden`, `soul_archive`, `magma_delta_rim` |
| ⭐ The End (Erweitert) | 3 | `end_coral_reef`, `void_sanctum`, `elytra_spires` |
| ✨ Mystisch & Märchenhaft | 10 | `starfall_meadow`, `dream_forest`, `rune_highlands`, `eclipse_valley`, ... |
| 🌎 Sonstige | 2 | `petrified_wasteland`, `verdant_chasms` |

**Deployment:** `geoworld-datapack/` → `world/datapacks/geoworld-datapack/`

Dokumentation: [`docs/CUSTOM_BIOMES.md`](docs/CUSTOM_BIOMES.md) · [`docs/WORLDGEN_ARCHITECTURE.md`](docs/WORLDGEN_ARCHITECTURE.md)

---

## 🔌 Komponente 2 – GeoWorld Plugin

Ein Paper-Plugin (Java 21, Maven), das nach dem Chunkload dynamisch Gebirgsflüsse, Wasserfälle und Bergseen in passenden Biomen generiert.

### Features
- **Automatische Quellenerkennung** – findet natürliche Wasserblöcke in Hochgebirgsbiomen (Vanilla + Custom) beim Chunk-Load
- **Pfadfindung bergab** – `RiverPathfinder` verfolgt den Weg des geringsten Widerstands mit `HeightMap.WORLD_SURFACE`
- **Wasserfälle** – bei Höhenunterschied > Schwellwert werden kaskadierende Wasserfälle gecarvet
- **Bergseen** – konfigurierbare Wahrscheinlichkeit für Seen am Fuß von Wasserfällen
- **Biom-angepasste Ufer** – Flussufer nutzen biom-passende Materialien via `RiverBiomeHelper`
- **GeoDiscovery-Hook** – optional: Seen werden in eine `pending_lakes.csv` für das GeoDiscovery-Plugin geloggt

### Konfiguration (`plugins/GeoWorld/geoworld_config.yml`)

```yaml
modules:
  river-flow:
    enabled: true
    min-spring-y: 200          # Minimale Y-Höhe für Quellen
    sea-level: 63
    waterfall-threshold: 4     # Delta-Y ab dem ein Wasserfall entsteht
    max-river-length: 800      # Maximale Pfadlänge in Nodes
    mountain-lake-chance: 0.35
    spring-chance: 0.08
    spring-max-per-chunk: 1
    chunks-per-tick: 3
    geodiscovery-hook: true
```

### Build

**Voraussetzungen:** JDK 21, Maven 3.9+

```bash
cd geoworld
mvn package -DskipTests
# JAR: geoworld/target/geoworld-*.jar
```

**Deployment:** JAR → `plugins/`

Dokumentation: [`docs/WORLDGEN_PLUGIN_ARCHITECTURE.md`](docs/WORLDGEN_PLUGIN_ARCHITECTURE.md)

---

## 🐉 Komponente 3 – MythicMobs Konfigurationen

Biom-spezifische Mob-Konfigurationen für MythicMobs 5.x. Jedes der 50 Custom-Biome hat eigene Kreaturen mit passenden Skills, Stats und Spawn-Bedingungen.

**Deployment:** `plugins/MythicMobs/` → `plugins/MythicMobs/` (mergen)

Nach dem Kopieren im laufenden Server: `/mm reload`

Dokumentation: [`docs/JULES_PROMPT_MYTHICMOBS.md`](docs/JULES_PROMPT_MYTHICMOBS.md) · [`docs/JULES_PROMPT_WILDLIFE.md`](docs/JULES_PROMPT_WILDLIFE.md)

---

## 🚀 Deployment (Reihenfolge)

1. **Server stoppen**
2. `geoworld-datapack/` → `world/datapacks/geoworld-datapack/`
3. Server starten → `/reload` oder neue Welt generieren
4. GeoWorld JAR → `plugins/` → Server neu starten
5. MythicMobs YMLs mergen → `/mm reload`

---

## 🔧 Abhängigkeiten

| Komponente | Abhängigkeit | Version |
|---|---|---|
| GeoWorld Plugin | Paper API | 1.21.x |
| GeoWorld Plugin | MythicMobs API (optional) | 5.x |
| MythicMobs YMLs | MythicMobs | 5.x |
| Datapack | Minecraft | 1.21+ |

---

## 📄 Dokumentation

| Datei | Inhalt |
|---|---|
| [`docs/CUSTOM_BIOMES.md`](docs/CUSTOM_BIOMES.md) | Alle 50 Custom-Biome mit Beschreibung, Icon und Spawn-Regeln |
| [`docs/WORLDGEN_ARCHITECTURE.md`](docs/WORLDGEN_ARCHITECTURE.md) | Technische Architektur des Datapacks |
| [`docs/WORLDGEN_PLUGIN_ARCHITECTURE.md`](docs/WORLDGEN_PLUGIN_ARCHITECTURE.md) | Architektur des GeoWorld Plugins |
| [`docs/JULES_PROMPT_WORLDGEN_PLUGIN_PHASE1.md`](docs/JULES_PROMPT_WORLDGEN_PLUGIN_PHASE1.md) | Jules-Prompt Phase 1 |
| [`docs/JULES_PROMPT_WORLDGEN_PLUGIN_PHASE2.md`](docs/JULES_PROMPT_WORLDGEN_PLUGIN_PHASE2.md) | Jules-Prompt Phase 2 |
| [`docs/JULES_PROMPT_MYTHICMOBS.md`](docs/JULES_PROMPT_MYTHICMOBS.md) | Jules-Prompt MythicMobs |
| [`docs/JULES_PROMPT_WILDLIFE.md`](docs/JULES_PROMPT_WILDLIFE.md) | Jules-Prompt Wildlife |

---

## 🐛 Bekannte Bugs / Offene Issues

Siehe [GitHub Issues](https://github.com/lothomax-bit/mc-paper-rpg-discovery/issues).
