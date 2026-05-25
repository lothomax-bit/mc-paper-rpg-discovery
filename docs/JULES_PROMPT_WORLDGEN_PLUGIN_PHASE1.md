# Jules-Prompt: GeoWorld WorldGen-Plugin – Phase 1 (River Flow Modul)

Dieser Prompt erstellt das GeoWorld-Plugin mit dem River-Flow-Modul.

---

```
Erstelle ein vollständiges Minecraft Paper-Plugin (Java 17+, Paper API 26.1 / 1.21.4+)
mit dem Namen "GeoWorld". Es ist ein Worldgen-Erweiterungs-Plugin das beim
Chunk-Generieren zusätzliche Features platziert.

Build-System: Maven
groupId: de.lothomax
artifactId: geoworld
version: 1.0.0-SNAPSHOT

=== POM.XML ===

Abhängigkeiten:
- Paper-API: io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT
- Maven Shade Plugin (kein HikariCP nötig in Phase 1)
- Java 17 Compiler-Target

=== PLUGIN-HAUPTKLASSE: de.lothomax.geoworld.GeoWorld ===

- Extends JavaPlugin
- onEnable():
  * Lädt GeoWorldConfig aus geoworld_config.yml
  * Initialisiert alle aktivierten Module (aktuell: RiverFlowModule)
  * Registriert ChunkPopulateListener
  * Startet RiverFlowTask
- onDisable():
  * Stoppt RiverFlowTask
  * Leert pendingChunks-Queue

=== KONFIGURATION: de.lothomax.geoworld.config.GeoWorldConfig ===

Lädt folgende geoworld_config.yml:

```yaml
modules:
  river-flow:
    enabled: true
    min-spring-y: 200
    sea-level: 63
    waterfall-threshold: 4
    max-river-length: 800
    mountain-lake-chance: 0.35
    mountain-lake-min-radius: 3
    mountain-lake-max-radius: 7
    river-width-high: 1
    river-width-mid: 2
    river-width-low: 3
    river-carve-depth: 2
    river-bank-width: 2
    chunks-per-tick: 3
    geodiscovery-hook: true
    geodiscovery-lake-icon: "💧"
```

GeoWorldConfig hat Getter für alle Werte.

=== LISTENER: de.lothomax.geoworld.listener.ChunkPopulateListener ===

- Implements Listener
- Hört auf: ChunkLoadEvent
- Bedingung: event.isNewChunk() == true
- Aktion: Fügt Chunk zu einer thread-sicheren LinkedBlockingQueue<Chunk>
  namens pendingChunks in RiverFlowModule ein
- Ignoriert Chunks unter Y-Limit (keine Bergbiome möglich)

=== MODUL: de.lothomax.geoworld.module.river.RiverFlowModule ===

- Hält pendingChunks Queue (LinkedBlockingQueue<Chunk>)
- start(): Startet RiverFlowTask als repeating async task (alle 10 Ticks)
- stop(): Cancelt Task, leert Queue
- Getter für Config-Werte

=== TASK: de.lothomax.geoworld.module.river.RiverFlowTask ===

Extends BukkitRunnable, läuft ASYNC:

1. Nimmt bis zu `chunks-per-tick` Chunks aus der pendingChunks Queue
2. Für jeden Chunk:
   a. Iteriere alle Blöcke im Chunk von Y=`min-spring-y` bis Y=maxY
   b. Prüfe ob Block == Material.WATER
   c. Prüfe ob darüber Luft ist (source block, kein fließendes Wasser)
   d. Prüfe ob Biom ein Bergbiom ist:
      Erlaubte Biome: JAGGED_PEAKS, FROZEN_PEAKS, STONY_PEAKS, WINDSWEPT_HILLS,
      WINDSWEPT_GRAVELLY_HILLS, SNOWY_SLOPES + alle geoworld:-Biome die
      continentalness > 0.4 haben (crystal_peaks, skyreach_plateau, obsidian_spires,
      floating_isles)
   e. Rufe RiverPathfinder.trace(block.getLocation()) auf
   f. Sammle List<RiverNode> Ergebnis
3. Wechsle auf Main-Thread (Bukkit.getScheduler().runTask()) für Block-Platzierung
4. Platziere alle gesammelten Blöcke via WaterfallPlacer

=== PATHFINDER: de.lothomax.geoworld.module.river.RiverPathfinder ===

Methode: List<RiverNode> trace(Location start)

RiverNode ist ein Record mit:
- int x, y, z
- RiverNodeType type  // FLOW, WATERFALL_START, WATERFALL_END, LAKE, MOUTH

Algorithmus (läuft komplett ohne Block-API, nur mit Höhendaten):

1. Starte bei start (x, y, z)
2. Schleife (max `max-river-length` Iterationen):
   a. Hole Höhe der 4 Nachbarn (N/S/O/W) via chunk.getHighestBlockAt()
   b. Wähle niedrigsten Nachbarn
   c. Berechne deltaY = currentY - neighborY
   d. Wenn deltaY > `waterfall-threshold`:
      - Füge WATERFALL_START Node ein
      - Füge alle Y-Stufen senkrecht nach unten als WATERFALL Nodes ein
      - Füge WATERFALL_END Node ein
      - Wenn `mountain-lake-chance` erfüllt: LAKE Node einfügen
   e. Sonst: FLOW Node einfügen
   f. Wenn neighborY <= `sea-level`: MOUTH Node, Abbruch
   g. Wenn Nachbar bereits Wasser: Abbruch
3. Rückgabe: List<RiverNode>

Breite je nach Y-Position:
- Y > 400: river-width-high (1 Block)
- Y 200–400: river-width-mid (2 Blöcke)
- Y < 200: river-width-low (3 Blöcke)

=== NEUE KLASSE: de.lothomax.geoworld.module.river.RiverBiomeHelper ===

Statische Hilfsklasse, die für ein gegebenes Biom den passenden
Ufermaterial-Typ zurückgibt.

```java
public static Material getBankMaterial(Biome biome) {
    return switch (biome) {
        // Schnee & Eis
        case FROZEN_PEAKS, SNOWY_SLOPES, SNOWY_PLAINS,
             SNOWY_TAIGA, SNOWY_BEACH, ICE_SPIKES
            -> Material.SNOW_BLOCK;

        // Wüste & Sand
        case DESERT, BADLANDS, ERODED_BADLANDS,
             WOODED_BADLANDS, BEACH
            -> Material.SAND;

        // Sumpf & Feuchtgebiet
        case SWAMP, MANGROVE_SWAMP
            -> Material.MUD;

        // Pilzinsel
        case MUSHROOM_FIELDS
            -> Material.MYCELIUM;

        // Steinige Berge & Gebirge
        case STONY_PEAKS, STONY_SHORE, WINDSWEPT_GRAVELLY_HILLS,
             JAGGED_PEAKS, WINDSWEPT_HILLS
            -> Material.GRAVEL;

        // Tiefland-Gras & Wald (Standard)
        case PLAINS, SUNFLOWER_PLAINS, MEADOW,
             FOREST, FLOWER_FOREST, BIRCH_FOREST,
             OLD_GROWTH_BIRCH_FOREST, DARK_FOREST,
             TAIGA, OLD_GROWTH_PINE_TAIGA, OLD_GROWTH_SPRUCE_TAIGA,
             WINDSWEPT_FOREST, WINDSWEPT_SAVANNA,
             SAVANNA, SAVANNA_PLATEAU
            -> Material.GRASS_BLOCK;

        // Dschungel
        case JUNGLE, SPARSE_JUNGLE, BAMBOO_JUNGLE
            -> Material.DIRT;  // Dschungelufer: feuchte Erde

        // Nether (Sonderfall, Flüsse im Nether unwahrscheinlich, Fallback)
        case NETHER_WASTES, CRIMSON_FOREST, WARPED_FOREST,
             SOUL_SAND_VALLEY, BASALT_DELTAS
            -> Material.SOUL_SAND;

        // GeoWorld Custom Biome (via Namespace-String, kein enum)
        // Wird separat behandelt, siehe getCustomBankMaterial()
        default -> Material.GRAVEL;  // Universeller Fallback: Kies
    };
}

// Für geoworld:-Biome die nicht im Biome-enum sind:
public static Material getCustomBankMaterial(String biomeKey) {
    return switch (biomeKey) {
        case "geoworld:glacial_abyss",
             "geoworld:aurora_tundra",
             "geoworld:rime_coast"          -> Material.PACKED_ICE;
        case "geoworld:dune_sea",
             "geoworld:coral_archipelago",
             "geoworld:pearl_lagoon",
             "geoworld:volcanic_island"     -> Material.SAND;
        case "geoworld:red_canyon",
             "geoworld:ember_plains",
             "geoworld:volcanic_ashfields"  -> Material.RED_SAND;
        case "geoworld:cursed_bayou",
             "geoworld:mangrove_labyrinth",
             "geoworld:peat_moors"          -> Material.MUD;
        case "geoworld:fungal_expanse",
             "geoworld:fungal_depths"       -> Material.MYCELIUM;
        case "geoworld:salt_flats"          -> Material.CALCITE;
        case "geoworld:ancient_forest",
             "geoworld:verdant_chasms",
             "geoworld:dream_forest"        -> Material.MOSS_BLOCK;
        case "geoworld:spore_jungle",
             "geoworld:bamboo_highlands"    -> Material.DIRT;
        // Alle anderen geoworld:-Biome: Kies
        default                             -> Material.GRAVEL;
    };
}
```

=== PLACER: de.lothomax.geoworld.module.river.WaterfallPlacer ===

Methode: void place(World world, List<RiverNode> nodes, GeoWorldConfig config)

Läuft auf Main-Thread.

**Grundprinzip:**
Der Fluss besteht aus drei Zonen (Querschnitt):

```
[UFER-LINKS]  [FLUSSBETT]  [UFER-RECHTS]
   Sand/Gras    Kies+Wasser   Sand/Gras
```

- **Flussbett** (alle FLOW-Nodes in Breite `river-width`):
  Bodenblock = GRAVEL (immer, unabhängig vom Biom)
  Darüber = WATER (source block)

- **Ufer** (orthogonal links und rechts, Breite `river-bank-width`):
  Bodenblock = biomspezifisches Material aus RiverBiomeHelper
  Kein Wasser, Ufer liegt auf gleicher Höhe wie Flussbett-Oberkante

---

Vorgehensweise pro FLOW-Node bei Koordinate (x, y, z),
Flussrichtung = (dx, dz) (Einheitsvektor, vom Pathfinder mitgeliefert):

**Schritt 1 – Flussbett carven:**
  Für alle Positionen in der Flussbreite (orthogonal zur Richtung):
  1. highestSolidY = höchster Festkörper-Block an (x, z)
  2. Untersten Flussbett-Block (highestSolidY - carveDepth + 1): setze GRAVEL
  3. Darüber (highestSolidY - carveDepth + 2 bis highestSolidY): setze WATER
  Nur wenn isCarveableBlock(original) == true

**Schritt 2 – Ufer setzen (links und rechts):**
  Orthogonale Richtung zum Fluss: perpDir = (-dz, dx)
  Für jede Ufer-Seite (Vorzeichen +1 und -1):
    Für b = 1 bis `river-bank-width`:
      ux = x + perpDir.x * (riverWidth + b)
      uz = z + perpDir.z * (riverWidth + b)
      highestSolidY = höchster Festkörper-Block an (ux, uz)
      Biom an (ux, highestSolidY, uz) bestimmen:
        - Vanilla Biome: RiverBiomeHelper.getBankMaterial(biome)
        - Custom Biome (Key beginnt mit "geoworld:"): RiverBiomeHelper.getCustomBankMaterial(key)
      setze Ufer-Block: world.getBlockAt(ux, highestSolidY, uz).setType(bankMaterial)
      Wichtig: Nur ersetzen wenn isCarveableBlock(original) == true
      Kein Wasser auf Uferblöcken setzen.

**Breite:** riverWidth ist die aktuelle Stufe (high/mid/low) aus der Config.
**Richtungsvektor (dx, dz):** WaterfallPlacer erhält ihn als Parameter pro Node.
Der RiverPathfinder speichert die Bewegungsrichtung im RiverNode:
```java
record RiverNode(int x, int y, int z, RiverNodeType type, int dirX, int dirZ) {}
```

---

Vorgehensweise pro WATERFALL-Node (senkrechter Abfall):
  - NICHT carven, nur Wasser in die Luft setzen:
    Setze Material.WATER (source) an (x, y, z) wenn AIR oder ersetzbarer Block.
  - Wasserfallblöcke ersetzen KEINE Festkörperblöcke.
  - Keine Ufer für Wasserfallblöcke.

Vorgehensweise pro LAKE-Node (Bergsee):
  - Radius zwischen `mountain-lake-min-radius` und `mountain-lake-max-radius` (random)
  - Für jeden Block im See-Kreis:
    1. highestSolidY an (bx, bz)
    2. Setze Seeboden: GRAVEL
    3. Setze WATER darüber (carveDepth Blöcke)
  - Für jeden Block im Ufer-Ring (Radius+1 bis Radius+`river-bank-width`):
    1. highestSolidY an (bx, bz)
    2. Setze biomspezifisches Ufermaterial (wie bei FLOW)

Bergsee-Generierung:
```java
for (int dx = -radius; dx <= radius; dx++) {
    for (int dz = -radius; dz <= radius; dz++) {
        int distSq = dx*dx + dz*dz;
        int bx = lakeX + dx;
        int bz = lakeZ + dz;
        int highestSolidY = getHighestSolid(world, bx, bz);
        if (distSq <= radius * radius) {
            // Seebett: Kies + Wasser
            Block floor = world.getBlockAt(bx, highestSolidY - carveDepth, bz);
            if (isCarveableBlock(floor.getType())) floor.setType(Material.GRAVEL);
            for (int dy = 0; dy < carveDepth; dy++) {
                Block b = world.getBlockAt(bx, highestSolidY - dy, bz);
                if (isCarveableBlock(b.getType())) b.setType(Material.WATER);
            }
        } else if (distSq <= (radius + bankWidth) * (radius + bankWidth)) {
            // Seeufer: biomspezifisch
            Block b = world.getBlockAt(bx, highestSolidY, bz);
            Material bankMat = getBankMaterialForBlock(b);
            if (isCarveableBlock(b.getType())) b.setType(bankMat);
        }
    }
}
```

**Hilfsmethode isCarveableBlock(Material m):**
```java
private static final Set<Material> CARVEABLE = Set.of(
    Material.GRASS_BLOCK, Material.DIRT, Material.COARSE_DIRT,
    Material.ROOTED_DIRT, Material.PODZOL, Material.MUD, Material.CLAY,
    Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE,
    Material.GRAVEL, Material.SAND, Material.SANDSTONE,
    Material.RED_SAND, Material.RED_SANDSTONE,
    Material.SNOW_BLOCK, Material.POWDER_SNOW,
    Material.DEEPSLATE, Material.TUFF, Material.CALCITE,
    Material.MYCELIUM, Material.MOSS_BLOCK, Material.SOUL_SAND,
    Material.SOUL_SOIL, Material.PACKED_ICE, Material.BLUE_ICE,
    Material.TERRACOTTA, Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA,
    Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA,
    Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA,
    Material.GRAY_TERRACOTTA, Material.LIGHT_GRAY_TERRACOTTA,
    Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA,
    Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA,
    Material.BLACK_TERRACOTTA
);
```

=== GEODISCOVERY HOOK ===

Wenn ein LAKE Node erzeugt wird und `geodiscovery-hook: true`:
```java
Plugin geo = Bukkit.getPluginManager().getPlugin("GeoDiscovery");
if (geo != null && geo.isEnabled()) {
    // Logge Location des Sees in eine separate CSV-Datei
    // plugins/GeoWorld/pending_lakes.csv
    // Format: worldUuid,x,y,z,radius
    // GeoDiscovery-API wird später diese CSV importieren können
}
```

=== PLUGIN.YML ===

name: GeoWorld
main: de.lothomax.geoworld.GeoWorld
version: 1.0.0
api-version: '1.21'
author: lothomax
depend: []
softdepend: [GeoDiscovery]
description: World generation enhancement plugin with river flow, waterfalls and mountain lakes

=== PERFORMANCE-HINWEISE ===

- RiverFlowTask läuft ASYNC – niemals Block.setType() im async Task!
- Alle Block-Änderungen IMMER via Bukkit.getScheduler().runTask() auf Main-Thread
- getHighestBlockAt() ist teuer – nur einmal pro Koordinate aufrufen, Ergebnis cachen
- isCarveableBlock() und getBankMaterial() sind O(1) Set/Switch-Operationen
- RiverBiomeHelper.getCustomBankMaterial() nur aufrufen wenn biomeKey mit "geoworld:" beginnt
- Chunks aus der Queue entfernen bevor sie verarbeitet werden (nicht danach)
- Wenn ein Chunk während der Verarbeitung ungeladen wird: try/catch mit ChunkUnloadException

Gib die vollständige Projektstruktur mit allen Dateien aus, fertig zum
Kompilieren mit mvn package.
```
