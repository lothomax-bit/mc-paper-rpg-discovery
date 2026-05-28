# Jules-Prompt: Vanilla Density-Functions für GeoWorld-Datapack

Dieser Prompt behebt den Startfehler:  
`Unbound values in registry minecraft:worldgen/density_function: [minecraft:overworld/offset]`

---

```
Das GeoWorld-Datapack überschreibt `data/minecraft/worldgen/noise_settings/overworld.json`
und referenziert dabei 16 externe Density-Functions unter `minecraft:overworld/...`.
Diese Dateien müssen als eigenständige JSON-Dateien im Datapack vorhanden sein, weil
Minecraft ab Version 26.1.2 bei überschriebenen Noise-Settings alle Abhängigkeiten
vollständig im Datapack erwartet.

=== AUFGABE ===

Erstelle folgende 16 Dateien im Datapack unter dem Pfad:
  geoworld-datapack/data/minecraft/worldgen/density_function/overworld/

Alle Inhalte sind 1:1 die Vanilla-Standardwerte (misode/mcmeta, refs/heads/data).

=== DATEI 1: continents.json ===

{
  "type": "minecraft:flat_cache",
  "argument": {
    "type": "minecraft:shifted_noise",
    "noise": "minecraft:continentalness",
    "shift_x": "minecraft:shift_x",
    "shift_y": 0.0,
    "shift_z": "minecraft:shift_z",
    "xz_scale": 0.25,
    "y_scale": 0.0
  }
}

=== DATEI 2: depth.json ===

{
  "type": "minecraft:add",
  "argument1": {
    "type": "minecraft:y_clamped_gradient",
    "from_value": 1.5,
    "from_y": -64,
    "to_value": -1.5,
    "to_y": 320
  },
  "argument2": "minecraft:overworld/offset"
}

=== DATEI 3: erosion.json ===

{
  "type": "minecraft:flat_cache",
  "argument": {
    "type": "minecraft:shifted_noise",
    "noise": "minecraft:erosion",
    "shift_x": "minecraft:shift_x",
    "shift_y": 0.0,
    "shift_z": "minecraft:shift_z",
    "xz_scale": 0.25,
    "y_scale": 0.0
  }
}

=== DATEI 4: ridges.json ===

{
  "type": "minecraft:flat_cache",
  "argument": {
    "type": "minecraft:shifted_noise",
    "noise": "minecraft:ridge",
    "shift_x": "minecraft:shift_x",
    "shift_y": 0.0,
    "shift_z": "minecraft:shift_z",
    "xz_scale": 0.25,
    "y_scale": 0.0
  }
}

=== DATEI 5: ridges_folded.json ===

{
  "type": "minecraft:mul",
  "argument1": -3.0,
  "argument2": {
    "type": "minecraft:add",
    "argument1": -0.3333333333333333,
    "argument2": {
      "type": "minecraft:abs",
      "argument": {
        "type": "minecraft:add",
        "argument1": -0.6666666666666666,
        "argument2": {
          "type": "minecraft:abs",
          "argument": "minecraft:overworld/ridges"
        }
      }
    }
  }
}

=== DATEI 6: base_3d_noise.json ===

{
  "type": "minecraft:interpolated",
  "argument": {
    "type": "minecraft:noise",
    "noise": "minecraft:gravel",
    "xz_scale": 0.9,
    "y_scale": 0.9
  }
}

=== DATEI 7: sloped_cheese.json ===

{
  "type": "minecraft:add",
  "argument1": {
    "type": "minecraft:mul",
    "argument1": 4.0,
    "argument2": {
      "type": "minecraft:quarter_negative",
      "argument": {
        "type": "minecraft:mul",
        "argument1": {
          "type": "minecraft:add",
          "argument1": "minecraft:overworld/depth",
          "argument2": {
            "type": "minecraft:flat_cache",
            "argument": {
              "type": "minecraft:mul",
              "argument1": "minecraft:overworld/jaggedness",
              "argument2": {
                "type": "minecraft:half_negative",
                "argument": {
                  "type": "minecraft:noise",
                  "noise": "minecraft:jagged",
                  "xz_scale": 1500.0,
                  "y_scale": 0.0
                }
              }
            }
          }
        },
        "argument2": "minecraft:overworld/factor"
      }
    }
  },
  "argument2": "minecraft:overworld/base_3d_noise"
}

=== DATEI 8: caves/entrances.json ===

(Inhalt: Vanilla caves/entrances density function –
 Quelle: https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/caves/entrances.json)

Lade den Inhalt direkt von obiger URL herunter und speichere ihn 1:1.

=== DATEI 9: caves/noodle.json ===

(Quelle: https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/caves/noodle.json)

=== DATEI 10: caves/pillars.json ===

(Quelle: https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/caves/pillars.json)

=== DATEI 11: caves/spaghetti_2d.json ===

(Quelle: https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/caves/spaghetti_2d.json)

=== DATEI 12: caves/spaghetti_2d_thickness_modulator.json ===

{
  "type": "minecraft:cache_once",
  "argument": {
    "type": "minecraft:noise",
    "noise": "minecraft:spaghetti_2d_thickness",
    "xz_scale": 2.0,
    "y_scale": 1.0
  }
}

=== DATEI 13: caves/spaghetti_roughness_function.json ===

{
  "type": "minecraft:cache_once",
  "argument": {
    "type": "minecraft:mul",
    "argument1": {
      "type": "minecraft:add",
      "argument1": -0.05,
      "argument2": {
        "type": "minecraft:mul",
        "argument1": -0.05,
        "argument2": {
          "type": "minecraft:noise",
          "noise": "minecraft:spaghetti_roughness_modulator",
          "xz_scale": 1.0,
          "y_scale": 1.0
        }
      }
    },
    "argument2": {
      "type": "minecraft:add",
      "argument1": -0.4,
      "argument2": {
        "type": "minecraft:abs",
        "argument": {
          "type": "minecraft:noise",
          "noise": "minecraft:spaghetti_roughness",
          "xz_scale": 1.0,
          "y_scale": 1.0
        }
      }
    }
  }
}

=== DATEIEN 14–16: offset.json, factor.json, jaggedness.json ===

Diese drei Dateien sind sehr groß (60 KB / 34 KB / 12 KB) und enthalten
komplexe verschachtelte Spline-Objekte.

Lade sie direkt von folgenden URLs herunter und speichere sie 1:1:

- offset.json:
  https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/offset.json

- factor.json:
  https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/factor.json

- jaggedness.json:
  https://raw.githubusercontent.com/misode/mcmeta/refs/heads/data/data/minecraft/worldgen/density_function/overworld/jaggedness.json

=== RESULTIERENDE VERZEICHNISSTRUKTUR ===

geoworld-datapack/
└── data/
    └── minecraft/
        └── worldgen/
            ├── density_function/
            │   └── overworld/
            │       ├── base_3d_noise.json
            │       ├── continents.json
            │       ├── depth.json
            │       ├── erosion.json
            │       ├── factor.json
            │       ├── jaggedness.json
            │       ├── offset.json
            │       ├── ridges.json
            │       ├── ridges_folded.json
            │       ├── sloped_cheese.json
            │       └── caves/
            │           ├── entrances.json
            │           ├── noodle.json
            │           ├── pillars.json
            │           ├── spaghetti_2d.json
            │           ├── spaghetti_2d_thickness_modulator.json
            │           └── spaghetti_roughness_function.json
            └── noise_settings/
                └── overworld.json  ← bereits vorhanden, nicht anfassen

=== VALIDIERUNG ===

Nach Erstellung aller Dateien:
1. Prüfe, ob jede JSON-Datei valides JSON ist (kein Syntaxfehler)
2. Prüfe, ob alle `"minecraft:overworld/..."` Referenzen in overworld.json
   durch eine der 16 erstellten Dateien abgedeckt sind
3. Die Datei `data/minecraft/worldgen/noise_settings/overworld.json`
   darf NICHT verändert werden

=== HINTERGRUND ===

Minecraft 26.1.2 (Paper API 1.21.4) lädt Density-Functions lazy aus der
Registry. Wenn noise_settings/overworld.json per Datapack überschrieben wird,
erwartet die Registry alle darin enthaltenen Density-Function-Referenzen
entweder als Vanilla-Built-in ODER als Datei im Datapack. Da das GeoWorld-
Datapack den noise_router der overworld überschreibt, muss es alle 16
referenzierten Density-Functions als eigene Dateien mitliefern.

Ohne diese Dateien: `IllegalStateException: Unbound values in registry`
Mit diesen Dateien: Server startet ohne Registry-Fehler
```
