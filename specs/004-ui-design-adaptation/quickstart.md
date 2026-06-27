# Quickstart Validation Guide: UI Design Adaptation

**Feature**: 004-ui-design-adaptation

**Date**: 2026-06-27

---

## Prerequisites

- Android Studio with Kotlin 1.9+
- Project builds successfully
- Device or emulator running API 26+ (Android 8.0)

---

## Setup

1. **Verify build configuration**: Ensure Outfit and Inter fonts are loaded in resource directories (`res/font/`).
2. **Launch App**: Open the app. The landing home screen should load and reflect the new glare-reducing color scheme.

---

## Validation Scenarios

### Scenario 1: Light & Dark Theme Configuration
1. Open the Android settings menu.
2. Toggle between Light Mode and Dark Mode.
3. Observe all screens (Home, Wizard, Dashboard tabs).
4. **Expected**:
   - Light Mode displays Soft Pearl (`#f7f9ff`) surfaces with Forest Teal (`#00685c`) primary details.
   - Dark Mode displays Deep Obsidian (`#0C1013`) background with Vibrant Mint (`#2ECC71`) highlights.

### Scenario 2: Home Screen Bento Grid Layout
1. Open the Home screen.
2. Verify that active project cards display location, capacity, status badge, and compliance score as a grid of responsive cards.
3. Perform a long-press on a project card.
4. **Expected**:
   - The card scales down to `98%` with a subtle ripple effect.
   - The new project card has a dashed border and "+" icon.

### Scenario 3: House Dimensions Wizard Step 1
1. Tap the "+" card on the Home screen to open the wizard.
2. Observe Step 1: Dimensions screen.
3. Modify the Length and Width fields.
4. **Expected**:
   - Trailing units "m" remain aligned inside inputs.
   - The total estimated surface area and 2D outline canvas preview scale dynamically under 300ms.

### Scenario 4: Arabic RTL Translation & Layout
1. Change the system language/locale to Arabic (`ar`).
2. Re-open the app and navigate to the Home screen and Wizard steps.
3. **Expected**:
   - The layout mirrors completely (hamburger menu on the right, title aligned to the right).
   - Bottom navigation items flow right-to-left.
   - Next/Previous button positions are flipped.

### Scenario 5: Interactive 2D Blueprint Zooming
1. Navigate to the Blueprint dashboard tab.
2. View equipment symbols (propeller fan, fire heater, feed lines, water lines).
3. Perform pinch-to-zoom and pan gestures.
4. **Expected**:
   - Canvas scales smoothly from `0.5x` up to `4.0x`.
   - Grid background and legend labels remain legible.

---

## Test Commands

```bash
# Verify theme colors & resource definitions compile
./gradlew assembleDebug

# Run theme & layout verification tests
./gradlew testDebugUnitTest --tests "com.poultry.broiler.presentation.theme.*"

# Run UI instrumentation tests (requires active emulator/device)
./gradlew connectedDebugAndroidTest --tests "com.poultry.broiler.presentation.home.ProjectListTest"
./gradlew connectedDebugAndroidTest --tests "com.poultry.broiler.presentation.wizard.HouseDimensionsStepTest"

# Verify coding style and lint rules
./gradlew ktlintCheck detekt lint
```
