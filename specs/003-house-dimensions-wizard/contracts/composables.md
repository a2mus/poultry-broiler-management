# UI Component Contracts: House Dimensions Wizard Step

**Feature**: 003-house-dimensions-wizard
**Date**: 2026-06-24

## Shared/Reusable Wizard Components

### WizardStepIndicator

Progress badge showing the current wizard step.

```kotlin
@Composable
fun WizardStepIndicator(
    currentStep: Int,           // 1-based step number
    totalSteps: Int,            // Total wizard steps (6)
    modifier: Modifier = Modifier,
)
```

**Visual**: Rounded badge (8dp corners) with `labelSmall` typography showing "Étape {current}/{total}". Uses `primary` color background with `onPrimary` text. Min height 32dp.

**Localization**: String resource `wizard_step_indicator` with format args. Arabic: "الخطوة {current}/{total}".

**Reuse**: All 6 wizard steps.

---

### WizardNavigationBar

Bottom navigation bar with Previous/Next buttons and RTL mirroring.

```kotlin
@Composable
fun WizardNavigationBar(
    canGoNext: Boolean,         // Enables/disables Next button
    canGoPrevious: Boolean,     // Enables/disables Previous button (false on step 1)
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
)
```

**Visual**: Horizontal `Row` at bottom with two `Button` composables. Previous uses `OutlinedButton`; Next uses filled `Button`. Both have 24dp pill corners, 48dp min height. Buttons swap positions in RTL layout via `CompositionLocalProvider(LocalLayoutDirection)`.

**Behavior**: Next button disabled (grayed out) when `canGoNext == false`. Previous hidden or disabled on step 1.

**Reuse**: All 6 wizard steps.

---

### NumericInputField (Enhanced — existing component)

Enhanced version of the existing `presentation/components/NumericInputField.kt`.

```kotlin
@Composable
fun NumericInputField(
    value: String,              // Current text value
    onValueChange: (String) -> Unit,  // NEW: Callback for input changes
    label: String,              // Field label (e.g., "Longueur")
    unitLabel: String,          // Trailing unit (e.g., "m", "mm")
    errorMessage: String?,      // NEW: Inline error text (null = no error)
    enabled: Boolean = true,    // NEW: Enable/disable input
    modifier: Modifier = Modifier,
)
```

**Visual**: `OutlinedTextField` with trailing `Text` for unit label. Error state shows `MaterialTheme.colorScheme.error` border and `errorMessage` text below field in `labelSmall` error color. Uses `KeyboardType.Decimal` and numeric keypad. Min height 48dp.

**Breaking change**: `readOnly = true` removed; `onValueChange` replaces empty lambda. Existing usages (if any) must migrate.

---

## Feature-Specific Components

### HouseDimensionsStep

The Step 1 composable — full form layout for the house dimensions wizard step.

```kotlin
@Composable
fun HouseDimensionsStep(
    formState: DimensionsFormState,
    onIntent: (WizardIntent) -> Unit,
    modifier: Modifier = Modifier,
)
```

**Layout**: Scrollable `Column` containing (in order):
1. Structural dimensions card (Length, Width, Wall Height + calculated Floor Area)
2. Roof type selector with conditional ridge height field
3. Wall material selector
4. Floor type selector
5. Insulation section (type selector + conditional thickness field)
6. Orientation compass selector
7. 2D Preview canvas

**Responsive**: Phone — single column, full-width fields. Tablet — 2-column grid for input pairs (Length/Width in one row, etc.), preview canvas takes full width below.

---

### DimensionPreviewCanvas

Live 2D top-down building outline preview with ruler markers.

```kotlin
@Composable
fun DimensionPreviewCanvas(
    length: Double?,            // Building length in meters (null = not yet entered)
    width: Double?,             // Building width in meters
    orientation: HouseOrientation?,  // Compass direction (null = not yet selected)
    modifier: Modifier = Modifier,
)
```

**Visual**: `Canvas` composable rendering:
- A proportionally scaled rectangle representing the building outline (stroke, no fill)
- Ruler markers along all four edges with dimension labels ("{value}m")
- Compass rose indicator in the top-right corner reflecting selected orientation
- Minimum visible size for very small dimensions (clamp to minimum 40dp rectangle)
- Background uses `surfaceVariant`; outline uses `primary`; rulers use `onSurface`

**Behavior**: Redraws on any parameter change. Maintains aspect ratio. Uses `TextMeasurer` for dimension labels.

**Constraints**: Fixed height (200dp phone, 280dp tablet). Width fills parent.

---

### RoofTypeSelector

Segmented selector for roof type with checkmark cards.

```kotlin
@Composable
fun RoofTypeSelector(
    selectedType: RoofType?,
    onSelect: (RoofType) -> Unit,
    modifier: Modifier = Modifier,
)
```

**Visual**: Horizontal `Row` of 3 selectable cards (Flat, Pitched, Arched). Each card shows an illustrative icon/silhouette and label text. Selected card has `primary` border, checkmark overlay, and `primaryContainer` background. Unselected cards have `outline` border and `surface` background. Card corners 16dp. Touch target 48dp min.

**Content descriptions**: Each card has `contentDescription` for accessibility (e.g., "Toit plat", "Toit à pignon", "Toit voûté").

---

### WallMaterialSelector

Segmented selector for wall material with icon badges.

```kotlin
@Composable
fun WallMaterialSelector(
    selectedMaterial: WallMaterial?,
    onSelect: (WallMaterial) -> Unit,
    modifier: Modifier = Modifier,
)
```

**Visual**: Same card layout pattern as `RoofTypeSelector`. Icons represent Block (brick pattern), Steel (metal sheet), Prefab (panel). Labels below icons.

---

### FloorTypeSelector

Segmented selector for floor type with icon badges.

```kotlin
@Composable
fun FloorTypeSelector(
    selectedType: FloorType?,
    onSelect: (FloorType) -> Unit,
    modifier: Modifier = Modifier,
)
```

**Visual**: Same card layout pattern. Icons represent Concrete (smooth slab), Dirt (earth texture), Slat (grid pattern). Labels below icons.

---

### InsulationConfigSection

Insulation type selector with conditional thickness field.

```kotlin
@Composable
fun InsulationConfigSection(
    selectedType: InsulationType?,
    onSelectType: (InsulationType) -> Unit,
    thickness: String,
    onThicknessChange: (String) -> Unit,
    thicknessError: String?,
    modifier: Modifier = Modifier,
)
```

**Visual**: Insulation type as selectable chips (None, Polystyrène, Polyuréthane, Laine minérale). When type is `NONE`, the thickness `NumericInputField` is hidden with `AnimatedVisibility`. When any other type is selected, the thickness field appears with "mm" trailing label.

**Behavior**: Selecting `NONE` triggers `WizardIntent.SelectInsulationType(NONE)` which clears the thickness value upstream.

---

### OrientationSelector

8-point compass selector for house orientation.

```kotlin
@Composable
fun OrientationSelector(
    selectedOrientation: HouseOrientation?,
    onSelect: (HouseOrientation) -> Unit,
    modifier: Modifier = Modifier,
)
```

**Visual**: Circular arrangement of 8 tappable compass points around a center icon. Selected point highlighted with `primary` color fill and bold label. Unselected points use `outline` color. Center shows a compass rose icon. Each point has a 48dp touch target. Labels use abbreviated direction names (N, NE, E, etc.).

**Alternative layout**: If circular layout proves too complex for accessibility, fall back to a 2-row segmented control: top row [NW, N, NE, E], bottom row [W, S, SW, SE].

---

## WizardScreen Container

### WizardScreen (replaces stub)

```kotlin
@Composable
fun WizardScreen(
    projectId: String,
    onNavigateBack: () -> Unit,
    viewModel: WizardViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
)
```

**Layout**: `Scaffold` with:
- Top: `WizardStepIndicator`
- Content: Step composable selected by `when (uiState.currentStep)` — Step 1 renders `HouseDimensionsStep`
- Bottom: `WizardNavigationBar`

**State collection**: `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`

**Navigation**: `onNavigateBack` callback invoked when user presses system back on step 1.
