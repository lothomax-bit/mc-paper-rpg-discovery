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

=== PLACER: de.lothomax.geoworld.module.river.WaterfallPlacer ===

Methode: void place(World world, List<RiverNode> nodes, GeoWorldConfig config)

Läuft auf Main-Thread.

**Grundprinzip (River Carving):**
Der Fluss liegt bündig im Terrain – wie in der Realität.
Statt Wasser auf den Boden zu legen, wird der Bodenblock selbst
durch Wasser ersetzt. Das ergibt ein natürliches Flussbett.

Vorgehensweise pro FLOW-Node bei Koordinate (x, y, z):

  1. Bestimme den höchsten Festkörper-Block an (x, z): highestSolidY
  2. Ersetze die obersten `river-carve-depth` Festkörper-Blöcke durch Material.WATER:
     ```
     for (int dy = 0; dy < carveDepth; dy++) {
         Block b = world.getBlockAt(x, highestSolidY - dy, z);
         if (b.getType().isSolid()) {
             b.setType(Material.WATER);
         }
     }
     ```
  3. Statt den Originalblock zu überschreiben: Nur ersetzen wenn der Block
     in der "erlaubten Material-Liste" liegt:
     Erlaubt: GRASS_BLOCK, DIRT, STONE, GRAVEL, SAND, SANDSTONE, COARSE_DIRT,
              PODZOL, MUD, CLAY, SNOW_BLOCK, POWDER_SNOW, ROOTED_DIRT,
              MOSSY_COBBLESTONE, COBBLESTONE, DEEPSLATE, TUFF,
              alle Terrakotta-Varianten, alle Sandstein-Varianten
     Nicht ersetzen: BEDROCK, OBSIDIAN, jede Erzsorte, CHEST, strukturelle Blöcke
  4. Breite: Für jede Breiten-Stufe (river-width) werden die Nachbarblöcke
     in einem Radius orthogonal zur Flussrichtung genauso gecarved.

Vorgehensweise pro WATERFALL-Node (senkrechter Abfall):
  - Hier wird NICHT gecarved, sondern Wasser in die Luft gesetzt:
    Setze Material.WATER (source) an (x, y, z) wenn der Block dort AIR oder
    ein ersetzbarer Block ist.
  - Wasserfallblöcke dürfen bestehende Festkörperblöcke NICHT ersetzen.

Vorgehensweise pro LAKE-Node (Bergsee):
  - Radius zwischen `mountain-lake-min-radius` und `mountain-lake-max-radius` (random)
  - Für jeden Block im Kreis:
    1. Bestimme highestSolidY an (x, z)
    2. Ersetze die obersten `river-carve-depth` Festkörper-Blöcke durch WATER
       (gleiche Materialprüfung wie bei FLOW)
    3. Boden des Sees (highestSolidY - carveDepth): ersetze durch GRAVEL oder SAND (random)
  - Erzeugt einen natürlichen Bergsee der ins Terrain eingebettet ist.

Bergsee-Generierung (Kreis-Algorithmus):
```java
for (int dx = -radius; dx <= radius; dx++) {
    for (int dz = -radius; dz <= radius; dz++) {
        if (dx*dx + dz*dz <= radius*radius) {
            int bx = lakeX + dx;
            int bz = lakeZ + dz;
            int highestSolidY = getHighestSolid(world, bx, bz);
            for (int dy = 0; dy < carveDepth; dy++) {
                Block b = world.getBlockAt(bx, highestSolidY - dy, bz);
                if (isCarveableBlock(b.getType())) {
                    b.setType(Material.WATER);
                }
            }
            // Seeboden
            Block floor = world.getBlockAt(bx, highestSolidY - carveDepth, bz);
            if (isCarveableBlock(floor.getType())) {
                floor.setType(Math.random() > 0.5 ? Material.GRAVEL : Material.SAND);
            }
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
    Material.DEEPSLATE, Material.TUFF,
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
- isCarveableBlock() ist ein Set.contains()-Check – O(1), kein Performance-Problem
- Chunks aus der Queue entfernen bevor sie verarbeitet werden (nicht danach)
- Wenn ein Chunk während der Verarbeitung ungeladen wird: try/catch mit ChunkUnloadException

Gib die vollständige Projektstruktur mit allen Dateien aus, fertig zum
Kompilieren mit mvn package.
```
