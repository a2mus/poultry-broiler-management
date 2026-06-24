# Quickstart Validation Guide: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## Prerequisites

- Feature #001 (Project Scaffolding & Design System) fully implemented and merged
- Android Studio with Kotlin 1.9+, Compose BOM, Room 2.6+, Hilt 2.51+
- Device or emulator running Android API 26+ (Android 8.0 Oreo)
- No network connectivity required (fully offline feature)

## Build & Run

```bash
# Build the debug variant
./gradlew assembleDevDebug

# Install on connected device/emulator
./gradlew installDevDebug

# Launch the app — Home Screen should appear as the entry point
adb shell am start -n com.poultry.broiler.dev/.MainActivity
```

## Validation Scenarios

### V1: Empty State on First Launch

1. Fresh install the app (clear data if previously installed).
2. **Expected**: Home Screen displays empty state with "+" dashed-border placeholder card and guidance text. Bottom navigation bar shows 4 tabs with Projects tab highlighted.

### V2: Create a New Installation Project

1. Tap the "+" placeholder card (or the FAB).
2. **Expected**: Creation dialog/bottom sheet opens with name field, type selector (New Installation / Existing Assessment), and optional location field.
3. Enter name: "Ferme El Baraka — Bâtiment 3".
4. Select type: "New Installation".
5. Enter location: "Blida, Algeria".
6. Tap confirm.
7. **Expected**: App navigates to the wizard stub screen. Press back.
8. **Expected**: Home Screen shows the new project card with "NEW" badge, "Draft" status, name, location, and creation date.

### V3: Create an Existing Assessment Project

1. Tap the FAB.
2. Enter name: "Ferme Saida — Évaluation".
3. Select type: "Existing Assessment".
4. Tap confirm.
5. **Expected**: App navigates to the dashboard stub screen. Press back.
6. **Expected**: Home Screen shows 2 project cards. "Ferme Saida" card has "EXISTING" badge. Cards are sorted with most recently modified first.

### V4: Validation — Empty Name Rejected

1. Tap FAB to open creation dialog.
2. Leave name field empty, tap confirm.
3. **Expected**: Inline error message displayed, dialog remains open, no project created.

### V5: Search and Filter

1. Ensure at least 2 projects exist (from V2 and V3).
2. Tap the search bar and type "Baraka".
3. **Expected**: Only "Ferme El Baraka" card is shown. Search is case-insensitive.
4. Clear the search text.
5. **Expected**: All projects displayed again.
6. Type a query that matches nothing (e.g., "ZZZZZ").
7. **Expected**: "No results" empty state with suggestion to clear the filter.

### V6: Edit Project Metadata

1. Long-press on "Ferme El Baraka" card.
2. **Expected**: Card performs scale-down animation, context menu appears with Edit, Duplicate, Delete.
3. Select "Edit".
4. **Expected**: Dialog opens pre-filled with current name and location. Type field shown as read-only.
5. Change name to "Ferme El Baraka — Bât. 3 (Rénové)".
6. Tap save.
7. **Expected**: Card immediately reflects the updated name.

### V7: Duplicate a Project

1. Long-press on the renamed project card.
2. Select "Duplicate".
3. **Expected**: New card appears with name "Ferme El Baraka — Bât. 3 (Rénové) (Copy)", status "Draft", same location and type.
4. **Expected**: The copy appears at the top of the list (most recently modified).

### V8: Delete a Project

1. Long-press on the duplicated copy card.
2. Select "Delete".
3. **Expected**: Confirmation dialog appears with the project name and warning message.
4. Tap confirm.
5. **Expected**: Project disappears from the Home Screen.
6. Force-stop and relaunch the app.
7. **Expected**: Deleted project is gone. Remaining projects persist correctly.

### V9: Tap to Open Existing Project

1. Tap on "Ferme El Baraka" card (type: New Installation).
2. **Expected**: Navigates to wizard stub screen.
3. Press back. Tap on "Ferme Saida" card (type: Existing Assessment).
4. **Expected**: Navigates to dashboard stub screen.

### V10: Bottom Navigation Tabs

1. Tap the "Design" tab.
2. **Expected**: Placeholder screen for Design displayed.
3. Tap "Reports" tab, then "Health" tab.
4. **Expected**: Each shows its placeholder screen.
5. Tap "Projects" tab.
6. **Expected**: Home Screen with project list displayed.

### V11: Data Persistence Across Restarts

1. Create 3 projects with distinct names.
2. Force-stop the app (`adb shell am force-stop com.poultry.broiler.dev`).
3. Relaunch.
4. **Expected**: All 3 projects present with correct metadata.
5. Repeat 3 times (total 10 restarts per SC-004).

### V12: Performance — 50+ Projects

1. Use a test utility or script to insert 55 projects into the Room database.
2. Launch the app.
3. **Expected**: Home Screen loads all cards within 1 second, smooth scrolling with no frame drops.
4. Type a search query.
5. **Expected**: Results update within 300ms.

## Running Tests

```bash
# Unit tests (UseCases, Repository, ViewModel, Mapper)
./gradlew testDevDebugUnitTest

# DAO integration tests (requires connected device/emulator)
./gradlew connectedDevDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.poultry.broiler.data.local.dao.ProjectDaoTest

# Compose UI tests (requires connected device/emulator)
./gradlew connectedDevDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.poultry.broiler.presentation.home.HomeScreenTest

# All tests
./gradlew testDevDebugUnitTest connectedDevDebugAndroidTest

# Coverage report
./gradlew jacocoTestReport
```

## Success Criteria Verification Matrix

| Criterion | Validation Scenario | How to Measure |
|-----------|-------------------|----------------|
| SC-001: Create in <30s | V2 | Stopwatch from app launch to card visible |
| SC-002: Load <1s with 50+ | V12 | Android Profiler or stopwatch |
| SC-003: Search <300ms | V12 | Android Profiler frame timing |
| SC-004: CRUD across 10 restarts | V11 | Manual restart cycle |
| SC-005: Data survives termination | V8, V11 | Force-stop + relaunch verification |
| SC-006: Find project in <5s | V5 | Stopwatch with 20+ projects |
| SC-007: ≤3 inputs to create | V2 | Count: name, type, (location) = 3 max |
