# Connectias Launcher - Entwicklungsplan v3.0

Dieser Plan beschreibt die aktualisierte Architektur und Implementierungsschritte für den **Connectias Launcher** (Android, Kotlin, Material 3 Expressive). Er integriert moderne Build-Tools (KSP), strukturiertes Logging (Timber) und Sicherheitsmechanismen.

## 1. Architektur & Basis-Setup
**Ziel:** Ein robustes, modulares MVVM-Fundament, optimiert für Performance und moderne UI.

*   **Architektur-Pattern:** MVVM (Model-View-ViewModel).
*   **Sprache:** Kotlin.
*   **UI Framework:** **Jetpack Compose** (Deklaratives UI).
*   **DI (Dependency Injection):** Hilt (Nutzung von KSP für Code-Generierung).
*   **Build & Code Gen:** **KSP (Kotlin Symbol Processing)** anstelle von KAPT für schnellere Builds und bessere Typ-Sicherheit (z.B. für Room, Hilt).
*   **Logging:** **Timber** als Logging-Fassade für konsistentes, verwaltbares Logging.
*   **Logging:** Coil (für asynchrones Laden von Icons/Wallpapern).
*   **Coroutine Scopes:** Konsequente Nutzung von `viewModelScope` und `lifecycleScope` für Repository-Aufrufe, um Coroutines an den Lebenszyklus von ViewModel/UI zu binden und Ressourcen effizient zu verwalten (insbesondere bei Home-Screen-Pausen).

### Aufgaben:
- [ ] Gradle-Dependencies aktualisieren:
    -   Compose BOM & Material 3.
    -   Hilt & Hilt Compiler (KSP).
    -   Room & Room Compiler (KSP).
    -   **Timber**.
    -   **KSP Plugin** integrieren.
- [ ] Projektstruktur anlegen (`ui`, `domain`, `data`, `di`, `features/...`).
- [ ] Basis-Theme (Material 3 Expressive, Dynamic Colors).
- [ ] **Timber Initialisierung** in der `Application`-Klasse implementieren.
- [ ] **Window Insets** Handling für Edge-to-Edge.

## 2. Logging-Strategie & Fehlerbehandlung
**Ziel:** Transparenz im Fehlerfall ohne Performance-Verlust im Release.

*   **Timber Konfiguration:**
    -   **Debug Builds:** Verwendung des `Timber.DebugTree()` für ausführliche Logs im Logcat.
    -   **Release Builds:** Einsatz eines `CrashReportingTree` (bzw. leeren Trees), der nur WARN/ERROR loggt und diese ggf. an ein Crash-Reporting-Tool weiterleitet (nicht ins öffentliche Logcat).
*   **Safe Mode & Crash Handling:**
    -   **System-Safe-Mode Erkennung:** Nutzung von `ActivityManager.isInSafeMode()` um festzustellen, ob das System im abgesicherten Modus gestartet wurde.
    -   **Verhalten im Safe Mode:** Vollständige Deaktivierung von Widgets und Drittanbieter-Plugins, um Boot-Loops zu verhindern. Abstimmung mit Androids Boot-Prozess statt reinem Custom-Crash-Counter.

## 3. Datenhaltung (Persistence Layer)
**Ziel:** Reaktive Speicherung mit performanter Code-Generierung via KSP.

*   **Code Generation:** Nutzung von **KSP** für Room-DAOs und Database-Implementierungen.
*   **Einstellungen:** `DataStore` für User-Settings.
*   **Datenbank:** `Room` Database für Widgets, Shortcuts und Caches.
*   **Backup & Restore:** Implementierung einer Export/Import-Funktion (z.B. via JSON) für Homescreen-Layouts und Einstellungen, zusätzlich zum Android Auto-Backup.

## 4. Kern-Module

### A. Wallpaper-Modul
*   **Logik:** `WallpaperManager` & `ContentResolver`.
*   **Farben:** Präzise Monet-Integration mittels `MaterialTheme.colorScheme.fromWallpaper()`.
*   **UI:** Compose-Screens.

### B. App-Drawer
*   **Daten:** `PackageManager`, `LauncherApps`.
*   **Updates:** `BroadcastReceiver` für App-Installationen.
*   **Performance:** `LazyVerticalGrid` (Compose) + **Coil** für Icon-Loading.
*   **Suche:** Integrierter, reaktiver Suchdialog für Apps (und optional Kontakte/Einstellungen) mit "Search-as-you-type" Vorschlägen.
*   **Paging:** Einsatz von Paging 3 für App-Drawer-Listen, insbesondere wenn `QUERY_ALL_PACKAGES` eine große Anzahl von Apps liefert, um Speicherverbrauch und Ladezeiten zu optimieren.
*   **Berechtigungen & Play Store Compliance:**
    -   `QUERY_ALL_PACKAGES` Permission im Manifest.
    -   Ergänzung von spezifischen `<queries>` Deklarationen für robustes App-Discovery (Fallback).
    -   **Play Console:** Vorbereitung der Begründung "Core Purpose" (Launcher) für den Play Store Review, um Ablehnung zu vermeiden.
*   **Accessibility:** Implementierung von `semantics` für `LazyVerticalGrid` und interaktive Elemente, um vollständige TalkBack-Kompatibilität zu gewährleisten (entscheidend für Launcher).

### C. Dock, Gesten & Navigation
*   **UI:** `LazyRow` (Compose).
*   **Gestensteuerung:** Implementierung gängiger Launcher-Gesten (z.B. Swipe-Up für App-Drawer, Swipe-Down für Benachrichtigungen).
*   **Drag & Drop:** Implementierung mittels Compose `Modifier.pointerInput` mit `detectDragGesturesAfterLongPress` für natürliche Launcher-Gesten (Long-Press → Drag).
*   **Accessibility:** Sicherstellung der TalkBack-Kompatibilität durch Hinzufügen entsprechender `semantics` zu Drag & Drop-Zielen und `LazyRow`-Elementen.

### D. Homescreen & Widgets
*   **Personalisierung:** Freie Anordnung von Shortcuts und Widgets im Grid-Layout. Unterstützung für **Ordner** (Gruppierung von Apps).
*   **Integration:** `AndroidView` Wrapper für `AppWidgetHostView`.
*   **Lifecycle Management:** Implementierung eines `LifecycleObserver`, um `AppWidgetHost.startListening()` und `stopListening()` korrekt an den Lifecycle zu binden und Memory Leaks zu vermeiden.
*   **Safe Mode:** Deaktivierung der Widget-Hosts, wenn `isInSafeMode()` true ist.

## 5. Erweiterbarkeit & Schnittstellen
*   **Interfaces:** Nutzung von Interfaces für Repositories.
*   **Modularität:** Feature-Package-Struktur.

## 6. Qualitätssicherung & CI/CD
*   **Unit Tests:** JUnit 5 & Mockk.
*   **Instrumented Tests:**
    -   Tests für Launcher-spezifische Flows (z.B. App-Installation via `BroadcastReceiver`).
    -   Nutzung von **Hilt-Test-Rules** und **Mockk** (Android) für stabile Integrationstests.
*   **Logging Validierung:** Sicherstellen, dass im Release-Build keine sensiblen Daten via Timber geloggt werden.
*   **KSP Validierung:** Überprüfung der generierten Klassen (in `build/generated/ksp`) auf Korrektheit während des Builds, mittels Gradle Task `./gradlew kspDebugKotlin`.
*   **ProGuard/R8:** Sorgfältige Konfiguration der R8-Regeln für Room DAOs und Hilt (insbesondere KSP-generierte Klassen), um Shrinking-Probleme im Release-Build zu vermeiden.

## Nächste Schritte
1.  `build.gradle.kts` (Root & App) aktualisieren: KSP Plugin, Timber, Hilt/Room (KSP), Compose.
2.  Application-Klasse erstellen und Timber `plant()` aufrufen.
3.  Basis-Architektur aufsetzen.