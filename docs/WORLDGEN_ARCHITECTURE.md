# GeoDiscovery – Worldgen Datapack Architektur

## Ziel

Ein vollständiges eigenes Datapack das:
- Terrain bis **Y +2000** generiert (min_y: -64, height: 2064)
- **Massiv große Biome** – mehrere Minuten Laufzeit pro Biom
- **Große, tiefe Ozeane** als natürliche Grenzen zwischen Kontinenten
- Epische Bergketten bis ~Y 1800
- Nahtlose Integration mit GeoDiscovery (Biome & Strukturen werden erkannt)

Minecraft-Version: **26.1** (neues Versionsnummernsystem seit Dez 2025)
Pack Format: `48` (für 26.1)

---

## Wie große Biome funktionieren

Die Biomgröße wird über den `firstOctave`-Parameter im `biome_source` gesteuert.

```
Vanilla Default:      firstOctave ~ -7  → kleine Biome (~200–500 Blöcke)
Vanilla Large Biomes: firstOctave ~ -9  → mittlere Biome (~1000 Blöcke)
Unser Ziel:           firstOctave ~ -12 → riesige Biome (5000–10000 Blöcke)
```

Die vier Noise-Parameter die angepasst werden müssen:
- `temperature` – welches Biom-Klima
- `vegetation` (humidity) – Trocken vs. Feucht
- `continentalness` – Inland vs. Küste vs. Ozean
- `erosion` – Flach vs. Bergig

**Für große Ozeane:** Der `continentalness`-Noise wird so konfiguriert, dass
der Ozean-Bereich breiter ausfällt (mehr negative continentalness-Werte
über größere Flächen).

---

## Dateistruktur des Datapacks

```
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
```

---

## Schlüsselparameter

### noise_settings/overworld.json
```json
"noise": {
  "min_y": -64,
  "height": 2064,
  "size_horizontal": 1,
  "size_vertical": 2
}
```
> min_y + height darf 2032 nicht überschreiten → -64 + 2064 = 2000 ✅

### Biom-Größe (noise/*.json)
Alle vier Noise-Dateien bekommen `firstOctave: -12` statt Standard `-7`.
Amplituden werden entsprechend angepasst um Kontrast zu erhalten.

### Berghöhe (density_function/overworld/offset.json)
Der Y-Offset-Spline wird so konfiguriert, dass Peaks bei ~Y 1200–1800 liegen
statt bei Vanilla ~Y 256.

### Große Ozeane
Im `continentalness`-Noise: `firstOctave: -11`, breiter Ozean-Bereich im
biome_source spawnTarget (continentalness zwischen -1.0 und -0.19 für Ozean,
statt Vanilla -1.0 bis -0.11).

---

## Integration mit GeoDiscovery

Da das Datapack die Vanilla-Biom-Keys beibehält, funktioniert GeoDiscovery
**ohne Änderungen**:
- Alle `enabled-biomes` in der config.yml werden weiterhin erkannt
- Struktur-Spawns funktionieren normal (Paper API)
- Spieler laufen jetzt mehrere Minuten durch ein Biom → mehr Entdeckungsmomente

---

## Performance-Hinweise

| Faktor | Auswirkung |
|---|---|
| height: 2064 | ~5x mehr Chunk-Daten, mind. 12 GB RAM für Server empfohlen |
| Große Biome | Weniger Biom-Wechsel-Berechnung, leicht besser für Performance |
| size_vertical: 2 | Nötig für realistisches 2000-Blöcke-Terrain |
| Neue Welt nötig | Bestehende Chunks behalten alte Höhe, keine Migration möglich |
