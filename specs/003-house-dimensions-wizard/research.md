# Research: House Dimensions Wizard Step

**Feature**: 003-house-dimensions-wizard
**Date**: 2026-06-24

## Research Tasks

### 1. Wizard Step Framework Pattern

**Decision**: Multi-step wizard using a single `WizardScreen` composable as container with `WizardViewModel` managing step state, and individual step composables rendered via `when` branch on current step index.

**Rationale**: Compose Navigation nested graphs add unnecessary complexity for wizard flows where all steps share a single ViewModel and data context. A single-screen approach with step composables keeps state management simple, supports animated transitions between steps, and avoids the overhead of managing back stack entries per step. The ViewModel holds all wizard data in a single `WizardUiState` and routes step intents.

**Alternatives considered**:
- **Nested NavHost per step**: Rejected — adds back stack management complexity, each step would need its own ViewModel or shared ViewModel scope, and step transitions would require NavHost animations rather than Compose `AnimatedContent`.
- **HorizontalPager**: Rejected — designed for swipeable content, not form wizards. Would require disabling swipe gestures (form scrolling conflicts) and doesn't naturally support conditional step skipping.

### 2. NumericInputField Enhancement Strategy

**Decision**: Enhance the existing `NumericInputField` component (`presentation/components/NumericInputField.kt`) to accept user input by adding `onValueChange` callback, `errorMessage` parameter for inline validation display, and `enabled` flag. Keep backward compatibility.

**Rationale**: The component already follows design token conventions (48dp min height, trailing unit label, `KeyboardType.Decimal`). It is currently read-only — enhancing it rather than creating a new component avoids duplication and ensures consistent styling across all features that will use numeric inputs.

**Alternatives considered**:
- **New `WizardNumericInput` component**: Rejected — would duplicate styling logic and diverge from the shared component library.
- **Direct `OutlinedTextField` usage**: Rejected — would bypass the established trailing unit label pattern and require repeating configuration in every wizard step.

### 3. 2D Preview Canvas Implementation

**Decision**: Use Compose `Canvas` composable with `drawRect`, `drawLine`, and `drawText` (via `TextMeasurer`) for the top-down building outline. The canvas renders a proportionally scaled rectangle with ruler markers on all four edges showing dimension values. A compass indicator shows the selected orientation.

**Rationale**: Constitution Art 1.3 mandates Compose Canvas API for 2D drawing. The preview is a simplified subset of the full blueprint canvas (Feature #008) — it only shows the building envelope without equipment. Using `Canvas` directly keeps the implementation lightweight and avoids introducing graphics libraries.

**Alternatives considered**:
- **Custom `View` with Android `Canvas`**: Rejected — Constitution Art 1.2.3 prohibits Fragment/View usage; all UI must be Compose.
- **Third-party charting library**: Rejected — Constitution Art 1.4 requires justification for new dependencies; the drawing is simple enough for native `Canvas`.

### 4. HouseDimensions Data Persistence Strategy

**Decision**: Create a new `house_dimensions` Room table with a `projectId` foreign key referencing the `projects` table. One-to-one relationship: each project has at most one `HouseDimensionsEntity`. Data is upserted (insert or replace) on each field change for crash resilience.

**Rationale**: Storing dimensions as a separate entity (rather than adding columns to `ProjectEntity`) follows Clean Architecture separation of concerns. The house dimensions data is specific to the wizard step and will grow with additional fields in future features. A separate table allows the wizard to evolve independently without bloating the Project entity.

**Alternatives considered**:
- **Add columns to `ProjectEntity`**: Rejected — would create a wide table with nullable columns for every wizard step's data; violates single-responsibility principle.
- **JSON blob column on Project**: Rejected — loses query capability, makes migrations harder, and bypasses Room's type safety.

### 5. Room Migration v2 → v3

**Decision**: Create `MIGRATION_2_3` that adds the `house_dimensions` table with all columns and a foreign key constraint to `projects.id` with `CASCADE` delete.

**Rationale**: The existing database is at version 2 (from Feature #002's migration adding `projects`). A new migration is cleaner than destructive recreation and preserves user data.

**Alternatives considered**:
- **Auto-migration**: Rejected — Room auto-migrations don't reliably handle foreign key constraints and new table creation in all edge cases. Explicit SQL migration is more predictable.

### 6. Dual-Layer Validation Architecture

**Decision**: Two validation layers:
1. **Compose layer** (`NumericInputField` with `errorMessage` parameter): Provides immediate visual feedback using simple range checks. Runs on every keystroke via `onValueChange`.
2. **Domain layer** (`ValidateHouseDimensionsUseCase`): Runs comprehensive validation including cross-field rules (e.g., ridge height required when roof type is Pitched) and business constraints. Called before save operations and before "Next" navigation.

**Rationale**: Constitution Art 4.3 mandates dual-layer validation. The Compose layer gives instant UX feedback; the Domain layer enforces business invariants independently of the UI, enabling unit testing without Android framework.

**Alternatives considered**:
- **Single validation layer in ViewModel**: Rejected — violates Constitution Art 4.3 and makes domain validation untestable without ViewModel dependencies.

### 7. Segmented Selector UI Pattern

**Decision**: Custom composable using `Row` of `SelectableChip`-style cards with checkmark overlay and icon badges. Not using Material 3 `SegmentedButton` directly because M3's segmented button doesn't support icon badges with illustrative images as specified in the UI spec.

**Rationale**: The UI spec requires visual icon badges alongside text labels (e.g., building silhouettes for roof types, material textures for wall materials). M3 `SegmentedButton` supports only small leading icons, not the illustrative badge layout needed. A custom composable using design tokens (16dp card corners, 2dp elevation, 48dp touch targets) maintains consistency.

**Alternatives considered**:
- **Material 3 `SegmentedButton`**: Rejected — insufficient icon/badge support for the required visual design.
- **Radio buttons**: Rejected — doesn't match the card-with-icon visual pattern specified in the UI spec.

### 8. Orientation Compass Selector

**Decision**: Custom composable showing 8 compass points (N, NE, E, SE, S, SW, W, NW) arranged in a circular/octagonal layout or as a segmented control. Each point is a tappable target with the selected direction highlighted. The 2D preview canvas reflects the chosen orientation with a compass rose indicator.

**Rationale**: A compass-style selector is the most intuitive way for farmers and consultants to indicate building orientation. The 8-point granularity provides sufficient precision for solar gain calculations without the complexity of a 360° dial.

**Alternatives considered**:
- **Dropdown/spinner**: Rejected — less intuitive for spatial orientation selection.
- **Free-form degree input**: Rejected — adds complexity without proportional benefit; 8 points sufficient for poultry house heating calculations.

## Dependencies Resolution

No new third-party dependencies are required. All functionality is achievable with the existing tech stack established in Features #001 and #002:

| Capability | Solution | Already Available |
|-----------|---------|-------------------|
| 2D Canvas rendering | Compose Canvas API | Yes (Constitution §1.3) |
| Local persistence | Room 2.6+ | Yes (PoultryDatabase v2) |
| DI | Hilt 2.51+ | Yes (DatabaseModule, RepositoryModule) |
| Testing | JUnit 5 + MockK + Turbine | Yes (Feature #001 CI) |
| UI Framework | Jetpack Compose M3 | Yes (Feature #001) |
| Numeric input | NumericInputField (enhanced) | Partial (needs onValueChange) |
