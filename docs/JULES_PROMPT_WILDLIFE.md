# Jules-Prompt: GeoWorld Wildlife – Tiere für Vanilla- & Custom-Biome

Dieser Prompt erstellt MythicMobs-YML-Konfigurationen für passive und
semi-aggressive Wildtiere, die Vanilla-Biome und GeoWorld-Custom-Biome
bevölkern. Er ist unabhängig von den anderen Jules-Prompts und kann
parallel ausgeführt werden.

Ablageort: `plugins/MythicMobs/Mobs/wildlife/`

---

```
Erstelle vollständige MythicMobs-YML-Dateien für passive und semi-aggressive
Wildtiere in Minecraft. Die Tiere bevölkern sowohl Vanilla-Biome als auch
die GeoWorld-Custom-Biome aus `docs/CUSTOM_BIOMES.md`.

=== DATEISTRUKTUR ===

plugins/MythicMobs/Mobs/wildlife/
├── wildlife_forest.yml         // Wälder (Reh, Fuchs, Bär, Luchs, Dachs, Eule)
├── wildlife_plains.yml         // Ebenen & Savanne (Wildschwein, Hase, Pferd, Giraffe, Elefant, Löwe, Gepard)
├── wildlife_mountains.yml      // Gebirge (Steinbock, Murmeltier, Adler, Schneeleo)
├── wildlife_swamp.yml          // Sümpfe (Krokodil, Frosch, Reiher, Schlange)
├── wildlife_ocean.yml          // Küste & Ozean (Pelikan, Seehund, Delphin-NPC, Schildkröte)
├── wildlife_desert.yml         // Wüste (Kamel, Geier, Hyäne, Wüstenfuchs, Skorpion passiv)
└── wildlife_geoworld.yml       // Alle GeoWorld-Custom-Biome

=== ALLGEMEINE REGELN ===

**Tiere sind keine Kampfmobs. Sie sollen die Welt lebendig machen:**
- Passive Tiere: Damage: 0, fliehen bei Angriff (Speed erhöht on Damaged)
- Semi-aggressive (Bär, Löwe, Krokodil): greifen an wenn man zu nah kommt
  oder sie angreift, aber patroullieren friedlich
- Keine HP-Bars anzeigen (BossBar: false)
- Kein Despawn: true (sie sollen in der Welt bleiben)
- Silent: false (Vanilla-Sounds, kein Custom-Sound nötig)
- PreventOtherDrops: true (nur eigene Drops)

**Entity-Typ-Mapping für Tiere:**
- Reh / Hirsch → COW (klein skaliert) oder RABBIT (klein)
- Fuchs → FOX (Vanilla! direkt nutzbar)
- Bär → POLAR_BEAR
- Luchs / Wildkatze → CAT oder OCELOT
- Dachs → RABBIT (geduckt wirkend)
- Eule → PARROT (sitzend auf Baum via Spawn)
- Wildschwein → PIG
- Hase → RABBIT
- Wildpferd → HORSE
- Giraffe → CAMEL (langhalsig, perfekte Basis!)
- Elefant → RAVAGER (groß, massig)
- Löwe / Gepard → WOLF
- Steinbock → GOAT (Vanilla! direkt nutzbar)
- Murmeltier → RABBIT
- Adler / Geier → PHANTOM (fliegend) oder PARROT
- Schneeleo → CAT oder WOLF (weiß)
- Krokodil → RAVAGER oder HOGLIN
- Schlange → SILVERFISH oder ENDERMITE
- Pelikan / Reiher → PHANTOM (tief fliegend, langsam)
- Seehund → COD (Wasser) oder PANDA (Land)
- Kamel → CAMEL (Vanilla! direkt nutzbar)
- Hyäne → WOLF
- Wüstenfuchs → FOX

=== PFLICHTFELDER PRO MOB ===

```yaml
MobID:
  Type: <VANILLA_ENTITY_TYPE>
  Display: '<Anzeigename, z.B. "&fReh">'
  Health: <float>
  Damage: <float>
  Armor: 0
  Options:
    MovementSpeed: <float>
    Despawn: false
    PreventOtherDrops: true
    PreventItemPickup: true
    Silent: false
    NoDamageTicks: 0
  Faction: Wildlife_<Kategorie>   // z.B. Wildlife_Forest
  Skills:
    - <mindestens 2 Skills>
  Drops:
    - <1-3 thematische Drops>
  # Kein BossBar-Block!
```

=== SKILL-VORLAGEN FÜR TIERE ===

**Passives Tier (flieht bei Angriff):**
```yaml
Skills:
  # Fluchttempo erhöhen wenn angegriffen
  - setspeed{speed=0.45} @Self ~onDamaged
  # Nach 3 Sekunden wieder normal
  - setspeed{speed=0.25} @Self ~onTimer:60
  # Idle-Verhalten: gelegenheitliches Weiden/Scharren
  - look{yaw=random} @Self ~onTimer:200
```

**Semi-aggressives Tier (Bär, Löwe – greift an wenn zu nah):**
```yaml
Skills:
  # Warnung bei Nähe < 5 Blocks
  - message{m='<&e>Das Tier warnt dich!'} @NearestPlayer{r=5} ~onTimer:40 ?distance{d=<5}
  # Angriff wenn < 3 Blocks oder angegriffen
  - damage{amount=6} @Target ~onAttack
  - setspeed{speed=0.38} @Self ~onDamaged
  # Territoriales Verhalten: greift bei Nähe an
  - threat{amount=100} @NearestPlayer{r=3} ~onTimer:20
```

**Rudeltier (Wölfe, Löwen – in Gruppen):**
```yaml
Skills:
  # Ruft Rudel zur Hilfe
  - summon{type=self;amount=1;radius=20} @Self ~onDamaged{healthPercent<70}
```

**Fliehendes Tier (Reh, Hase – sehr scheu):**
```yaml
Skills:
  # Flieht sofort bei Spielernähe
  - setspeed{speed=0.50} @Self ~onTimer:10 ?distance{d=<8}
  - setspeed{speed=0.25} @Self ~onTimer:100
```

=== DROP-VORLAGEN ===

Wälder: LEATHER 1-2 0.8, RABBIT_HIDE 1 0.5, FEATHER 1-3 0.4, BONE 1 0.3
Ebenen/Savanne: LEATHER 1-3 0.8, BEEF 1-2 0.6, BONE 1-2 0.4
Gebirge: LEATHER 1 0.7, FEATHER 2-4 0.6, RABBIT_HIDE 1 0.5
Sümpfe: SLIMEBALL 1 0.3, LEATHER 1 0.6, SPIDER_EYE 1 0.2
Wüste: LEATHER 1 0.7, BONE 1-2 0.5, RABBIT_HIDE 1 0.4

=== VANILLA-BIOM SPAWN-GRUPPEN ===

Füge pro YML einen RandomSpawning-Block ein.
Da GeoWorld das Spawning für Custom-Biome steuert, sind diese auf enabled: false.
Für Vanilla-Biome: enabled: true mit passenden Conditions.

**Wald-Gruppe (wildlife_forest.yml):**
- Reh (WildDeer): FOREST, BIRCH_FOREST, TAIGA, OLD_GROWTH_PINE_TAIGA
- Rotfuchs (RedFox): FOREST, TAIGA, SNOWY_TAIGA (FOX ist Vanilla, als Basis nutzbar)
- Schwarzbär (BlackBear): FOREST, DARK_FOREST, OLD_GROWTH_BIRCH_FOREST
- Eurasischer Luchs (EurasianLynx): TAIGA, OLD_GROWTH_SPRUCE_TAIGA
- Dachs (EuropeanBadger): FOREST, PLAINS
- Waldkauz (TawnyOwl): FOREST, DARK_FOREST (nachts spawnen via Condition)

**Ebenen & Savanne (wildlife_plains.yml):**
- Wildschwein (WildBoar): PLAINS, SAVANNA, FOREST
- Feldhase (EuropeanHare): PLAINS, SUNFLOWER_PLAINS, MEADOW
- Wildpferd (WildHorse): PLAINS, SAVANNA
- Giraffe (Giraffe): SAVANNA, SAVANNA_PLATEAU
- Afrikanischer Elefant (AfricanElephant): SAVANNA, SAVANNA_PLATEAU
- Löwe (Lion): SAVANNA, SAVANNA_PLATEAU (semi-aggressiv)
- Gepard (Cheetah): SAVANNA (flieht, greift schwache Spieler an)
- Zebra (Zebra): SAVANNA
- Gnu (Wildebeest): SAVANNA, SAVANNA_PLATEAU (Rudeltier)

**Gebirge (wildlife_mountains.yml):**
- Alpensteinbock (AlpineIbex): JAGGED_PEAKS, STONY_PEAKS, MEADOW (GOAT-Basis)
- Alpines Murmeltier (AlpineMarmot): MEADOW, SNOWY_SLOPES
- Steinadler (GoldenEagle): JAGGED_PEAKS, WINDSWEPT_HILLS (fliegend)
- Schneeleopard (SnowLeopard): FROZEN_PEAKS, SNOWY_SLOPES (semi-aggressiv)

**Sümpfe (wildlife_swamp.yml):**
- Nilkrokodil (NileCrocodile): SWAMP, MANGROVE_SWAMP (semi-aggressiv)
- Baumfrosch (TreeFrog): SWAMP, MANGROVE_SWAMP (passiv)
- Graureiher (GreyHeron): SWAMP, RIVER
- Ringelnatter (GrassSnake): SWAMP, JUNGLE

**Küste & Ozean (wildlife_ocean.yml):**
- Braunpelikan (BrownPelican): BEACH, OCEAN
- Kegelrobbe (HarbourSeal): BEACH, COLD_OCEAN
- Unechte Karettschildkröte (LoggerheadTurtle): BEACH, WARM_OCEAN

**Wüste (wildlife_desert.yml):**
- Dromedar (Dromedary): DESERT (CAMEL-Basis)
- Ohrengeier (LappetFacedVulture): DESERT, BADLANDS (fliegend)
- Gefleckte Hyäne (SpottedHyena): DESERT, SAVANNA (semi-aggressiv, Rudeltier)
- Fennec (FennecFox): DESERT (FOX-Basis, sehr scheu)

=== GEOWORLD-TIERE (wildlife_geoworld.yml) ===

Für jeden Custom-Biome-Typ 1-2 thematisch passende Tiere.
Spawn wird ausschließlich vom GeoWorld-Plugin gesteuert (enabled: false im RandomSpawning).

Lies `docs/CUSTOM_BIOMES.md` für die Biom-Beschreibungen.

Beispiel-Zuordnungen:

geoworld:glacial_abyss     → GlacialPolarBear (POLAR_BEAR, aggressiv bei Nähe),
                              IceSeabird (PHANTOM tief, passiv)
geoworld:aurora_tundra     → TundraReindeer (COW-Basis, passiv),
                              ArcticWolf (WOLF, Rudeltier)
geoworld:dune_sea          → DuneSandviper (SILVERFISH, scheu),
                              DuneCamel (CAMEL, passiv)
geoworld:red_canyon        → CanyonCondor (PHANTOM, kreisend),
                              RedCanyonLizard (SILVERFISH, scheu)
geoworld:coral_archipelago → ReefParrotfish (TROPICAL_FISH-Basis, passiv),
                              IslandIguana (RABBIT-Basis, scheu)
geoworld:ancient_forest    → AncientStagBeetle (SILVERFISH, passiv),
                              GiantMoose (RAVAGER-Basis, semi-aggressiv bei Nähe)
geoworld:fungal_expanse    → GlowBeetle (ENDERMITE, passiv, leuchtet),
                              FungalMoose (COW-Basis, passiv)
geoworld:volcanic_ashfields→ AshRaven (PHANTOM, passiv),
                              LavaLizard (SILVERFISH, scheu)
geoworld:dream_forest      → DreamDeer (FOX-Basis, scheu, selten),
                              LunarMoth (ALLAY, passiv)
geoworld:bamboo_highlands  → GiantPanda (PANDA-Vanilla, direkt nutzbar!),
                              RedPanda (FOX-Basis, scheu)
geoworld:salt_flats        → BrineFlamingo (PARROT, passiv),
                              SaltFlatsHare (RABBIT, sehr scheu)
geoworld:peat_moors        → MoorGrouse (PARROT, bodenbrütend),
                              PeatBogNewt (SILVERFISH, passiv)

Für alle anderen GeoWorld-Biome: Leite 1-2 thematisch passende Tiere
aus den Biom-Beschreibungen in CUSTOM_BIOMES.md selbst ab.

=== GEOWORLD SPAWN-HOOK ===

Am Ende von wildlife_geoworld.yml einen Kommentarblock einfügen:

```yaml
# === GEOWORLD SPAWN-INTEGRATION ===
# Diese Tiere werden vom GeoWorld-Plugin via MythicMobs API gespawnt.
# Trigger: ChunkPopulateEvent in RiverFlowTask oder separatem WildlifeSpawnTask
#
# Java-Aufruf im GeoWorld-Plugin:
#   MythicMobs api = MythicMobs.inst();
#   api.getMobManager()
#      .spawnMob("GiantMoose", loc, 1);
#
# Spawn-Logik im Plugin:
# - Pro neuem Chunk: 1-3 Tiere spawn-versuche (random aus Biom-Tabelle)
# - Nur tagsüber spawnen (oder via onSpawn Condition)
# - Max 5 Wildlife-Mobs pro Chunk (via MythicMobs MaxMobsPerChunk)
# - Spawn-Chance pro Tier: 0.15-0.30 je nach Seltenheit
```

=== AUSGABE ===

Gib alle 7 YML-Dateien vollständig aus.
Jede Datei beginnt mit einem Kommentar-Header:

```yaml
# GeoWorld Wildlife – <Kategorie>
# Ablageort: plugins/MythicMobs/Mobs/wildlife/geoworld_wildlife_<kategorie>.yml
# Alle Mobs sind passiv oder semi-aggressiv (keine Boss-Mobs)
```

Hinweise:
- Tiere sollen sich natürlich anfühlen, nicht wie Kampfgegner
- Deutsche Anzeigenamen bevorzugen (Display: '&fReh')
- ResourcePack-CustomModelData wird später separat zugewiesen;
  in Phase 1 sehen die Tiere wie ihre Vanilla-Basis aus
- Alle MythicMobs v5 Syntax verwenden
```
