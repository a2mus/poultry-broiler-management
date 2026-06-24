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
