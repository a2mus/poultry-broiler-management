# Implementation Plan: Project Management

**Branch**: `002-project-management` | **Date**: 2026-06-24 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/002-project-management/spec.md`

## Summary

Implement the Project Management feature — the application's primary entry point — delivering a Home Screen with a responsive bento grid of project cards, full CRUD operations (create, browse, search, edit, duplicate, delete) via Room-persisted `ProjectEntity`, a creation dialog/bottom sheet with type selection (New Installation / Existing Assessment), long-press context menu with scale-down micro-interaction, docked search bar with real-time case-insensitive filtering, and automatic status lifecycle (Draft → In Progress → Completed). All operations follow the MVVM + Clean Architecture pattern established in Feature #001, with ViewModel exposing `StateFlow<HomeUiState>` and receiving sealed `HomeIntent` events. The bottom navigation bar (from #001) remains with the Projects tab active and other tabs as stubs.

## Technical Context

**Language/Version**: Kotlin 1.9+ (targeting latest stable)

**Primary Dependencies**: Jetpack Compose BOM (latest stable), Material 3, Room 2.6+, Hilt 2.51+, Compose Navigation (all established in Feature #001)

**Storage**: Room (SQLite) — `ProjectEntity` table with full CRUD via `ProjectDao`

**Testing**: JUnit 5 + MockK (unit tests for UseCases, Repository, ViewModel), Turbine (StateFlow emissions), Compose UI Test (Home Screen flows), Room in-memory database (DAO integration tests)

**Target Platform**: Android API 26+ (Android 8.0 Oreo) — phone and tablet form factors

**Project Type**: Mobile application (single-module Android app, building on Feature #001 scaffold)

**Performance Goals**: Home Screen card list renders <1s with 50+ projects, search filtering <300ms response, no frame drops during scrolling, project CRUD operations <500ms perceived latency

**Constraints**: 100% offline-capable (no network), single Activity with Compose Navigation, French-first localization with Arabic stubs, all styling via design tokens from Theme.kt, UDF via StateFlow + sealed intents

**Scale/Scope**: 1 primary entity (Project), 22 functional requirements, 6 user stories, 7 success criteria, single-user on-device operation

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| # | Constitution Rule | Spec Compliance | Status |
|---|------------------|-----------------|--------|
| 1 | Art 1.1 — MVVM + Clean Architecture | HomeViewModel → UseCases → ProjectRepository → ProjectDao; 3-layer separation | PASS |
| 2 | Art 1.2.1 — Clean Layer Isolation | Domain layer (Project model, UseCases, Repository interface) is pure Kotlin; no Android imports | PASS |
| 3 | Art 1.2.2 — Offline-First Data Flow | All CRUD operations against Room only; no network calls | PASS |
| 4 | Art 1.2.3 — Single Activity Architecture | Project creation dialog, context menu, and all screens via Compose Navigation within existing single Activity | PASS |
| 5 | Art 1.2.4 — UDF via StateFlow | HomeViewModel exposes `StateFlow<HomeUiState>`; UI sends `HomeIntent` sealed class events | PASS |
| 6 | Art 1.2.5 — Hilt DI for all injectables | ProjectDao, ProjectRepository, all UseCases, HomeViewModel injected via Hilt | PASS |
| 7 | Art 1.4 — Pinned deps, Maven Central/Google Maven | No new dependencies required; all deps from Feature #001 Version Catalog | PASS |
| 8 | Art 2.1 — JUnit 5 + MockK, ≥80% coverage | Unit tests for all UseCases + Repository; DAO integration tests; ViewModel StateFlow tests with Turbine | PASS |
| 9 | Art 2.2 — ktlint + Detekt + Android Lint | CI pipeline from Feature #001 enforces all three on every PR | PASS |
| 10 | Art 2.3 — Null safety, sealed classes | Sealed classes for `ProjectType`, `ProjectStatus`, `HomeUiState`, `HomeIntent`; value class for domain IDs | PASS |
| 11 | Art 3.1 — Design tokens in Theme.kt | All card styling, badge colors, typography, spacing, elevation from Theme.kt tokens; zero hardcoded values | PASS |
| 12 | Art 3.2 — Component Standards | Composables: `ProjectCard`, `NewProjectDialog`, `SearchBar`, `ProjectContextMenu` — PascalCase, domain-prefixed | PASS |
| 13 | Art 3.3 — WCAG AA, 48dp touch targets | All interactive elements (cards, FAB, context menu items, dialog buttons) ≥48dp; contentDescription on icons | PASS |
| 14 | Art 4.3 — Input Validation | Project name validated non-empty at both Compose field level and UseCase precondition | PASS |
| 15 | Art 7.1 — Directory structure | Files placed per Constitution §7.1: data/local/, domain/model/, domain/usecase/, presentation/home/ | PASS |
| 16 | Art 8.1 — French primary, Arabic secondary | All UI strings in `values/strings.xml` (French default), Arabic stubs in `values-ar/` | PASS |

**Gate Result**: ALL PASS — No violations. Proceeding to Phase 0.

## Project Structure

### Documentation (this feature)

```text
specs/002-project-management/
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
app/src/main/java/com/poultry/broiler/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── ProjectDao.kt              # Room DAO for project CRUD + search queries
│   │   └── entity/
│   │       └── ProjectEntity.kt           # Room entity with all project fields
│   ├── repository/
│   │   └── ProjectRepositoryImpl.kt       # Repository implementation bridging DAO ↔ Domain
│   └── mapper/
│       └── ProjectMapper.kt               # ProjectEntity ↔ Project domain model mapper
├── domain/
│   ├── model/
│   │   ├── Project.kt                     # Domain model (pure Kotlin)
│   │   ├── ProjectType.kt                 # Enum: NEW_INSTALLATION, EXISTING_ASSESSMENT
│   │   └── ProjectStatus.kt              # Enum: DRAFT, IN_PROGRESS, COMPLETED
│   ├── repository/
│   │   └── ProjectRepository.kt           # Repository interface (domain contract)
│   └── usecase/
│       ├── CreateProjectUseCase.kt        # Create with validation
│       ├── GetProjectsUseCase.kt          # Get all projects sorted by updatedAt DESC
│       ├── SearchProjectsUseCase.kt       # Filter by name/location (case-insensitive)
│       ├── UpdateProjectUseCase.kt        # Update name/location with validation
│       ├── DuplicateProjectUseCase.kt     # Deep copy with " (Copy)" suffix + Draft status
│       └── DeleteProjectUseCase.kt        # Permanent deletion by ID
├── presentation/
│   └── home/
│       ├── HomeScreen.kt                  # Main Home Screen composable (bento grid)
│       ├── HomeViewModel.kt               # StateFlow<HomeUiState> + HomeIntent handler
│       ├── HomeUiState.kt                 # Sealed interface for UI states
│       ├── HomeIntent.kt                  # Sealed class for user actions
│       └── components/
│           ├── ProjectCard.kt             # Rich project card with metadata
│           ├── NewProjectPlaceholderCard.kt # "+" dashed-border card
│           ├── ProjectSearchBar.kt        # Docked search bar
│           ├── NewProjectDialog.kt        # Creation dialog/bottom sheet
│           ├── EditProjectDialog.kt       # Edit dialog with pre-filled fields
│           ├── DeleteConfirmationDialog.kt # Deletion confirmation
│           └── ProjectContextMenu.kt      # Long-press context menu (Edit/Duplicate/Delete)
└── di/
    └── ProjectModule.kt                   # Hilt bindings for ProjectRepository, UseCases

app/src/main/res/
├── values/strings.xml                     # French strings (append project-management keys)
└── values-ar/strings.xml                  # Arabic stubs (append project-management keys)

app/src/test/java/com/poultry/broiler/
├── data/
│   ├── repository/ProjectRepositoryImplTest.kt
│   └── mapper/ProjectMapperTest.kt
├── domain/usecase/
│   ├── CreateProjectUseCaseTest.kt
│   ├── GetProjectsUseCaseTest.kt
│   ├── SearchProjectsUseCaseTest.kt
│   ├── UpdateProjectUseCaseTest.kt
│   ├── DuplicateProjectUseCaseTest.kt
│   └── DeleteProjectUseCaseTest.kt
└── presentation/home/HomeViewModelTest.kt

app/src/androidTest/java/com/poultry/broiler/
├── data/local/dao/ProjectDaoTest.kt       # In-memory Room DAO tests
└── presentation/home/HomeScreenTest.kt    # Compose UI tests
```

**Structure Decision**: Extends the single `app` module established in Feature #001 following Constitution §7.1. The `home/` package under `presentation/` contains all Home Screen composables and ViewModel. Use Cases are individual classes under `domain/usecase/` following the `<Verb><Noun>UseCase` naming convention. The `ProjectModule` Hilt module binds the repository interface to its implementation.

## Complexity Tracking

> No constitution violations detected. No complexity justifications needed.
