# Critical Review v2.0: Connectias Launcher Plan

**Reviewer:** Senior Android Architect
**Datum:** 01.12.2025
**Basis:** Entwicklungsplan v2.0 (Jetpack Compose Focus)

Dieser Review bewertet den aktualisierten Plan spezifisch auf Realisierbarkeit und Projektmanagement-Aspekte.

## 1. Technische Machbarkeit
**Gesamtstatus:** Hoch, aber mit spezifischen "Pain Points".

*   **Compose & Widgets (Risiko: Hoch):**
    *   **Herausforderung:** Das Einbetten von `AppWidgetHostView` (klassisches View-System) in `AndroidView` (Compose) funktioniert grundsätzlich, ist aber fehleranfällig bei Gesten.
    *   **Problem:** Scroll-Konflikte zwischen der Widget-Liste (Compose LazyColumn) und scrollbaren Widgets (z.B. Gmail Widget). Das Event-Dispatching muss hier manuell sehr sauber gelöst werden.
*   **Drag-and-Drop in Compose (Risiko: Mittel):**
    *   **Herausforderung:** Anders als beim alten `ItemTouchHelper` gibt es in Compose noch keine voll etablierte Standard-Lösung für Grid-Reordering, die *perfekt* performt.
    *   **Lösung:** Wahrscheinlich ist der Einsatz einer externen Library (z.B. `reorderable`) oder sehr komplexer Boilerplate-Code notwendig, um Items zwischen Drawer und Dock zu verschieben.
*   **Icon Loading Performance:**
    *   Das Laden von hunderten Icons im Drawer darf den UI-Thread nicht blockieren. Der Plan erwähnt Caching, aber die *asynchrone Dekodierung* der Icons in Bitmaps muss explizit über eine Library wie **Coil** erfolgen, sonst ruckelt das Scrollen massiv.

## 2. Ressourcenzuweisung
*   **Skill-Gap Compose:** Der Wechsel zu Compose ist richtig, verlangt aber tiefes Verständnis von `State`-Management (Recomposition). Ein Launcher hat extrem viele State-Updates (Download-Fortschritt, App-Installationen, Uhrzeit). Wenn hier "zu viel" recomposed wird, wird das Gerät heiß.
*   **Memory Management:** Ein Launcher läuft 24/7.
    *   **Ressource:** Es muss Zeit für Profiling (Memory Leaks) eingeplant werden. Bitmaps (Wallpaper, Icons) sind die Hauptursache für `OutOfMemoryErrors`.

## 3. Zeitplan-Realismus
Der Plan ist ambitioniert. Folgende Zeitfresser werden oft übersehen:

*   **Grid-Raster-Berechnung:** Widgets haben keine fixen Pixelgrößen, sondern "Cells". Die Umrechnung von `dp` in `Cells` auf verschiedenen Displaygrößen (Handy vs. Tablet) ist reine Mathematik und trial-and-error. **Faktor x2 auf die Zeitplanung für das Home-Screen-Grid.**
*   **System-Inkonsistenzen:** Samsung OneUI verhält sich anders als Pixel UI oder Xiaomi. Das Abrufen von Wallpaper-Farben oder Statusleisten-Transparenz erfordert oft herstellerspezifische Hacks.

## 4. Risikominimierung
*   **Der "Crash Loop of Death":**
    *   **Risiko:** Wenn der Launcher beim Start abstürzt, ist das Telefon für den normalen Nutzer unbedienbar.
    *   **Mitigation:** Einbau eines "Safe Mode". Wenn der Launcher 3x in Folge beim Start crasht, muss er alle Widgets deaktivieren und Einstellungen zurücksetzen. Das fehlt im Plan.
*   **Datenverlust bei Updates:**
    *   Änderungen am `Room`-Schema für das Grid-Layout sind kritisch. Eine fehlerhafte Migration löscht den Homescreen des Nutzers. Strikte Teststrategie für Datenbank-Migrationen ist Pflicht.

## 5. Erfolgskriterien (KPIs)
Die im Prompt genannten "Messbaren Ziele" müssen technisch konkretisiert werden:

*   **Cold Start Time:** < 500ms (Der Launcher muss sofort da sein, wenn man "Home" drückt).
*   **Battery Drain:** < 1% pro Stunde im Hintergrund.
*   **Jank Stats:** 99% der Frames müssen unter 16ms gerendert werden (60fps), besonders beim Öffnen des Drawers.

## Fazit & Empfehlung
Der Plan ist technisch valide (State-of-the-Art), aber das **Risiko liegt in der Detailimplementierung der Hybrid-Komponenten** (Widgets in Compose) und der **Performance-Optimierung**.

**Genehmigung:** Unter Vorbehalt. Der Fokus in Phase 1 muss auf einem stabilen Core liegen, bevor komplexe Features wie Widget-Resizing gebaut werden.
