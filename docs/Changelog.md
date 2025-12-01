# Changelog Connectias Launcher

## Version 0.4.0 - Core Modules (App Drawer & Homescreen)

**Datum:** Montag, 1. Dezember 2025

**Beschreibung:**
Implementierung der Kernfunktionalitäten: App Drawer, Homescreen, Dock und Wallpaper-Unterstützung. Der Launcher ist nun funktional nutzbar (Apps starten, Shortcuts anlegen).

**Wichtige Änderungen:**
*   **App Drawer:**
    *   `AppRepository`: Lädt installierte Apps via `LauncherApps`-API (unterstützt Work Profiles).
    *   `AppDrawerViewModel` & `AppDrawerScreen`: Zeigt Apps in einem Grid an.
    *   Funktion: Starten von Apps durch Antippen, langes Drücken fügt Shortcut zum Homescreen hinzu.
*   **Homescreen:**
    *   `HomescreenRepository`: Lädt Shortcuts aus der Room-DB.
    *   `HomescreenViewModel` & `Homescreen`: Zeigt Shortcuts in einem Grid an und enthält ein statisches Dock.
    *   Funktion: Starten von Shortcuts, Löschen durch langes Drücken (vorläufig).
*   **Wallpaper:**
    *   Themes angepasst (`android:windowShowWallpaper=true`), um das System-Hintergrundbild transparent durchscheinen zu lassen.
*   **Navigation:**
    *   Integration von Jetpack Navigation Compose (`NavHost`) zum Wechsel zwischen Homescreen und App Drawer.
*   **System Services:**
    *   `SystemServiceModule`: Stellt `LauncherApps` und `UserManager` via Hilt bereit.
    *   `material-icons-extended` hinzugefügt für erweiterte Icons.

## Version 0.3.0 - Data Persistence (Room & DataStore)

**Datum:** Montag, 1. Dezember 2025

**Beschreibung:**
Implementierung der persistenten Datenspeicherung für Launcher-Komponenten wie App-Verknüpfungen und Benutzereinstellungen.

**Wichtige Änderungen:**
*   **Room Database:**
    *   `ShortcutEntity`: Definiert die Datenstruktur für App-Verknüpfungen auf dem Homescreen (Paketname, Klassenname, Label, Position, Bildschirm-ID).
    *   `ShortcutDao`: Datenzugriffsobjekt für CRUD-Operationen auf Shortcut-Entitäten.
    *   `LauncherDatabase`: Room-Datenbankklasse, die `ShortcutEntity` und `ShortcutDao` verwaltet.
    *   **Hilt Integration:** `DatabaseModule` zur Bereitstellung von `LauncherDatabase` und `ShortcutDao` über Dependency Injection.
*   **DataStore Preferences:**
    *   `PreferenceRepository`: Verwaltet Benutzereinstellungen wie z.B. das Flag für den ersten Start (`is_first_run`), die Homescreen-Rastergröße (`grid_columns`) und das ausgewählte Icon-Pack (`icon_pack_package`).
    *   **Hilt Integration:** `PreferenceRepository` ist durch `@Inject` Konstruktor Hilt-fähig und wird über `@ApplicationContext` initialisiert.
*   **Build-System:** Aktualisierung von `gradle/libs.versions.toml` und `app/build.gradle.kts` zur Integration der DataStore-Bibliothek.

## Version 0.2.0 - Logging & Safe Mode

**Datum:** Montag, 1. Dezember 2025

**Beschreibung:**
Implementierung der Logging-Strategie und der Fehlerbehandlung, um die Stabilität des Launchers zu gewährleisten.

**Wichtige Änderungen:**
*   **SafeModeRepository:** Implementierung einer Logik zur Erkennung des System-Safe-Modes (`PackageManager.isSafeMode()`) und eines App-internen Crash-Loops.
*   **CrashHandler:** Globaler `UncaughtExceptionHandler`, der Abstürze abfängt, sie via `SafeModeRepository` persistent zählt und dann an den Standard-Handler weitergibt. Verhindert "Boot-Loops", indem nach wiederholten Abstürzen ein Safe-Mode-Zustand erkannt wird.
*   **Timber Logging:**
    *   **Debug:** Volles Logging via `Timber.DebugTree()`.
    *   **Release:** Reduziertes Logging (Crash Reporting Vorbereitung) via `CrashReportingTree`.
*   **MainActivity:** Prüfung auf Safe-Mode beim Start. Warnung im Log und visuelles Feedback (Platzhalter), wenn Safe Mode aktiv ist.

## Version 0.1.0 - Initial Setup & Core Infrastructure

**Datum:** Montag, 1. Dezember 2025

**Beschreibung:**
Initiales Setup des Connectias Launchers mit Fokus auf eine moderne Android-Entwicklungsumgebung basierend auf Kotlin, Jetpack Compose, Hilt und KSP. Der Entwicklungsplan wurde iterativ erstellt und verfeinert.

**Wichtige Änderungen:**

### Entwicklungsplan (`docs/entwicklungsplan.md`):
*   **Initialer Entwurf:** Erstellung eines detaillierten Entwicklungsplans für den Launcher.
*   **Vorschläge integriert (v1):**
    *   Spezifizierung der `QUERY_ALL_PACKAGES`-Nutzung und Play Store-Compliance (`<queries>`).
    *   Umstellung der "Safe Mode"-Erkennung auf `ActivityManager.isInSafeMode()` statt eines reinen Custom-Zählers, inklusive Widget-Deaktivierung im Safe Mode.
    *   Einsatz von `LifecycleObserver` für `AppWidgetManager.startListening`/`stopListening` zur Vermeidung von Memory Leaks.
    *   Nutzung von `Modifier.pointerInput` für Compose Drag & Drop.
    *   Hinzufügen von Instrumented Tests, Mockk und Hilt-Test-Rules, insbesondere für `BroadcastReceiver`-Flows.
*   **Kleinere Optimierungen integriert (v2):**
    *   `detectDragGesturesAfterLongPress` für natürliche Drag & Drop-Gesten.
    *   `MaterialTheme.colorScheme.fromWallpaper()` für präzise Monet-Integration im Wallpaper-Modul.
    *   Ergänzung des Gradle Tasks `./gradlew kspDebugKotlin` für KSP-Validierung in CI/CD.
    *   Einführung von Paging 3 für den App-Drawer bei großen App-Listen.
*   **Letzte Feinheiten integriert (v3):**
    *   Konsequente Nutzung von `viewModelScope`/`lifecycleScope` für Coroutine-Management.
    *   Implementierung von `semantics` für Barrierefreiheit (TalkBack-Kompatibilität) bei `LazyGrids` und Drag & Drop.
    *   Sorgfältige Konfiguration von R8-Regeln für Room DAOs und Hilt zur Vermeidung von Shrinking-Problemen.
*   **"Muss-Features" integriert (v4):**
    *   Erweiterung des App-Drawers um eine schnelle App-Suchfunktion.
    *   Ergänzung der Gestensteuerung (z.B. Swipe-Up für App-Drawer).
    *   Integration von Homescreen-Ordnern und freier Anordnung von Elementen.
    *   Spezifizierung einer Backup & Restore-Strategie (z.B. JSON-Export) für Layouts und Einstellungen.

### Build-System & Projektstruktur:
*   **`libs.versions.toml` aktualisiert:** Hinzufügen von Versionen und Bibliotheken für Jetpack Compose, Hilt, Room, Timber, Coil, Lifecycle, Activity Compose, Navigation Compose und KSP.
*   **Root `build.gradle.kts` aktualisiert:** Plugins für KSP und Hilt hinzugefügt.
*   **App `app/build.gradle.kts` aktualisiert:**
    *   Plugins für KSP und Hilt angewendet.
    *   Compose Build-Feature aktiviert.
    *   `JAVA_VERSION` auf 17 gesetzt.
    *   Neue Dependencies eingebunden.
    *   `buildConfig = true` in `buildFeatures` aktiviert.
    *   `libs.material` Dependency hinzugefügt, um Kompatibilität mit `Theme.MaterialComponents` im XML-Theme sicherzustellen.
*   **Standard-Ordnerstruktur erstellt:** `ui`, `domain`, `data`, `di`, `features` im Hauptpaket.
*   **`ConnectiasLauncherApp.kt` erstellt:** Basis-`Application`-Klasse mit `@HiltAndroidApp` und Timber-Initialisierung.
*   **`AndroidManifest.xml` aktualisiert:** `android:name=".ConnectiasLauncherApp"` gesetzt und `MainActivity` mit Launcher Intent-Filtern deklariert.
*   **XML-Themes (`themes.xml`, `themes-night.xml`) angepasst:** Auf `Theme.MaterialComponents.DayNight.NoActionBar` umgestellt, um die Action Bar zu entfernen.
*   **Compose UI Theme erstellt:** `Color.kt`, `Type.kt`, `Theme.kt` für Material 3 Design.
*   **`MainActivity.kt` erstellt:** Standard `ComponentActivity` mit `@AndroidEntryPoint`, `enableEdgeToEdge()` und `ConnectiasLauncherTheme`.
*   **Erfolgreicher Build:** Das Projekt kompiliert nach den Anpassungen fehlerfrei mit Gradle.
