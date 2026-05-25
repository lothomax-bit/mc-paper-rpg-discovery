# GeoWorld – Custom Biome Design

Alle Biome nutzen den Namespace `geoworld:`. Sie sind in GeoDiscovery's `config.yml` unter `enabled-biomes` einzutragen.
Wenn zusätzliche Plugins (Mobs, Quests, Loot) integriert werden sollen, einfach Bescheid geben.

> 🧹 **MythicMobs** ist installiert. Alle Mob-Einträge beziehen sich auf MythicMobs-IDs, die in `plugins/MythicMobs/Mobs/` als `.yml`-Dateien definiert werden.

---

## 🌊 Ozeane & Tiefwasser

### 1. `geoworld:abyssal_trench`
**Der Abyssalgraben**
Extrem tiefer Meeresgraben, Y -64 bis Y -20. Absolute Dunkelheit, kein Himmelslicht.
Boden aus Basalt und Deepslate. Schwache Biolumineszenz durch gelegentliche
Glow-Lichen-Cluster. Gequetschte Lavataschen im Boden.
- **Atmosphäre:** Schwarzes Wasser, dunkelblauer Himmel (unsichtbar unter Wasser), leise Unterwassergeräusche
- **Oberfläche:** Basalt → Deepslate → Stone
- **MythicMobs:** `AbyssalHunter` (Tiefseekreatur, blind, spürt Bewegung), `DeepOneSorcerer` (magischer Unterwasserboss)
- **GeoDiscovery Icon:** `🗻`

### 2. `geoworld:bioluminescent_shallows`
**Die Leuchtseichten**
Flache Küstenwässer (Y 30–60) mit leuchtendem Plankton. Boden aus
weißem Sandstein und Prismarin-Clustern. Nächtlich strahlen ganze Buchten blaues Licht aus.
- **Atmosphäre:** Cyan-getöntes Wasser, sanfter Glow-Effekt durch Glow Lichen am Boden
- **Oberfläche:** Sand → Sandstone → Stone
- **MythicMobs:** `LuminousJellyfish` (Passiv, Kontaktschaden durch Gift), `CoralGuardian` (Verteidigt Riff-Strukturen)
- **GeoDiscovery Icon:** `💧`

### 3. `geoworld:coral_archipelago`
**Das Korallenarchipel**
Gruppen kleiner Inseln mit üppigen Korallenriffen zwischen ihnen (Y 40–70).
Jede Insel hat einen einzigartigen Palmenhain aus großen Jungle-Bäumen.
- **Atmosphäre:** Türkisblaues Wasser, warme Farben, Papageienspawns
- **Oberfläche:** Sand → Sandstone mit Korallen-Features
- **MythicMobs:** `IslandPirate` (Bandit-Mob mit Schusswaffe), `SandCrab` (Passiv tagsüber, aggressiv nachts)
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
- **MythicMobs:** `CrystalGolem` (Bewacht Amethyst-Geoden), `ShardWyvern` (Fliegender Miniboss, schießt Kristallsplitter)
- **GeoDiscovery Icon:** `💎`

### 5. `geoworld:skyreach_plateau`
**Das Himmelsplateau**
Flache Hochebenen ab Y 900, umgeben von senkrechten Klippen. Obendrauf:
kurzes Gras, vereinzelte knorrige Bäume, und kristallklare Seen.
Mit Nebel-Effekt durch Clouds auf Höhe Y 800.
- **Atmosphäre:** Dünne Luft, Wind-Sounds, blassblauer Himmel
- **Oberfläche:** Grass → Dirt → Stone mit Moos-Patches
- **MythicMobs:** `CloudEagle` (Späher-Mob, greift bei Sichtkontakt an), `PlateauWarden` (Geist-Elite, bewacht Aussichtspunkte)
- **GeoDiscovery Icon:** `☁️`

### 6. `geoworld:obsidian_spires`
**Die Obsidiannadeln**
Vulkanisches Gebirge mit riesigen Obsidian-Säulen (Y 300–700).
Lava fließt zwischen den Nadeln. Kein Pflanzenwuchs, nur Basalt und Obsidian.
- **Atmosphäre:** Roter Himmel, Lava-Glow, unterirdisches Grollen
- **Oberfläche:** Obsidian → Basalt → Blackstone
- **MythicMobs:** `LavaStalker` (Teleportiert sich durch Lava), `ObsidianColossus` (Seltener Weltendboss)
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
- **MythicMobs:** `AncientTreeSpirit` (Beschützt alte Bäume, unsichtbar im Schatten), `ForestTroll` (Stark, langsam, wirft Felsbrocken)
- **GeoDiscovery Icon:** `🌳`

### 8. `geoworld:ember_plains`
**Die Glutebenen**
Flache Overworld-Ebene mit gelegentlichen Lava-Seen und verschmortem Gras.
Netherrack-Patches brechen durch den Boden. Kein Regen, permanente Hitzeflimmern.
- **Atmosphäre:** Orangerot getönter Himmel, Hitze-Sounds, Aschefall
- **Oberfläche:** Netherrack-Patches in Grass → Dirt → Netherrack
- **MythicMobs:** `CinderHound` (Feuer-Wolf, hinterlässt brennende Spuren), `AshWalker` (Untote, tauchen aus Aschefeldern auf)
- **GeoDiscovery Icon:** `🔥`

### 9. `geoworld:salt_flats`
**Die Salzwüste**
Endlose, blendend weiße Ebene aus Calcite und weißem Terrakotta.
Kein Pflanzenwuchs. Spiegelglatte Pfützen bei Regen. Überraschend schöne
Sonnenuntergänge durch den weißen Boden.
- **Atmosphäre:** Gließend heller Himmel, Wind-Sounds, gelegentliche Staubstürme
- **Oberfläche:** Calcite → White Terracotta → Stone
- **MythicMobs:** `SaltGolem` (Langsam, sehr hohe Rüstung), `MiragePhantom` (Täuscht Spieler mit falschen Strukturen)
- **GeoDiscovery Icon:** `⬜`

### 10. `geoworld:fungal_expanse`
**Das Pilzmeer**
Riesige Pilze 20–40 Blöcke hoch bedecken eine feuchte, dunkle Ebene.
Myzel-Boden, Sporen in der Luft (Partikeleffekte), kein normales Gras.
Unterirdisch: Pilz-Höhlensysteme mit leuchtendem Myzel.
- **Atmosphäre:** Lila-brauner Dunst, Sporenwolken, feuchte Sounds
- **Oberfläche:** Mycelium → Dirt → Stone mit Pilz-Features
- **MythicMobs:** `SporeSpreader` (Infiziert Spieler mit Gift-Debuff), `FungalBehemoth` (Riesiger Pilz-Golem-Boss)
- **GeoDiscovery Icon:** `🍄`

---

## ❄️ Eis & Kälte

### 11. `geoworld:glacial_abyss`
**Der Gletscherabgrund**
Tiefe Schluchten in einer Eiswüste, Y -20 bis Y 200. Die Wände bestehen aus
transparentem Blaueis (Packed Ice). Am Grund fließen tiefgekühlte Flüsse.
- **Atmosphäre:** Eisblauer Himmel, Windpfeifen, Eisbrechen-Sounds
- **Oberfläche:** Blue Ice → Packed Ice → Stone
- **MythicMobs:** `GlacialWraith` (Geist, friert Spieler mit Slow ein), `IceTitan` (Seltener Boss, erschaff Eis-Klippen beim Stampfen)
- **GeoDiscovery Icon:** `❄️`

### 12. `geoworld:aurora_tundra`
**Die Nordlicht-Tundra**
Flache Schneeebene, fast keine Vegetation. Nachts: Simuliertes Nordlicht
durch Partikeleffekte im Himmel (via Plugin). Gelegentliche Felsnadeln.
- **Atmosphäre:** Tiefblauer Nachthimmel, Windgeheul, Kältepartikel
- **Oberfläche:** Snow → Dirt → Stone mit vereinzelten Felsen-Features
- **MythicMobs:** `AuroraShaMan` (Beschwört Nordlicht-Blitze), `TundraWolf` (Rudel-Mob, greift in Gruppen an)
- **GeoDiscovery Icon:** `🌌`

---

## 🌋 Exotisch & Einzigartig

### 13. `geoworld:floating_isles`
**Die Schwebenden Inseln**
Inseln die ab Y 400 in der Luft schweben, verbunden durch natürliche
Steinbögen. Unter den Inseln hängen Wurzeln und Wasserfälle bis Y 200 hinunter.
- **Atmosphäre:** Weiße Wolken auf Höhe Y 350, Vogelrufe, Wind
- **Oberfläche:** Grass → Dirt → Stone, Wurzel-Hänger aus Mangrove Roots
- **MythicMobs:** `SkyDrake` (Kleiner Drache, bewacht schwebende Schatzkisten), `IslePhoenix` (Elite, belebt sich einmal pro Kampf wieder)
- **GeoDiscovery Icon:** `🌤️`

### 14. `geoworld:petrified_wasteland`
**Die Versteinerte Ädnis**
Ehemalige Wälder, vollständig zu Stein geworden. Riesige versteinerte
Baumstämme aus Stone und Calcite ragen 30–50 Blöcke hoch. Alles grau, kein Leben.
- **Atmosphäre:** Grauer Himmel, Stille, gelegentliche Steinfall-Sounds
- **Oberfläche:** Calcite → Stone → Deepslate mit versteinertem Holz (Stone-Stämme)
- **MythicMobs:** `StoneGolem` (Passiv bis angegriffen, extrem langsam), `PetrifiedWarden` (Elite, vollständig immun gegen Knockback)
- **GeoDiscovery Icon:** `🗿`

### 15. `geoworld:verdant_chasms`
**Die Grünen Schluchten**
Tiefe Schluchten (Y -64 bis Y 150) überwuchert von riesiger Vegetation.
Die Schluchtenwände sind bedeckt mit Moos, Farn und Lianen. Am Grund:
kristalleines Wasser und Lush Cave Features.
- **Atmosphäre:** Sattes Grün, Wasserrauschen, tropische Vogelrufe
- **Oberfläche:** Moss Block → Dirt → Stone mit Lush-Cave-Features auf Wandflächen
- **MythicMobs:** `VineStrangler` (Greift aus dem Gebüsch an, legt Spieler kurz fest), `ChasmSerpent` (Riesenschlange, patroulliert den Schluchtgrund)
- **GeoDiscovery Icon:** `🌿`

---
---

## 🌎 Neue Biome (Erweiterung)

> Die folgenden 35 Biome erweitern das GeoWorld-System. Jedes folgt dem gleichen Format und ist MythicMobs-kompatibel.

---

## 🌧️ Sümpfe & Feuchtgebiete

### 16. `geoworld:cursed_bayou`
**Das Verfluchte Bayou**
Dunkler Sümpf mit ständigem Säurestaub-Partikeleffekt. Totes Holz, stagniertes
Wasser und violette Pilze. Kein Mondlicht dringt durch das Kronendach.
- **Atmosphäre:** Lila Dunst, gelegentliche Schreie aus dem Nichts, blaues Sumpffeuer
- **Oberfläche:** Mud → Clay → Dirt mit totem Holz und schwarzen Pilzen
- **MythicMobs:** `BayouWitch` (Verzaubert Spieler mit zufälligen Debuffs), `VoodooSkeleton` (Schickt Fluchdolche auf Distanz)
- **GeoDiscovery Icon:** `💀`

### 17. `geoworld:mangrove_labyrinth`
**Das Mangrovenlabyrinth**
Dichtes Netz aus Mangrovenwurzeln, so eng verwachsen, dass Orientierung kaum
möglich ist. Tiefes, klares Wasser zwischen den Wurzeln, beleuchtet von Glow Lichen.
- **Atmosphäre:** Grünes Lichtgefunkel, Froschrufe, sanftes Planschen
- **Oberfläche:** Mud → Clay mit Mangrove-Root-Netz und Wasser-Channels
- **MythicMobs:** `BogCrocodile` (Lauert unter Wasser, instant-Angriff), `GlowFrog` (Passiv, lässt Alchemiematerial fallen)
- **GeoDiscovery Icon:** `🐸`

### 18. `geoworld:peat_moors`
**Die Torfmoore**
Endlose braune Moore mit nebelverhangenen Horizonten. Gelegentliche Irrlichter
(Partikel) locken Spieler ins Abseits. Torf-Blöcke leuchten schwach.
- **Atmosphäre:** Grauer Nebel, Windstöße, entfernte Glockengeräusche
- **Oberfläche:** Dirt → Clay → Stone mit Moos-Patches und Torf-Features
- **MythicMobs:** `WillOWisp` (Irrlicht, verführt Spieler – kein Schaden, aber führt in Fallen), `MoorBanshee` (Schreiangriff, erzeugt AoE-Schaden)
- **GeoDiscovery Icon:** `🌫️`

---

## 🏜️ Wüsten & Halbwüsten

### 19. `geoworld:dune_sea`
**Das Dünenmeer**
Endlose Sanddünen bis Y 250, geformt wie Wellen. Kein Schatten, kein Wasser.
Unterirdisch: Antike Gruften aus Sandstein.
- **Atmosphäre:** Oranger Sandstaubnebel, Hitzeflirrern, Kamelvogel-Rufe
- **Oberfläche:** Sand → Sandstone → Stone mit Dünen-Wellenform
- **MythicMobs:** `DuneScorpion` (Vergiftet bei Stich), `SandWraith` (Taucht aus Sand auf, sehr schnell)
- **GeoDiscovery Icon:** `🏜️`

### 20. `geoworld:red_canyon`
**Der Rote Canyon**
Tiefe rote Sandstein-Schluchten inspiriert von realen Erosionslandschaften.
Farbenspiel von Rot bis Orange. Blöße Felswände mit Riesenzeichnungen.
- **Atmosphäre:** Tiefrotes Abendlicht, Windpfeifen in den Rissen, Adlerrufe
- **Oberfläche:** Red Sand → Red Sandstone → Terracotta in Schichten
- **MythicMobs:** `CanyonHawk` (Sturzflug-Angriff aus der Luft), `RedSandElemental` (Beschwört Sandstürme um sich)
- **GeoDiscovery Icon:** `🧱`

### 21. `geoworld:volcanic_ashfields`
**Die Vulkan-Aschfelder**
Flache, graue Landschaft bedeckt von Asche. Gelegentliche Rauchsäulen
steigen auf. Unter der Ascheoberfläche: noch glühende Magmataschen.
- **Atmosphäre:** Grauer Ascheregen, Schwefeldampf, unterirdisches Grollen
- **Oberfläche:** Gravel → Blackstone → Magma Block (tief)
- **MythicMobs:** `AshPhoenix` (Stirbt und ersteht aus der Asche), `MagmaWorm` (Bohrt sich durch den Boden, surprise-Angriff)
- **GeoDiscovery Icon:** `🌫️`

---

## 🌲 Wälder (Erweitert)

### 22. `geoworld:whispering_pines`
**Der Flüsternde Kiefernwald**
Gleichmäßige, hohe Kiefern (Spruce). Das Nadeldach dämpft alle Sounds.
Boden aus tiefem Moos. Nächtlich flüstern der Wind zwischen den Stämmen.
- **Atmosphäre:** Dämmerlicht, gedämpfte Sounds, gelegentliches Rauschen
- **Oberfläche:** Moss Block → Podzol → Dirt → Stone
- **MythicMobs:** `PineNymph` (Heilt sich durch Baumnähe), `ShadowStalker` (Unsichtbar außerhalb von Lichtkegeln)
- **GeoDiscovery Icon:** `🌲`

### 23. `geoworld:haunted_grove`
**Der Heimgesuchte Hain**
Verdrehte, knorrige Bäume aus Dark Oak. Geisterlichter zwischen den Ästen.
Boden mit Seelen-Sand-Patches. Permanent leichter Nebel.
- **Atmosphäre:** Grünliches Nachtlicht, Geistersounds, leises Wimmern
- **Oberfläche:** Soul Sand Patches in Grass → Dirt mit Dark-Oak-Features
- **MythicMobs:** `GroveSpecter` (Teleportiert sich zu Spielern), `WickerConstruct` (Aus Holz erschaffen, brennt nicht)
- **GeoDiscovery Icon:** `👻`

### 24. `geoworld:golden_woodland`
**Der Goldene Wald**
Herbstlicher Wald mit kupfer- und goldgefärbtem Laub (Orange/Yellow Leaves).
Sanfter Wind weht Blätter (Partikel). Warmes Abendlicht immer präsent.
- **Atmosphäre:** Goldoranges Licht, raschendes Laub, Eichhörnchen-Sounds
- **Oberfläche:** Grass → Dirt → Stone mit Birch/Oak-Mischwald
- **MythicMobs:** `GoldenFox` (Passiv, stehlen Loot aus Inventar bei Berührung), `HarvestGolem` (Bewacht Obstgarten-Strukturen)
- **GeoDiscovery Icon:** `🍂`

### 25. `geoworld:bamboo_highlands`
**Die Bambushochländer**
Dicht bewachsene Bambuswälder auf Hochebenen ab Y 150. Nebel hängt zwischen
den Halmen. Vereinzelte Tempelruinen aus Moos-Stein.
- **Atmosphäre:** Grüner Nebel, Bambus-Rauschen, fernes Flötenspiel
- **Oberfläche:** Grass → Dirt → Stone mit dichtem Bambus-Feature
- **MythicMobs:** `PandaMonk` (Passiv außer bei Angriffen, stark), `BambooNinja` (Schnell, hinterhältige Attacken aus dem Gebüsch)
- **GeoDiscovery Icon:** `🎋`

### 26. `geoworld:twilight_canopy`
**Das Zwielichtkronendach**
So dicht bewachsen, dass permanente Dämmerung herrscht. Leuchtende Blumen
am Boden erhellen den Weg schwach. Baumstämme sind mit Glow Lichen bedeckt.
- **Atmosphäre:** Dunkelblaues Licht, Insekten-Sounds, gelegentliches Aufleuchten
- **Oberfläche:** Moss Block → Dirt → Stone mit Glow-Lichen-Features
- **MythicMobs:** `CanopyMoth` (Blind, greift auf Lichtquellen zu), `TwilightDruid` (Beschwört Wurzeln als Fallen)
- **GeoDiscovery Icon:** `🌑`

---

## 🪨 Höhlen & Untergrund

### 27. `geoworld:crystal_caverns`
**Die Kristallkaverne**
Unterirdische Höhlen gefüllt mit riesigen Quarzkristallen. Jeder Kristall
wirft Prismenlichter (Partikel). Magische Resonanz – keine normalen Mobs hier.
- **Atmosphäre:** Weißes Prismenlicht, Kristallklingen, magisches Summen
- **Oberfläche:** Quartz → Calcite → Deepslate mit Kristall-Säulen
- **MythicMobs:** `CrystalMimic` (Tarnt sich als Kristall, spring-Angriff), `GeodeGuardian` (Elite, reflektiert Schadensanteil)
- **GeoDiscovery Icon:** `✨`

### 28. `geoworld:magma_depths`
**Die Magmatiefen**
Überflutete Lavahöhlen tief unter der Erde. Hitze deformiert den Stein.
Magmablöcke ersetzen fast alles. Lava fällt als "Regen" von der Decke.
- **Atmosphäre:** Roter Feuerschein, Tropfen-Sounds, intensives Grollen
- **Oberfläche:** Magma Block → Blackstone → Deepslate
- **MythicMobs:** `MoltenElemental` (Immun gegen Feuer, hinterlässt Lava-Pfade), `InfernoDrake` (Tiefen-Boss, atmet Lavabälle)
- **GeoDiscovery Icon:** `🔥`

### 29. `geoworld:echo_chambers`
**Die Echokammern**
Riesige natürliche Höhlendom-Strukturen. Sounds hallen mehrfach wider.
Boden aus glattem Stein mit Gesteinsschichten wie natürliches Gemälde.
- **Atmosphäre:** Hall-Effekt, leises Tropfen, gelegentliche Steinbrocken-Sounds
- **Oberfläche:** Smooth Stone → Stone → Deepslate mit Schicht-Features
- **MythicMobs:** `EchoBat` (Angriff erzeugt kurzen Blindness-Debuff), `ChamberColossus` (Riesiger Stein-Boss, erschüttert den Boden)
- **GeoDiscovery Icon:** `🔊`

### 30. `geoworld:fungal_depths`
**Die Pilztiefen**
Tiefe Höhlen überwuchert von leuchtendem Myzel und riesigen Pilzen.
Sporen füllen die Luft (Partikel). Kein natürliches Licht, nur Pilzleuchten.
- **Atmosphäre:** Blau-lila Biolumin, Sporen-Partikel, feuchtes Tropfen
- **Oberfläche:** Mycelium → Dirt → Stone mit leuchtendem Pilz-Features
- **MythicMobs:** `GlowMushroomCrab` (Langsam, sehr hohe Verteidigung), `SporeQueen` (Boss, infiziert mit stackenden Gift-Debuffs)
- **GeoDiscovery Icon:** `🍄`

---

## 🔥 Nether (Erweitert)

### 31. `geoworld:nether_garden`
**Der Nethergarten**
Ein erstaunlich bewachsener Bereich im Nether. Warped und Crimson Vegetation
wächst chaotisch vermischt. Seltsame Stille im sonst lärmigen Nether.
- **Atmosphäre:** Mischung aus Blau und Rot, leises organisches Wachsen
- **Oberfläche:** Netherrack mit Warped/Crimson Vegetation-Mix
- **MythicMobs:** `NetherBloom` (Statischer Mob, vergiftet im Radius), `WarpedPiglin` (Mutierter Piglin, doppelt so stark)
- **GeoDiscovery Icon:** `🌺`

### 32. `geoworld:soul_archive`
**Das Seelenarchiv**
Riesige Bibliothek-ähnliche Höhlenformation aus Soul Sand und Soul Soil.
Bücher-ähnliche Steinformationen. Seelen schreien aus dem Boden.
- **Atmosphäre:** Blaue Seelenflammen, Flüstern und Schreien, Kälte trotz Nether
- **Oberfläche:** Soul Sand → Soul Soil mit Seelen-Feuer-Features
- **MythicMobs:** `SoulLibrarian` (Beschwört Geister als Ablenkung), `ArchivistWreath` (Elite, stiehlt XP bei Angriff)
- **GeoDiscovery Icon:** `📚`

### 33. `geoworld:magma_delta_rim`
**Der Magmadelten-Rand**
Randgebiet der Basalt-Deltas mit noch aktiveren Lavafontänen.
Riesige Magmablasen steigen auf. Extremes Terrain, kaum begehbar.
- **Atmosphäre:** Rotes Glutlicht, Explosionsgeräusche, heißes Zählen
- **Oberfläche:** Magma Block → Basalt → Blackstone mit Fontänen-Features
- **MythicMobs:** `BasaltBrute` (Sehr stark, wirft Magmabrocken), `LavaDjinn` (Fliegend, beschwört Lavafalls)
- **GeoDiscovery Icon:** `🌋`

---

## 🌟 The End (Erweitert)

### 34. `geoworld:end_coral_reef`
**Das End-Korallenriff**
Endstein-Korallen-Hybridstruktur weit draußen in den End-Außeninseln.
Endstone-"Korallen" in Lila und Weiß, umgeben von Chorus-Pflanzen.
- **Atmosphäre:** Violettes Leuchten, Chorus-Blüten-Partikel, fernes Summen
- **Oberfläche:** End Stone → Purpur Block mit Chorus-Features
- **MythicMobs:** `EndCrawler` (Kriechtier, teleportiert sich kurz vor Angriff), `ChorusReaper` (Elite, verschluckt Spieler kurz in eine Blindnessdimension)
- **GeoDiscovery Icon:** `💜`

### 35. `geoworld:void_sanctum`
**Das Void-Sanktum**
Ein mysteriöser Ort direkt am Rand des Void. Schwebende Plattformen aus
End-Stein, verbunden durch schmale Brücken. Fallen in den Void enden immer tödlich.
- **Atmosphäre:** Absolute Stille, Void-Partikel, Zeit scheint still zu stehen
- **Oberfläche:** End Stone → Purpur mit schwebenden Plattform-Features
- **MythicMobs:** `VoidSentinel` (Wacht, greift ohne Vorwarnung an), `OblivionBoss` (Weltenboss, wirft Spieler in den Void)
- **GeoDiscovery Icon:** `🕳️`

### 36. `geoworld:elytra_spires`
**Die Elytra-Nadeln`**
Hohe, dünne Türme aus Purpur weit in den End-Außeninseln. Ideal als
Startpunkt für Elytra-Flüge. Schatzräume an der Spitze jedes Turms.
- **Atmosphäre:** Windgepeitschte Höhe, Flatter-Sounds, Sternenhimmel
- **Oberfläche:** Purpur Pillar → End Stone mit schmalem Türm-Feature
- **MythicMobs:** `SpireGargoyle` (Steinern, stürzt auf Spieler herab), `EndKnight` (Bewacht Schatzraum an der Spitze)
- **GeoDiscovery Icon:** `🪂`

---

## 🌊 Weitere Ozeane & Inseln

### 37. `geoworld:sunken_city`
**Die Versunkene Stadt`**
Angestorbene Unterwasserstadt aus Prismarin und dunklem Prismarin.
Rückgebliebene Bewohner als Untote. Laternen leuchten noch immer.
- **Atmosphäre:** Grünliches Unterwasserlicht, Glockenklang, trübes Wasser
- **Oberfläche:** Dark Prismarine → Prismarine → Stone am Meeresboden
- **MythicMobs:** `DrownedKnight` (Bewaffneter ertrunkener Krieger, trägt Rüstung), `TideCaller` (Priester-Mob, ruft Hai-Mobs herbei)
- **GeoDiscovery Icon:** `🏛️`

### 38. `geoworld:volcanic_island`
**Die Vulkaninsel`**
Einzelne Insel mit aktivem Vulkankegel in der Mitte. Lava fließt zum Meer.
Ringsherum: Schwarzer Sand und Obsidian-Strände.
- **Atmosphäre:** Rauchäulen, Ascheregen, Hämmern des Vulkans
- **Oberfläche:** Obsidian → Basalt → Black Sand mit Lava-River-Features
- **MythicMobs:** `VolcanoSpirit` (Beschützt die Insel, sehr aggressiv), `LavaGull` (Fliegend, taucht in Lava ein ohne Schaden)
- **GeoDiscovery Icon:** `🌋`

### 39. `geoworld:pearl_lagoon`
**Die Perlenlagune`**
Geschützte, ruhige Lagune mit klarem Wasser und perlweißem Sand.
Unterhalb wachsen seltene Perlen-Strukturen aus Calcite und Prismarin.
- **Atmosphäre:** Türkises Licht, sanftes Plätschern, Möwen-Rufe
- **Oberfläche:** White Sand → Calcite mit Unterwasser-Perlen-Features
- **MythicMobs:** `PearlOyster` (Passiv, lässt seltene Perlen fallen), `LaguneShark` (Schnell, aggressiv bei Blütgeruch)
- **GeoDiscovery Icon:** `🪚`

---

## ⭐ Mystisch & Märchenhaft

### 40. `geoworld:starfall_meadow`
**Die Sternfallwiese`**
Flache Wiese, auf der jede Nacht Sternschnuppen (Partikel) landen.
Meteorit-Einschlaglöcher aus Deepslate und Crying Obsidian.
- **Atmosphäre:** Tiefblauer Nachthimmel, Leuchtsterne, magisches Summen
- **Oberfläche:** Grass → Dirt mit Einschlag-Kratern aus Crying Obsidian
- **MythicMobs:** `StarFragment` (Lebendig gewordener Meteoritenbruchstück), `CelestialGuardian` (Elite, hüllt sich in Sternenlicht)
- **GeoDiscovery Icon:** `⭐`

### 41. `geoworld:dream_forest`
**Der Traumwald`**
Ein Wald der aussieht wie aus einem Traum – Pastellfarben, riesige Schmetterlinge
(Partikel), alles leuchtet leicht. Regenbogenfärbige Blumen auf Moos-Boden.
- **Atmosphäre:** Pastell-Partikel, sanfte Musik-Sounds, Schwebegefühl
- **Oberfläche:** Moss Block → Dirt mit Glow-Lichen und Azaleen
- **MythicMobs:** `DreamWeaver` (Gibt Spielern zufällige positive Effekte), `Nightmare` (Nachts: erscheint als feindliche Kopie des Spielers)
- **GeoDiscovery Icon:** `🌈`

### 42. `geoworld:rune_highlands`
**Die Runenhochebenen`**
Hochebenen übersät mit alten Runensteinen. Magische Energie fließt sichtbar
als Partikel zwischen den Steinen. Altes Wissen ist hier eingeschrieben.
- **Atmosphäre:** Violette Magiepartikel, runenleuchten, leises Murmeln
- **Oberfläche:** Stone → Mossy Stone → Deepslate mit Runen-Struktur-Features
- **MythicMobs:** `RuneGuardian` (Arkaner Wächter, schießt magische Projektile), `AncientSeer` (Zaubert AoE-Slow und Confusion)
- **GeoDiscovery Icon:** `🔮`

### 43. `geoworld:eclipse_valley`
**Das Finsternistal`**
Ein Tal, in dem die Sonne niemals scheint – permanente Sonnenfinsternis.
Alles in Grau- und Schwarztönen. Seltsame Schwerkraft (langsameres Fallen).
- **Atmosphäre:** Dunkel mit rotem Lichtring am Horizont, absolute Stille
- **Oberfläche:** Deepslate → Stone mit schwarzen Felsen und Eclipse-Features
- **MythicMobs:** `EclipsePhantom` (Nur sichtbar im Dunkeln), `SolarWraith` (Boss, dreht den Tag-Nacht-Zyklus lokal um)
- **GeoDiscovery Icon:** `🌑`

### 44. `geoworld:mirage_oasis`
**Die Fata-Morgana-Oase`**
Eine Oase, die manchmal da ist und manchmal nicht (strukturelle Zufallsfeatures).
Palmen, klares Wasser, früchtetragende Bäume – aber alles wirkt zu perfekt.
- **Atmosphäre:** Flirrende Hitze, leises Glücklichsein, Trugbild-Partikel
- **Oberfläche:** Sand → Grass Patches mit Wasser und Palmen-Features
- **MythicMobs:** `OasisDjinn` (Gibt gefälschte Quest-Items), `MirageHydra` (Boss, erscheint wenn Spieler trinkt)
- **GeoDiscovery Icon:** `🌵`

### 45. `geoworld:moonstone_basin`
**Das Mondsteinbecken`**
Nachts ein Wunder – der Boden aus hellgrauem Stein reflektiert das Mondlicht
perfekt und leuchtet silbern. Tagsüber unscheinbar.
- **Atmosphäre:** Silbernes Nachtleuchten, leises Wasser-Kristall-Klingen
- **Oberfläche:** Calcite → Stone mit Quarz-Einschlüssen und Mondstein-Features
- **MythicMobs:** `MoonElemental` (Nur nachts aktiv, bei Tag verschwindet er), `LunarWolf` (Rudel, extremer Buff bei Vollmond)
- **GeoDiscovery Icon:** `🌙`

### 46. `geoworld:thunder_steppe`
**Die Gewittersteppe`**
Flache Ebene mit permanentem Gewitter – Blitze schlagen ein. Verbrannte
Flecken im Boden zeigen wo Blitze getroffen haben. Kein Unterschlupf.
- **Atmosphäre:** Schwarze Wolken, dauerhafter Donner, Blitzeinschläge
- **Oberfläche:** Grass → Dirt mit verbrannten Patches aus Netherrack
- **MythicMobs:** `ThunderCallerWyvern` (Fliegend, zieht Blitze an), `StormGolem` (Entsteht aus Blitzeinschlägen)
- **GeoDiscovery Icon:** `⚡`

### 47. `geoworld:spore_jungle`
**Der Sporendschungel`**
Tropischer Dschungel, dessen Pflanzen aggressive Sporen ausstoßen.
Jede Pflanze ist leicht leuchtend. Giftiger Nebel am Boden.
- **Atmosphäre:** Grüner Giftdunst, ständiges Prusten der Pflanzen, Sporenregen
- **Oberfläche:** Grass → Dirt mit Jungle-Vegetation und Gift-Sporen-Features
- **MythicMobs:** `SporeVine` (Statisch, spritzt Gift auf Spieler in Nähe), `JungleStalker` (Camouflage, greift aus dem Hinterhalt an)
- **GeoDiscovery Icon:** `🌻`

### 48. `geoworld:rime_coast`
**Die Raureifküste`**
Eisige Küste mit Eisüberzug auf allem. Selbst die Wellen sind eingefroren.
Fisch-Strukturen sichtbar unter dem Eis. Kein Wind, aber extreme Kälte.
- **Atmosphäre:** Staßeisklare Luft, Eiskristall-Klingen, perfekte Stille
- **Oberfläche:** Ice → Packed Ice → Stone mit eingefrorenen Meeresboden-Features
- **MythicMobs:** `FrozenMariner` (Eingefrorener Seemann, erwacht bei Nähe), `IceWhale` (Riesig, passiv bis bedroht, zerstört Eis-Blöcke beim Bewegen)
- **GeoDiscovery Icon:** `🧊`

### 49. `geoworld:ancient_ruins`
**Die Alten Ruinen`**
Verfallene Zivilisation, zerstreut über eine große Ebene. Säulen, Mauern,
gepflasterte Straßen – alles von Natur überwuchert. Schatzkammern tief drin.
- **Atmosphäre:** Stille Ehrfurcht, Wind durch Ruinen, gelegentliche Echos
- **Oberfläche:** Mossy Cobblestone → Stone → Deepslate mit Ruinen-Struktur-Features
- **MythicMobs:** `RuinsCurator` (Schützender Geist, sehr aggressiv bei Diebstahl), `ColossalStatue` (Steinstatue erwacht wenn Spieler Schatz berührt)
- **GeoDiscovery Icon:** `🏛️`

### 50. `geoworld:lava_falls`
**Die Lavafälle`**
Höhlenformation mit spektakulären, breiten Lavafällen von Y 200 bis Y -30.
Das Donnern und Leuchten ist weithin sichtbar. Magma-Seen am Grund.
- **Atmosphäre:** Rotes Glutlicht, Donnern der Lava, Hitzewellen
- **Oberfläche:** Magma → Blackstone → Deepslate mit Lavafall-Features
- **MythicMobs:** `CinderSprite` (Kleiner Feuerdämon, schwamartig), `LavaFallTitan` (Riesiger Boss hinter dem Wasserfall)
- **GeoDiscovery Icon:** `🍋`

---

## 💦 Besondere Höhlen hinter Wasserfällen

> Das Plugin generiert an geeigneten Stellen Wasserfälle – hinter diesen verbergen sich besondere, versteckte Höhlen. Sie sind von außen nicht sichtbar und können nur durch den Wasservorhang betreten werden.

### Merkmale dieser Höhlen

- **Eingang:** Ausschließlich durch einen vom Plugin generierten Wasserfall erreichbar. Der Wasserschleier verdeckt den Eingang vollständig.
- **Generierung:** Die Höhle wird direkt hinter dem Wasserfallblock in den Fels generiert – Größe und Form variieren je nach Biom.
- **Atmosphäre:** Feuchte Wände (Moos, Glow Lichen), leises Tropfen, gedämpftes Licht durch das Wasser.
- **Beleuchtung:** Kein natürliches Licht – nur schwaches Leuchten durch Glow Lichen oder Amethyst-Einschlüsse.

### Inhalte & Belohnungen

Je nach Biom, in dem der Wasserfall liegt, variiert der Inhalt der Höhle:

| Biom-Typ | Mögliche Inhalte |
|---|---|
| Wälder / Ebenen | Kleines Lager, Schatztruhe mit seltenen Materialien, Pilze, Glow Lichen |
| Gebirge / Plateau | Erzadern (Emerald, Diamant), Kristall-Einschlüsse, versteinerte Strukturen |
| Sumpf / Feuchtgebiet | Alchemie-Zutaten, vergiftete Truhen, Moosstrukturen |
| Wüste / Canyon | Antike Grabkammer, Sandstein-Reliefs, seltene Keramik-Loot |
| Eis / Kälte | Eingefrorene Strukturen, gefrorene Truhen, Ice-Kristall-Cluster |
| Mystisch / Märchenhaft | Rätselraum mit Runen, magische Partikeleffekte, Boss-Spawn |

### MythicMobs (Beispiel-Spawns)

- `CaveHermit` – Ein einsamer NPC oder Mob, der die Höhle bewohnt und bei Störung angreift
- `WaterfallSpirit` – Geistiger Wächter des Wasserfalls, erscheint wenn Spieler die Höhle betritt
- `MossGolem` – Kleiner Golem aus Moos und Stein, bewacht die Schatztruhe

### Implementierungshinweis

Die Höhlengenerierung wird vom Plugin beim Setzen eines Wasserfalls automatisch ausgelöst. In der `config.yml` kann unter `waterfall-caves` gesteuert werden, ob und wie häufig Höhlen erscheinen:

```yaml
waterfall-caves:
  enabled: true
  chance: 0.65        # 65% Wahrscheinlichkeit pro generiertem Wasserfall
  min-size: 5         # Minimale Höhlenradius in Blöcken
  max-size: 15        # Maximaler Höhlenradius in Blöcken
  loot-table: "waterfall_cave_loot"
  mob-spawns: true
```

---

## GeoDiscovery config.yml Ergänzung

Diese Biome in `enabled-biomes` in der `config.yml` des GeoDiscovery-Plugins ergänzen:

```yaml
# Custom GeoWorld Biome
enabled-biomes:
  # ... (Vanilla Biome wie gehabt) ...
  # Original 15
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
  # Neue 35
  - geoworld:cursed_bayou
  - geoworld:mangrove_labyrinth
  - geoworld:peat_moors
  - geoworld:dune_sea
  - geoworld:red_canyon
  - geoworld:volcanic_ashfields
  - geoworld:whispering_pines
  - geoworld:haunted_grove
  - geoworld:golden_woodland
  - geoworld:bamboo_highlands
  - geoworld:twilight_canopy
  - geoworld:crystal_caverns
  - geoworld:magma_depths
  - geoworld:echo_chambers
  - geoworld:fungal_depths
  - geoworld:nether_garden
  - geoworld:soul_archive
  - geoworld:magma_delta_rim
  - geoworld:end_coral_reef
  - geoworld:void_sanctum
  - geoworld:elytra_spires
  - geoworld:sunken_city
  - geoworld:volcanic_island
  - geoworld:pearl_lagoon
  - geoworld:starfall_meadow
  - geoworld:dream_forest
  - geoworld:rune_highlands
  - geoworld:eclipse_valley
  - geoworld:mirage_oasis
  - geoworld:moonstone_basin
  - geoworld:thunder_steppe
  - geoworld:spore_jungle
  - geoworld:rime_coast
  - geoworld:ancient_ruins
  - geoworld:lava_falls

region-icons:
  # ... (Vanilla Icons wie gehabt) ...
  # Original 15
  geoworld:abyssal_trench: "🗻"
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
  # Neue 35
  geoworld:cursed_bayou: "💀"
  geoworld:mangrove_labyrinth: "🐸"
  geoworld:peat_moors: "🌫️"
  geoworld:dune_sea: "🏜️"
  geoworld:red_canyon: "🧱"
  geoworld:volcanic_ashfields: "🌫️"
  geoworld:whispering_pines: "🌲"
  geoworld:haunted_grove: "👻"
  geoworld:golden_woodland: "🍂"
  geoworld:bamboo_highlands: "🎋"
  geoworld:twilight_canopy: "🌑"
  geoworld:crystal_caverns: "✨"
  geoworld:magma_depths: "🔥"
  geoworld:echo_chambers: "🔊"
  geoworld:fungal_depths: "🍄"
  geoworld:nether_garden: "🌺"
  geoworld:soul_archive: "📚"
  geoworld:magma_delta_rim: "🌋"
  geoworld:end_coral_reef: "💜"
  geoworld:void_sanctum: "🕳️"
  geoworld:elytra_spires: "🪂"
  geoworld:sunken_city: "🏛️"
  geoworld:volcanic_island: "🌋"
  geoworld:pearl_lagoon: "🪚"
  geoworld:starfall_meadow: "⭐"
  geoworld:dream_forest: "🌈"
  geoworld:rune_highlands: "🔮"
  geoworld:eclipse_valley: "🌑"
  geoworld:mirage_oasis: "🌵"
  geoworld:moonstone_basin: "🌙"
  geoworld:thunder_steppe: "⚡"
  geoworld:spore_jungle: "🌻"
  geoworld:rime_coast: "🧊"
  geoworld:ancient_ruins: "🏛️"
  geoworld:lava_falls: "🍋"
```
