# Jules-Prompt: Custom Biome JSON-Dateien für alle 50 GeoWorld-Biome

Dieser Prompt baut auf dem Worldgen-Datapack-Prompt auf.
Erst `JULES_PROMPT_WORLDGEN_DATAPACK.md` ausführen, dann diesen.

Die vollständige Biom-Spezifikation befindet sich in `docs/CUSTOM_BIOMES.md`
im selben Repository. Jules soll diese Datei als alleinige Vorlage nutzen.

---

```
Lies die Datei `docs/CUSTOM_BIOMES.md` aus diesem Repository vollständig.
Sie enthält alle 50 GeoWorld-Biome mit Atmosphäre, Oberfläche, MythicMobs
und GeoDiscovery-Icons.

Ergänze das bestehende GeoWorld-Datapack um 50 vollständige Custom-Biom-JSON-Dateien.
Alle Biome nutzen den Namespace `geoworld`.

Ordner: data/geoworld/worldgen/biome/

=== PFLICHTFELDER PRO BIOM-JSON ===

Jede Biom-JSON muss folgende Felder enthalten:

{
  "temperature": <float>,
  "downfall": <float, 0.0 = trocken, 1.0 = sehr feucht>,
  "has_precipitation": <bool>,
  "effects": {
    "fog_color": <int>,
    "water_color": <int>,
    "water_fog_color": <int>,
    "sky_color": <int>,
    // optional:
    "foliage_color": <int>,
    "grass_color": <int>
  },
  "spawners": {
    "monster": [...],
    "creature": [...],
    "ambient": [...],
    "water_creature": [...],
    "water_ambient": [...]
  },
  "spawn_costs": {},
  "carvers": {},
  "features": [...]
}

Spawner-Einträge: Vanilla-Mobs aus der CUSTOM_BIOMES.md-Beschreibung ableiten.
MythicMobs-Spawns werden NICHT in der Biom-JSON eingetragen – diese werden
vom GeoWorld-Plugin (StructureMobSpawner) übernommen.

=== PARAMETER-ABLEITUNG AUS CUSTOM_BIOMES.MD ===

Leite alle JSON-Parameter aus den Beschreibungen in CUSTOM_BIOMES.md ab:

- "Atmosphäre" → fog_color, sky_color
- "Oberfläche" → surface_rule Materialien (Oberschicht, Tiefe)
- Feuchtigkeit aus Atmosphäre/Oberfläche → downfall
- Temperatur aus Atmosphäre → temperature (heiß ≥ 1.5, warm 0.7–1.4, gemäßigt 0.3–0.6, kalt ≤ 0.2, gefrierend ≤ -0.5)
- "has_precipitation: false" für alle Biome mit Beschreibung "kein Regen", "trocken" oder temperature > 1.0
- Vanilla-Spawns aus den genannten Tieren (z.B. "Papageienspawns" → creature: parrot)

=== MULTI-NOISE PARAMETER (BIOME SOURCE) ===

Ergänze in der noise_settings/overworld.json im biome_source den spawn_target
für jedes der 50 Biome. Nutze folgende Kategorien als Leitfaden:

OZEAN & TIEFWASSER (continentalness stark negativ):
  geoworld:abyssal_trench:            continentalness: -1.0 bis -0.7, depth: 1.0
  geoworld:bioluminescent_shallows:   continentalness: -0.2 bis 0.0,  temperature: 0.5–1.0, depth: 0.0
  geoworld:coral_archipelago:         continentalness: -0.1 bis 0.1,  temperature: 0.8–1.0, depth: 0.0
  geoworld:glacial_abyss:             continentalness: -0.6 bis -0.2, temperature: -1.0 bis -0.7, depth: 0.5–1.0
  geoworld:sunken_city:               continentalness: -0.5 bis -0.2, depth: 0.8–1.0
  geoworld:pearl_lagoon:              continentalness: -0.15 bis 0.05, temperature: 0.6–1.0, depth: 0.0
  geoworld:volcanic_island:           continentalness: -0.1 bis 0.15, temperature: 1.5–2.0, depth: 0.0
  geoworld:rime_coast:                continentalness: -0.2 bis 0.1,  temperature: -1.0 bis -0.5, depth: 0.0

GEBIRGE & HOCHLAGEN (continentalness positiv, erosion negativ):
  geoworld:crystal_peaks:             continentalness: 0.6–1.0,  erosion: -1.0 bis -0.6, weirdness: 0.5–1.0
  geoworld:skyreach_plateau:          continentalness: 0.5–0.8,  erosion: -0.4 bis -0.2, temperature: -0.3–0.3
  geoworld:obsidian_spires:           continentalness: 0.4–0.8,  temperature: 1.5–2.0,   erosion: -0.8 bis -0.4
  geoworld:floating_isles:            continentalness: 0.3–0.7,  weirdness: 0.8–1.0,     erosion: -1.0 bis -0.7
  geoworld:bamboo_highlands:          continentalness: 0.4–0.7,  humidity: 0.6–1.0,      erosion: -0.3 bis 0.1
  geoworld:rune_highlands:            continentalness: 0.4–0.7,  weirdness: -0.3 bis 0.3, erosion: -0.4 bis 0.0
  geoworld:thunder_steppe:            continentalness: 0.3–0.7,  weirdness: 0.5–1.0,     erosion: 0.2–0.6

WÄLDER & EBENEN (mittlere continentalness):
  geoworld:ancient_forest:            continentalness: 0.1–0.5,  humidity: 0.7–1.0,   temperature: 0.5–0.9, erosion: 0.0–0.4
  geoworld:ember_plains:              continentalness: 0.1–0.6,  temperature: 1.8–2.0, erosion: 0.2–0.8
  geoworld:salt_flats:                continentalness: 0.2–0.7,  temperature: 1.5–2.0, humidity: -1.0 bis -0.6, erosion: 0.5–1.0
  geoworld:fungal_expanse:            continentalness: 0.0–0.4,  humidity: 0.8–1.0,   temperature: 0.7–1.0, weirdness: -1.0 bis -0.5
  geoworld:aurora_tundra:             continentalness: 0.1–0.6,  temperature: -1.0 bis -0.6, erosion: 0.4–1.0
  geoworld:petrified_wasteland:       continentalness: 0.2–0.6,  temperature: 0.3–0.7, humidity: -0.8 bis -0.4, weirdness: -0.8 bis -0.3
  geoworld:whispering_pines:          continentalness: 0.1–0.5,  humidity: 0.4–0.7,   temperature: 0.2–0.6, erosion: 0.0–0.5
  geoworld:haunted_grove:             continentalness: 0.1–0.5,  humidity: 0.3–0.6,   weirdness: -0.6 bis -0.2
  geoworld:golden_woodland:           continentalness: 0.1–0.6,  temperature: 0.5–0.8, humidity: 0.2–0.5
  geoworld:twilight_canopy:           continentalness: 0.1–0.5,  humidity: 0.6–0.9,   weirdness: -0.4 bis 0.0
  geoworld:golden_woodland:           continentalness: 0.2–0.6,  temperature: 0.5–0.8, humidity: 0.2–0.5
  geoworld:ancient_ruins:             continentalness: 0.2–0.6,  temperature: 0.3–0.7, weirdness: -0.5 bis -0.1
  geoworld:spore_jungle:              continentalness: 0.1–0.5,  humidity: 0.8–1.0,   temperature: 0.8–1.2, weirdness: -0.3 bis 0.3

SÜMPFE & FEUCHTGEBIETE:
  geoworld:cursed_bayou:              continentalness: 0.0–0.3,  humidity: 0.8–1.0,   temperature: 0.6–1.0, weirdness: -0.8 bis -0.4
  geoworld:mangrove_labyrinth:        continentalness: -0.05 bis 0.2, humidity: 0.9–1.0, temperature: 0.7–1.0
  geoworld:peat_moors:                continentalness: 0.1–0.4,  humidity: 0.5–0.8,   temperature: 0.2–0.5, weirdness: -0.4 bis 0.0

WÜSTEN & HALBWÜSTEN:
  geoworld:dune_sea:                  continentalness: 0.2–0.8,  temperature: 1.5–2.0, humidity: -1.0 bis -0.7, erosion: 0.3–0.7
  geoworld:red_canyon:                continentalness: 0.3–0.7,  temperature: 1.2–1.8, humidity: -0.8 bis -0.4, erosion: -0.2 bis 0.3
  geoworld:volcanic_ashfields:        continentalness: 0.2–0.6,  temperature: 1.6–2.0, humidity: -1.0 bis -0.5, erosion: 0.1–0.5
  geoworld:mirage_oasis:              continentalness: 0.3–0.6,  temperature: 1.5–2.0, humidity: -0.8 bis -0.3, weirdness: 0.4–0.8
  geoworld:eclipse_valley:            continentalness: 0.2–0.6,  temperature: 0.3–0.7, weirdness: 0.6–1.0

MYSTISCH & MÄRCHENHAFT:
  geoworld:starfall_meadow:           continentalness: 0.1–0.5,  weirdness: 0.5–0.8,  temperature: 0.4–0.8
  geoworld:dream_forest:              continentalness: 0.1–0.5,  humidity: 0.5–0.9,   weirdness: 0.3–0.7
  geoworld:moonstone_basin:           continentalness: 0.2–0.5,  temperature: 0.3–0.6, weirdness: -0.2 bis 0.3

HÖHLEN & UNTERGRUND (depth: 0.5–1.0):
  geoworld:verdant_chasms:            continentalness: 0.1–0.5,  humidity: 0.8–1.0,   temperature: 0.6–0.9, depth: 0.8–1.0
  geoworld:crystal_caverns:           continentalness: 0.2–0.7,  depth: 0.6–1.0,     weirdness: 0.2–0.8
  geoworld:magma_depths:              continentalness: 0.1–0.7,  temperature: 1.8–2.0, depth: 0.7–1.0
  geoworld:echo_chambers:             continentalness: 0.1–0.6,  depth: 0.5–0.9
  geoworld:fungal_depths:             continentalness: 0.0–0.4,  humidity: 0.8–1.0,   depth: 0.6–1.0
  geoworld:lava_falls:                continentalness: 0.1–0.5,  temperature: 1.8–2.0, depth: 0.5–1.0

NETHER (dimension: minecraft:the_nether):
  geoworld:nether_garden:             Nether-Biom: warped/crimson Mix
  geoworld:soul_archive:              Nether-Biom: soul sand valley Variante
  geoworld:magma_delta_rim:           Nether-Biom: basalt deltas Variante

THE END (dimension: minecraft:the_end):
  geoworld:end_coral_reef:            End-Biom: outer islands
  geoworld:void_sanctum:              End-Biom: outer islands, weirdness extrem
  geoworld:elytra_spires:             End-Biom: outer islands, hoch

=== BESONDERE REGELN ===

1. Nether- und End-Biome bekommen eigene JSON-Ordner:
   - Nether: data/geoworld/worldgen/biome/nether/
   - End:    data/geoworld/worldgen/biome/end/
   Die noise_settings werden entsprechend für nether_biome_source / end_biome_source ergänzt.

2. Für Biome mit "kein Pflanzenwuchs", "keine Vegetation":
   features: [] (leere Liste)

3. Für Unterwasser-Biome (abyssal_trench, sunken_city, glacial_abyss etc.):
   water_fog_color identisch mit fog_color setzen.

4. Spawner-Einträge nur mit Vanilla-Mobs befüllen.
   Jeder Eintrag folgt dem Schema:
   {"type": "minecraft:<mob>", "weight": <int>, "minCount": <int>, "maxCount": <int>}

5. surface_rule pro Biom ableiten aus dem "Oberfläche"-Feld in CUSTOM_BIOMES.md,
   Schichtung von oben nach unten (erster Block = Oberfläche).

=== AUSGABE ===

Gib alle 50 Biom-JSON-Dateien vollständig aus, geordnet nach Kategorie:
1. Ozeane & Tiefwasser (8 Biome)
2. Gebirge & Hochlagen (7 Biome)
3. Wälder & Ebenen (9 Biome)
4. Sümpfe & Feuchtgebiete (3 Biome)
5. Wüsten & Halbwüsten (5 Biome)
6. Mystisch & Märchenhaft (3 Biome)
7. Höhlen & Untergrund (6 Biome)
8. Nether (3 Biome)
9. The End (3 Biome)
10. Eisbiome (3 Biome: glacial_abyss, aurora_tundra, rime_coast)

Sowie die ergänzte noise_settings/overworld.json mit allen 50 spawn_targets.
```
