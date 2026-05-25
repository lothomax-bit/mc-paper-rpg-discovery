# GeoWorld – WorldGen-Plugin Architektur

Das WorldGen-Plugin (`GeoWorld`) ist ein eigenständiges Paper-Plugin das
nahtlos mit GeoDiscovery zusammenarbeitet. Es enthält mehrere Module.

---

## Modulübersicht

```
de.lothomax.geoworld
├── GeoWorld.java                        // Plugin-Hauptklasse
├── module
│   ├── river
│   │   ├── RiverFlowModule.java         // Modul-Einstieg, wird von GeoWorld geladen
│   │   ├── RiverFlowTask.java           // BukkitRunnable: verarbeitet Chunk-Queue
│   │   ├── RiverPathfinder.java         // Gradient-Descent Algorithmus
│   │   └── WaterfallPlacer.java         // Platziert Wasserfälle und Flussbetten
│   └── (weitere Module später: structures, lore, ...)
├── listener
│   └── ChunkPopulateListener.java       // Hört auf ChunkLoadEvent, füllt Queue
└── config
    └── GeoWorldConfig.java              // Lädt geoworld_config.yml
```

---

## Modul: River Flow

### Ablauf

```
ChunkLoadEvent (isNewChunk = true)
    └─► ChunkPopulateListener
            └─► Chunk in pendingChunks-Queue einreihen

RiverFlowTask (async BukkitScheduler, alle 10 Ticks)
    └─► Nimmt max. N Chunks aus Queue
            └─► Scan: alle Wasserblöcke im Chunk bei Y > min-spring-y
                    └─► Für jeden Quellblock: RiverPathfinder.trace()
                            ├─► Wasserfall bei Y-Sprung > waterfall-threshold
                            └─► Stopp bei Y ≤ sea-level
```

### RiverPathfinder – Gradient-Descent

Algorithmus läuft **asynchron** (kein Blockzugriff auf Main-Thread):

1. Starte bei Quellblock (x, y, z)
2. Prüfe 4 Nachbarblöcke (N, S, O, W) auf Bodenhöhe
3. Bewege zu niedrigstem Nachbarn
4. Wenn Y-Unterschied > `waterfall-threshold` (default: 4 Blöcke): Wasserfall
5. Wenn Boden = Wasser oder Y ≤ `sea-level` (default: 63): Stopp
6. Maximale Pfadlänge: `max-river-length` (default: 800 Blöcke)
7. Gesammelte Blöcke werden in einem Batch **synchron** auf Main-Thread platziert

### WaterfallPlacer

- Erkennt vertikale Klippen (Y-Sprung > Threshold)
- Platziert Wasser senkrecht nach unten bis Aufprallpunkt
- Erzeugt optional kleinen **Bergsee** (3–7 Blöcke Radius) am Fuß eines Wasserfalls
  wenn genügend flacher Boden vorhanden ist
- Flussbettbreite: 1–3 Blöcke je nach Höhe (oben schmal, unten breiter)

```
Y 1200 ─ Quellblock (1 Block breit)
Y  900 ─ Wasserfall 40 Blöcke senkrecht
Y  860 ─ Bergsee (Radius 4)
Y  600 ─ Wasserfall an Klippenwand
Y  200 ─ Flussbett (2–3 Blöcke breit)
Y   63 ─ Mündet in Ozean/See → Stopp
```

---

## GeoDiscovery-Integration

Wenn ein Fluss mündet oder ein Bergsee entsteht, wird optional ein
**GeoDiscovery-Hook** aufgerufen:

```java
// Prüfe ob GeoDiscovery geladen ist
Plugin geoDiscovery = Bukkit.getPluginManager().getPlugin("GeoDiscovery");
if (geoDiscovery != null && geoDiscovery.isEnabled()) {
    // Registriere Bergsee als unbenannte Region (region_type: WATER_FEATURE)
    // GeoDiscovery API Aufruf (wird später in GeoDiscovery als API implementiert)
}
```

Dafür muss GeoDiscovery später eine **simple Plugin-API** bekommen:
- `GeoDiscoveryAPI.registerRegion(Location, regionType, biomeKey)` → unbenannte Region anlegen
- Spieler die später durch den Bereich laufen werden zur Benennung aufgefordert

---

## geoworld_config.yml (River-Sektion)

```yaml
modules:
  river-flow:
    enabled: true
    min-spring-y: 200            # Mindest-Höhe für Quellblock-Scan
    sea-level: 63                # Höhe bei der der Fluss stoppt
    waterfall-threshold: 4       # Minimaler Y-Sprung für Wasserfall
    max-river-length: 800        # Max. Blöcke pro Fluss-Trace
    mountain-lake-chance: 0.35   # Wahrscheinlichkeit eines Bergsees am Wasserfallfuß
    mountain-lake-min-radius: 3
    mountain-lake-max-radius: 7
    river-width-high: 1          # Breite über Y 400
    river-width-mid: 2           # Breite Y 200–400
    river-width-low: 3           # Breite unter Y 200
    chunks-per-tick: 3           # Chunks pro Task-Tick (Performance-Tuning)
    geodiscovery-hook: true      # Bergseen als GeoDiscovery-Region registrieren
    geodiscovery-lake-icon: "💧"
```
