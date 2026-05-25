# Jules-Prompt: MythicMobs YML-Dateien für alle 50 GeoWorld-Biome

Dieser Prompt ist unabhängig von den Worldgen-Prompts und kann parallel
ausgeührt werden. Die Mob-YMLs werden im Ordner `plugins/MythicMobs/Mobs/`
des Minecraft-Servers abgelegt.

Die vollständige Biom- und Mob-Spezifikation befindet sich in
`docs/CUSTOM_BIOMES.md` im selben Repository.

---

```
Lies die Datei `docs/CUSTOM_BIOMES.md` aus diesem Repository vollständig.
Sie enthält alle 50 GeoWorld-Biome mit ihren MythicMobs-IDs und
kurzen Verhaltensbeschreibungen.

Erstelle für jeden Mob eine vollständige MythicMobs-YML-Datei.

=== DATEISTRUKTUR ===

Eine YML-Datei pro Biom-Kategorie:

plugins/MythicMobs/Mobs/
├── geoworld_ocean.yml          // Ozeane & Tiefwasser (Biome 1–3, 37–39)
├── geoworld_mountains.yml      // Gebirge & Hochlagen (Biome 4–6)
├── geoworld_forests.yml        // Wälder & Ebenen (Biome 7–10, 22–26)
├── geoworld_ice.yml            // Eis & Kälte (Biome 11–12, 48)
├── geoworld_exotic.yml         // Exotisch & Einzigartig (Biome 13–15)
├── geoworld_swamps.yml         // Sümpfe & Feuchtgebiete (Biome 16–18)
├── geoworld_deserts.yml        // Wüsten & Halbwüsten (Biome 19–21)
├── geoworld_caves.yml          // Höhlen & Untergrund (Biome 27–30, 50)
├── geoworld_nether.yml         // Nether (Biome 31–33)
├── geoworld_end.yml            // The End (Biome 34–36)
├── geoworld_mystic.yml         // Mystisch & Märchenhaft (Biome 40–47, 49)
└── geoworld_waterfall.yml      // Wasserfallhöhlen-Mobs (CaveHermit, WaterfallSpirit, MossGolem)

=== PFLICHTFELDER PRO MOB ===

Jeder Mob-Eintrag folgt diesem MythicMobs-YML-Schema:

```yaml
MobID:
  Type: <VANILLA_ENTITY_TYPE>
  Display: '<Anzeigename mit Farbe, z.B. "&6Mob-Name">'
  Health: <float>
  Damage: <float>
  Armor: <float>
  Options:
    MovementSpeed: <float>
    Despawn: false
    PreventOtherDrops: true
    PreventItemPickup: true
    Silent: <bool>
  Faction: <Fraktionsname>
  Skills:
    - <Skill-Liste>
  Drops:
    - <Drop-Liste>
  LevelModifiers:
    Health: 0.5
    Damage: 0.25
```

=== KATEGORIEN & VERHALTEN ===

Leite alle Werte aus den Beschreibungen in CUSTOM_BIOMES.md ab.
Nutze folgende Regeln für die Ableitung:

**Entity-Typ-Mapping (Basis):**
- Geister/Phantome/Wraith → PHANTOM oder VEX
- Golems/Steinwesen → IRON_GOLEM oder RAVAGER
- Drachen/Wyrm → ENDER_DRAGON (Boss) oder PHANTOM (klein)
- Wölfe/Hunde/Hunde-artig → WOLF
- Wasserkreaturen → DROWNED oder GUARDIAN
- Pilz-Wesen → CREEPER (schleichend) oder RAVAGER
- Hexen/Magier → WITCH oder EVOKER
- Skelette/Untote → SKELETON oder ZOMBIE
- Spinnen/Spinnenartig → CAVE_SPIDER oder SPIDER
- Krabben/Panzer → SHULKER
- Haie/aggressive Fische → ELDER_GUARDIAN
- Piranhas/kleine Fische → PUFFERFISH (kein Schaden-Mob) → GUARDIAN
- Viecher im Boden/Bohren → SILVERFISH oder ENDERMITE
- Riesen/Boss → IRON_GOLEM (groß) oder ELDER_GUARDIAN
- Flügeltüre/Vögel → PHANTOM oder ALLAY
- Konstrukte/Holzwesen → IRON_GOLEM
- Djinn/Geister → BLAZE oder VEX
- Statuen/Schlafend → IRON_GOLEM mit Silent: true

**Gesundheits-Skala:**
- Passiv/NPC: 20–40 HP
- Normal (Gegner): 40–120 HP
- Elite: 150–400 HP
- Miniboss: 500–1500 HP
- Boss: 2000–10000 HP
- Weltenboss: 20000–50000 HP

**Schaden-Skala:**
- Passiv: 0 (kein Angriff) oder 1–2 (Kontakt)
- Normal: 3–7
- Elite: 8–15
- Boss: 16–30

**Bewegungsgeschwindigkeit:**
- Langsam (Golem, Panzer): 0.15–0.20
- Normal: 0.25–0.30
- Schnell (Ninja, Stalker): 0.35–0.45
- Sehr schnell (Wraith, Phantom): 0.50–0.60

=== SKILLS ===

Jeder Mob erhält mindestens 2 thematische Skills basierend auf seiner Beschreibung.
Nutze MythicMobs v5 Skill-Syntax:

```yaml
Skills:
  # Skill auf Timer (alle X Ticks)
  - mechanic{...} @Target ~onTimer:100
  # Skill bei Schaden erhalten
  - mechanic{...} @Self ~onDamaged
  # Skill bei Tod
  - mechanic{...} @World ~onDeath
  # Skill beim Angriff
  - mechanic{...} @Target ~onAttack
```

**Häufige Mechaniken nach Beschreibung:**

"Vergiftet / Gift-Debuff":
  - potion{type=POISON;duration=100;amplifier=1} @Target ~onAttack

"Slow / Einfrieren":
  - potion{type=SLOWNESS;duration=80;amplifier=2} @Target ~onAttack

"Teleportiert sich":
  - teleport{} @Self ~onDamaged{healthPercent<50}

"Unsichtbar / Tarnt sich":
  - potion{type=INVISIBILITY;duration=200;amplifier=0} @Self ~onSpawn
  - potion{type=INVISIBILITY;duration=200;amplifier=0} @Self ~onTimer:200

"Heilt sich durch X":
  - heal{amount=5} @Self ~onTimer:60

"Knockback-immun":
  - setvar{var=caster.noKnockback;value=true} @Self ~onSpawn

"AoE-Schaden":
  - damage{amount=8;radius=5} @PIR{r=5} ~onTimer:120

"Beschwört Mobs":
  - summon{type=ZOMBIE;amount=2;radius=3} @Self ~onDamaged{healthPercent<30}

"Sturzflug-Angriff" (Vogel/Drache):
  - projectile{type=ARROW;v=2.5;onTick=[potion{type=SLOWNESS;d=40} @Target]} @Target ~onTimer:80

"Stiehlt XP":
  - setvar{var=target.xp;value=-50} @Target ~onAttack

"Wirft Blöcke/Brocken":
  - projectile{type=SNOWBALL;v=1.5;damage=8} @Target ~onTimer:100

"Lava-Pfade hinterlassen":
  - blockwave{material=MAGMA_BLOCK;radius=2;duration=100} @Self ~onTimer:60

"Nur nachts aktiv":
  - message{m='<&7>Der Mob verschwindet im Tageslicht...'} @Self ~onTimer:20
  - remove{} @Self onConditions[isDay=true] ~onTimer:20

"Rudel-Mob (greift in Gruppen an)":
  - summon{type=self;amount=2;radius=10} @Self ~onFirstSpawn

"Spiegelt Schaden":
  - damage{amount=3} @Attacker ~onDamaged

"Belebt sich wieder":
  - heal{amount=full} @Self ~onDeath
  - message{m='<&c>Er ersteht wieder!'} @World ~onDeath
  (Hinweis: einmalig per Variable tracken)

=== DROPS ===

Jeder Mob lässt passende Items fallen. Nutze MythicMobs Drop-Syntax:

```yaml
Drops:
  - ITEM_NAME MENGE DROP-CHANCE
  # Beispiele:
  - BONE 1-3 0.8
  - DIAMOND 1 0.05
  - GOLD_NUGGET 2-5 0.6
```

**Drop-Kategorien aus Biom-Typ:**

OZEAN-Mobs: PRISMARINE_SHARD, PRISMARINE_CRYSTALS, COD, TROPICAL_FISH, NAUTILUS_SHELL
GEBIRGS-Mobs: AMETHYST_SHARD, CALCITE (kein Item, nutze STONE), IRON_INGOT, COAL
WALD-Mobs: OAK_LOG, MOSS_BLOCK, BONE, ROTTEN_FLESH, FEATHER
EIS-Mobs: PACKED_ICE, BLUE_ICE, SNOWBALL, LEATHER
SÜMPF-Mobs: SLIMEBALL, CLAY_BALL, SPIDER_EYE, FERMENTED_SPIDER_EYE
WÜSTEN-Mobs: SAND, SANDSTONE, BONE, GOLD_NUGGET
HÖHLEN-Mobs: STONE, IRON_INGOT, COAL, GLOW_INK_SAC
NETHER-Mobs: BLAZE_ROD, NETHER_BRICK, GOLD_INGOT, GHAST_TEAR
END-Mobs: ENDER_PEARL, CHORUS_FRUIT, PURPUR_BLOCK
MYSTISCH-Mobs: AMETHYST_SHARD, ECHO_SHARD, GLOWSTONE_DUST, EXPERIENCE_BOTTLE
BOSS-Mobs: Seltenes Item (5%) + gutes Item (30%) + normales Item (80%)

=== VOLLSTÄNDIGE MOB-LISTE ===

Alle 103 MythicMobs-IDs aus CUSTOM_BIOMES.md:

OZEAN & TIEFWASSER:
  AbyssalHunter, DeepOneSorcerer, LuminousJellyfish, CoralGuardian,
  IslandPirate, SandCrab, DrownedKnight, TideCaller,
  PearlOyster, LaguneShark, VolcanoSpirit, LavaGull

GEBIRGE & HOCHLAGEN:
  CrystalGolem, ShardWyvern, CloudEagle, PlateauWarden,
  LavaStalker, ObsidianColossus

WÄLDER & EBENEN:
  AncientTreeSpirit, ForestTroll, CinderHound, AshWalker,
  SaltGolem, MiragePhantom, SporeSpreader, FungalBehemoth,
  PineNymph, ShadowStalker, GroveSpecter, WickerConstruct,
  GoldenFox, HarvestGolem, PandaMonk, BambooNinja,
  CanopyMoth, TwilightDruid

EIS & KÄLTE:
  GlacialWraith, IceTitan, AuroraShaMan, TundraWolf,
  FrozenMariner, IceWhale

EXOTISCH & EINZIGARTIG:
  SkyDrake, IslePhoenix, StoneGolem, PetrifiedWarden,
  VineStrangler, ChasmSerpent

SÜMPFE & FEUCHTGEBIETE:
  BayouWitch, VoodooSkeleton, BogCrocodile, GlowFrog,
  WillOWisp, MoorBanshee

WÜSTEN & HALBWÜSTEN:
  DuneScorpion, SandWraith, CanyonHawk, RedSandElemental,
  AshPhoenix, MagmaWorm

HÖHLEN & UNTERGRUND:
  CrystalMimic, GeodeGuardian, MoltenElemental, InfernoDrake,
  EchoBat, ChamberColossus, GlowMushroomCrab, SporeQueen,
  CinderSprite, LavaFallTitan

NETHER:
  NetherBloom, WarpedPiglin, SoulLibrarian, ArchivistWreath,
  BasaltBrute, LavaDjinn

THE END:
  EndCrawler, ChorusReaper, VoidSentinel, OblivionBoss,
  SpireGargoyle, EndKnight

MYSTISCH & MÄRCHENHAFT:
  StarFragment, CelestialGuardian, DreamWeaver, Nightmare,
  RuneGuardian, AncientSeer, EclipsePhantom, SolarWraith,
  OasisDjinn, MirageHydra, MoonElemental, LunarWolf,
  ThunderCallerWyvern, StormGolem, SporeVine, JungleStalker,
  RuinsCurator, ColossalStatue

WASSERFALLHÖHLEN:
  CaveHermit, WaterfallSpirit, MossGolem

=== BOSS-MOBS (besondere Behandlung) ===

Folgende Mobs sind als Bosse zu definieren (zusätzliche BossBar):

```yaml
ObsidianColossus:
  # ... (wie oben)
  BossBar:
    Enabled: true
    Title: '<&4>Obsidian-Koloss'
    Range: 64
    Color: RED
    Style: SEGMENTED_10
```

Boss-Mobs (BossBar erforderlich):
  ObsidianColossus, InfernoDrake, IceTitan, FungalBehemoth,
  ChamberColossus, SporeQueen, LavaFallTitan, OblivionBoss,
  MirageHydra, SolarWraith, ShardWyvern, TideCaller,
  DeepOneSorcerer, PlateauWarden, IslePhoenix

=== SPAWNREGELN (RandomSpawning) ===

Füge am Ende jeder YML-Datei einen RandomSpawning-Block ein:

```yaml
# RandomSpawning wird vom GeoWorld-Plugin übersteuert.
# Diese Einträge dienen als Fallback für Vanilla-Biom-Spawns.

AbyssalHunter:
  # ... Mob-Definition ...

RandomSpawning:
  AbyssalHunter:
    Enabled: false   # GeoWorld Plugin steuert den Spawn
    Worlds: world
    Biomes: DEEP_OCEAN
    Chance: 0.05
    MaxMobsPerChunk: 2
```

=== AUSGABE ===

Gib alle 12 YML-Dateien vollständig aus.
Jede Datei beginnt mit einem Kommentar-Header:

```yaml
# GeoWorld MythicMobs – <Kategorie>
# Generiert aus docs/CUSTOM_BIOMES.md
# Ablageort: plugins/MythicMobs/Mobs/geoworld_<kategorie>.yml
```

Alle Mob-IDs exakt so schreiben wie in CUSTOM_BIOMES.md angegeben
(CamelCase, keine Leerzeichen, kein Namespace).
```
