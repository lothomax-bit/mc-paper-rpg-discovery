# Jules-Prompt: Worldgen Datapack – 2000 Blöcke, Riesige Biome, Große Ozeane

Dieser Prompt ist für Google Jules optimiert. Einfach 1:1 einfügen.

---

```
Erstelle ein vollständiges Minecraft Datapack für Version 26.1 (Pack-Format 48)
mit dem Namen "GeoWorld". Das Datapack soll die Overworld-Weltgenerierung vollständig
überschreiben mit folgenden Zielen:

1. Terrain bis Y +2000 (min_y: -64, height: 2064)
2. Massiv große Biome – ein Spieler soll mehrere echte Minuten laufen müssen,
   um von einem Biom ins nächste zu kommen
3. Große, tiefe Ozeane als natürliche Kontinentgrenzen
4. Epische Bergketten mit Peaks bis ca. Y 1200–1800
5. Alle Vanilla-Biom-Keys bleiben erhalten (keine Custom-Biome)

=== DATEISTRUKTUR ===

Erstelle folgende Dateien:

geoworld-datapack/
├── pack.mcmeta
└── data/
    └── minecraft/
        └── worldgen/
            ├── noise_settings/
            │   └── overworld.json
            ├── noise/
            │   ├── temperature.json
            │   ├── vegetation.json
            │   ├── continentalness.json
            │   └── erosion.json
            └── density_function/
                └── overworld/
                    ├── offset.json
                    ├── factor.json
                    ├── jaggedness.json
                    └── depth.json

=== PACK.MCMETA ===

{
  "pack": {
    "pack_format": 48,
    "description": "GeoWorld – Epic terrain up to Y 2000 with massive biomes"
  }
}

=== NOISE_SETTINGS/OVERWORLD.JSON ===

Überschreibe minecraft:overworld noise settings mit:
- min_y: -64
- height: 2064  (ergibt Bauhöhe bis Y 2000; min_y + height = 2000, darf 2032 nicht überschreiten)
- size_horizontal: 1
- size_vertical: 2
- default_block: minecraft:stone
- default_fluid: minecraft:water (level: "0")
- noise_router: Verweise auf die density_function Dateien aus diesem Datapack
- Übernimm alle anderen Felder (aquifer, ore veins, surface rules etc.) aus
  dem Vanilla amplified.json als Basis, aber skaliere den vertikalen Spline
  im final_density so, dass Peaks bis Y ~1600 möglich sind

=== NOISE/TEMPERATURE.JSON, VEGETATION.JSON, CONTINENTALNESS.JSON, EROSION.JSON ===

Für ALLE vier Noise-Dateien:
- Basis: Vanilla large_biomes Variante der jeweiligen Datei
- Ändere firstOctave auf -12 (statt Standard -7, statt Large Biomes -9)
  Das macht Biome ca. 5–10x größer als Large Biomes
- Amplituden (amplitudes-Array) entsprechend anpassen:
  Verwende 12 Einträge beginnend bei firstOctave -12, abnehmende Werte
  Beispiel für temperature: [1.5, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]

Für CONTINENTALNESS.JSON zusätzlich:
- Der Ozean-Bereich soll breiter sein: negative continentalness Werte
  sollen über größere Flächen auftreten
- firstOctave: -11 (etwas kleiner als die anderen für mehr Ozean-Variation)
- Amplitudes so wählen, dass tiefer Ozean (< -0.4) häufiger vorkommt

=== DENSITY_FUNCTION/OVERWORLD/OFFSET.JSON ===

Definiere einen Cubic-Spline der den vertikalen Terrain-Bias setzt:
- Bei continentalness = -1.0 (tiefer Ozean): Offset so dass Boden bei Y ~-30
- Bei continentalness = 0.0 (Küste): Offset so dass Terrain bei Y ~60
- Bei continentalness = 0.5 (Inland, flach): Offset so dass Terrain bei Y ~100
- Bei continentalness = 1.0 (Gebirge): Offset so dass Peaks bis Y ~1600 möglich
- Verwende "type": "minecraft:spline" mit dem erosion und continentalness
  als Eingabedimensionen wie im Vanilla amplified.json, aber mit
  deutlich höheren Y-Werten im Spline

=== DENSITY_FUNCTION/OVERWORLD/FACTOR.JSON ===

Terrain-Kompression/Streckung:
- Basis: Vanilla amplified factor.json
- Erhöhe die Terrain-Amplifikation (factor) für Gebirgsbereiche
  so dass die vertikale Ausdehnung mit dem erhöhten Offset Schritt hält
- Flache Biome (Ebenen, Ozeanböden) sollen weiterhin flach bleiben

=== DENSITY_FUNCTION/OVERWORLD/JAGGEDNESS.JSON ===

Bergspitzen-Schärfe:
- Basis: Vanilla amplified jaggedness.json
- Erhöhe den Jaggedness-Wert für Peak-Biome (JAGGED_PEAKS, STONY_PEAKS)
  sodass die Berge wirklich zackig und dramatisch aussehen
- Normale Biome: jaggedness ~0.0 bis 0.3
- Peak-Biome: jaggedness ~0.8 bis 1.2

=== DENSITY_FUNCTION/OVERWORLD/DEPTH.JSON ===

- Berechnet depth = offset + factor * y_gradient
- y_gradient: minecraft:y_clamped_gradient von (min_y + 1.5 * size_vertical * 4)
  bis (max_y - 1.5 * size_vertical * 4), Wert 2.0 bis -2.0
- Kombiniere offset.json und factor.json entsprechend

=== WICHTIGE HINWEISE ===

- Alle Noise-Funktionen sollen deterministisch sein (kein random seed in den Dateien)
- Vanilla-Strukturen (Dörfer, Festungen etc.) sollen weiter spawnen
- Vanilla-Biom-Keys bleiben EXAKT erhalten – nur Größe und Höhe ändern sich
- Das Datapack überschreibt NUR die minecraft:overworld Dimension
- Nether und End bleiben unverändert

Gib alle Dateien vollständig und korrekt aus, fertig zum Einlegen in den
datapacks-Ordner des Minecraft-Servers.
```
