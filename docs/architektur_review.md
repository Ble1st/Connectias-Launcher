# Architektur-Review: Connectias Launcher

**Reviewer:** Senior Android Developer
**Datum:** 01.12.2025
**Status:** Kritisch geprüft

Der vorliegende Plan (`docs/entwicklungsplan.md`) bildet ein solides Fundament für eine klassische Android-App, weist jedoch für einen **modernen Launcher** im Jahr 2025 signifikante Schwächen auf.

## 1. Technologie-Stack: XML vs. Jetpack Compose (Kritisch)
Der Plan schlägt `RecyclerView` vor. Das ist veraltet für ein Projekt, das **Material 3 Expressive** (M3) nutzen will.
*   **Problem:** M3 Expressive setzt stark auf Motion, Morphing und dynamische Layouts. Das ist mit dem klassischen View-System (`RecyclerView`, `XML`) extrem aufwendig und führt zu viel Boilerplate-Code.
*   **Empfehlung:** **Zwingender Wechsel auf Jetpack Compose.**
    *   `RecyclerView` -> `LazyVerticalGrid`
    *   `LinearLayoutManager` -> `LazyRow`
    *   Animationen in Compose sind deklarativ und deutlich performanter für UI-Übergänge (z.B. Öffnen des Drawers).

## 2. Daten-Aktualisierung & Performance
Der Plan erwähnt das Laden der Apps, aber nicht deren Überwachung.
*   **Problem:** Ein Launcher muss *live* reagieren. Wenn der Nutzer eine App installiert oder deinstalliert, muss der Drawer sofort aktualisiert werden.
*   **Fehlend:** Implementierung eines `BroadcastReceiver` für `ACTION_PACKAGE_ADDED`, `ACTION_PACKAGE_REMOVED` und `ACTION_PACKAGE_CHANGED`.
*   **Cache:** Das reine Laden im ViewModel reicht bei vielen Apps nicht. Ein initiales Laden dauert zu lange. Es wird eine persistente Cache-Strategie (z.B. in Room oder speicheroptimierte In-Memory Cache Singleton) benötigt.

## 3. Widget-Integration (Komplexität unterschätzt)
Der Plan nennt `AppWidgetHost`, geht aber nicht auf die Tücken ein.
*   **Problem:** Widgets benötigen korrekte Lifecycle-Verwaltung (`startListening`, `stopListening`), sonst ziehen sie den Akku leer. Zudem müssen Widget-Konfigurations-Activities (wenn ein Widget beim Hinzufügen Einstellungen fordert) korrekt gehandhabt werden.
*   **Herausforderung:** Integration von `AppWidgetHostView` (klassisches View-Objekt) in eine moderne Compose-Umgebung via `AndroidView`.

## 4. System-Integration & Immersive Mode
*   **Fehlend:** Ein Launcher muss "Edge-to-Edge" zeichnen. Der Plan erwähnt keine Handhabung von **Window Insets**. Der Launcher muss hinter die Statusleiste und Navigationsleiste zeichnen, sonst sieht es unprofessionell aus.
*   **Wallpaper Colors:** Um M3 voll zu nutzen, muss das Theme dynamisch die Farben aus dem Wallpaper extrahieren (Monet Engine).

## 5. Permissions
*   **Fehlend:** Android 11+ erfordert die Berechtigung `QUERY_ALL_PACKAGES` im Manifest, sonst sieht der Launcher keine anderen Apps. Dies ist ein kritischer Blocker.

## Fazit
Der aktuelle Plan ist zu "konservativ". Er beschreibt eine Standard-App, keinen System-Launcher. Um die UX-Ziele (Material 3 Expressive) zu erreichen, muss die Architektur auf **Jetpack Compose** modernisiert werden.
