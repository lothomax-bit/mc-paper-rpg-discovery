# GeoDiscovery & Naming Plugin for Minecraft Paper

Ein innovatives RPG-Erkundungs-Plugin für Minecraft Paper-Server, das es Spielern ermöglicht, die Spielwelt dynamisch zu entdecken und geografischen Strukturen (wie Bergen, Seen, Meeren und Höhlen) eigene Namen zu geben.

---

## 🌟 Hauptmerkmale (Features)

*   **Geografische Biom-Erkennung:** Das Plugin erkennt im Hintergrund automatisch, wenn ein Spieler eine signifikante geografische Struktur betritt (z. B. Hochgebirge, Flüsse, Ozeane, tiefe Höhlensysteme).
*   **Einmaliges Entdeckungs-System:** Befindet sich der Spieler in einem Areal, das noch nie zuvor von jemandem benannt wurde, wird er als offizieller Entdecker registriert.
*   **Interaktives Benennungs-Popup:** Der Entdecker erhält eine Chat-Aufforderung, die Region mit dem Befehl `/taufe [Name]` dauerhaft zu benennen.
*   **Dezente Umgebungs-Anzeige:** Betritt später ein beliebiger Spieler diese Region, wird der vergebene Name dezent und klein in der Actionbar (über der Hotbar) angezeigt (z. B. `⛰️ Die Nebelberge (Entdeckt von Gomme)`).
*   **Effiziente Speicherung:** Alle benannten Regionen werden ressourcenschonend mit ihren Zentrum-Koordinaten und einem definierten Erkennungs-Radius in einer lokalen Datenbank gespeichert.

---

## 🛠️ Technische Spezifikationen (Für Entwickler / Google Jules)

### 1. Architektur & Performance
*   **Plattform:** Paper / Spigot API (Version 1.21+)
*   **Sprache:** Java 17+ (oder höher)
*   **Build-System:** Maven oder Gradle
*   **Asynchronität:** Alle Datenbank-Abfragen und Schreibvorgänge (I/O) müssen asynchron laufen, um Server-Lags zu vermeiden.

### 2. Datenstruktur & Datenbank (SQLite)
Das Plugin nutzt eine lokale SQLite-Datenbank (`locations.db`), um die Daten zu verwalten. 

**Tabelle: `discovered_regions`**
*   `id` (INTEGER, Primary Key, Auto-Increment)
*   `region_name` (VARCHAR) – Der vom Spieler gewählte Name
*   `discoverer_uuid` (VARCHAR) – UUID des Spielers, der es entdeckt hat
*   `discoverer_name` (VARCHAR) – Letzter bekannter Name des Spielers
*   `world_uuid` (VARCHAR) – Die Welt (Overworld, Nether, End)
*   `center_x` (DOUBLE) – X-Koordinate des Entdeckungspunktes
*   `center_z` (DOUBLE) – Z-Koordinate des Entdeckungspunktes
*   `radius` (INTEGER) – Der Bereich (z.B. 150 Blöcke), in dem die Region als "betreten" gilt
*   `timestamp` (DATETIME) – Datum und Uhrzeit der Entdeckung

### 3. Logik der Erkennung
Anstatt Chunks zu zählen, reagiert das Plugin auf das `PlayerMoveEvent` (optimiert, um nicht bei jedem Mikroschritt zu triggern, z. B. nur beim Wechsel von Block-Koordinaten) oder prüft periodisch die Position des Spielers:
1. Prüfe, ob das aktuelle Biom auf der Blacklist (z.B. normale `PLAINS`) oder der Whitelist steht (z.B. `JAGGED_PEAKS`, `RIVER`).
2. Berechne den Abstand (2D-Distanz auf X/Z) zu bereits existierenden Regionen in der Datenbank.
3. Ist der Spieler weiter entfernt als der `radius` aller bekannten Regionen, gilt das Gebiet als **unentdeckt**.

---

## 💻 Befehle & Berechtigungen (Commands & Permissions)

*   `/taufe [Name]` – Ermöglicht es dem Entdecker, der Region innerhalb eines Zeitfensters einen Namen zu geben.
    *   *Permission:* `geodiscovery.player.taufe`
*   `/geodiscovery reload` – Lädt die Konfiguration des Plugins neu (Nur für Admins).
    *   *Permission:* `geodiscovery.admin.reload`

---

## ⚙️ Geplante Konfiguration (`config.yml`)

```yaml
# GeoDiscovery Konfiguration

# Aktivierte Biome für Entdeckungen
enabled-biomes:
  - JAGGED_PEAKS
  - STONY_PEAKS
  - FROZEN_PEAKS
  - RIVER
  - DEEP_LUKE
  - OCEAN
  - DEEP_DARK

# Radius in Blöcken um den Entdeckungspunkt herum
region-radius: 150

# Zeit in Sekunden, die ein Spieler nach Entdeckung Zeit hat, den Ort zu benennen
naming-timeout: 60

# Nachrichten-Format für die Actionbar
messages:
  actionbar-enter: "§a§lEntering: §f%name% §7(Entdeckt von %player%)"
  discovery-prompt: "§6§lNeuland entdeckt! §7Nutze §b/taufe [Name]§7, um diesen Ort zu benennen."
```

---

## 🎯 Nächste Schritte für Google Jules

1. Erstelle die `pom.xml` / `build.gradle` mit den Paper-API Abhängigkeiten.
2. Implementiere die `Plugin`-Hauptklasse und die SQLite-Datenbank-Anbindung.
3. Schreibe den `PlayerMoveListener` für die Biom- und Distanzprüfung.
4. Erstelle die Befehlsstruktur für `/taufe`.
