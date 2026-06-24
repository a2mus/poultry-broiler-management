# Quickstart Validation Guide: House Dimensions Wizard Step

**Feature**: 003-house-dimensions-wizard
**Date**: 2026-06-24

## Prerequisites

- Android Studio with Kotlin 1.9+ and Compose BOM configured
- Project builds successfully with Features #001 and #002 complete
- Device or emulator running API 26+ (Android 8.0)
- Room database at version 2 (existing `projects` table with seed data)

## Setup

1. **Verify database migration**: After building, the app should upgrade Room from v2 → v3 without crash. The `house_dimensions` table is created with a foreign key to `projects`.

2. **Create a test project**: From the Home Screen, tap the "+" card to create a new project of type "Nouvelle Installation". Note the `projectId` — this project will be used for wizard validation.

3. **Navigate to wizard**: After creation, the app should navigate to `wizard/{projectId}`. The wizard Step 1 (House Dimensions) should render.

## Validation Scenarios

### Scenario 1: Dimension Entry and Floor Area Calculation

**Steps**:
1. Enter length = `100` in the Length field
2. Enter width = `12` in the Width field
3. Enter wall height = `3` in the Wall Height field

**Expected**:
- Each field shows a trailing "m" label
- Floor area displays "1 200,00 m²" (French locale formatting)
- 2D preview canvas shows a scaled rectangle with ruler markers "100m" × "12m"
- Numeric keypad appears for all three fields

**Verify**: Change width to `15` → floor area updates to "1 500,00 m²" and canvas resizes proportionally.

### Scenario 2: Roof Type with Conditional Ridge Height

**Steps**:
1. Tap "Plat" (Flat) roof type
2. Tap "À pignon" (Pitched) roof type
3. Enter ridge height = `5`
4. Tap "Voûté" (Arched) roof type

**Expected**:
- Step 1: "Plat" shows checkmark; no ridge height field visible
- Step 2: "À pignon" shows checkmark; ridge height field appears with animation
- Step 3: Ridge height field accepts input with "m" trailing label
- Step 4: "Voûté" shows checkmark; ridge height field disappears; ridge height value cleared

### Scenario 3: Material and Floor Selectors

**Steps**:
1. Tap "Parpaing" (Block) wall material
2. Tap "Acier" (Steel) wall material
3. Tap "Caillebotis" (Slat) floor type

**Expected**:
- Step 1: Block card highlighted with checkmark and active icon badge
- Step 2: Block deselected; Steel highlighted
- Step 3: Slat floor type highlighted; selection persisted immediately

### Scenario 4: Insulation Configuration

**Steps**:
1. Tap "Polystyrène" insulation type
2. Enter thickness = `50`
3. Tap "Aucune" (None) insulation type

**Expected**:
- Step 1: Polystyrene selected; thickness field appears with "mm" label
- Step 2: Thickness accepts input and shows "50 mm"
- Step 3: None selected; thickness field hidden; thickness value cleared

### Scenario 5: Orientation Selection

**Steps**:
1. Tap "NE" (Nord-Est) compass point
2. Observe the 2D preview canvas

**Expected**:
- NE compass point highlighted with primary color
- 2D preview shows compass indicator reflecting NE orientation

### Scenario 6: Input Validation

**Steps**:
1. Enter length = `0`
2. Enter length = `600`
3. Select "À pignon" roof type and leave ridge height empty
4. Tap "Next"

**Expected**:
- Step 1: Inline error "La valeur doit être supérieure à 0" below length field; Next disabled
- Step 2: Inline error "La valeur ne doit pas dépasser 500m" below length field
- Step 3+4: Inline error "La hauteur de faîtage est requise pour les toits à pignon"; Next stays disabled

**Verify**: Correct all values → errors disappear → Next becomes enabled.

### Scenario 7: All Fields Mandatory

**Steps**:
1. Fill only length, width, and wall height with valid values
2. Observe "Next" button state

**Expected**: "Next" remains disabled until ALL mandatory fields are filled: dimensions, roof type, wall material, floor type, insulation type (+ thickness if not "None"), and orientation.

### Scenario 8: Data Persistence

**Steps**:
1. Fill all fields with valid values
2. Press the Android home button (background the app)
3. Return to the app
4. Navigate away from the wizard (back button)
5. Navigate back to the wizard for the same project

**Expected**: All entered values are preserved in both cases (app backgrounding and wizard re-entry).

### Scenario 9: Wizard Navigation

**Steps**:
1. Observe the progress indicator at step load
2. Complete all fields with valid values
3. Tap "Next"

**Expected**:
- Progress badge shows "Étape 1/6"
- "Previous" button is disabled or hidden (this is step 1)
- "Next" advances to Step 2

### Scenario 10: RTL Layout (Arabic)

**Steps**:
1. Change device language to Arabic
2. Navigate to the wizard step

**Expected**:
- "Next" button appears on the left side; "Previous" on the right
- All text and labels are right-aligned
- Canvas coordinates remain mathematically consistent (not mirrored)

## Test Commands

```bash
# Unit tests (domain + data layers)
./gradlew testDebugUnitTest --tests "com.poultry.broiler.domain.usecase.ValidateHouseDimensionsUseCaseTest"
./gradlew testDebugUnitTest --tests "com.poultry.broiler.domain.usecase.CalculateFloorAreaUseCaseTest"
./gradlew testDebugUnitTest --tests "com.poultry.broiler.domain.usecase.SaveHouseDimensionsUseCaseTest"
./gradlew testDebugUnitTest --tests "com.poultry.broiler.data.repository.HouseDimensionsRepositoryImplTest"
./gradlew testDebugUnitTest --tests "com.poultry.broiler.data.mapper.HouseDimensionsMapperTest"
./gradlew testDebugUnitTest --tests "com.poultry.broiler.presentation.wizard.WizardViewModelTest"

# DAO integration tests (requires emulator/device)
./gradlew connectedDebugAndroidTest --tests "com.poultry.broiler.data.local.dao.HouseDimensionsDaoTest"

# UI tests (requires emulator/device)
./gradlew connectedDebugAndroidTest --tests "com.poultry.broiler.presentation.wizard.HouseDimensionsStepTest"

# Full test suite
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest

# Lint + static analysis
./gradlew ktlintCheck detekt lint
```

## Success Criteria Verification

| SC | Criterion | How to Verify |
|----|-----------|---------------|
| SC-001 | Floor area visible within 5s | Time from step load to area display after entering L+W |
| SC-002 | Canvas updates <300ms | Visually confirm smooth updates; no lag on dimension change |
| SC-003 | 100% data preserved | Background app, rotate device, navigate away — all values remain |
| SC-004 | Validation <200ms | Enter invalid value — error appears immediately, no perceptible delay |
| SC-005 | Complete step <2min | Time a first-use scenario filling all fields and tapping Next |
| SC-006 | Phone + tablet render | Test on both form factors — no clipping or overlap |
| SC-007 | RTL buttons mirrored | Switch to Arabic — Next/Previous swap sides correctly |
