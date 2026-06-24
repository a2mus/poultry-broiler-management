# Implementation Plan: Project Scaffolding & Design System

**Branch**: `001-project-scaffolding-design-system` | **Date**: 2026-06-24 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/001-project-scaffolding-design-system/spec.md`

## Summary

Establish the foundational Android project scaffold with Kotlin/Jetpack Compose/Material 3, including a complete design token system (Forest Teal light / Sleek Carbon dark palettes, Outfit/Inter typography, shape and elevation tokens), single-activity Compose Navigation with 5 placeholder screens and bottom navigation bar, Room database with `createFromAsset()` seed data infrastructure (breed profiles and equipment catalog), Hilt dependency injection, French-first localization with Arabic stubs, shared composable library (themed shells without interactive behavior), Gradle build flavors for dev/prod Firebase environments, and a GitHub Actions CI/CD skeleton enforcing ktlint, Detekt, Android Lint, and unit tests.

## Technical Context

**Language/Version**: Kotlin 1.9+ (targeting latest stable)

**Primary Dependencies**: Jetpack Compose BOM (latest stable), Material 3, Room 2.6+, Hilt 2.51+, Compose Navigation, Google Fonts (Outfit, Inter)

**Storage**: Room (SQLite) — offline-first local database with `createFromAsset()` seed mechanism

**Testing**: JUnit 5 + MockK (unit), Turbine (Flow/StateFlow), Compose UI Test (UI), Room in-memory database (integration)

**Target Platform**: Android API 26+ (Android 8.0 Oreo) — phone and tablet form factors

**Project Type**: Mobile application (single-module Android app)

**Performance Goals**: App launch < 3s on mid-range device, theme switch < 200ms, seed data load < 2s, CI pipeline < 10 minutes

**Constraints**: 100% offline-capable, single Activity (no Fragments), French-first localization, all styling via design tokens (zero hardcoded values), single `app` module

**Scale/Scope**: 5 placeholder screens, 2 seed entity types (BreedProfile, EquipmentItem), ~20 functional requirements, single-user on-device operation

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| # | Constitution Rule | Spec Compliance | Status |
|---|------------------|-----------------|--------|
| 1 | Art 1.1 — MVVM + Clean Architecture | FR-011: Domain layer zero Android imports; 3-layer separation | PASS |
| 2 | Art 1.2.1 — Clean Layer Isolation | FR-011, FR-013: Hilt enforces boundaries, domain is pure Kotlin | PASS |
| 3 | Art 1.2.2 — Offline-First Data Flow | Assumption: "No network required for any functionality" | PASS |
| 4 | Art 1.2.3 — Single Activity Architecture | FR-007: Single activity with Compose Navigation, no Fragments | PASS |
| 5 | Art 1.2.4 — UDF via StateFlow | Constitution mandate; enforced in placeholder ViewModels | PASS |
| 6 | Art 1.2.5 — Hilt DI for all injectables | FR-013: Hilt across ViewModels, Repositories, Use Cases, Data Sources | PASS |
| 7 | Art 1.4 — Pinned deps, Maven Central/Google Maven | FR-012: Version Catalog with pinned versions, no `+` qualifiers | PASS |
| 8 | Art 2.1 — JUnit 5 + MockK, ≥80% coverage | Testing infra in scope; coverage target applies to domain/data layers | PASS |
| 9 | Art 2.2 — ktlint + Detekt + Android Lint | FR-017: CI pipeline runs all three on every PR | PASS |
| 10 | Art 2.3 — Null safety, sealed classes | Design uses sealed classes for navigation routes and screen states | PASS |
| 11 | Art 3.1 — Design tokens in Theme.kt | FR-002–006, SC-003: All styling via design tokens | PASS |
| 12 | Art 3.3 — WCAG AA, 48dp touch targets | FR-019, FR-020: Explicit accessibility requirements | PASS |
| 13 | Art 4.4 — google-services.json excluded from repo | Assumption: excluded via .gitignore, provided separately | PASS |
| 14 | Art 5.1 — GitHub Actions CI/CD | FR-017: Pipeline runs on every PR | PASS |
| 15 | Art 5.2 — dev/prod Gradle flavors | FR-016: Build flavors switch Firebase config | PASS |
| 16 | Art 7.1 — Directory structure | Constitution §7.1 defines exact layout; spec follows it | PASS |
| 17 | Art 8.1 — French primary, Arabic secondary | FR-014, FR-015: French default, Arabic stub prepared | PASS |

**Gate Result**: ALL PASS — No violations. Proceeding to Phase 0.

## Project Structure

### Documentation (this feature)

```text
specs/001-project-scaffolding-design-system/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   ├── repositories.md  # Repository interface contracts
│   ├── daos.md          # Room DAO contracts
│   └── composables.md   # Shared composable API contracts
└── tasks.md             # Phase 2 output (/speckit-tasks — NOT created by /speckit-plan)
```

### Source Code (repository root)

```text
app/src/main/
├── java/com/poultry/broiler/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/                # Room DAOs (BreedProfileDao, EquipmentItemDao)
│   │   │   ├── entity/             # Room entities (BreedProfileEntity, EquipmentItemEntity)
│   │   │   ├── converter/          # TypeConverters (GrowthTarget list JSON)
│   │   │   └── PoultryDatabase.kt  # Room database definition + createFromAsset()
│   │   ├── repository/             # Repository implementations (BreedRepositoryImpl, EquipmentRepositoryImpl)
│   │   └── mapper/                 # Entity ↔ Domain model mappers
│   ├── domain/
│   │   ├── model/                  # Domain models (BreedProfile, EquipmentItem, GrowthTarget)
│   │   └── repository/             # Repository interfaces (BreedRepository, EquipmentRepository)
│   ├── presentation/
│   │   ├── home/                   # HomeScreen placeholder + HomeViewModel
│   │   ├── wizard/                 # WizardScreen placeholder
│   │   ├── dashboard/              # DashboardScreen placeholder
│   │   ├── catalog/                # CatalogScreen placeholder
│   │   ├── settings/               # SettingsScreen placeholder
│   │   ├── components/             # StatusBadge, NumericInputField, BottomSheet (themed shells)
│   │   ├── navigation/             # NavHost setup, NavRoute sealed class, BottomNavBar
│   │   └── theme/                  # Theme.kt, Color.kt, Type.kt, Shape.kt, Spacing.kt, Elevation.kt
│   ├── di/                         # DatabaseModule, RepositoryModule
│   ├── util/                       # Extension functions, SeedErrorHandler
│   ├── PoultryApp.kt               # @HiltAndroidApp Application class
│   └── MainActivity.kt             # Single @AndroidEntryPoint Activity
├── res/
│   ├── values/                     # Default strings (French), colors.xml, themes.xml
│   ├── values-fr/                  # French strings (mirrors default)
│   ├── values-ar/                  # Arabic stubs (empty strings.xml)
│   └── font/                       # Outfit, Inter font files (.ttf)
├── assets/
│   └── seed/
│       └── poultry.db              # Pre-built SQLite seed database
├── dev/                            # dev flavor: google-services.json placeholder (gitignored)
└── prod/                           # prod flavor: google-services.json placeholder (gitignored)

app/src/test/                       # Unit tests (JUnit 5 + MockK)
├── java/com/poultry/broiler/
│   ├── data/                       # Repository + mapper tests
│   └── domain/                     # Domain model tests

app/src/androidTest/                # Instrumented tests
├── java/com/poultry/broiler/
│   ├── data/local/                 # DAO integration tests (in-memory Room)
│   └── presentation/               # Compose UI tests

gradle/
└── libs.versions.toml              # Gradle Version Catalog

.github/workflows/
└── ci.yml                          # GitHub Actions CI pipeline

detekt.yml                          # Detekt configuration
.editorconfig                       # ktlint configuration
```

**Structure Decision**: Single `app` module following Constitution §7.1 directory structure. The data/domain/presentation layer separation is enforced within the module via package boundaries and Detekt custom rules. Multi-module decomposition is explicitly deferred per spec assumptions.

## Complexity Tracking

> No constitution violations detected. No complexity justifications needed.
