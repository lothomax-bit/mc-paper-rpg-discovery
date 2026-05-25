# GeoWorld – Custom Biome Design

Alle Biome nutzen den Namespace `geoworld:`. Sie sind in GeoDiscovery’s `config.yml` unter `enabled-biomes` einzutragen.
Wenn zusätzliche Plugins (Mobs, Quests, Loot) integriert werden sollen, einfach Bescheid geben.

---

## 🌊 Ozeane & Tiefwasser

### 1. `geoworld:abyssal_trench`
**Der Abyssalgraben**
Extrem tiefer Meeresgraben, Y -64 bis Y -20. Absolute Dunkelheit, kein Himmelslicht.
Boden aus Basalt und Deepslate. Schwache Biolumineszenz durch gelegentliche
Glow-Lichen-Cluster. Gequetschte Lavataschen im Boden.
- **Atmosphäre:** Schwarzes Wasser, dunkelblauer Himmel (unsichtbar unter Wasser), leise Unterwassergeräusche
- **Oberfläche:** Basalt → Deepslate → Stone
- **Plugin-Synergie:** Eigene Tiefsee-Mobs, Ancient-Artefakt-Loot, Druckkammer-Mechaniken
- **GeoDiscovery Icon:** `🗾`

### 2. `geoworld:bioluminescent_shallows`
**Die Leuchtseichten**
Flache Küstengewässer (Y 30–60) mit leuchtendem Plankton. Boden aus
weißem Sandstein und Prismarin-Clustern. Nächtlich strahlen ganze Buchten blaues Licht aus.
- **Atmosphäre:** Cyan-getöntes Wasser, sanfter Glow-Effekt durch Glow Lichen am Boden
- **Oberfläche:** Sand → Sandstone → Stone
- **Plugin-Synergie:** Alchemie-Zutaten, Perlentaucher-Quests
- **GeoDiscovery Icon:** `💧`

### 3. `geoworld:coral_archipelago`
**Das Korallenarchipel**
Gruppen kleiner Inseln mit üppigen Korallenriffen zwischen ihnen (Y 40–70).
Jede Insel hat einen einzigartigen Palmenhain aus großen Jungle-Bäumen.
- **Atmosphäre:** Türkisblaues Wasser, warme Farben, Papageienspawns
- **Oberfläche:** Sand → Sandstone mit Korallen-Features
- **Plugin-Synergie:** Händler-Strukturen, Piratenlager, Schatzkarten
- **GeoDiscovery Icon:** `🌴`

---

## ⛰️ Gebirge & Höhenlagen

### 4. `geoworld:crystal_peaks`
**Die Kristallgipfel**
Berge ab Y 600 bis Y 1800 mit Amethyst-Gestein an der Oberfläche.
Riesige Amethyst-Drusen ragen aus den Felswänden. Gelegentliche Amethyst-Geoden
so groß wie ganze Bergkuppen.
- **Atmosphäre:** Violett-getönter Himmel, Kristall-Ambient-Sounds
- **Oberfläche:** Amethyst Block → Calcite → Stone → Deepslate
- **Plugin-Synergie:** Magie-System (Kristalle als Ressource), Dungeon-Eingänge in Geoden
- **GeoDiscovery Icon:** `💎`

### 5. `geoworld:skyreach_plateau`
**Das Himmelsplateau**
Flache Hochebenen ab Y 900, umgeben von senkrechten Klippen. Obendrauf:
kurzes Gras, vereinzelte knorrige Bäume, und kristallklare Seen.
Mit Nebel-Effekt durch Clouds auf Höhe Y 800.
- **Atmosphäre:** Dünne Luft, Wind-Sounds, blassblauer Himmel
- **Oberfläche:** Grass → Dirt → Stone mit Moos-Patches
- **Plugin-Synergie:** Aussichtspunkte, Gleitschirm-Mechaniken, Adler-Nester
- **GeoDiscovery Icon:** `☁️`

### 6. `geoworld:obsidian_spires`
**Die Obsidiannadeln**
Vulkanisches Gebirge mit riesigen Obsidian-Säulen (Y 300–700).
Lava fließt zwischen den Nadeln. Kein Pflanzenwuchs, nur Basalt und Obsidian.
- **Atmosphäre:** Roter Himmel, Lava-Glow, unterirdisches Grollen
- **Oberfläche:** Obsidian → Basalt → Blackstone
- **Plugin-Synergie:** Schmiede-Strukturen, Drachenkult-Quests, seltene Erze
- **GeoDiscovery Icon:** `🔥`

---

## 🌲 Wälder & Ebenen

### 7. `geoworld:ancient_forest`
**Der Uralte Wald**
Riesige Bäume mit Stämmen aus 2x2 Blöcken, 40–60 Blöcke hoch.
Das Kronendach ist so dicht, dass am Boden ewige Dämmerung herrscht.
Moos, Farne und Pilze bedecken alles.
- **Atmosphäre:** Dunkelgrüner Nebel, Vogelrufe, leises Knacken
- **Oberfläche:** Podzol → Dirt → Stone mit Riesenbaum-Features
- **Plugin-Synergie:** Druiden-Quests, versteckte Waldhütten, Elfen-Lore
- **GeoDiscovery Icon:** `🌳`

### 8. `geoworld:ember_plains`
**Die Glutebenen**
Flache Overworld-Ebene mit gelegentlichen Lava-Seen und verschmortem Gras.
Netherrack-Patches brechen durch den Boden. Kein Regen, permanente Hitzeflimmern.
- **Atmosphäre:** Orangerot getönter Himmel, Hitze-Sounds, Aschefall
- **Oberfläche:** Netherrack-Patches in Grass → Dirt → Netherrack
- **Plugin-Synergie:** Feuer-Mobs, Schmiedewerk-Strukturen, Feuertempel
- **GeoDiscovery Icon:** `🔥`

### 9. `geoworld:salt_flats`
**Die Salzwüste**
Endlose, blendend weiße Ebene aus Calcite und weißem Terrakotta.
Kein Pflanzenwuchs. Spiegelglatte Pfützen bei Regen. Überraschend schöne
Sonnenuntergänge durch den weißen Boden.
- **Atmosphäre:** Gleißend heller Himmel, Wind-Sounds, gelegentliche Staubstürme
- **Oberfläche:** Calcite → White Terracotta → Stone
- **Plugin-Synergie:** Händlerkarawanen, Salz als Ressource, versteckte Ruinen
- **GeoDiscovery Icon:** `⬜`

### 10. `geoworld:fungal_expanse`
**Das Pilzmeer**
Riesige Pilze 20–40 Blöcke hoch bedecken eine feuchte, dunkle Ebene.
Myzel-Boden, Sporen in der Luft (Partikeleffekte), kein normales Gras.
Unterirdisch: Pilz-Höhlensysteme mit leuchtendem Myzel.
- **Atmosphäre:** Lila-brauner Dunst, Sporenwolken, feuchte Sounds
- **Oberfläche:** Mycelium → Dirt → Stone mit Pilz-Features
- **Plugin-Synergie:** Alchemie-Zutaten, Pilzvolk-Mobs, Gift-Mechaniken
- **GeoDiscovery Icon:** `🍄`

---

## ❄️ Eis & Kälte

### 11. `geoworld:glacial_abyss`
**Der Gletscherabgrund`**
Tiefe Schluchten in einer Eiswüste, Y -20 bis Y 200. Die Wände bestehen aus
transparentem Blaueis (Packed Ice). Am Grund fließen tiefgekühlte Flüsse.
- **Atmosphäre:** Eisblauer Himmel, Windpfeifen, Eisbrechen-Sounds
- **Oberfläche:** Blue Ice → Packed Ice → Stone
- **Plugin-Synergie:** Eismob-Spawner, eingefrorene Kreaturen, Eis-Magie
- **GeoDiscovery Icon:** `❄️`

### 12. `geoworld:aurora_tundra`
**Die Nordlicht-Tundra`**
Flache Schneeebene, fast keine Vegetation. Nachts: Simuliertes Nordlicht
durch Partikeleffekte im Himmel (via Plugin). Gelegentliche Felsnadeln.
- **Atmosphäre:** Tiefblauer Nachthimmel, Windgeheul, Kältepartikel
- **Oberfläche:** Snow → Dirt → Stone mit vereinzelten Felsen-Features
- **Plugin-Synergie:** Schamanismus-Quests, Polarwölfe, Nordlicht-Events
- **GeoDiscovery Icon:** `🌌`

---

## 🌋 Exotisch & Einzigartig

### 13. `geoworld:floating_isles`
**Die Schwebenden Inseln**
Inseln die ab Y 400 in der Luft schweben, verbunden durch natürliche
Steinbögen. Unter den Inseln hängen Wurzeln und Wasserfälle bis Y 200 hinunter.
- **Atmosphäre:** Weiße Wolken auf Höhe Y 350, Vogelrufe, Wind
- **Oberfläche:** Grass → Dirt → Stone, Wurzel-Hänger aus Mangrove Roots
- **Plugin-Synergie:** Flug-Mechaniken, schwebende Städte, Luftschiffe
- **GeoDiscovery Icon:** `🌤️`

### 14. `geoworld:petrified_wasteland`
**Die Versteinerte Ädnis**
Ehemalige Wälder, vollständig zu Stein geworden. Riesige versteinerte
Baumstämme aus Stone und Calcite ragen 30–50 Blöcke hoch. Alles grau, kein Leben.
- **Atmosphäre:** Grauer Himmel, Stille, gelegentliche Steinfall-Sounds
- **Oberfläche:** Calcite → Stone → Deepslate mit versteinertem Holz (Stone-Stämme)
- **Plugin-Synergie:** Fluchen-Lore, Steingolem-Mobs, Verfluchungs-Quests
- **GeoDiscovery Icon:** `🗿`

### 15. `geoworld:verdant_chasms`
**Die Grünen Schluchten**
Tiefe Schluchten (Y -64 bis Y 150) überwuchert von riesiger Vegetation.
Die Schluchtenwände sind bedeckt mit Moos, Farn und Lianen. Am Grund:
kristalleines Wasser und Lush Cave Features.
- **Atmosphäre:** Sattes Grün, Wasserrauschen, tropische Vogelrufe
- **Oberfläche:** Moss Block → Dirt → Stone mit Lush-Cave-Features auf Wandflächen
- **Plugin-Synergie:** Dschungel-Expeditions-Quests, seltene Pflanzen, Kletter-Mechaniken
- **GeoDiscovery Icon:** `🌿`

---

## GeoDiscovery config.yml Ergänzung

Diese Biome in `enabled-biomes` in der `config.yml` des GeoDiscovery-Plugins ergänzen:

```yaml
# Custom GeoWorld Biome
enabled-biomes:
  # ... (Vanilla Biome wie gehabt) ...
  - geoworld:abyssal_trench
  - geoworld:bioluminescent_shallows
  - geoworld:coral_archipelago
  - geoworld:crystal_peaks
  - geoworld:skyreach_plateau
  - geoworld:obsidian_spires
  - geoworld:ancient_forest
  - geoworld:ember_plains
  - geoworld:salt_flats
  - geoworld:fungal_expanse
  - geoworld:glacial_abyss
  - geoworld:aurora_tundra
  - geoworld:floating_isles
  - geoworld:petrified_wasteland
  - geoworld:verdant_chasms

region-icons:
  # ... (Vanilla Icons wie gehabt) ...
  geoworld:abyssal_trench: "🗾"
  geoworld:bioluminescent_shallows: "💧"
  geoworld:coral_archipelago: "🌴"
  geoworld:crystal_peaks: "💎"
  geoworld:skyreach_plateau: "☁️"
  geoworld:obsidian_spires: "🔥"
  geoworld:ancient_forest: "🌳"
  geoworld:ember_plains: "🔥"
  geoworld:salt_flats: "⬜"
  geoworld:fungal_expanse: "🍄"
  geoworld:glacial_abyss: "❄️"
  geoworld:aurora_tundra: "🌌"
  geoworld:floating_isles: "🌤️"
  geoworld:petrified_wasteland: "🗿"
  geoworld:verdant_chasms: "🌿"
```
