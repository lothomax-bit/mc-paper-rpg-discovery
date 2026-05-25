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
    mountain-lake-min-radius: 2
    mountain-lake-max-radius: 3
    # Quellenwahrscheinlichkeit & Limit pro Chunk
    # 0.08 ≈ 1 Quelle pro ~12 Bergbiom-Chunks
    spring-chance: 0.08
    # Maximal 1 Fluss-Quelle pro Chunk (auch wenn mehrere WATER-Blocks vorhanden)
    spring-max-per-chunk: 1
    # Breite & Tiefe skalieren graduell mit abnehmendem Y
    # Stufe 1: Hochgebirge (Y > 400)
    river-width-1: 1
    river-depth-1: 1
    # Stufe 2: Mittlere Höhe (Y 250-400)
    river-width-2: 2
    river-depth-2: 2
    # Stufe 3: Tiefland (Y 150-250)
    river-width-3: 4
    river-depth-3: 3
    # Stufe 4: Tiefland nähe Küste (Y < 150)
    river-width-4: 7
    river-depth-4: 4
    river-bank-width: 2
    chunks-per-tick: 3
    geodiscovery-hook: true
    geodiscovery-lake-icon: "💧"
```

GeoWorldConfig hat Getter für alle Werte. Die vier Stufen werden als
`RiverProfile`-Records gespeichert:
```java
record RiverProfile(int width, int depth) {}

public RiverProfile getProfile(int y) {
    if (y > 400) return new RiverProfile(width1, depth1);
    if (y > 250) return new RiverProfile(width2, depth2);
    if (y > 150) return new RiverProfile(width3, depth3);
    return      new RiverProfile(width4, depth4);
}
```

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
   a. Initialisiere springsThisChunk = 0
   b. Iteriere alle Blöcke im Chunk von Y=`min-spring-y` bis Y=maxY
   c. Prüfe ob Block == Material.WATER
   d. Prüfe ob darüber Luft ist (source block, kein fließendes Wasser)
   e. Prüfe ob Biom ein Bergbiom ist:
      Erlaubte Biome: JAGGED_PEAKS, FROZEN_PEAKS, STONY_PEAKS, WINDSWEPT_HILLS,
      WINDSWEPT_GRAVELLY_HILLS, SNOWY_SLOPES + alle geoworld:-Biome die
      continentalness > 0.4 haben (crystal_peaks, skyreach_plateau, obsidian_spires,
      floating_isles)
   f. Quellenfilter (NUR wenn alle Bedingungen oben erfüllt):
      - Wenn springsThisChunk >= `spring-max-per-chunk`: Chunk überspringen (break)
      - Wenn random.nextDouble() >= `spring-chance`: diesen Block überspringen (continue)
      - Sonst: springsThisChunk++, weiter mit Schritt g
   g. Rufe RiverPathfinder.trace(block.getLocation()) auf
   h. Sammle List<RiverNode> Ergebnis
3. Wechsle auf Main-Thread (Bukkit.getScheduler().runTask()) für Block-Platzierung
4. Platziere alle gesammelten Blöcke via WaterfallPlacer

Hinweis zur Quellenwahrscheinlichkeit:
- spring-chance: 0.08 bedeutet ~8% je gefundenem WATER-Source-Block im Bergbiom.
- Da ein Bergchunk typischerweise wenige WATER-Source-Blöcke über Y=200 hat,
  ergibt sich in der Praxis ca. 1 aktiver Fluss pro 12 Bergbiom-Chunks.
- spring-max-per-chunk: 1 stellt sicher, dass selbst bei vielen WATER-Blöcken
  nie mehr als ein Flusssystem pro Chunk startet. Das verhindert überlappende
  Flussbetten und Performance-Spitzen.

=== PATHFINDER: de.lothomax.geoworld.module.river.RiverPathfinder ===

Methode: List<RiverNode> trace(Location start)

RiverNode ist ein Record mit:
- int x, y, z
- RiverNodeType type  // FLOW, WATERFALL_START, WATERFALL_END, LAKE, MOUTH
- int dirX, dirZ      // Bewegungsrichtung (Einheitsvektor)

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

**Breite & Tiefe** werden NICHT im Pathfinder festgelegt – der Placer berechnet
sie pro Node anhand des Y-Werts via `config.getProfile(node.y())`.
Dadurch skaliert der Fluss automatisch während er bergab fließt.

Der Übergang zwischen Stufen ist graduell: Wenn sich das Profil zwischen
zwei aufeinanderfolgenden Nodes ändert, wird die neue Breite sofort übernommen.
Das erzeugt einen natürlichen, sanften Breitenunterschied.

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
            -> Material.DIRT;

        // Nether (Fallback)
        case NETHER_WASTES, CRIMSON_FOREST, WARPED_FOREST,
             SOUL_SAND_VALLEY, BASALT_DELTAS
            -> Material.SOUL_SAND;

        default -> Material.GRAVEL;
    };
}

// Für geoworld:-Biome:
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
        default                             -> Material.GRAVEL;
    };
}
```

=== PLACER: de.lothomax.geoworld.module.river.WaterfallPlacer ===

Methode: void place(World world, List<RiverNode> nodes, GeoWorldConfig config)

Läuft auf Main-Thread.

**Querschnitt pro Node:**

```
←─ Ufer (bank-width) ─→←──── Flussbett (width) ────→←─ Ufer (bank-width) ─→
   Sand / Gras / MUD        [GRAVEL] + [WATER x depth]    Sand / Gras / MUD
```

Breite und Tiefe werden per Node aus `config.getProfile(node.y())` gelesen:

| Y-Höhe | Breite | Tiefe | Charakter |
|---------|--------|-------|-----------|
| > 400   | 1 Bl.  | 1 Bl. | Schmaler Gebirgsbach |
| 250–400 | 2 Bl.  | 2 Bl. | Bergfluss |
| 150–250 | 4 Bl.  | 3 Bl. | Breiter Mittellandfluss |
| < 150   | 7 Bl.  | 4 Bl. | Breiter Tieflandfluss Richtung Ozean |

---

**Schritt 1 – Flussbett carven** (für alle FLOW-Nodes):

  Für alle Positionen orthogonal zur Flussrichtung im Radius `width`:
  1. highestSolidY = höchster Festkörper-Block an (x, z)
  2. Unterster Flussbett-Block (highestSolidY - depth): setze GRAVEL
  3. Darüber (`depth - 1` Blöcke): setze WATER (source)
  Nur wenn isCarveableBlock(original) == true

  Korrekte Schichtung:
  - depth=1: highestSolidY-1 = GRAVEL, highestSolidY = WATER
  - depth=2: highestSolidY-1 = GRAVEL, highestSolidY = WATER
  - depth=3: highestSolidY-2 = GRAVEL, highestSolidY-1 = WATER, highestSolidY = WATER
  - depth=4: highestSolidY-3 = GRAVEL, highestSolidY-2..highestSolidY = WATER

**Schritt 2 – Ufer setzen** (links und rechts, `bank-width` Blöcke):

  perpDir = (-dirZ, dirX)  // Orthogonal zur Flussrichtung
  Für side in {+1, -1}:
    Für b = 1 bis `river-bank-width`:
      ux = x + perpDir.x * (width + b)
      uz = z + perpDir.z * (width + b)
      highestSolidY = höchster Festkörper-Block an (ux, uz)
      bankMaterial = RiverBiomeHelper.get[Custom]BankMaterial(biome at ux,y,uz)
      setze world.getBlockAt(ux, highestSolidY, uz) = bankMaterial
      (nur wenn isCarveableBlock == true, kein Wasser setzen)

---

**WATERFALL-Nodes:**
  Kein Carving. WATER source in die Luft setzen wenn Block AIR ist.
  Keine Ufer für Wasserfallblöcke.
  Breite des Wasserfalls = Breite des Flusses an WATERFALL_START-Y.

**LAKE-Nodes (Bergsee):**
  Radius: zufällig zwischen `mountain-lake-min-radius` (2) und
  `mountain-lake-max-radius` (3). Bergseen sind bewusst klein –
  kompakt, versteckt, realistisch.

  Tiefe des Sees = `river-depth` des aktuellen Y-Profils (meist 2).

  Für jeden Block im Kreis:
    if distSq <= radius^2:
      Seeboden (highestSolidY - depth): GRAVEL
      Darüber (depth Blöcke): WATER
    else if distSq <= (radius + bankWidth)^2:
      Uferblock: biomspezifisch (wie FLOW)

```java
for (int dx = -outerR; dx <= outerR; dx++) {
    for (int dz = -outerR; dz <= outerR; dz++) {
        int distSq = dx*dx + dz*dz;
        int bx = lakeX + dx, bz = lakeZ + dz;
        int top = getHighestSolid(world, bx, bz);
        if (distSq <= radius * radius) {
            // Seebett
            if (isCarveableBlock(world.getBlockAt(bx, top - depth, bz).getType()))
                world.getBlockAt(bx, top - depth, bz).setType(Material.GRAVEL);
            for (int dy = 0; dy < depth; dy++) {
                Block b = world.getBlockAt(bx, top - dy, bz);
                if (isCarveableBlock(b.getType())) b.setType(Material.WATER);
            }
        } else if (distSq <= outerR * outerR) {
            // Seeufer
            Block b = world.getBlockAt(bx, top, bz);
            if (isCarveableBlock(b.getType()))
                b.setType(getBankMaterialForLocation(world, bx, top, bz));
        }
    }
}
// outerR = radius + bankWidth
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
- spring-max-per-chunk: 1 verhindert, dass ein einzelner Chunk dutzende Traces startet
- Bei Stufe 4 (width=7, depth=4): bis zu (7*2+1) * 4 = 60 Blöcke pro Node.
  Bei 800 Nodes max = 48.000 Block-Operationen pro Fluss. Akzeptabel, da async.
- isCarveableBlock() und getBankMaterial() sind O(1) Set/Switch-Operationen
- Chunks aus der Queue entfernen bevor sie verarbeitet werden (nicht danach)
- Wenn ein Chunk während der Verarbeitung ungeladen wird: try/catch

Gib die vollständige Projektstruktur mit allen Dateien aus, fertig zum
Kompilieren mit mvn package.
```
