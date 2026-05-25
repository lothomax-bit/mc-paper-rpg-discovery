# Jules-Prompt: GeoWorld WorldGen-Plugin – Phase 2 (Structure Engine)

Dieser Prompt baut auf Phase 1 auf. Das GeoWorld-Plugin muss bereits existieren
(RiverFlowModule, WaterfallPlacer, pending_lakes.csv). Erst Phase 1 abschließen,
dann diesen Prompt ausführen.

---

```
Erweitere das bestehende GeoWorld Paper-Plugin (de.lothomax.geoworld, Maven,
Java 17+, Paper API 26.1 / 1.21.4+) um ein vollständiges Structure-Modul.

Das Modul generiert biom-spezifische Strukturen beim Chunk-Generieren –
programmatisch via Block-API, kein NBT/Schematic-System.

=== NEUE PAKETSTRUKTUR ===

de.lothomax.geoworld
└── module
    └── structure
        ├── StructureModule.java          // Modul-Einstieg, wird von GeoWorld geladen
        ├── StructureRegistry.java        // Biom → Generator Mapping
        ├── StructureGenerator.java       // Abstrakte Basisklasse
        ├── StructureResult.java          // Record: Location, generatorId, biomKey
        ├── generators
        │   ├── WaterfallCaveGenerator.java     // Höhlen hinter Wasserfällen
        │   ├── WizardTowerGenerator.java        // Zaubererturm (Märchenwald)
        │   ├── OutpostGenerator.java            // Außenposten (Postapokalyptisch)
        │   ├── UnderwaterPalaceGenerator.java   // Unterwasser-Palast
        │   ├── ObsidianCitadelGenerator.java    // Obsidian-Zitadelle
        │   ├── FrozenFortressGenerator.java     // Eingefrorene Festung
        │   ├── RuneCircleGenerator.java         // Runenkreis
        │   ├── MoonTempleGenerator.java         // Mondtempel
        │   └── AshOutpostGenerator.java         // Asche-Außenposten
        ├── loot
        │   └── BiomeLootProvider.java    // Biom-spezifische Truheninhalte
        └── mobs
            └── StructureMobSpawner.java  // MythicMobs-Spawn-Integration

=== ABSTRAKTE BASISKLASSE: StructureGenerator ===

```java
public abstract class StructureGenerator {
    protected final GeoWorldConfig config;

    public abstract String getId();           // z.B. "wizard_tower"
    public abstract List<String> getBiomes(); // z.B. ["geoworld:dream_forest"]
    public abstract double getChance();       // Spawn-Wahrscheinlichkeit pro Chunk (0.0–1.0)
    public abstract int getMinY();            // Minimale Y-Höhe für Spawn
    public abstract int getMaxY();            // Maximale Y-Höhe für Spawn

    // Hauptmethode – läuft auf Main-Thread
    public abstract StructureResult generate(World world, int chunkX, int chunkZ);

    // Hilfsmethode: Ellipsoid aushöhlen
    protected void carveEllipsoid(World world, Location center, int rx, int ry, int rz) { ... }

    // Hilfsmethode: Quader füllen
    protected void fillBox(World world, Location min, Location max, Material mat) { ... }

    // Hilfsmethode: Einzelnen Block sicher setzen (Chunk-Loaded-Check)
    protected void setBlock(World world, int x, int y, int z, Material mat) { ... }

    // Hilfsmethode: Truhe platzieren und mit BiomeLootProvider befüllen
    protected void placeLootChest(World world, Location loc, String biomKey) { ... }
}
```

=== MODUL: StructureModule ===

- start(): Lädt alle Generator-Instanzen in StructureRegistry
- Hört auf ChunkLoadEvent (isNewChunk == true) via eigenem Listener
- Pro neuem Chunk:
  1. Prüfe Biom des Chunk-Zentrums
  2. Hole passenden Generator aus StructureRegistry
  3. Würfle gegen generator.getChance()
  4. Prüfe: Wurde in den letzten `min-chunk-distance` Chunks bereits eine
     Struktur dieses Typs gesetzt? (verhindert Clustering)
  5. Wenn alles passt: generator.generate() auf Main-Thread aufrufen
  6. Speichere StructureResult in placed_structures.csv:
     Format: worldUuid,chunkX,chunkZ,generatorId,biomKey,x,y,z
- Beim Server-Start: placed_structures.csv einlesen um Dopplungen zu vermeiden

=== STRUKTUR-REGISTRY ===

```java
public class StructureRegistry {
    private final Map<String, List<StructureGenerator>> biomToGenerators = new HashMap<>();

    public void register(StructureGenerator gen) {
        for (String biom : gen.getBiomes()) {
            biomToGenerators.computeIfAbsent(biom, k -> new ArrayList<>()).add(gen);
        }
    }

    public List<StructureGenerator> getForBiome(String biomKey) {
        return biomToGenerators.getOrDefault(biomKey, Collections.emptyList());
    }
}
```

=== GENERATOR 1: WaterfallCaveGenerator ===

Biome: ALLE geoworld:-Biome + alle Vanilla-Biome
Chance: 0.65 (liest aus pending_lakes.csv ob in diesem Chunk ein Wasserfall ist)
MinY: 50, MaxY: 1800

Ablauf:
1. Lese pending_lakes.csv – gibt es einen Eintrag für diesen Chunk?
2. Wenn ja: Finde die Wasserfall-Wand (Block mit Wasser neben Fels)
3. Carve Ellipsoid hinter der Wand: rx=random(5,15), ry=random(4,10), rz=random(5,15)
4. Boden: Moos-Block oder biom-spezifisches Material
5. Beleuchtung: Glow-Lichen an Decke und Wänden (random, 30% Dichte)
6. Platziere 1 Schatztruhe (BiomeLootProvider)
7. Platziere 1–2 MythicMobs-Spawn-Points (StructureMobSpawner: CaveHermit, WaterfallSpirit)
8. GeoDiscovery-Hook: Region als CAVE_FEATURE registrieren

=== GENERATOR 2: WizardTowerGenerator ===

Biome: geoworld:dream_forest, geoworld:rune_highlands, geoworld:twilight_canopy
Chance: 0.03
MinY: 60, MaxY: 300

Turm-Struktur (von unten nach oben):
- Fundament: 5x5 Mossy Cobblestone, 2 Blöcke tief im Boden
- Erdgeschoss (Y+0 bis Y+5): 3x3 Polished Deepslate Wände, Tür aus Dark Oak
  * Innen: Crafting Table, Bücherregal (2x), Cauldron
- Etage 1 (Y+6 bis Y+11): 3x3, Bücherregale ringsum
  * Innen: Enchanting Table, Lapis-Block, 1x Schatztruhe
- Etage 2 (Y+12 bis Y+17): 3x3, Glow-Lichen Beleuchtung
  * Innen: Brewing Stand, Potion-Materialien-Truhe
- Dach (Y+18 bis Y+21): Kegelform aus Spruce Slabs und Stairs
  * Gipfel: Amethyst-Block mit Kristallspitze (Amethyst Cluster)
- Treppe: Innen spiralförmig aus Oak Stairs
- Außen: Schlingpflanzen (Vines) an 2 Seiten
- 1x Boss-Spawn am Dach: MythicMobs ID "DreamWeaver" oder "TwilightDruid" je nach Biom
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 3: OutpostGenerator ===

Biome: geoworld:petrified_wasteland, geoworld:ember_plains,
       geoworld:volcanic_ashfields, geoworld:salt_flats, geoworld:eclipse_valley
Chance: 0.04
MinY: 60, MaxY: 400

Außenposten-Struktur:
- Umzäunung: 9x9 Cobblestone Wall, halb eingerissen (zufällig 30% Wand-Segmente fehlen)
- Hauptgebäude: 5x5x4 aus biom-spezifischen Blöcken:
  * ember_plains / volcanic_ashfields: Blackstone + Cracked Stone Bricks
  * petrified_wasteland: Calcite + Cracked Stone Bricks
  * salt_flats: White Terracotta + Calcite
  * eclipse_valley: Deepslate + Cracked Deepslate Bricks
- Innen: Barrel (Loot), Crafting Table, beschädigtes Bett (1 Block fehlt)
- Wachturm: 3x3x6 an einer Ecke, Leiter innen, Aussichtsplattform oben
- Lagerfeuer (campfire) im Innenhof, erloschen (nicht aktiv)
- 2–3 MythicMobs-Spawn-Points:
  * ember_plains: CinderHound (2x), AshWalker (1x)
  * petrified_wasteland: StoneGolem (1x), PetrifiedWarden (1x)
  * volcanic_ashfields: AshWalker (2x), AshPhoenix (1x, selten)
  * salt_flats: SaltGolem (1x), MiragePhantom (1x)
  * eclipse_valley: EclipsePhantom (2x), SolarWraith (1x Boss)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 4: UnderwaterPalaceGenerator ===

Biome: geoworld:sunken_city, geoworld:abyssal_trench
Chance: 0.02
MinY: -64, MaxY: 30

Palast-Struktur (vollständig unter Wasser):
- Fundament: 11x11 Dark Prismarine Platte am Meeresboden
- Haupthalle: 9x9x6 aus Dark Prismarine + Prismarine Bricks
  * Dach: Prismarine Slab mit Glow-Lichen (Beleuchtung)
  * Innen: 2x Schatztruhen, Sea Lanterns als Beleuchtung
- Säulen: 4 Ecksäulen aus Prismarine Bricks, 8 Blöcke hoch
- Seitenflügel: 2x (5x5x4) links und rechts der Haupthalle
- Türen: Iron Bars (Gitter) als Unterwassertüren
- Außendeko: Seagrass, Kelp, Korallen am Fundament
- 2–3 MythicMobs-Spawn-Points:
  * sunken_city: DrownedKnight (2x), TideCaller (1x Boss)
  * abyssal_trench: AbyssalHunter (2x), DeepOneSorcerer (1x Boss)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 5: ObsidianCitadelGenerator ===

Biome: geoworld:obsidian_spires
Chance: 0.015
MinY: 200, MaxY: 900

Zitadellen-Struktur:
- Mauern: 13x13 Obsidian Mauer, 6 Blöcke hoch, mit Zinnen (Obsidian Wall oben)
- Hauptturm: 5x5x12 Obsidian + Crying Obsidian Akzente
  * Innen: Spiraltreppe aus Blackstone Stairs
  * Gipfel: 1 Netherstern-Truhe (seltenes Loot)
- Lava-Graben: Ring um die Mauer, 2 Blöcke breit, gefüllt mit Lava
- 4 Ecktürme: 3x3x8 Blackstone
- Boss-Spawn: ObsidianColossus (1x, im Innenhof)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 6: FrozenFortressGenerator ===

Biome: geoworld:glacial_abyss
Chance: 0.02
MinY: -20, MaxY: 200

Gefrorene Festung:
- Wände: 11x11 Blue Ice + Packed Ice, teilweise transparent
- Hauptgebäude: 7x7x5 Packed Ice, darin eingeschlossene Strukturen sichtbar
- Eingefrorene Objekte (dekorativ, im Eis eingeschlossen):
  * Zufällig: Barrel, Crafting Table, Skeleton-Spawn (als wäre er eingefroren)
- Eisturm: 3x3x10 Blue Ice, Spitze aus Pointed Dripstone
- 1 Schatztruhe (tiefgefroren, mit Ice-Block-Abdeckung)
- Boss-Spawn: IceTitan (1x)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 7: RuneCircleGenerator ===

Biome: geoworld:rune_highlands
Chance: 0.05
MinY: 100, MaxY: 600

Runenkreis:
- Kreis aus 8 Runensteinen (Chiseled Stone Bricks auf Stone Pillar, Höhe 3)
  Radius: 7 Blöcke vom Zentrum
- Zentrum: Enchanting Table auf Mossy Stone Bricks Platte (3x3)
- Verbindungslinien: Mossy Cobblestone Pfad zwischen Runensteinen
- Magiepartikel-Effekt: Wird über GeoDiscovery-Region-Kommentar dokumentiert
  (Partikel-Implementierung in separatem Effekt-System)
- 2–3 Mob-Spawn-Points: RuneGuardian (2x), AncientSeer (1x)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 8: MoonTempleGenerator ===

Biome: geoworld:moonstone_basin
Chance: 0.025
MinY: 60, MaxY: 300

Mondtempel (besondere Regel: Spawnt nur nachts – prüfe world.getTime() < 13000 || > 23000):
- Plattform: 7x7 Calcite + Quartz Slab
- Tempel: 5x5x5 Calcite + White Terracotta
  * Innen: Conduit auf Seelenstein-Sockel (dekorativ), 1 Schatztruhe
- 4 Mondstein-Säulen (Quartz Pillars) an den Ecken, Höhe 4
- Mondlicht-Brunnen: 3x3 Wasser-Feature im Zentrum der Plattform
- Besonderheit: Mob spawnt nur bei world.getTime() zwischen 13000 und 23000
  MoonElemental (1x), LunarWolf (2x)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== GENERATOR 9: AshOutpostGenerator ===

Biome: geoworld:volcanic_ashfields, geoworld:ember_plains
Chance: 0.035
MinY: 60, MaxY: 500

Unter-Asche-Außenposten (halb vergraben):
- Basisgebäude: 7x7x4 Gravel + Blackstone, NUR die obere Hälfte ragt aus dem Boden
  (untere 2 Blöcke werden durch Terrain verdeckt – carve Eingang)
- Eingang: 2x2 Tunnel aus Blackstone der schräg nach unten führt
- Innen: Barrel (Loot), Soul Sand Boden, Seelen-Feuer Beleuchtung
- Asche-Dach: Gravel + Blackstone Slabs, Asche-Partikel-Kommentar
- 2 Mob-Spawns: AshWalker (2x) oder MagmaWorm (1x)
- GeoDiscovery-Hook: Region als STRUCTURE registrieren

=== LOOT: BiomeLootProvider ===

Methode: ItemStack[] getLoot(String biomKey, String structureType)

Loot-Tabellen pro Biom-Kategorie (je 5–8 zufällige Items aus der Liste):

MYSTISCH (dream_forest, rune_highlands, moonstone_basin, starfall_meadow):
- Enchanted Books (Fortune III, Silk Touch, Mending)
- Amethyst Shards (8–16x)
- Echo Shard
- Totem of Undying (sehr selten, 3%)
- Experience Bottle (4–8x)
- Nether Star Fragment (Custom Item, Name: "Sternsplitter")

POSTAPOKALYPTISCH (petrified_wasteland, ember_plains, volcanic_ashfields, salt_flats, eclipse_valley):
- Iron Ingots (8–16x)
- Gunpowder (4–8x)
- TNT (1–2x, selten)
- Chainmail Armor (beschädigt, random Durability 10–40%)
- Compass
- Cobweb (2–4x)
- Rotten Flesh (4–8x)
- "Verwittertes Tagebuch" (Custom Book mit Lore-Text)

OZEAN (sunken_city, abyssal_trench, coral_archipelago):
- Heart of the Sea (selten, 5%)
- Prismarine Crystals (8–16x)
- Nautilus Shell (2–4x)
- Trident (sehr selten, 2%)
- Waterbreathing Potion (2x)
- Fishing Rod (enchanted, Luck of the Sea II)

EIS (glacial_abyss, aurora_tundra):
- Blue Ice (4–8x)
- Packed Ice (8–16x)
- Diamond (1–2x, selten 8%)
- Freeze Arrow (Custom Item, Name: "Frostpfeil")
- Leather Armor (vollständig, enchanted)

VULKANISCH (obsidian_spires, magma_depths, lava_falls):
- Obsidian (8–16x)
- Blaze Rod (4–8x)
- Fire Resistance Potion (2x)
- Netherite Scrap (1x, sehr selten 4%)
- Magma Block (4–8x)
- "Geschmolzenes Siegel" (Custom Item mit Lore)

WASSERFALLHÖHLEN (alle Biome, waterfall_cave):
- Glow Lichen (4–8x)
- Moss Block (4–8x)
- Azalea (2–4x)
- Gold Nuggets (4–8x)
- Cave Spider Spawn Egg (sehr selten, 2%)
- "Feuchte Aufzeichnungen" (Custom Book mit Lore)

=== MOB-SPAWNER: StructureMobSpawner ===

Methode: void spawnAt(Location loc, String mythicMobId)

- Prüft ob MythicMobs-Plugin geladen ist:
  Plugin mm = Bukkit.getPluginManager().getPlugin("MythicMobs");
- Wenn ja: MythicMobs API aufrufen um Mob zu spawnen
- Wenn nein: Fallback auf Vanilla-Mobs:
  * "DreamWeaver" → EVOKER
  * "RuneGuardian" → VINDICATOR  
  * "IceTitan" → ELDER_GUARDIAN
  * "ObsidianColossus" → IRON_GOLEM
  * "TideCaller" → ELDER_GUARDIAN
  * "AshWalker" → ZOMBIE (mit Fire Resistance)
  * Alle anderen → ZOMBIE
- Spawn-Delay: 20 Ticks nach Struktur-Generierung (via BukkitScheduler.runTaskLater)
- Mob erhält CustomName aus MythicMobs-ID wenn kein MythicMobs vorhanden

=== ERWEITERUNG GEODISCOVERY HOOK ===

Wenn eine Struktur generiert wird und geodiscovery-hook: true:
```java
Plugin geo = Bukkit.getPluginManager().getPlugin("GeoDiscovery");
if (geo != null && geo.isEnabled()) {
    // Logge in placed_structures.csv:
    // worldUuid,chunkX,chunkZ,generatorId,biomKey,centerX,centerY,centerZ
    // GeoDiscovery importiert diese CSV beim nächsten Reload
}
```

=== ERWEITERUNG GEOWORLD_CONFIG.YML ===

Füge folgende Sektion zur bestehenden geoworld_config.yml hinzu:

```yaml
modules:
  # ... river-flow Sektion bleibt unverändert ...

  structure:
    enabled: true
    min-chunk-distance: 8          # Mindestabstand zwischen gleichen Strukturtypen (in Chunks)
    geodiscovery-hook: true
    generators:
      waterfall-cave:
        enabled: true
        chance: 0.65
        min-size: 5
        max-size: 15
      wizard-tower:
        enabled: true
        chance: 0.03
      outpost:
        enabled: true
        chance: 0.04
      underwater-palace:
        enabled: true
        chance: 0.02
      obsidian-citadel:
        enabled: true
        chance: 0.015
      frozen-fortress:
        enabled: true
        chance: 0.02
      rune-circle:
        enabled: true
        chance: 0.05
      moon-temple:
        enabled: true
        chance: 0.025
      ash-outpost:
        enabled: true
        chance: 0.035
```

=== ERWEITERUNG GEOWORLD HAUPTKLASSE ===

Ergänze in GeoWorld.java onEnable():
```java
// Phase 2: Structure Module
if (config.isStructureModuleEnabled()) {
    StructureModule structureModule = new StructureModule(this, config);
    structureModule.start();
    modules.add(structureModule);
}
```

=== PERFORMANCE-HINWEISE ===

- Structure-Generierung läuft auf Main-Thread (Block-Placement benötigt Main-Thread)
- Pro Chunk wird maximal EINE Struktur generiert (erste passende aus Registry)
- placed_structures.csv wird gepuffert: Schreiben nur alle 50 Einträge (FileWriter mit Buffer)
- MoonTempleGenerator: world.getTime()-Check verhindert Spawn tagsüber
- Chunk-Load-Check vor JEDEM Block-Set: if (!world.isChunkLoaded(x>>4, z>>4)) return;
- Strukturen die über Chunk-Grenzen gehen: nur generieren wenn ALLE betroffenen
  Chunks geladen sind – sonst in pending_structures_delayed.csv schreiben und
  beim nächsten passenden ChunkLoadEvent nachholen

=== NEUE DATEIEN (vollständig ausgeben) ===

1. src/main/java/de/lothomax/geoworld/module/structure/StructureModule.java
2. src/main/java/de/lothomax/geoworld/module/structure/StructureRegistry.java
3. src/main/java/de/lothomax/geoworld/module/structure/StructureGenerator.java
4. src/main/java/de/lothomax/geoworld/module/structure/StructureResult.java
5. src/main/java/de/lothomax/geoworld/module/structure/generators/WaterfallCaveGenerator.java
6. src/main/java/de/lothomax/geoworld/module/structure/generators/WizardTowerGenerator.java
7. src/main/java/de/lothomax/geoworld/module/structure/generators/OutpostGenerator.java
8. src/main/java/de/lothomax/geoworld/module/structure/generators/UnderwaterPalaceGenerator.java
9. src/main/java/de/lothomax/geoworld/module/structure/generators/ObsidianCitadelGenerator.java
10. src/main/java/de/lothomax/geoworld/module/structure/generators/FrozenFortressGenerator.java
11. src/main/java/de/lothomax/geoworld/module/structure/generators/RuneCircleGenerator.java
12. src/main/java/de/lothomax/geoworld/module/structure/generators/MoonTempleGenerator.java
13. src/main/java/de/lothomax/geoworld/module/structure/generators/AshOutpostGenerator.java
14. src/main/java/de/lothomax/geoworld/module/structure/loot/BiomeLootProvider.java
15. src/main/java/de/lothomax/geoworld/module/structure/mobs/StructureMobSpawner.java

Außerdem anpassen:
- src/main/java/de/lothomax/geoworld/GeoWorld.java (Structure Module initialisieren)
- src/main/java/de/lothomax/geoworld/config/GeoWorldConfig.java (neue Config-Felder)
- src/main/resources/geoworld_config.yml (structure-Sektion ergänzen)

Gib alle Dateien vollständig und korrekt aus, fertig zum Kompilieren mit mvn package.
```
