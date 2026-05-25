# 🌍 Custom Biomes – GeoDiscovery Plugin

Diese Datei dokumentiert **35 zusätzliche Biome** (Vanilla Bukkit-API), die im GeoDiscovery-Plugin als Entdeckungsorte genutzt werden können, sowie **1 eigenes Custom-Biom** mit Datapack-Integration.  
Jedes Biom enthält den Bukkit-API-Namen, ein passendes Icon, eine kurze Beschreibung sowie einen empfohlenen Entdeckungs-Radius.

---

## 📋 Übersicht

| # | Biom-Name (API) | Icon | Kategorie | Radius |
|---|-----------------|------|-----------|--------|
| 1 | `PLAINS` | 🌾 | Ebene | 200 |
| 2 | `SUNFLOWER_PLAINS` | 🌻 | Ebene | 200 |
| 3 | `MEADOW` | 🌸 | Ebene | 180 |
| 4 | `FOREST` | 🌳 | Wald | 150 |
| 5 | `FLOWER_FOREST` | 🌺 | Wald | 150 |
| 6 | `BIRCH_FOREST` | 🌿 | Wald | 150 |
| 7 | `OLD_GROWTH_BIRCH_FOREST` | 🌲 | Wald | 160 |
| 8 | `TAIGA` | 🌲 | Taiga | 160 |
| 9 | `SNOWY_TAIGA` | ❄️ | Taiga | 160 |
| 10 | `GROVE` | 🌲 | Taiga | 140 |
| 11 | `CHERRY_GROVE` | 🌸 | Wald | 130 |
| 12 | `WINDSWEPT_FOREST` | 🌬️ | Bergwald | 150 |
| 13 | `SNOWY_PLAINS` | ❄️ | Schnee | 200 |
| 14 | `BEACH` | 🏖️ | Küste | 130 |
| 15 | `STONY_SHORE` | 🪨 | Küste | 120 |
| 16 | `RIVER` | 🏞️ | Wasser | 100 |
| 17 | `FROZEN_RIVER` | ❄️ | Wasser | 100 |
| 18 | `MUSHROOM_FIELDS` | 🍄 | Selten | 120 |
| 19 | `WOODED_BADLANDS` | 🌵 | Badlands | 170 |
| 20 | `DEEP_COLD_OCEAN` | 🌊 | Ozean | 250 |
| 21 | `DEEP_FROZEN_OCEAN` | 🧊 | Ozean | 250 |
| 22 | `THE_END` | 🌟 | The End | 300 |
| 23 | `THE_VOID` | 🕳️ | The End | 50 |
| 24 | `PALE_GARDEN` | 🌫️ | Wald | 140 |
| 25 | `WINDSWEPT_GRAVELLY_HILLS` | ⛰️ | Berg | 160 |
| 26 | `SAVANNA_PLATEAU` | 🦁 | Savanne | 200 |
| 27 | `WINDSWEPT_SAVANNA` | 🌬️ | Savanne | 180 |
| 28 | `SPARSE_JUNGLE` | 🌴 | Dschungel | 160 |
| 29 | `BAMBOO_JUNGLE` | 🎋 | Dschungel | 160 |
| 30 | `SNOWY_BEACH` | ❄️ | Küste | 120 |
| 31 | `SNOWY_SLOPES` | 🏔️ | Berg | 160 |
| 32 | `MANGROVE_SWAMP` | 🌿 | Sumpf | 150 |
| 33 | `LUSH_CAVES` | 🍄 | Höhle | 120 |
| 34 | `DRIPSTONE_CAVES` | 🪨 | Höhle | 120 |
| 35 | `BASALT_DELTAS` | 🔥 | Nether | 180 |
| **36** | **`geoworld:waterfall_grotto`** | **🕏** | **Custom Höhle** | **100** |

---

## 📖 Detailbeschreibungen

### 🌾 Ebenen & Felder

#### 1. `PLAINS`
- **Icon:** 🌾
- **Beschreibung:** Weite, flache Grasebenen – häufig, aber ideal für Dörfer und Siedlungen.
- **Entdeckungs-Radius:** 200 Blöcke
- **Besonderheit:** Häufigstes Biom – nur aktivieren, wenn explizit gewünscht.

#### 2. `SUNFLOWER_PLAINS`
- **Icon:** 🌻
- **Beschreibung:** Sonnenblumenfelder, die sich im Wind wiegen – ein leuchtend gelber Teppich.
- **Entdeckungs-Radius:** 200 Blöcke
- **Besonderheit:** Seltenere Variante der Plains; immer Richtung Osten zeigend.

#### 3. `MEADOW`
- **Icon:** 🌸
- **Beschreibung:** Sanfte Blumenwiesen in Bergтälern, häufig mit Bienen und Eichen.
- **Entdeckungs-Radius:** 180 Blöcke
- **Besonderheit:** Nur in Bergregionen (Höhe 1.18+).

---

### 🌳 Wälder

#### 4. `FOREST`
- **Icon:** 🌳
- **Beschreibung:** Klassischer Eichen- und Birkenwald – grün, dicht, voller Leben.
- **Entdeckungs-Radius:** 150 Blöcke
- **Besonderheit:** Viele Pilze und Tiere; Basis-Holzquelle.

#### 5. `FLOWER_FOREST`
- **Icon:** 🌺
- **Beschreibung:** Ein Wald übersät mit allen Blumenarten – ein natürliches Kunstwerk.
- **Entdeckungs-Radius:** 150 Blöcke
- **Besonderheit:** Einziger Ort, an dem alle Blumenarten gleichzeitig spawnen.

#### 6. `BIRCH_FOREST`
- **Icon:** 🌿
- **Beschreibung:** Helle Birkenwälder mit weißen Stämmen – freundlich und luftig.
- **Entdeckungs-Radius:** 150 Blöcke
- **Besonderheit:** Ideal für helles Holz ohne Dschungel.

#### 7. `OLD_GROWTH_BIRCH_FOREST`
- **Icon:** 🌲
- **Beschreibung:** Uralte, turmhohe Birken – ein majestätischer Hain.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Bäume bis zu 14 Blöcke hoch.

#### 8. `CHERRY_GROVE`
- **Icon:** 🌸
- **Beschreibung:** Rosafarbener Kirschblütenwald – eines der schönsten Biome Minecrafts.
- **Entdeckungs-Radius:** 130 Blöcke
- **Besonderheit:** Eingeführt in Java 1.20; Kirschholz-Quelle.

#### 9. `WINDSWEPT_FOREST`
- **Icon:** 🌬️
- **Beschreibung:** Vom Wind geformte Bäume auf felsigen Klippen und Hügeln.
- **Entdeckungs-Radius:** 150 Blöcke
- **Besonderheit:** Hybrid aus Windswept Hills und Wald.

#### 10. `PALE_GARDEN`
- **Icon:** 🌫️
- **Beschreibung:** Ein blasser, nebliger Wald mit weißem Moos – geheimnisvoll und unheimlich.
- **Entdeckungs-Radius:** 140 Blöcke
- **Besonderheit:** Eingeführt in Java 1.21.4; Heimat des Creaking-Mobs.

---

### 🌲 Taiga & Nadelwälder

#### 11. `TAIGA`
- **Icon:** 🌲
- **Beschreibung:** Dichter Nadelwald aus Fichten und Farnen – kalt und dunkel.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Wölfe und Füchse spawnen hier natürlich.

#### 12. `SNOWY_TAIGA`
- **Icon:** ❄️
- **Beschreibung:** Verschneiter Nadelwald – ruhig, weißbedeckt, eisig kalt.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Iglus können in der Nähe spawnen.

#### 13. `GROVE`
- **Icon:** 🌲
- **Beschreibung:** Dicht verschneiter Fichtenhain in alpinen Hochlagen.
- **Entdeckungs-Radius:** 140 Blöcke
- **Besonderheit:** Übergang zwischen Schneeebene und Bergspitze.

---

### ❄️ Schnee & Eis

#### 14. `SNOWY_PLAINS`
- **Icon:** ❄️
- **Beschreibung:** Endlose weiße Schneeebene – leer, still und weit.
- **Entdeckungs-Radius:** 200 Blöcke
- **Besonderheit:** Eisbären und Streuner spawnen hier.

#### 15. `SNOWY_SLOPES`
- **Icon:** 🏔️
- **Beschreibung:** Verschneite Hänge unterhalb der Gipfel – rutschig und steil.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Übergangszone zu Frozen/Jagged Peaks.

---

### 🏖️ Küsten & Gewässer

#### 16. `BEACH`
- **Icon:** 🏖️
- **Beschreibung:** Goldener Sandstrand am Ozean – Schildkröten legen hier Eier ab.
- **Entdeckungs-Radius:** 130 Blöcke
- **Besonderheit:** Schildkröten-Spawn; Sandstein unter dem Sand.

#### 17. `STONY_SHORE`
- **Icon:** 🪨
- **Beschreibung:** Felsige Küstenlinie aus Stein – unwirtlich und abweisend.
- **Entdeckungs-Radius:** 120 Blöcke
- **Besonderheit:** Kein Sand; meist an Bergketten-Rändern.

#### 18. `SNOWY_BEACH`
- **Icon:** ❄️
- **Beschreibung:** Eiskalter Strand mit Schnee bedeckt – selten und unwirtlich.
- **Entdeckungs-Radius:** 120 Blöcke
- **Besonderheit:** Grenzt an Frozen Ocean.

#### 19. `RIVER`
- **Icon:** 🏞️
- **Beschreibung:** Ein fließendes Gewässer durch die Landschaft – Lebensader der Welt.
- **Entdeckungs-Radius:** 100 Blöcke
- **Besonderheit:** Kleinster Radius da sehr häufig.

#### 20. `FROZEN_RIVER`
- **Icon:** ❄️
- **Beschreibung:** Ein zugefrorener Fluss – glatt, still und gefährlich.
- **Entdeckungs-Radius:** 100 Blöcke
- **Besonderheit:** Eis bricht unter schwerem Druck.

---

### 🍄 Seltene Biome

#### 21. `MUSHROOM_FIELDS`
- **Icon:** 🍄
- **Beschreibung:** Insel aus Mycel und riesigen Pilzen – kein natürlicher Mob-Spawn.
- **Entdeckungs-Radius:** 120 Blöcke
- **Besonderheit:** Einziges Biom ohne feindliche Mobs; sehr selten.

---

### 🏜️ Badlands

#### 22. `WOODED_BADLANDS`
- **Icon:** 🌵
- **Beschreibung:** Badlands mit Bäumen auf den Plateaus – Gold in Minen sehr häufig.
- **Entdeckungs-Radius:** 170 Blöcke
- **Besonderheit:** Erhöhte Gold-Spawn-Rate in Minen.

---

### 🌊 Tiefseeozeane

#### 23. `DEEP_COLD_OCEAN`
- **Icon:** 🌊
- **Beschreibung:** Eiskalter Tiefseeboden – dunkel, tief und lebensfeindlich.
- **Entdeckungs-Radius:** 250 Blöcke
- **Besonderheit:** Wracks und Ruinen auf dem Meeresboden.

#### 24. `DEEP_FROZEN_OCEAN`
- **Icon:** 🧊
- **Beschreibung:** Tiefgefrorener Ozean – Eisberge ragen aus dem Wasser.
- **Entdeckungs-Radius:** 250 Blöcke
- **Besonderheit:** Polarbären an der Oberfläche; Eisberge als Wahrzeichen.

---

### 🌴 Dschungel

#### 25. `SPARSE_JUNGLE`
- **Icon:** 🌴
- **Beschreibung:** Lichter Dschungel – weniger dicht, aber genauso warm und feucht.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Melonen und Kakao wachsen hier wild.

#### 26. `BAMBOO_JUNGLE`
- **Icon:** 🎋
- **Beschreibung:** Ein Dschungel dominiert von hohem Bambus – Pandas leben hier.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Einziger natürlicher Bambus-Spawn; Pandas exklusiv hier.

---

### 🦁 Savanne

#### 27. `SAVANNA_PLATEAU`
- **Icon:** 🦁
- **Beschreibung:** Erhöhte Savannenplateaus mit weitem Ausblick.
- **Entdeckungs-Radius:** 200 Blöcke
- **Besonderheit:** Lamas spawnen auf den Hochebenen.

#### 28. `WINDSWEPT_SAVANNA`
- **Icon:** 🌬️
- **Beschreibung:** Eine zerrissene, vom Wind gepeitschte Savanne mit steilen Klippen.
- **Entdeckungs-Radius:** 180 Blöcke
- **Besonderheit:** Extreme Geländeverwerfungen; selten.

---

### 🌿 Sumpf

#### 29. `MANGROVE_SWAMP`
- **Icon:** 🌿
- **Beschreibung:** Tropischer Mangrovensumpf mit verschlungenen Wurzeln über dem Wasser.
- **Entdeckungs-Radius:** 150 Blöcke
- **Besonderheit:** Eingeführt in 1.19; Frösche und Kaulquappen spawnen hier.

---

### 🪨 Höhlen

#### 30. `LUSH_CAVES`
- **Icon:** 🍄
- **Beschreibung:** Saftig grüne Unterwelt-Höhle mit Moos, Azaleen und Axolotls.
- **Entdeckungs-Radius:** 120 Blöcke
- **Besonderheit:** Axolotls spawn nur hier; Azaleen-Bäume zeigen den Weg nach unten.

#### 31. `DRIPSTONE_CAVES`
- **Icon:** 🪨
- **Beschreibung:** Tropfsteinkammer mit riesigen Stalaktiten und Stalagmiten.
- **Entdeckungs-Radius:** 120 Blöcke
- **Besonderheit:** Spitzen richten Schaden an; Tropfstein füllt Kessel mit Lava.

---

### 🔥 Nether

#### 32. `BASALT_DELTAS`
- **Icon:** 🔥
- **Beschreibung:** Vulkanische Basaltlandschaft mit Lavapools – das gefährlichste Nether-Biom.
- **Entdeckungs-Radius:** 180 Blöcke
- **Besonderheit:** Magmawürfel spawnen massenhaft; Ghasts selten hier.

---

### 🌟 The End

#### 33. `THE_END`
- **Icon:** 🌟
- **Beschreibung:** Die zentrale End-Insel – Heimat des Enderdrachen.
- **Entdeckungs-Radius:** 300 Blöcke
- **Besonderheit:** Nur einmal pro Welt; größter empfohlener Radius.

#### 34. `THE_VOID`
- **Icon:** 🕳️
- **Beschreibung:** Absolutes Nichts zwischen den End-Inseln – finstere Leere.
- **Entdeckungs-Radius:** 50 Blöcke
- **Besonderheit:** Kleinster Radius; kein festes Terrain vorhanden.

---

### ⛰️ Weitere Berge

#### 35. `WINDSWEPT_GRAVELLY_HILLS`
- **Icon:** ⛰️
- **Beschreibung:** Kiesbedeckte Hügel, vom Wind geformt – rau, steinig, unwirtlich.
- **Entdeckungs-Radius:** 160 Blöcke
- **Besonderheit:** Kies statt Erde unter dem Gras; häufige Erosion.

---

## 🕏 Custom Biom: Waterfall Grotto

### 36. `geoworld:waterfall_grotto`
**Die Wasserfallgrotte**

Versteckte Höhlen, die sich direkt hinter den vom Datapack generierten Wasserfällen befinden.
Der Wasserfall dient als natürlicher Vorhang – dahinter öffnet sich eine geräumige Grotte
aus Moos-Stein, Calcite und Tropfstein. Glow Lichen beleuchtet die Wände schwach.
Tiefe Wasserbäder am Boden und ein kleines Rinnsal führen tiefer ins Berginnere.

- **Icon:** 🕏
- **Entdeckungs-Radius:** 100 Blöcke
- **Atmosphäre:** Gischt-Partikel, Wasserrauschen, dämmriges Grün von Glow Lichen, Kühle
- **Oberfläche/Schichtung:** Mossy Stone → Calcite → Stone → Deepslate (Grotten-Boden); Wasserfall aus der Datapack-Struktur als Eingang
- **Besonderheit:** Spawnt **nur** dort, wo das Datapack einen Wasserfall generiert hat. Die Höhle ist von außen unsichtbar – der Wasserfall verbirgt den Eingang vollständig.

### 🧹 MythicMobs-Integration

| Mob-ID | Name | Verhalten | Spawn-Bedingung |
|---|---|---|---|
| `GrottoSerpent` | Grottennattern | Lauert im Wasser, greift bei Durchwaten an | Im Wasserbad am Grottengrund |
| `MossBehemoth` | Mooskolosse | Passiv bis berührt, extrem hohe HP, bewacht innere Kammer | Innerste Höhlenkammer |
| `WaterfallSprite` | Wasserfallgeist | Passiv, gibt Spielern kurz Wasseratmungs-Buff | Am Wasserfall-Eingang |
| `GrottoWarden` | Grottenwächter *(Elite)* | Erscheint nur wenn Spieler die innere Kammer betritt; Blindness + Slowness AoE | Innere Kammer, 1 Spawn pro Grotte |

**MythicMobs Beispiel-Mob** (`plugins/MythicMobs/Mobs/waterfall_grotto.yml`):
```yaml
GrottoSerpent:
  Type: GUARDIAN
  Display: '§bGrottennatter'
  Health: 40
  Damage: 6
  Options:
    PreventOtherDrops: true
    MovementSpeed: 0.28
  Skills:
  - skill{s=PoisonBite} @trigger ~onAttack
  - effect:particles{p=drip_water;amount=8} @self ~onTimer:10
  Drops:
  - PRISMARINE_SHARD 1-3 1
  - EXPERIENCE_ORB 15 1

MossBehemoth:
  Type: IRON_GOLEM
  Display: '§2Mooskoloss'
  Health: 300
  Damage: 14
  Options:
    PreventOtherDrops: true
    Neutral: true
  Skills:
  - skill{s=GroundSlam} @NearestPlayer{r=4} ~onTimer:40
  Drops:
  - MOSS_BLOCK 3-6 1
  - MOSSY_COBBLESTONE 5-10 1
  - EXPERIENCE_ORB 60 1

WaterfallSprite:
  Type: ALLAY
  Display: '§bWasserfallgeist'
  Health: 20
  Faction: friendly
  Options:
    PreventOtherDrops: true
    Neutral: true
  Skills:
  - potion{type=WATER_BREATHING;duration=120;amp=0} @NearestPlayer{r=5} ~onTimer:60

GrottoWarden:
  Type: ELDER_GUARDIAN
  Display: '§4§lGrottenwächter'
  Health: 500
  Damage: 18
  Options:
    PreventOtherDrops: true
    Boss: true
  Skills:
  - skill{s=BlindnessAoE} @PIR{r=10} ~onSpawn
  - skill{s=CaveCollapse} @NearestPlayer{r=6} ~onTimer:60
  Drops:
  - HEART_OF_THE_SEA 1 0.15
  - NAUTILUS_SHELL 2-4 1
  - EXPERIENCE_ORB 150 1
```

### 📦 Datapack-Verbindung

Das Biom setzt voraus, dass der Wasserfall als **placed_feature** im Datapack definiert ist.
Die Grotte wird als zweite Feature-Schicht direkt hinter dem Wasserfall platziert:

```
datapack/
  data/
    geoworld/
      worldgen/
        placed_feature/
          waterfall.json          ← Wasserfall-Feature (bereits vorhanden)
          waterfall_grotto.json   ← Grotten-Feature (neu, direkt dahinter)
        biome/
          waterfall_grotto.json   ← Biom-Definition
```

**`waterfall_grotto.json` (placed_feature):**
```json
{
  "feature": "geoworld:waterfall_grotto_carver",
  "placement": [
    {
      "type": "minecraft:count",
      "count": 1
    },
    {
      "type": "minecraft:in_square"
    },
    {
      "type": "minecraft:height_range",
      "height": {
        "type": "minecraft:uniform",
        "min_inclusive": { "absolute": 30 },
        "max_inclusive": { "absolute": 150 }
      }
    },
    {
      "type": "minecraft:biome"
    }
  ]
}
```

**`waterfall_grotto.json` (biome):**
```json
{
  "precipitation": "rain",
  "temperature": 0.6,
  "downfall": 0.8,
  "effects": {
    "sky_color": 7972607,
    "fog_color": 12638463,
    "water_color": 4159204,
    "water_fog_color": 329011,
    "ambient_sound": "minecraft:ambient.cave",
    "mood_sound": {
      "sound": "minecraft:ambient.cave",
      "tick_delay": 6000,
      "block_search_extent": 8,
      "offset": 2.0
    },
    "additions_sound": {
      "sound": "minecraft:ambient.underwater.loop.additions.ultra_rare",
      "tick_chance": 0.001
    },
    "particle": {
      "options": { "type": "minecraft:dripping_water" },
      "probability": 0.02
    }
  },
  "spawners": {
    "monster": [],
    "creature": [
      { "type": "minecraft:axolotl", "weight": 10, "minCount": 1, "maxCount": 2 }
    ],
    "ambient": [
      { "type": "minecraft:bat", "weight": 10, "minCount": 1, "maxCount": 4 }
    ],
    "water_creature": [],
    "water_ambient": []
  },
  "features": [
    [], [], [], [], [], [], [],
    ["geoworld:waterfall_grotto"],
    [],
    []
  ],
  "carvers": {}
}
```

### ⚙️ GeoDiscovery config.yml

```yaml
enabled-biomes:
  # ... (alle anderen Biome wie gehabt) ...
  - geoworld:waterfall_grotto

region-icons:
  geoworld:waterfall_grotto: "🕏"

region-names:
  geoworld:waterfall_grotto: "Wasserfallgrotte"

region-descriptions:
  geoworld:waterfall_grotto: "Eine verborgene Höhle hinter einem Wasserfall. Was verbirgt sich im Inneren?"
```

---

## ⚙️ config.yml Integration (alle Biome)

Um alle 35 Vanilla-Biome + das Custom-Biom in der `config.yml` zu aktivieren:

```yaml
enabled-biomes:
  # Ebenen
  - PLAINS
  - SUNFLOWER_PLAINS
  - MEADOW
  # Wälder
  - FOREST
  - FLOWER_FOREST
  - BIRCH_FOREST
  - OLD_GROWTH_BIRCH_FOREST
  - CHERRY_GROVE
  - WINDSWEPT_FOREST
  - PALE_GARDEN
  # Taiga
  - TAIGA
  - SNOWY_TAIGA
  - GROVE
  # Schnee
  - SNOWY_PLAINS
  - SNOWY_SLOPES
  # Küsten & Gewässer
  - BEACH
  - STONY_SHORE
  - SNOWY_BEACH
  - RIVER
  - FROZEN_RIVER
  # Selten
  - MUSHROOM_FIELDS
  # Badlands
  - WOODED_BADLANDS
  # Tiefsee
  - DEEP_COLD_OCEAN
  - DEEP_FROZEN_OCEAN
  # Dschungel
  - SPARSE_JUNGLE
  - BAMBOO_JUNGLE
  # Savanne
  - SAVANNA_PLATEAU
  - WINDSWEPT_SAVANNA
  # Sumpf
  - MANGROVE_SWAMP
  # Höhlen
  - LUSH_CAVES
  - DRIPSTONE_CAVES
  # Nether
  - BASALT_DELTAS
  # The End
  - THE_END
  - THE_VOID
  # Berge
  - WINDSWEPT_GRAVELLY_HILLS
  # Custom
  - geoworld:waterfall_grotto
```

---

*Zuletzt aktualisiert: Mai 2026 | Kompatibel mit Minecraft Java 1.21+*
