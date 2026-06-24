# Quickstart Validation Guide: Project Scaffolding & Design System

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`

---

## Prerequisites

- Android Studio (latest stable) installed
- Android SDK with API 26+ platform installed
- JDK 17+ configured
- An Android emulator (API 26+) or physical device available
- No network connectivity required for any validation scenario

---

## Scenario 1: Clean Build Validation (SC-001, FR-001, FR-012)

**Goal**: Verify the project compiles from a clean clone.

```bash
# Clone and build
git clone <repo-url> && cd poultry-broiler-management
./gradlew assembleDevDebug
```

**Expected outcome**:
- Build completes with **zero errors**
- APK produced at `app/build/outputs/apk/dev/debug/app-dev-debug.apk`
- All dependencies resolved from `gradle/libs.versions.toml` (no `+` or `latest` versions)
- Build time < 5 minutes on standard development machine

**Verification**:
```bash
# Confirm APK exists
ls app/build/outputs/apk/dev/debug/app-dev-debug.apk

# Verify no dynamic dependency versions
grep -r "+" gradle/libs.versions.toml | grep -v "#" | grep -v "kotlin" # should return empty
```

---

## Scenario 2: App Launch & Theme Validation (SC-002, FR-002–FR-006)

**Goal**: Verify the app launches with Forest Teal light theme and design tokens.

1. Install `app-dev-debug.apk` on an API 26+ emulator
2. Launch the app

**Expected outcome**:
- App launches in < 3 seconds
- Home screen visible with Forest Teal light palette colors
- Headings rendered in Outfit font
- Body text rendered in Inter font
- Bottom navigation bar visible with 5 labeled tabs

**Dark mode verification**:
1. Switch emulator to dark mode (Settings → Display → Dark theme)
2. Return to the app

**Expected outcome**:
- All colors transition to Sleek Carbon dark palette
- No hardcoded light-mode colors visible
- Transition completes within 200ms with no visual artifacts

---

## Scenario 3: Navigation Validation (SC-006, FR-007, FR-008)

**Goal**: Verify all 5 navigation destinations work.

1. Launch the app (starts on Home)
2. Tap each bottom navigation tab in sequence: Home → Wizard → Dashboard → Catalog → Settings

**Expected outcome per tab**:
- Each tab displays its corresponding placeholder screen with a label
- No navigation errors or crashes
- Bottom navigation bar remains visible on all screens

**Back navigation**:
1. Navigate to Wizard screen
2. Press system back button

**Expected outcome**:
- App returns to Home screen (start destination)
- No crash or unexpected behavior

---

## Scenario 4: Seed Data Validation (SC-007, FR-009, FR-010)

**Goal**: Verify Room database initializes with seed data on first launch.

1. Uninstall the app (fresh install)
2. Install and launch the app
3. Open Android Studio Database Inspector (View → Tool Windows → App Inspection → Database Inspector)

**Expected outcome** — `breed_profiles` table:
- At least 2 records present
- Row 1: `breed_name = "Ross 308"`, `supplier = "Aviagen"`
- Row 2: `breed_name = "Cobb 500"`, `supplier = "Cobb-Vantress"`
- `growth_targets_json` contains valid JSON arrays

**Expected outcome** — `equipment_items` table:
- Records present across categories: VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING
- All records have non-empty `name`, `category`, and `unit` fields
- Data available within 2 seconds of first launch

Refer to [data-model.md](./data-model.md) for complete field definitions and seed data expectations.

---

## Scenario 5: Localization Validation (SC-010, FR-014, FR-015)

**Goal**: Verify French-first localization.

**French locale**:
1. Set emulator locale to French (Settings → System → Language → Français)
2. Launch the app

**Expected outcome**:
- All navigation labels in French (Accueil, Assistant, Tableau de bord, Catalogue, Paramètres)
- All visible text in French

**Non-supported locale fallback**:
1. Set emulator locale to English
2. Launch the app

**Expected outcome**:
- App displays all text in French (default fallback)
- No English strings visible

**Resource structure**:
```bash
# Verify resource directories exist
ls app/src/main/res/values/strings.xml        # French (default)
ls app/src/main/res/values-fr/strings.xml      # French
ls app/src/main/res/values-ar/                  # Arabic (stub directory exists)
```

---

## Scenario 6: Domain Layer Purity (SC-005, FR-011)

**Goal**: Verify domain layer contains zero Android framework imports.

```bash
# Search for Android imports in domain layer
grep -r "import android\." app/src/main/java/com/poultry/broiler/domain/
grep -r "import androidx\." app/src/main/java/com/poultry/broiler/domain/
```

**Expected outcome**: Both commands return **no results** (empty output).

---

## Scenario 7: CI Pipeline Validation (SC-008, FR-017)

**Goal**: Verify code quality checks pass locally (mirrors CI pipeline).

```bash
# Run the full quality pipeline locally
./gradlew ktlintCheck
./gradlew detekt
./gradlew lint
./gradlew testDevDebugUnitTest
```

**Expected outcome**:
- All 4 commands complete with **zero errors**
- Total execution time < 10 minutes
- CI workflow file exists at `.github/workflows/ci.yml`

---

## Scenario 8: Build Flavors Validation (FR-016)

**Goal**: Verify dev and prod flavors build with distinct configurations.

```bash
# Build both flavors
./gradlew assembleDevDebug
./gradlew assembleProdRelease  # Will fail signing without keystore — that's expected
```

**Expected outcome**:
- `dev` flavor builds successfully
- `prod` flavor compiles (signing failure without keystore is acceptable for scaffolding)
- `dev` flavor has `applicationIdSuffix = ".dev"`
- Source sets `app/src/dev/` and `app/src/prod/` exist for `google-services.json` placement

---

## Scenario 9: Accessibility Validation (FR-019, FR-020)

**Goal**: Verify touch targets and contrast ratios.

1. Launch the app
2. Run Accessibility Scanner (from Play Store or Android Studio)

**Expected outcome**:
- All interactive elements (navigation tabs, buttons) meet 48dp × 48dp minimum touch target
- Body text contrast ratio ≥ 4.5:1
- No accessibility scanner errors for WCAG AA compliance

---

## Scenario 10: Shared Composables Validation (FR-018)

**Goal**: Verify themed shell composables render correctly.

1. Open Android Studio
2. Navigate to `presentation/components/` 
3. Use `@Preview` annotations to render each composable

**Expected outcome**:
- `StatusBadge`: Renders a pill-shaped badge with 8dp corners, themed colors, Inter label text
- `NumericInputField`: Renders an outlined text field with trailing unit label, numeric keyboard type, 48dp+ height
- `BottomSheet`: Renders a modal sheet container with 28dp top corners, drag handle, themed surface color

Refer to [contracts/composables.md](./contracts/composables.md) for exact API signatures and token specifications.
