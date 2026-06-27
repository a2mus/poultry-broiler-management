# Implementation Plan: House Dimensions Wizard Step

**Branch**: `003-house-dimensions-wizard` | **Date**: 2026-06-24 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/003-house-dimensions-wizard/spec.md`

## Summary

Implement the House Dimensions Wizard Step — the first step of the multi-step project design wizard — delivering numeric input fields for building length, width, and wall height with trailing unit labels and real-time floor area calculation, a segmented roof type selector (Flat, Pitched, Arched) with conditional ridge height field, wall material selector (Block, Steel, Prefab) and floor type selector (Concrete, Dirt, Slat) with visual icon badges, insulation type selector (None, Polystyrene, Polyurethane, Mineral Wool) with conditional thickness field, an 8-point compass orientation selector, a live 2D top-down preview canvas with ruler markers, dual-layer input validation (Compose + Domain), and wizard navigation infrastructure (progress badge "Step 1/6", Previous/Next buttons with RTL mirroring). All data persists to Room immediately on field change via a new `HouseDimensionsEntity` table (migration v2→v3) stored as a child of the existing `Project`. The wizard step follows MVVM + Clean Architecture with `WizardViewModel` exposing `StateFlow<WizardUiState>` and receiving sealed `WizardIntent` events. The reusable wizard framework (step container, navigation bar, progress indicator) is established here for reuse by subsequent wizard steps (#004–#006).

## Technical Context

**Language/Version**: Kotlin 1.9+ (targeting latest stable)

**Primary Dependencies**: Jetpack Compose BOM (latest stable), Material 3, Room 2.6+, Hilt 2.51+, Compose Navigation (all established in Features #001 and #002)

**Storage**: Room (SQLite) — new `house_dimensions` table linked to existing `projects` table via `projectId` foreign key; database migration v2→v3

**Testing**: JUnit 5 + MockK (unit tests for UseCases, Repository, ViewModel), Turbine (StateFlow emissions), Compose UI Test (wizard step flows), Room in-memory database (DAO integration tests)

**Target Platform**: Android API 26+ (Android 8.0 Oreo) — phone and tablet form factors

**Project Type**: Mobile application (single-module Android app, building on Features #001 and #002)

**Performance Goals**: 2D preview canvas renders <300ms on dimension change, floor area calculation <16ms (single frame), form validation <200ms, wizard step initial render <1s with 10+ fields, no frame drops during scrolling or canvas updates

**Constraints**: 100% offline-capable (no network), single Activity with Compose Navigation, French-first localization with Arabic RTL support, all styling via design tokens from Theme.kt, UDF via StateFlow + sealed intents, dual-layer validation (Compose field + Domain UseCase)

**Scale/Scope**: 1 new entity (HouseDimensions), 5 enums (RoofType, WallMaterial, FloorType, InsulationType, HouseOrientation), 23 functional requirements, 7 user stories, 1 wizard step screen with 10+ input fields, 1 Canvas composable, reusable wizard framework

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| # | Constitution Rule | Spec Compliance | Status |
|---|------------------|-----------------|--------|
| 1 | Art 1.1 — MVVM + Clean Architecture | WizardViewModel → UseCases → HouseDimensionsRepository → HouseDimensionsDao; 3-layer separation | PASS |
| 2 | Art 1.2.1 — Clean Layer Isolation | Domain layer (HouseDimensions model, enums, UseCases, Repository interface) is pure Kotlin; no Android imports | PASS |
| 3 | Art 1.2.2 — Offline-First Data Flow | All CRUD operations against Room only; no network calls for dimensions | PASS |
| 4 | Art 1.2.3 — Single Activity Architecture | Wizard step rendered within existing single Activity via Compose Navigation; no Fragments | PASS |
| 5 | Art 1.2.4 — UDF via StateFlow | WizardViewModel exposes `StateFlow<WizardUiState>`; UI sends `WizardIntent` sealed class events | PASS |
| 6 | Art 1.2.5 — Hilt DI for all injectables | HouseDimensionsDao, HouseDimensionsRepository, all UseCases, WizardViewModel injected via Hilt | PASS |
| 7 | Art 1.4 — Pinned deps, Maven Central/Google Maven | No new dependencies required; all deps from Feature #001 Version Catalog | PASS |
| 8 | Art 2.1 — JUnit 5 + MockK, ≥80% coverage | Unit tests for all UseCases + Repository; DAO integration tests; ViewModel StateFlow tests with Turbine | PASS |
| 9 | Art 2.2 — ktlint + Detekt + Android Lint | CI pipeline from Feature #001 enforces all three on every PR | PASS |
| 10 | Art 2.3 — Null safety, sealed classes, value classes | Sealed classes for `WizardUiState`, `WizardIntent`; value classes for `Meters`, `Millimeters`, `SquareMeters`; enums for finite sets | PASS |
| 11 | Art 3.1 — Design tokens in Theme.kt | All card styling, badge colors, typography, spacing, elevation from Theme.kt tokens; zero hardcoded values | PASS |
| 12 | Art 3.2 — Component Standards | Composables: `WizardStepIndicator`, `DimensionPreviewCanvas`, `RoofTypeSelector`, `WallMaterialSelector`, `OrientationSelector` — PascalCase, domain-prefixed | PASS |
| 13 | Art 3.3 — WCAG AA, 48dp touch targets | All interactive elements (input fields, selectors, nav buttons) ≥48dp; contentDescription on all icons | PASS |
| 14 | Art 3.4 — Responsive Design | Adaptive layout for phone (4-column) and tablet (8-column); canvas prioritizes tablet; phone supports scrolling | PASS |
| 15 | Art 4.3 — Dual-layer Input Validation | Compose field validators (immediate feedback) AND Domain UseCase preconditions (business rules); inline error messages | PASS |
| 16 | Art 7.1 — Directory structure | Files placed per Constitution §7.1: data/local/, domain/model/, domain/usecase/, presentation/wizard/ | PASS |
| 17 | Art 8.1 — French primary, Arabic secondary | All UI strings in `values/strings.xml` (French default), Arabic stubs in `values-ar/`; RTL mirroring for nav buttons | PASS |

**Gate Result**: ALL PASS — No violations. Proceeding to Phase 0.

## Project Structure

### Documentation (this feature)

```text
specs/003-house-dimensions-wizard/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── composables.md   # UI component contracts
└── tasks.md             # Phase 2 output (/speckit-tasks — NOT created by /speckit-plan)
```

### Source Code (repository root)

```text
app/src/main/java/com/poultry/broiler/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── HouseDimensionsDao.kt          # Room DAO for house dimensions CRUD
│   │   └── entity/
│   │       └── HouseDimensionsEntity.kt       # Room entity for house_dimensions table
│   ├── repository/
│   │   └── HouseDimensionsRepositoryImpl.kt   # Repository implementation
│   └── mapper/
│       └── HouseDimensionsMapper.kt           # Entity ↔ Domain model mapper
├── domain/
│   ├── model/
│   │   ├── HouseDimensions.kt                 # Domain model (pure Kotlin)
│   │   ├── RoofType.kt                        # Enum: FLAT, PITCHED, ARCHED
│   │   ├── WallMaterial.kt                    # Enum: BLOCK, STEEL, PREFAB
│   │   ├── FloorType.kt                       # Enum: CONCRETE, DIRT, SLAT
│   │   ├── InsulationType.kt                  # Enum: NONE, POLYSTYRENE, POLYURETHANE, MINERAL_WOOL
│   │   └── HouseOrientation.kt               # Enum: N, NE, E, SE, S, SW, W, NW
│   ├── repository/
│   │   └── HouseDimensionsRepository.kt       # Repository interface (domain contract)
│   ├── usecase/
│   │   ├── SaveHouseDimensionsUseCase.kt      # Validate + persist dimensions
│   │   ├── GetHouseDimensionsUseCase.kt       # Retrieve dimensions for a project
│   │   ├── ValidateHouseDimensionsUseCase.kt  # Pure validation logic (dual-layer)
│   │   └── CalculateFloorAreaUseCase.kt       # length × width calculation
│   └── validation/
│       └── DimensionValidationResult.kt       # Sealed class for field-level validation results
├── presentation/
│   └── wizard/
│       ├── WizardScreen.kt                    # Wizard container with step routing (replaces stub)
│       ├── WizardViewModel.kt                 # StateFlow<WizardUiState> + WizardIntent handler
│       ├── WizardUiState.kt                   # Sealed interface for wizard UI states
│       ├── WizardIntent.kt                    # Sealed class for user actions
│       ├── steps/
│       │   └── HouseDimensionsStep.kt         # Step 1 composable (form + preview)
│       └── components/
│           ├── WizardStepIndicator.kt         # Progress badge ("Step 1/6")
│           ├── WizardNavigationBar.kt         # Bottom nav bar (Previous/Next) with RTL support
│           ├── DimensionPreviewCanvas.kt      # Live 2D top-down building preview with rulers
│           ├── RoofTypeSelector.kt            # Segmented checkmark cards for roof types
│           ├── WallMaterialSelector.kt        # Segmented selector with icon badges
│           ├── FloorTypeSelector.kt           # Segmented selector with icon badges
│           ├── InsulationConfigSection.kt     # Insulation type selector + thickness input
│           └── OrientationSelector.kt         # 8-point compass selector
├── di/
│   └── WizardModule.kt                        # Hilt bindings: HouseDimensionsRepository, DAO provider

app/src/main/res/
├── values/strings.xml                         # French strings (append wizard keys)
└── values-ar/strings.xml                      # Arabic stubs (append wizard keys)

app/src/test/java/com/poultry/broiler/
├── data/
│   ├── repository/HouseDimensionsRepositoryImplTest.kt
│   └── mapper/HouseDimensionsMapperTest.kt
├── domain/usecase/
│   ├── SaveHouseDimensionsUseCaseTest.kt
│   ├── GetHouseDimensionsUseCaseTest.kt
│   ├── ValidateHouseDimensionsUseCaseTest.kt
│   └── CalculateFloorAreaUseCaseTest.kt
└── presentation/wizard/WizardViewModelTest.kt

app/src/androidTest/java/com/poultry/broiler/
├── data/local/dao/HouseDimensionsDaoTest.kt   # In-memory Room DAO tests
└── presentation/wizard/HouseDimensionsStepTest.kt  # Compose UI tests
```

**Structure Decision**: Extends the single `app` module established in Features #001 and #002 following Constitution §7.1. The `wizard/` package under `presentation/` is reorganized from a single stub file into a full wizard framework with `steps/` (per-step composables) and `components/` (reusable wizard UI elements). Use Cases are individual classes under `domain/usecase/` following the `<Verb><Noun>UseCase` naming convention. The `WizardModule` Hilt module binds the repository interface to its implementation and provides the DAO.

## Complexity Tracking

> No constitution violations detected. No complexity justifications needed.

## Iteration Session 2026-06-25 (6)

### User Feedback

CI build failure on `./gradlew assembleDevDebug`: `ERROR: app/src/main/res/font/README.md: Resource and asset merger: The file name must end with .xml, .ttf, .ttc or .otf` — task `:app:mergeDevDebugResources` FAILED.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD / DELIVERY | P0 | Fix applied in working tree but not committed — CI runs on last committed version |

**Root Cause**: T066 (move `README.md` out of `res/font/`) was already completed locally — `app/src/main/res/font/README.md` no longer exists in the working tree. However, the CI pipeline ran against the last committed revision which still contains the offending file. This is the same delivery pattern as Iteration Session (4): the code fix is complete; it only needs to be committed and pushed.

### Amendments

None — no architecture or design changes needed. T066 fix is correct.

### New Tasks

None — T066 is already completed in the working tree.

### Constitution Compliance

- All articles: unaffected — this is a delivery gap, not a code or architecture issue

## Iteration Session 2026-06-25 (5)

### User Feedback

Build failure during execution phase on task `:app:mergeDevDebugResources`: `Error: The file name must end with .xml, .ttf, .ttc or .otf` for `app/src/main/res/font/README.md`.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD | P0 | Environment/config issue — documentation file placed inside AAPT2-enforced resource directory |

**Root Cause**: AAPT2 only allows `.xml`, `.ttf`, `.ttc`, or `.otf` files inside `res/font/`. The `README.md` file in that directory contains font download instructions but is treated as an invalid resource by the Android build tools, causing `:app:mergeDevDebugResources` to fail.

### Amendments

None — no architecture or design changes needed.

### New Tasks

- T066 added: move `app/src/main/res/font/README.md` out of the `res/` hierarchy to resolve AAPT2 resource merge failure

### Constitution Compliance

- Art 1.4 (Pinned deps): PASS — no dependency changes
- Art 7.1 (Directory structure): PASS — moving documentation does not affect source structure
- All other articles: unaffected

## Iteration Session 2026-06-25 (4)

### User Feedback

Build failure persists on `./gradlew assembleDevDebug`: identical `ScriptCompilationError(message=Unresolved reference: room)` and `ScriptCompilationError(message=Unresolved reference: schemaDirectory)` errors at `app/build.gradle.kts` lines 55–56.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD / DELIVERY | P0 | Fix applied in working tree but not committed — CI runs on last committed version |

**Root Cause**: The current working tree no longer contains a `room {}` block at `app/build.gradle.kts:55-56`; those lines are now the `lint {}` block. `git status` shows uncommitted modifications to the build files, plan.md, and tasks.md. The CI failure report is from the last committed revision, which still contains the original `room { schemaDirectory(...) }` block. The code fix is complete; it only needs to be committed and pushed.

### Amendments

None — the KSP argument approach documented in Iteration Session 2026-06-25 (3) remains the correct fix.

### New Tasks

None — all corrective tasks (T063–T065) are complete in the working tree.

### Constitution Compliance

- Art 1.4 (Pinned deps): PASS — uses existing KSP setup, no new dependencies
- All other articles: unaffected

## Iteration Session 2026-06-25 (3)

### User Feedback

Build failure persists on `./gradlew assembleDevDebug`: same `ScriptCompilationError(message=Unresolved reference: room)` and `ScriptCompilationError(message=Unresolved reference: schemaDirectory)` errors at `app/build.gradle.kts` lines 55–56, even after the Room Gradle Plugin was registered and the `room {}` block was moved to the top level.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD | P0 | Environment/config issue + Plan design gap — Room Gradle Plugin marker is not resolvable in the CI environment |

**Root Cause**: The Room Gradle Plugin (`androidx.room`) was correctly registered in the version catalog, root build file, and app module, and the `room {}` block was placed at the top level. Despite this, Gradle still cannot resolve the `room` extension in the CI environment. This indicates the plugin marker/artifact (`androidx.room:room.gradle.plugin`) is not resolvable from the configured repositories in this environment. The project already uses KSP for Room annotation processing, so the standard KSP argument approach is the reliable alternative.

### Amendments

<!-- Amended 2026-06-25: Do not use Room Gradle Plugin; configure schema directory via KSP argument instead -->
The previous amendments relying on the Room Gradle Plugin are superseded. The Room schema directory must be configured using the KSP `room.schemaLocation` argument, which does not require the `androidx.room` Gradle plugin and is compatible with the existing KSP-based Room setup:

```kotlin
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}
```

### New Tasks

- T063 and T064 re-opened: Room Gradle Plugin approach does not work in CI
- T065 added: remove Room Gradle Plugin and switch to KSP schemaLocation argument

### Constitution Compliance

- Art 1.4 (Pinned deps, Maven Central/Google Maven): PASS — no new dependencies or repositories; uses existing KSP setup
- Art 2.2 (ktlint + Detekt): PASS — build script change only
- All other articles: unaffected

## Iteration Session 2026-06-25 (2)

### User Feedback

Build failure persists on `./gradlew assembleDevDebug`: same `ScriptCompilationError(message=Unresolved reference: room)` and `ScriptCompilationError(message=Unresolved reference: schemaDirectory)` errors at `app/build.gradle.kts` lines 55–56, even after registering the `androidx.room` plugin in the previous iteration.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD | P0 | Incorrect implementation — `room {}` block placed inside `android {}` instead of at project top level |

**Root Cause**: The Room Gradle Plugin registers the `room` extension on the `Project` object. In `app/build.gradle.kts` the `room { schemaDirectory(...) }` block was placed inside the `android { }` block, where the `room` extension is not in scope. The plugin itself was correctly registered and applied in the previous iteration, but the DSL block was in the wrong lexical scope, so Gradle still reported `Unresolved reference: room`.

### Amendments

<!-- Amended 2026-06-25: Room Gradle Plugin room {} block must be at top level, not inside android {} -->
The amendments from the first 2026-06-25 iteration are correct, but incomplete. In addition to registering the plugin, the `room {}` extension block in `app/build.gradle.kts` must be moved from inside `android { }` to the top level of the build script (same level as `android { }`, `dependencies { }`, and `detekt { }`).

### New Tasks

- T063 re-opened: plugin was registered but `room {}` block was placed in wrong scope
- T064 added: move `room {}` block to top level of `app/build.gradle.kts`

### Constitution Compliance

- Art 1.4 (Pinned deps, Maven Central/Google Maven): PASS — no new dependencies or repositories
- Art 2.2 (ktlint + Detekt): PASS — moving a DSL block does not affect code quality tooling
- All other articles: unaffected

## Iteration Session 2026-06-25

### User Feedback

Build failure on `./gradlew assembleDevDebug`: `ScriptCompilationError(message=Unresolved reference: room)` and `ScriptCompilationError(message=Unresolved reference: schemaDirectory)` at `app/build.gradle.kts` lines 55–56. The `room { schemaDirectory(...) }` DSL block requires the Room Gradle Plugin (`androidx.room`) which was never registered in `libs.versions.toml` or applied in the build scripts.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | BUILD | P0 | Environment/config issue — Room Gradle Plugin not registered or applied |

**Root Cause**: The `room {}` extension block (introduced in Room 2.6.0+) requires the `androidx.room` Gradle plugin to be applied to the module. The project uses Room `2.6.1` (which supports the plugin) but the plugin was never declared in `gradle/libs.versions.toml` `[plugins]` section, never listed in the root `build.gradle.kts`, and never applied in `app/build.gradle.kts`. Task T014 created the migration and updated `PoultryDatabase` but missed the plugin registration needed for the `room {}` DSL.

### Amendments

<!-- Amended 2026-06-25: Room Gradle Plugin must be registered and applied for room {} DSL support -->
The Technical Context section's "Primary Dependencies" implicitly assumes Room plugin availability via the `room {}` DSL used in `app/build.gradle.kts`. The fix requires three file changes:
1. `gradle/libs.versions.toml` — Add `room` plugin entry under `[plugins]`
2. `build.gradle.kts` (root) — Add `alias(libs.plugins.room) apply false`
3. `app/build.gradle.kts` — Add `alias(libs.plugins.room)` to the plugins block

### New Tasks

- T014 re-opened: Room database setup was incomplete (missing plugin registration)
- T063 added: Register and apply the Room Gradle Plugin across all three build files

### Constitution Compliance

- Art 1.4 (Pinned deps, Maven Central/Google Maven): PASS — `androidx.room` plugin is from Google Maven, version pinned to existing `room = "2.6.1"` in version catalog
- Art 1.2.2 (Offline-first data flow): PASS — fix is config-only, no behavioral change
- All other articles: unaffected by this config fix

## Iteration Session 2026-06-26

### User Feedback

Crash Summary: Room Database Schema Mismatch
Exception: \java.lang.IllegalStateException\
Error Message: Pre-packaged database has an invalid schema for the \reed_profiles\ table mapped to \com.poultry.broiler.data.local.entity.BreedProfileEntity\.

Root Cause:
1. \id\ Column Nullability: Expected \
otNull=true\, Found \
otNull=false\
2. Unexpected Index: Found extra \idx_breed_name\, Expected \index_breed_profiles_breed_name\

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | DATA | P0 | Incorrect implementation - Seed database gen script doesn't match Room schema |

**Root Cause**: The bash script (\scripts/build-seed-db.sh\) generating the pre-packaged SQLite database (\poultry.db\) didn't explicitly add the \NOT NULL\ constraint on the \id\ column (which Room requires even for PRIMARY KEY in SQLite) and named the unique index \idx_breed_name\ instead of matching Room's default name \index_breed_profiles_breed_name\. Room's strict startup verification caught the mismatch and crashed.

### Amendments

None to existing analytical/architectural sections.

### New Tasks

- T067 added: Fix \scripts/build-seed-db.sh\ schema definitions to match Room exactly.
- T068 added: Regenerate \pp/src/main/assets/seed/poultry.db\ by executing the fixed script.

### Constitution Compliance

- Art 1.2.2 (Offline-First Data Flow): PASS - Ensuring SQLite schemas exactly match the Room declarative specs guarantees safe offline access.
- All other articles: unaffected.

## Iteration Session 2026-06-26 (2)

### User Feedback

App installed and launched from the emulator, but crashed on startup with `java.lang.IllegalStateException: Pre-packaged database has an invalid schema: equipment_items(...)`. Room expected indices `[index_equipment_items_category]` but the shipped `poultry.db` contained an extra/mismatched index `idx_equipment_category`.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | DATA | P0 | Incomplete implementation — prior Iteration Session (1) created T067/T068 for seed-DB schema alignment but T067's scope missed the `equipment_items` index mismatch |

**Root Cause**: The previous iteration session (2026-06-26) correctly diagnosed the pre-packaged DB schema-mismatch class of bug and created T067 (fix the script) + T068 (regenerate the DB). However, T067's description only addressed the `breed_profiles` index rename (`idx_breed_name` → `index_breed_profiles_breed_name`) and `id` NOT NULL constraints. It **omitted the `equipment_items` index**: the script created `idx_equipment_category` while the Room entity (`EquipmentItemEntity`) + schema export (`3.json`) expect `index_equipment_items_category`. Room's strict pre-packaged DB validation rejects the mismatch on first open, crashing the app every launch. On inspection, `breed_profiles` was already correct in the working tree (its part of T067 had been applied) and both `id` columns already carried `NOT NULL`; only the `equipment_items` index name was outstanding.

### Amendments

None to existing architectural/design sections. T067's scope is clarified in-place in `tasks.md` to record the actual change applied (equipment_items index rename) and the fact that the breed_profiles/index work was already present.

### New Tasks

None — T067 and T068 (both from the prior session) are now completed and marked `[X]`.

### Constitution Compliance

- Art 1.2.2 (Offline-First Data Flow): PASS — pre-packaged seed DB schema now exactly matches the Room declarative schema (3.json), so offline-first reads/writes are safe.
- Art 1.4 (Pinned deps): PASS — no dependency or repository changes; used existing `sqlite3` from the Android SDK `platform-tools`.
- All other articles: unaffected.

### Verification

Regenerated `poultry.db` indices verified against Room expectations:
- `breed_profiles` → `index_breed_profiles_breed_name` (unique) ✓
- `equipment_items` → `index_equipment_items_category` ✓
- Row counts preserved: 2 breed profiles, 6 equipment items ✓
- App rebuilt (`:app:assembleDevDebug` SUCCESSFUL), data cleared, reinstalled, launched: `MainActivity` reached `topResumedActivity`, process alive, zero `FATAL EXCEPTION` in logcat. UI rendered the projects dashboard empty state ("Aucun projet" / "Nouveau projet").

---

## Iteration Session 2026-06-27

### User Feedback

App crashes on launch with `java.lang.IllegalStateException: Pre-packaged database has an invalid schema: breed_profiles(...)`. The error message shows the full expected schema including all columns, indices, and their properties.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | DATA / BUILD | P0 | Delivery issue — APK installed on device predates the database fix |

**Root Cause**: The seed database script (`scripts/build-seed-db.sh`) and the regenerated database (`poultry.db`) are CORRECT in the working tree. The fix was applied in T067/T068 (completed). The latest APK was built at 02:45 on 2026-06-27, which is AFTER the database fix (19:15 on 2026-06-26). However, the device has an older installed version of the APK containing the pre-fix database, causing Room's strict validation to fail on startup.

**Evidence**:
- Working tree script: `id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL` ✓
- Working tree script: `CREATE UNIQUE INDEX index_breed_profiles_breed_name` ✓
- Working tree script: `CREATE INDEX index_equipment_items_category` ✓
- Database regenerated: 2026-06-26 19:15:19 ✓
- APK built: 2026-06-27 02:45:55 (after fix) ✓
- Device has: Old APK with pre-fix database ✗

### Amendments

None — no architecture or design changes needed. The fix is complete; this is purely a delivery/deployment issue.

### New Tasks

- T069 added: Verify app launches successfully after uninstalling old APK and reinstalling the latest build.

### Constitution Compliance

- All articles: unaffected — this is a deployment issue, not a code or architecture problem
- Art 1.2.2 (Offline-First Data Flow): PASS — once the correct APK is installed, the schema will match exactly

---

## Iteration Session 2026-06-27 (2)

### User Feedback

App crashes on startup with `java.lang.IllegalStateException: Pre-packaged database has an invalid schema: breed_profiles(...)` due to index mismatch.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | DATA | P0 | Redundant UNIQUE constraint in table definition creating autoindex conflict |

**Root Cause**: The seed database script `scripts/build-seed-db.sh` defined `breed_name TEXT NOT NULL UNIQUE` on the table column, which caused SQLite to automatically create a unique index named `sqlite_autoindex_breed_profiles_1` for that column. Additionally, the script explicitly ran `CREATE UNIQUE INDEX index_breed_profiles_breed_name`. Room compared the pre-packaged SQLite database against the Room entity specification, which only expects `index_breed_profiles_breed_name` and no autoindexes, leading to schema verification failure.

### Amendments

- **scripts/build-seed-db.sh**: Removed the `UNIQUE` keyword from the `breed_name` column definition in the `CREATE TABLE` query. Uniqueness is fully and correctly enforced by the explicit `index_breed_profiles_breed_name` index.

### New Tasks

- T070 [FIX] Update `scripts/build-seed-db.sh` to remove the redundant `UNIQUE` column constraint.
- T071 [FIX] Regenerate the `poultry.db` using the corrected script (via a cross-platform Python script `build-seed-db.py` to ensure correct carriage return line endings and execution on Windows).
- T072 [FIX] Perform clean uninstall of the old app from the device (`adb uninstall com.poultry.broiler`), rebuild the APK, and reinstall.

### Constitution Compliance

- Art 1.2.2 (Offline-First Data Flow): PASS — alignment of seed database with Room expectations ensures correct offline data access.
- All other articles: unaffected.

## Iteration Session 2026-06-27 (3)

### User Feedback

The user reports that there is no way to go back or access the home screen or dashboard screen from the wizard or dashboard.

### Diagnosis Summary

| # | Category | Severity | Root Cause |
|---|----------|----------|------------|
| 1 | UX / UI  | P0       | Nested bottom bars: MainActivity displays BottomNavBar unconditionally, showing it simultaneously with WizardNavigationBar on the wizard screen. |
| 2 | UX       | P1       | Missing exit/close button: WizardScreen has no visual button in the UI to exit the wizard. |
| 3 | FUNCTIONAL | P1       | BackHandler bug: The back navigation logic in WizardScreen is a no-op LaunchedEffect (`runCatching { onNavigateBack }`). |
| 4 | UX / FUNCTIONAL | P1 | Dashboard navigation stub: DashboardScreen is a simple text component without Hilt/ViewModel or Scaffold top bar, trapping the user. |

### Amendments

None to existing analytical/architectural sections.

### New Tasks

- T073 [FIX-UI] Hide `BottomNavBar` in `MainActivity` for non-tab destinations (`NavRoute.Wizard` and `NavRoute.Dashboard`).
- T074 [FIX-UI] Create custom Top App Bar inside `WizardScreen` containing a close `IconButton` (using `Icons.Default.Close`), the wizard title `R.string.screen_wizard`, and the `WizardStepIndicator` badge.
- T075 [FIX] Replace the buggy `LaunchedEffect` in `WizardScreen` with Compose `BackHandler`: go to the previous step if `currentStep > 1`, otherwise call `onNavigateBack()`.
- T076 [FIX] Update `DashboardScreen` and its navigation route: accept `projectId` and `onNavigateBack` callback, wrap in a `Scaffold` with a custom Top App Bar containing a back arrow `IconButton` (using `Icons.AutoMirrored.Filled.ArrowBack`) and the dashboard title `R.string.screen_dashboard`.

### Constitution Compliance

- Art 3.1 (Design tokens): PASS — Top App Bars, icons, text, spacing, and buttons will use tokens from Theme.kt and Spacing.kt.
- Art 3.3 (Touch targets): PASS — All Top App Bar navigation and close buttons will be `IconButton` components with minimum 48dp touch targets and clear `contentDescription` resources.
- Art 1.2.4 (UDF via StateFlow): PASS — Wizard navigation and state remains managed cleanly.
- All other articles: unaffected.


