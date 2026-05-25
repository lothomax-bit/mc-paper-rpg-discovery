# Jules-Prompt: Custom Biome JSON-Dateien für alle 15 GeoWorld-Biome

Dieser Prompt baut auf dem Worldgen-Datapack-Prompt auf. Erst den Worldgen-Prompt ausführen, dann diesen.

---

```
Ergänze das bestehende GeoWorld-Datapack um 15 vollständige Custom-Biom-Definitionen.
Alle Biome nutzen den Namespace `geoworld`.

Ordner: data/geoworld/worldgen/biome/

Jede Biom-JSON muss folgende Felder enthalten:
- temperature (float)
- downfall (float, 0.0 = trocken, 1.0 = sehr feucht)
- has_precipitation (bool)
- effects: (fog_color, water_color, water_fog_color, sky_color, optional: foliage_color, grass_color)
- spawners: (monster, creature, ambient, water_creature, water_ambient)
- spawn_costs: {}
- carvers: {}
- features: (Verweise auf Vanilla placed_features die zum Biom passen)
- surface_rule: (eigene Oberfläche je nach Biom)

Die Biome und ihre Parameter:

=== 1. geoworld:abyssal_trench ===
temperature: 0.5, downfall: 0.0, has_precipitation: false
water_color: 0x050a1a (fast schwarz-blau)
fog_color: 0x030608
sky_color: 0x080c14
Oberfläche: basalt -> deepslate -> stone
Features: glow_lichen Cluster, lava_pool (selten), amethyst_geode (sehr selten)
Spawner: keine Standard-Spawner (für Custom-Mob-Plugin vorbereitet)

=== 2. geoworld:bioluminescent_shallows ===
temperature: 0.8, downfall: 0.4, has_precipitation: true
water_color: 0x00e5ff (leuchtendes Cyan)
fog_color: 0x00bcd4
sky_color: 0x77d7f5
Oberfläche: sand -> sandstone -> stone
Features: glow_lichen am Boden, seagrass (dicht), coral_tree, coral_fan, coral_mushroom
Spawner: water_ambient: tropical_fish (häufig), squid (selten)

=== 3. geoworld:coral_archipelago ===
temperature: 1.0, downfall: 0.8, has_precipitation: true
water_color: 0x3ab3da
fog_color: 0x050533
sky_color: 0x7ec8e3
Oberfläche: sand -> sandstone -> stone
Features: jungle_tree (groß, vereinzelt), coral_reef Features (dicht), seagrass
Spawner: creature: parrot (selten), water_creature: dolphin, tropical_fish

=== 4. geoworld:crystal_peaks ===
temperature: -0.5, downfall: 0.1, has_precipitation: true (Schnee)
water_color: 0x3f76e4
fog_color: 0xc0c0e8
sky_color: 0x9090c8
Oberfläche: amethyst_block -> calcite -> stone -> deepslate
Features: amethyst_geode (sehr häufig, groß), pointed_dripstone (vereinzelt als Kristalle)
Spawner: creature: goat (häufig)

=== 5. geoworld:skyreach_plateau ===
temperature: 0.0, downfall: 0.3, has_precipitation: true (Schnee auf Höhe)
water_color: 0x3f76e4
fog_color: 0xc6dff7
sky_color: 0xa8d0f0
Oberfläche: grass_block -> dirt -> stone mit Moos-Patches
Features: oak_tree (knorrig, vereinzelt), moss_patch, azalea (selten)
Spawner: creature: goat, sheep

=== 6. geoworld:obsidian_spires ===
temperature: 2.0, downfall: 0.0, has_precipitation: false
water_color: 0x3f76e4
fog_color: 0x4a0000
sky_color: 0x8a1010
Oberfläche: obsidian -> basalt -> blackstone
Features: basalt_pillar (häufig, hoch), lava_pool (häufig), fire (am Boden)
Spawner: monster: magma_cube (selten), blaze (sehr selten)

=== 7. geoworld:ancient_forest ===
temperature: 0.7, downfall: 0.9, has_precipitation: true
water_color: 0x2d6b2d
fog_color: 0x1a3d1a
sky_color: 0x4a7c4a
Oberfläche: podzol -> dirt -> stone
Features: mega_spruce_tree (sehr häufig, 2x2 Stämme), brown_mushroom, red_mushroom,
          fern (dicht), moss_carpet, hanging_roots
Spawner: monster: witch (selten), creature: wolf, fox

=== 8. geoworld:ember_plains ===
temperature: 2.0, downfall: 0.0, has_precipitation: false
water_color: 0xff6600
fog_color: 0x5c1a00
sky_color: 0xb03a00
Oberfläche: netherrack (patches in grass_block) -> dirt -> netherrack -> stone
Features: fire (gelegentlich), lava_pool, basalt_patch (selten)
Spawner: monster: blaze (selten), magma_cube

=== 9. geoworld:salt_flats ===
temperature: 2.0, downfall: 0.0, has_precipitation: false
water_color: 0xffffff
fog_color: 0xe8e0d0
sky_color: 0xf5e8c0
Oberfläche: calcite -> white_terracotta -> stone
Features: keine Vegetation, gelegentlich calcite_boulder
Spawner: creature: rabbit (selten)

=== 10. geoworld:fungal_expanse ===
temperature: 0.9, downfall: 1.0, has_precipitation: true
water_color: 0x5c2d7a
fog_color: 0x3d1a52
sky_color: 0x6b3d8a
Oberfläche: mycelium -> dirt -> stone
Features: huge_brown_mushroom (sehr häufig, riesig), huge_red_mushroom,
          brown_mushroom (Boden), red_mushroom (Boden), glow_lichen (unter
ground)
Spawner: creature: mooshroom (selten), monster: slime (Höhlen)

=== 11. geoworld:glacial_abyss ===
temperature: -1.0, downfall: 0.5, has_precipitation: true (Schnee)
water_color: 0x0a4060
fog_color: 0x0a2030
sky_color: 0x1a5070
Oberfläche: blue_ice -> packed_ice -> stone
Features: ice_spike (vereinzelt), packed_ice_boulder, frozen_river am Grund
Spawner: creature: polar_bear, stray (monster)

=== 12. geoworld:aurora_tundra ===
temperature: -0.8, downfall: 0.2, has_precipitation: true (Schnee)
water_color: 0x3f76e4
fog_color: 0x8ab4e8
sky_color: 0x2a6090  
Oberfläche: snow_block -> dirt -> stone mit vereinzelten Felsen
Features: snow_layer (dicht), boulder (selten), spruce_tree (sehr vereinzelt)
Spawner: creature: wolf (selten), polar_bear (selten)

=== 13. geoworld:floating_isles ===
temperature: 0.5, downfall: 0.5, has_precipitation: true
water_color: 0x3f76e4
fog_color: 0xc6e0f5
sky_color: 0x8ac8f0
Oberfläche: grass_block -> dirt -> stone mit Mangrove-Root-Unterhängern
Features: oak_tree (vereinzelt), azalea, hanging_roots (nach unten),
          waterfall (nach unten hängend)
Spawner: creature: sheep, chicken, eagle (Custom wenn Plugin vorhanden)

=== 14. geoworld:petrified_wasteland ===
temperature: 0.5, downfall: 0.0, has_precipitation: false
water_color: 0x808080
fog_color: 0x6a6a6a
sky_color: 0x909090
Oberfläche: calcite -> stone -> deepslate
Features: stone_pillar (häufig, simuliert versteinerte Bäume aus Stone+Calcite),
          calcite_boulder, keine Vegetation
Spawner: monster: skeleton (selten), silverfish (Höhlen, häufig)

=== 15. geoworld:verdant_chasms ===
temperature: 0.8, downfall: 1.0, has_precipitation: true
water_color: 0x00e676
fog_color: 0x1b5e20
sky_color: 0x4caf50
Oberfläche: moss_block -> dirt -> stone mit lush_cave Features auf Wandflächen
Features: lush_caves Features (azalea_tree, spore_blossom, glow_lichen, dripleaf),
          waterfall (häufig), hanging_roots, cave_vines
Spawner: creature: axolotl (häufig), tropical_fish, glow_squid

=== BIOME SOURCE INTEGRATION ===

Ergänze in der noise_settings/overworld.json im biome_source den spawn_target
für jedes Custom-Biom. Verwende folgende Multi-Noise-Parameter:

geoworld:abyssal_trench:
  continentalness: -1.0 bis -0.7, depth: 1.0, weirdness: beliebig
  (tiefstes Wasser, sehr weit von der Küste)

geoworld:bioluminescent_shallows:
  continentalness: -0.2 bis 0.0, temperature: 0.5 bis 1.0, depth: 0.0
  (flaches Wasser nahe Küste, warm)

geoworld:coral_archipelago:
  continentalness: -0.1 bis 0.1, temperature: 0.8 bis 1.0, depth: 0.0
  (Küstenlinie, sehr warm)

geoworld:crystal_peaks:
  continentalness: 0.6 bis 1.0, erosion: -1.0 bis -0.6, weirdness: 0.5 bis 1.0
  (Hochgebirge, wenig Erosion = schroffe Gipfel)

geoworld:skyreach_plateau:
  continentalness: 0.5 bis 0.8, erosion: -0.4 bis -0.2, temperature: -0.3 bis 0.3
  (Hochplateau, mittlere Höhe)

geoworld:obsidian_spires:
  continentalness: 0.4 bis 0.8, temperature: 1.5 bis 2.0, erosion: -0.8 bis -0.4
  (vulkanisches Gebirge, extrem heiß)

geoworld:ancient_forest:
  continentalness: 0.1 bis 0.5, humidity: 0.7 bis 1.0, temperature: 0.5 bis 0.9
  erosion: 0.0 bis 0.4 (feuchter Tieflandwald)

geoworld:ember_plains:
  continentalness: 0.1 bis 0.6, temperature: 1.8 bis 2.0, erosion: 0.2 bis 0.8
  (flache heiße Ebene)

geoworld:salt_flats:
  continentalness: 0.2 bis 0.7, temperature: 1.5 bis 2.0, humidity: -1.0 bis -0.6
  erosion: 0.5 bis 1.0 (flach, trocken, heiß)

geoworld:fungal_expanse:
  continentalness: 0.0 bis 0.4, humidity: 0.8 bis 1.0, temperature: 0.7 bis 1.0
  weirdness: -1.0 bis -0.5 (feuchte Niederung, hohe Weirdness-Abweichung)

geoworld:glacial_abyss:
  continentalness: -0.6 bis -0.2, temperature: -1.0 bis -0.7, depth: 0.5 bis 1.0
  (kaltes, tiefes Wasser)

geoworld:aurora_tundra:
  continentalness: 0.1 bis 0.6, temperature: -1.0 bis -0.6, erosion: 0.4 bis 1.0
  (flache Tundra, sehr kalt)

geoworld:floating_isles:
  continentalness: 0.3 bis 0.7, weirdness: 0.8 bis 1.0, erosion: -1.0 bis -0.7
  (extremste Weirdness = schwebend/isoliert)

geoworld:petrified_wasteland:
  continentalness: 0.2 bis 0.6, temperature: 0.3 bis 0.7, humidity: -0.8 bis -0.4
  weirdness: -0.8 bis -0.3 (trocken, mittlere Temperatur, negative Weirdness)

geoworld:verdant_chasms:
  continentalness: 0.1 bis 0.5, humidity: 0.8 bis 1.0, temperature: 0.6 bis 0.9
  depth: 0.8 bis 1.0 (tief unter der Erde / Schluchten)

Gib alle 15 Biom-JSON-Dateien vollständig aus.
```
