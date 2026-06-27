# Tasks: House Dimensions Wizard Step

**Input**: Design documents from `/specs/003-house-dimensions-wizard/`

**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/composables.md, quickstart.md

**Tests**: Included — plan.md explicitly lists test files and Constitution Art 2.1 mandates ≥80% coverage across Domain and Data layers.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Android mobile app (single `app` module)**: `app/src/main/java/com/poultry/broiler/`
- **Resources**: `app/src/main/res/`
- **Unit tests**: `app/src/test/java/com/poultry/broiler/`
- **Instrumented tests**: `app/src/androidTest/java/com/poultry/broiler/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Create directory structure, domain enums, value classes, and validation types needed by all subsequent phases

- [X] T001 Create directory structure for wizard feature: `domain/model/`, `domain/usecase/`, `domain/validation/`, `domain/repository/`, `data/local/dao/`, `data/local/entity/`, `data/mapper/`, `data/repository/`, `presentation/wizard/`, `presentation/wizard/steps/`, `presentation/wizard/components/`, `di/` under `app/src/main/java/com/poultry/broiler/`
- [X] T002 [P] Create value classes Meters, Millimeters, and SquareMeters in `app/src/main/java/com/poultry/broiler/domain/model/ValueClasses.kt` — `@JvmInline value class` per Constitution Art 2.3
- [X] T003 [P] Create RoofType enum (FLAT, PITCHED, ARCHED) with `displayNameFr` property in `app/src/main/java/com/poultry/broiler/domain/model/RoofType.kt`
- [X] T004 [P] Create WallMaterial enum (BLOCK, STEEL, PREFAB) with `displayNameFr` property in `app/src/main/java/com/poultry/broiler/domain/model/WallMaterial.kt`
- [X] T005 [P] Create FloorType enum (CONCRETE, DIRT, SLAT) with `displayNameFr` property in `app/src/main/java/com/poultry/broiler/domain/model/FloorType.kt`
- [X] T006 [P] Create InsulationType enum (NONE, POLYSTYRENE, POLYURETHANE, MINERAL_WOOL) with `displayNameFr` property in `app/src/main/java/com/poultry/broiler/domain/model/InsulationType.kt`
- [X] T007 [P] Create HouseOrientation enum (N, NE, E, SE, S, SW, W, NW) with `displayNameFr` and `degrees` properties in `app/src/main/java/com/poultry/broiler/domain/model/HouseOrientation.kt`
- [X] T008 [P] Create DimensionField enum and DimensionValidationResult sealed interface (Valid, Invalid with fieldErrors map) in `app/src/main/java/com/poultry/broiler/domain/validation/DimensionValidationResult.kt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core data layer, domain model, Room migration, repository, UI state/intent definitions, and Hilt bindings that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T009 [P] Create HouseDimensions domain model data class with all fields (id, projectId, length, width, wallHeight, roofType, ridgeHeight, wallMaterial, floorType, insulationType, insulationThickness, orientation, createdAt, updatedAt) and computed floorArea property in `app/src/main/java/com/poultry/broiler/domain/model/HouseDimensions.kt`
- [X] T010 [P] Create HouseDimensionsEntity Room entity with @Entity annotation, foreign key to ProjectEntity (CASCADE delete), unique index on projectId, and all columns matching data-model.md schema in `app/src/main/java/com/poultry/broiler/data/local/entity/HouseDimensionsEntity.kt`
- [X] T011 [P] Create HouseDimensionsDao Room DAO interface with getByProjectId (Flow), upsert (REPLACE), and deleteByProjectId methods in `app/src/main/java/com/poultry/broiler/data/local/dao/HouseDimensionsDao.kt`
- [X] T012 [P] Create HouseDimensionsRepository domain interface with getDimensionsByProjectId (Flow), saveDimensions, and deleteDimensionsByProjectId methods in `app/src/main/java/com/poultry/broiler/domain/repository/HouseDimensionsRepository.kt`
- [X] T013 Create HouseDimensionsMapper with toDomain() and toEntity() extension functions (enum string ↔ enum conversion, value class wrapping) in `app/src/main/java/com/poultry/broiler/data/mapper/HouseDimensionsMapper.kt`
- [X] T014 [REOPEN] Create Room MIGRATION_2_3 (CREATE TABLE house_dimensions with all columns, foreign key, unique index) and update PoultryDatabase: bump version to 3, register HouseDimensionsEntity, add migration, expose houseDimensionsDao() abstract function in existing database class — re-opened: Room Gradle Plugin was not registered, causing `room {}` DSL to fail
- [X] T015 Create HouseDimensionsRepositoryImpl implementing HouseDimensionsRepository using HouseDimensionsDao and HouseDimensionsMapper in `app/src/main/java/com/poultry/broiler/data/repository/HouseDimensionsRepositoryImpl.kt`
- [X] T016 [P] Create WizardUiState sealed interface (Loading, Active with currentStep/totalSteps/dimensions/canGoNext/canGoPrevious/saveError, Error) and DimensionsFormState data class with all form fields and fieldErrors map in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardUiState.kt`
- [X] T017 [P] Create WizardIntent sealed interface with all intent subclasses (UpdateLength, UpdateWidth, UpdateWallHeight, UpdateRidgeHeight, SelectRoofType, SelectWallMaterial, SelectFloorType, SelectInsulationType, UpdateInsulationThickness, SelectOrientation, GoNext, GoPrevious) in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardIntent.kt`
- [X] T018 Create WizardModule Hilt module (`@Module @InstallIn(SingletonComponent::class)`) binding HouseDimensionsRepository to HouseDimensionsRepositoryImpl and providing HouseDimensionsDao from PoultryDatabase in `app/src/main/java/com/poultry/broiler/di/WizardModule.kt`
- [X] T019 [P] Add French wizard string resources (field labels: Longueur, Largeur, Hauteur des murs, Hauteur de faîtage, Épaisseur; section headers; step indicator format "Étape %1$d/%2$d"; navigation buttons: Précédent, Suivant; validation error messages; selector labels for all enums) in `app/src/main/res/values/strings.xml` and Arabic stubs in `app/src/main/res/values-ar/strings.xml`

**Checkpoint**: Foundation ready — data layer, domain model, Room migration, repository, UI state definitions, Hilt bindings, and string resources are complete. User story implementation can now begin.

---

## Phase 3: User Story 1 — Enter Building Dimensions (Priority: P1) 🎯 MVP

**Goal**: Consultant enters building length, width, and wall height via numeric inputs with trailing unit labels. Floor area calculates in real time. A live 2D top-down preview renders the building outline with ruler markers.

**Independent Test**: Open a new project, enter length/width/height values, verify floor area calculation and 2D preview update correctly. Change a dimension and confirm recalculation.

### Implementation for User Story 1

- [X] T020 [P] [US1] Create CalculateFloorAreaUseCase (invoke takes Meters length + width, returns SquareMeters rounded to 2 decimals) in `app/src/main/java/com/poultry/broiler/domain/usecase/CalculateFloorAreaUseCase.kt`
- [X] T021 [P] [US1] Create GetHouseDimensionsUseCase (invoke takes projectId, delegates to HouseDimensionsRepository.getDimensionsByProjectId, returns Flow<HouseDimensions?>) in `app/src/main/java/com/poultry/broiler/domain/usecase/GetHouseDimensionsUseCase.kt`
- [X] T022 [P] [US1] Create SaveHouseDimensionsUseCase (invoke takes HouseDimensions, sets updatedAt, delegates to repository.saveDimensions, returns Result<HouseDimensions>) in `app/src/main/java/com/poultry/broiler/domain/usecase/SaveHouseDimensionsUseCase.kt` — initial version saves without domain validation; validation integration added in Phase 6 (US6)
- [X] T023 [US1] Enhance existing NumericInputField composable: add `onValueChange: (String) -> Unit` callback, `errorMessage: String?` parameter for inline error display below field, `enabled: Boolean` flag; keep backward compatibility with trailing unit label, KeyboardType.Decimal, and 48dp min height in `app/src/main/java/com/poultry/broiler/presentation/components/NumericInputField.kt`
- [X] T024 [US1] Create DimensionPreviewCanvas composable using Compose Canvas API: draw proportionally scaled rectangle for building outline, ruler markers on all edges with dimension labels ("{value}m"), minimum 40dp visible size for small dimensions, surfaceVariant background, primary stroke, onSurface rulers, fixed height (200dp phone / 280dp tablet), use TextMeasurer for labels in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/DimensionPreviewCanvas.kt`
- [X] T025 [US1] Create HouseDimensionsStep composable (initial): scrollable Column with structural dimensions card containing Length, Width, Wall Height NumericInputFields and calculated Floor Area display; pass formState and onIntent from parent in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`
- [X] T026 [US1] Create WizardViewModel with @HiltViewModel: inject GetHouseDimensionsUseCase, SaveHouseDimensionsUseCase, CalculateFloorAreaUseCase; expose StateFlow<WizardUiState>; handle UpdateLength/UpdateWidth/UpdateWallHeight intents; recalculate floorArea on dimension change; auto-save to Room on each field change via SaveHouseDimensionsUseCase; load existing dimensions on init via GetHouseDimensionsUseCase in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`
- [X] T027 [US1] Create WizardScreen composable (basic container): accept projectId and onNavigateBack, instantiate WizardViewModel via hiltViewModel(), collect uiState with collectAsStateWithLifecycle, render HouseDimensionsStep based on current step, handle Loading/Error states in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardScreen.kt` — replaces existing stub

### Tests for User Story 1

- [X] T028 [P] [US1] Write unit tests for CalculateFloorAreaUseCase: test basic area calculation, decimal precision (2 places), edge cases (very small values 0.1×0.1, large values 500×100) in `app/src/test/java/com/poultry/broiler/domain/usecase/CalculateFloorAreaUseCaseTest.kt`
- [X] T029 [P] [US1] Write unit tests for HouseDimensionsMapper: test toDomain and toEntity conversions, enum string ↔ enum conversion, value class wrapping/unwrapping, nullable fields (ridgeHeight, insulationThickness) in `app/src/test/java/com/poultry/broiler/data/mapper/HouseDimensionsMapperTest.kt`
- [X] T030 [P] [US1] Write unit tests for HouseDimensionsRepositoryImpl: test getDimensionsByProjectId (Flow emission), saveDimensions (delegates to DAO upsert), deleteDimensionsByProjectId (delegates to DAO), use MockK for DAO in `app/src/test/java/com/poultry/broiler/data/repository/HouseDimensionsRepositoryImplTest.kt`
- [X] T031 [P] [US1] Write unit tests for GetHouseDimensionsUseCase: test delegation to repository, Flow emission of null (no data) and HouseDimensions (existing data) in `app/src/test/java/com/poultry/broiler/domain/usecase/GetHouseDimensionsUseCaseTest.kt`
- [X] T032 [P] [US1] Write unit tests for SaveHouseDimensionsUseCase: test successful save (returns Result.success), timestamp update, delegation to repository in `app/src/test/java/com/poultry/broiler/domain/usecase/SaveHouseDimensionsUseCaseTest.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional — dimensions can be entered, floor area calculates in real time, 2D preview renders, and data persists to Room. This is the MVP.

---

## Phase 4: User Story 2 — Select Roof Type with Conditional Fields (Priority: P1)

**Goal**: Consultant selects roof type (Flat, Pitched, Arched) via segmented selector cards. Selecting Pitched reveals a ridge height input with smooth animation. Switching away from Pitched clears the ridge height value.

**Independent Test**: Select each roof type option, verify ridge height field appears only for Pitched, confirm the 2D preview reflects the roof selection, and verify ridge height is cleared on type switch.

### Implementation for User Story 2

- [X] T033 [P] [US2] Create RoofTypeSelector composable: horizontal Row of 3 selectable cards (Flat/Pitched/Arched) with illustrative icon silhouettes, checkmark overlay on selected card, primary border + primaryContainer background when selected, outline border + surface background when unselected, 16dp card corners, 48dp touch targets, contentDescription for accessibility in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/RoofTypeSelector.kt`
- [X] T034 [US2] Update HouseDimensionsStep to add roof type section below dimensions card: include RoofTypeSelector, add conditional ridge height NumericInputField (trailing "m" label) using AnimatedVisibility that appears when roofType == PITCHED in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`
- [X] T035 [US2] Update WizardViewModel to handle SelectRoofType and UpdateRidgeHeight intents: clear ridgeHeight when switching away from PITCHED (FR-019), update formState.roofType, trigger auto-save in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`

**Checkpoint**: Roof type selection works with conditional field display, smooth animation, and value clearing logic.

---

## Phase 5: User Story 5 — Set House Orientation (Priority: P1)

**Goal**: Consultant selects the house orientation from an 8-point compass selector. The 2D preview reflects the chosen direction with a compass indicator.

**Independent Test**: Select an orientation, verify it is visually highlighted, verify the 2D preview shows a compass/direction indicator, and confirm the value persists when navigating away.

### Implementation for User Story 5

- [X] T036 [P] [US5] Create OrientationSelector composable: circular arrangement of 8 tappable compass points (N, NE, E, SE, S, SW, W, NW) around a center compass rose icon, selected point highlighted with primary color fill and bold label, unselected points use outline color, 48dp touch targets, abbreviated direction labels; fallback to 2-row segmented control if circular layout is inaccessible in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/OrientationSelector.kt`
- [X] T037 [US5] Update HouseDimensionsStep to add orientation compass section below insulation/floor section in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`
- [X] T038 [US5] Update DimensionPreviewCanvas to render compass rose indicator in top-right corner reflecting selected orientation in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/DimensionPreviewCanvas.kt`
- [X] T039 [US5] Update WizardViewModel to handle SelectOrientation intent: update formState.orientation, trigger auto-save in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`

**Checkpoint**: Orientation selection works with compass UI, preview reflects direction, and value persists.

---

## Phase 6: User Story 6 — Input Validation and Error Feedback (Priority: P1)

**Goal**: All numeric inputs are validated in real time with dual-layer validation (Compose field + Domain UseCase). Non-positive values and values exceeding upper bounds show inline errors. The Next button is disabled when any error is present or any field is empty.

**Independent Test**: Enter invalid values (0, negative, exceeding bounds), verify inline errors appear below fields, verify Next is disabled, correct values and verify errors clear and Next enables.

### Implementation for User Story 6

- [X] T040 [US6] Create ValidateHouseDimensionsUseCase: validate length (0 < v ≤ 500), width (0 < v ≤ 100), wallHeight (0 < v ≤ 15), ridgeHeight when PITCHED (0 < v ≤ 20, non-null), ridgeHeight when not PITCHED (must be null), insulationThickness when not NONE (0 < v ≤ 500, non-null), insulationThickness when NONE (must be null); return DimensionValidationResult.Valid or Invalid(fieldErrors) in `app/src/main/java/com/poultry/broiler/domain/usecase/ValidateHouseDimensionsUseCase.kt`
- [X] T041 [US6] Update SaveHouseDimensionsUseCase to call ValidateHouseDimensionsUseCase before save: if invalid return Result.failure with validation errors, if valid proceed with save in `app/src/main/java/com/poultry/broiler/domain/usecase/SaveHouseDimensionsUseCase.kt`
- [X] T042 [US6] Update WizardViewModel to integrate dual-layer validation: add Compose-layer field validation on each UpdateX intent (immediate inline errors), call ValidateHouseDimensionsUseCase for domain validation, compute canGoNext (all fields filled AND no validation errors), populate fieldErrors in DimensionsFormState, handle save error display in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`
- [X] T043 [US6] Update HouseDimensionsStep to pass errorMessage from formState.fieldErrors to each NumericInputField, display inline error text below each invalid field using error color, reflect canGoNext state for Next button interaction in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`

### Tests for User Story 6

- [X] T044 [P] [US6] Write unit tests for ValidateHouseDimensionsUseCase: test all valid input returns Valid, test each field boundary (0, negative, max+1, max), test cross-field rules (ridgeHeight required for PITCHED, insulationThickness required when not NONE), test nullability constraints in `app/src/test/java/com/poultry/broiler/domain/usecase/ValidateHouseDimensionsUseCaseTest.kt`

**Checkpoint**: Validation is fully operational — invalid inputs show inline errors, Next is disabled when form is incomplete or invalid, correcting values clears errors.

---

## Phase 7: User Story 3 — Select Wall Material and Floor Type (Priority: P2)

**Goal**: Consultant selects wall material (Block, Steel, Prefab) and floor type (Concrete, Dirt, Slat) via segmented selectors with visual icon badges. Single selection per category, immediate persistence.

**Independent Test**: Select each wall material and floor type option, verify visual feedback (icon highlighting, checkmark), confirm selections persist when navigating away.

### Implementation for User Story 3

- [X] T045 [P] [US3] Create WallMaterialSelector composable: horizontal Row of 3 selectable cards (Block/Steel/Prefab) with illustrative icon badges (brick pattern, metal sheet, panel), checkmark overlay, same card styling pattern as RoofTypeSelector, 48dp touch targets, contentDescription in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/WallMaterialSelector.kt`
- [X] T046 [P] [US3] Create FloorTypeSelector composable: horizontal Row of 3 selectable cards (Concrete/Dirt/Slat) with illustrative icon badges (smooth slab, earth texture, grid pattern), checkmark overlay, same card styling pattern, 48dp touch targets, contentDescription in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/FloorTypeSelector.kt`
- [X] T047 [US3] Update HouseDimensionsStep to add wall material selector section and floor type selector section (below roof section, above insulation) in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`
- [X] T048 [US3] Update WizardViewModel to handle SelectWallMaterial and SelectFloorType intents: update formState, trigger auto-save in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`

**Checkpoint**: Wall material and floor type selectors work with visual feedback, single-selection behavior, and immediate persistence.

---

## Phase 8: User Story 4 — Configure Insulation Parameters (Priority: P2)

**Goal**: Consultant selects insulation type (None, Polystyrene, Polyurethane, Mineral Wool) and enters thickness in mm. Selecting "None" hides the thickness field and clears its value.

**Independent Test**: Select an insulation type, enter thickness, verify values persist. Select "None" and verify thickness field hides and value clears.

### Implementation for User Story 4

- [X] T049 [US4] Create InsulationConfigSection composable: insulation type as selectable chips (None, Polystyrène, Polyuréthane, Laine minérale), conditional NumericInputField for thickness (trailing "mm" label) using AnimatedVisibility (hidden when type == NONE), pass thicknessError for inline validation in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/InsulationConfigSection.kt`
- [X] T050 [US4] Update HouseDimensionsStep to add insulation configuration section (below floor type, above orientation) in `app/src/main/java/com/poultry/broiler/presentation/wizard/steps/HouseDimensionsStep.kt`
- [X] T051 [US4] Update WizardViewModel to handle SelectInsulationType and UpdateInsulationThickness intents: clear insulationThickness when selecting NONE, show thickness field for other types, trigger auto-save in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`

**Checkpoint**: Insulation configuration works with conditional thickness field, value clearing for NONE, and immediate persistence.

---

## Phase 9: User Story 7 — Wizard Navigation and Progress (Priority: P2)

**Goal**: Progress badge shows "Étape 1/6" at top. Navigation buttons at bottom with Previous disabled on step 1, Next validates before advancing. RTL mirroring for Arabic layouts.

**Independent Test**: Verify progress indicator shows "Étape 1/6", Previous is disabled, Next validates before advancing to Step 2, and layout mirrors correctly in RTL mode.

### Implementation for User Story 7

- [X] T052 [P] [US7] Create WizardStepIndicator composable: rounded badge (8dp corners) with labelSmall typography showing "Étape {current}/{total}" from string resource, primary color background with onPrimary text, 32dp min height in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/WizardStepIndicator.kt`
- [X] T053 [P] [US7] Create WizardNavigationBar composable: horizontal Row with Previous (OutlinedButton) and Next (filled Button), 24dp pill corners, 48dp min height, Previous disabled/hidden when canGoPrevious is false, Next disabled when canGoNext is false, buttons swap positions in RTL via CompositionLocalProvider(LocalLayoutDirection) in `app/src/main/java/com/poultry/broiler/presentation/wizard/components/WizardNavigationBar.kt`
- [X] T054 [US7] Update WizardScreen to full Scaffold layout: top section with WizardStepIndicator, content area with step composable (when branch on currentStep), bottom bar with WizardNavigationBar, wire GoNext/GoPrevious intents, handle navigation to Step 2 on Next in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardScreen.kt`
- [X] T055 [US7] Update WizardViewModel to handle GoNext and GoPrevious intents: enforce all-fields-valid gate before advancing, set canGoPrevious = false on step 1, manage currentStep state in `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardViewModel.kt`
- [X] T056 [US7] Register wizard/{projectId} route in Compose Navigation graph, wire from project creation flow to wizard Step 1, handle system back on step 1 via onNavigateBack callback in existing navigation setup

**Checkpoint**: Wizard navigation is fully operational — progress badge, Previous/Next buttons, validation gate, RTL mirroring, and navigation graph integration are complete.

---

## Phase 10: Polish & Cross-Cutting Concerns

**Purpose**: Comprehensive testing, edge case handling, and verification across all user stories

- [X] T057 [P] Write WizardViewModel unit tests with Turbine: test StateFlow emissions for all intents (dimension updates, selector changes, validation state, canGoNext computation, auto-save triggering, error handling), test Loading → Active state transition, test data restoration on init in `app/src/test/java/com/poultry/broiler/presentation/wizard/WizardViewModelTest.kt`
- [X] T058 [P] Write HouseDimensionsDaoTest (androidTest): test getByProjectId returns correct data, upsert inserts and updates, deleteByProjectId removes data, foreign key cascade from project deletion, unique index on projectId — use Room in-memory database in `app/src/test/java/com/poultry/broiler/data/local/dao/HouseDimensionsDaoTest.kt`
- [X] T059 [P] Write HouseDimensionsStepTest (androidTest): test all form fields render, dimension entry updates floor area display, roof type selection shows/hides ridge height, selector interactions, validation errors display inline, Next button state reflects form validity in `app/src/androidTest/java/com/poultry/broiler/presentation/wizard/HouseDimensionsStepTest.kt`
- [X] T060 Handle edge cases across all stories: preserve all values across device rotation (configuration changes per FR-022), render visible preview for extreme small dimensions (length=0.1, width=0.1), smooth animation on rapid roof type switching without visual glitches, display floor area with many decimal places rounded to 2 decimal places (FR-021), show error Snackbar on Room persistence failure with values retained in form
- [X] T061 Verify responsive layout on phone (compact 4-column grid, single-column fields, full-width scrollable) and tablet (expanded 8-column grid, 2-column input pairs, full-width canvas) form factors — no content clipping or overlapping (SC-006)
- [X] T062 Run quickstart.md validation scenarios (all 10 scenarios) and verify all 7 success criteria pass; run `./gradlew ktlintCheck detekt lint` for code quality; add KDoc documentation to all public interfaces per Constitution Art 2.5

---

## Phase 11: Iteration — 2026-06-25

**Purpose**: Fix build failure caused by missing Room Gradle Plugin registration. The `room { schemaDirectory(...) }` DSL block at `app/build.gradle.kts:55-56` requires the `androidx.room` Gradle plugin to be applied.

- [X] T063 [REOPEN] [FIX] Register and apply Room Gradle Plugin: (1) Add `room = { id = "androidx.room", version.ref = "room" }` to the `[plugins]` section in `gradle/libs.versions.toml`, (2) Add `alias(libs.plugins.room) apply false` to root `build.gradle.kts`, (3) Add `alias(libs.plugins.room)` to the plugins block in `app/build.gradle.kts` — re-opened again: Room Gradle Plugin marker is not resolvable in CI environment

- [X] T064 [REOPEN] [FIX] Move `room { schemaDirectory("$projectDir/schemas") }` block from inside `android { }` to the top level of `app/build.gradle.kts` (same level as `android { }`, `dependencies { }`, `detekt { }`) so the Room Gradle Plugin extension is in scope — re-opened: Room Gradle Plugin approach is not viable in CI environment
- [X] T065 [FIX] Switch Room schema directory configuration to KSP argument: (1) Remove `room {}` block from `app/build.gradle.kts`, (2) Remove `alias(libs.plugins.room)` from `app/build.gradle.kts`, (3) Remove `alias(libs.plugins.room) apply false` from root `build.gradle.kts`, (4) Remove `room` plugin entry from `gradle/libs.versions.toml`, (5) Add `ksp { arg("room.schemaLocation", "$projectDir/schemas") }` to top level of `app/build.gradle.kts`
- [X] T066 [FIX] Move `app/src/main/res/font/README.md` out of the `res/` directory hierarchy (e.g., to `app/FONTS.md` or `app/README.md`) so AAPT2 no longer rejects it during `:app:mergeDevDebugResources`

**Checkpoint**: Run `./gradlew assembleDevDebug --no-daemon` — build must pass configuration phase without `Unresolved reference: room` or `Unresolved reference: schemaDirectory` errors.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on Setup (Phase 1) completion — BLOCKS all user stories
- **US1 (Phase 3)**: Depends on Foundational (Phase 2) — MVP target
- **US2 (Phase 4)**: Depends on Phase 2; benefits from US1 (WizardViewModel/Step exist) but can be coded independently
- **US5 (Phase 5)**: Depends on Phase 2; benefits from US1 (DimensionPreviewCanvas exists for compass)
- **US6 (Phase 6)**: Depends on Phase 2 and US1 (adds validation to existing form + ViewModel)
- **US3 (Phase 7)**: Depends on Phase 2; independent of other user stories
- **US4 (Phase 8)**: Depends on Phase 2; independent of other user stories
- **US7 (Phase 9)**: Depends on Phase 2 and US1 (WizardScreen container exists to enhance)
- **Polish (Phase 10)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (P1)**: Can start after Phase 2 — No dependencies on other stories. **MVP target.**
- **US2 (P1)**: Can start after Phase 2 — Adds to HouseDimensionsStep/ViewModel from US1. Recommend sequential after US1.
- **US5 (P1)**: Can start after Phase 2 — Adds to HouseDimensionsStep/ViewModel. Recommend sequential after US2.
- **US6 (P1)**: Can start after Phase 2 — Cross-cutting validation. Recommend after US1/US2/US5 so all fields exist.
- **US3 (P2)**: Can start after Phase 2 — Independent selectors. Can parallel with US4.
- **US4 (P2)**: Can start after Phase 2 — Independent insulation section. Can parallel with US3.
- **US7 (P2)**: Can start after Phase 2 — Wizard framework. Recommend after US6 (canGoNext depends on validation).

### Within Each User Story

- Models/Use Cases before ViewModel updates
- ViewModel updates before composable updates
- Component composables before step integration
- Tests can run in parallel with each other (different files)

### Parallel Opportunities

- All Setup tasks T002–T008 are marked [P] and can run in parallel
- Foundational Phase: T009, T010, T011, T012, T016, T017, T019 can start in parallel; T013, T014, T015, T018 have sequential dependencies
- US1: T020, T021, T022 (use cases) can run in parallel; T028–T032 (tests) can all run in parallel
- US2: T033 (component) can start while T034, T035 run sequentially
- US3: T045, T046 (components) can run in parallel
- US5: T036 (component) can start while T037–T039 run sequentially
- US7: T052, T053 (components) can run in parallel
- Polish: T057, T058, T059 (test files) can all run in parallel

---

## Parallel Example: User Story 1

```text
# Launch all use cases for US1 in parallel (different files):
Task: T020 "Create CalculateFloorAreaUseCase in domain/usecase/CalculateFloorAreaUseCase.kt"
Task: T021 "Create GetHouseDimensionsUseCase in domain/usecase/GetHouseDimensionsUseCase.kt"
Task: T022 "Create SaveHouseDimensionsUseCase in domain/usecase/SaveHouseDimensionsUseCase.kt"

# Then sequential: ViewModel depends on use cases
Task: T026 "Create WizardViewModel in presentation/wizard/WizardViewModel.kt"

# Launch all US1 tests in parallel (different test files):
Task: T028 "Write CalculateFloorAreaUseCaseTest"
Task: T029 "Write HouseDimensionsMapperTest"
Task: T030 "Write HouseDimensionsRepositoryImplTest"
Task: T031 "Write GetHouseDimensionsUseCaseTest"
Task: T032 "Write SaveHouseDimensionsUseCaseTest"
```

## Parallel Example: User Story 3

```text
# Launch both selector components in parallel (different files):
Task: T045 "Create WallMaterialSelector in presentation/wizard/components/WallMaterialSelector.kt"
Task: T046 "Create FloorTypeSelector in presentation/wizard/components/FloorTypeSelector.kt"

# Then sequential: step integration depends on both components
Task: T047 "Update HouseDimensionsStep to add wall material and floor type sections"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (8 tasks)
2. Complete Phase 2: Foundational (11 tasks)
3. Complete Phase 3: User Story 1 (13 tasks)
4. **STOP and VALIDATE**: Enter dimensions, verify floor area calculation, verify 2D preview, verify persistence
5. Deploy/demo MVP — consultant can enter and save building dimensions

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add US1 → Test independently → **MVP!** (dimensions + area + preview)
3. Add US2 → Test independently → Roof type with conditional fields
4. Add US5 → Test independently → Orientation compass + preview
5. Add US6 → Test independently → Full validation + error feedback
6. Add US3 → Test independently → Wall material + floor type selectors
7. Add US4 → Test independently → Insulation configuration
8. Add US7 → Test independently → Navigation bar + progress indicator
9. Polish → Edge cases, comprehensive tests, quickstart validation

### Recommended Single-Developer Order

P1 stories first (US1 → US2 → US5 → US6), then P2 stories (US3 → US4 → US7), then Polish:

1. Phase 1 + Phase 2 → Foundation
2. Phase 3 (US1) → MVP checkpoint
3. Phase 4 (US2) → Roof selection
4. Phase 5 (US5) → Orientation
5. Phase 6 (US6) → Validation lock-down
6. Phase 7 (US3) + Phase 8 (US4) → Material selectors (can interleave)
7. Phase 9 (US7) → Wizard framework polish
8. Phase 10 → Final verification

---

## Notes

- [P] tasks = different files, no dependencies on other tasks in same phase
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- All styling MUST use design tokens from Theme.kt (Constitution Art 3.1) — zero hardcoded colors, fonts, or spacing
- All strings MUST be in resource files (Constitution Art 8.2) — zero hardcoded strings in composables
- Domain layer MUST NOT import Android framework classes (Constitution Art 7.3)
- All interactive elements MUST have 48dp touch targets (Constitution Art 3.3)
- NumericInputField enhancement (T023) is backward-compatible — verify no regressions in existing usages

## Phase 12: Iteration - 2026-06-26

**Purpose**: Fix Room Database Schema Mismatch in the pre-packaged seed database.

- [X] T067 [FIX] Updated scripts/build-seed-db.sh: equipment_items category index renamed from `idx_equipment_category` to `index_equipment_items_category` to match Room schema 3.json. (breed_profiles index and both `id` NOT NULL constraints were already correct in the working tree; only the equipment_items index name was outstanding.)
- [X] T068 [FIX] Regenerated the pre-packaged database app/src/main/assets/seed/poultry.db by running scripts/build-seed-db.sh. Verified indices (`index_breed_profiles_breed_name`, `index_equipment_items_category`) and row counts (2 breed profiles, 6 equipment items) match Room schema 3.json.

**Checkpoint**: Run `./gradlew assembleDevDebug` — APK builds successfully with corrected seed database.

---

## Phase 13: Iteration - 2026-06-27

**Purpose**: Verify database fix by reinstalling the app on the device.

- [X] T069 [FIX] Verify app launches successfully: (1) Uninstall old APK from device/emulator via `adb uninstall com.poultry.broiler`, (2) Install latest APK from `app/build/outputs/apk/dev/debug/app-dev-debug.apk`, (3) Launch app and verify it reaches the home screen without crash, (4) Confirm database schema validation passes in logcat. — re-opened/superseded by Phase 14 because Room threw a new schema mismatch on the autoindex.

**Checkpoint**: App launches successfully with zero `FATAL EXCEPTION` in logcat, home screen displays correctly.

---

## Phase 14: Iteration - 2026-06-27 (2)

**Purpose**: Fix Room Database Schema Mismatch (breed_profiles autoindex conflict) in the pre-packaged seed database.

- [X] T070 [FIX] Update `scripts/build-seed-db.sh` to remove the redundant `UNIQUE` column constraint.
- [X] T071 [FIX] Regenerate the `poultry.db` using the corrected script (via a cross-platform Python script `build-seed-db.py` to ensure correct carriage return line endings and execution on Windows).
- [X] T072 [FIX] Verify app launches successfully after database regeneration: (1) Uninstall old APK from device/emulator via `adb uninstall com.poultry.broiler`, (2) Install latest APK from `app/build/outputs/apk/dev/debug/app-dev-debug.apk`, (3) Launch app and verify it reaches the home screen without crash, (4) Confirm database schema validation passes in logcat.

**Checkpoint**: App launches successfully with zero `FATAL EXCEPTION` in logcat, home screen displays correctly.
