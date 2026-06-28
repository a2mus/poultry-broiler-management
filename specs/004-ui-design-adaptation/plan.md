# Implementation Plan: UI Design Adaptation

**Branch**: `004-ui-design-adaptation` | **Date**: 2026-06-27 | **Spec**: [spec.md](file:///d:/Developpement/Projets/WEB/poultry-broiler-management/specs/004-ui-design-adaptation/spec.md)

**Input**: Feature specification from `/specs/004-ui-design-adaptation/spec.md`

## Summary

Implement a comprehensive UI redesign and adaptation of the Poultry Broiler Management Android application to the premium "Agri-Integrity Pro" design system. This includes updating theme colors (Light Soft Pearl/Teal, Dark Carbon/Mint), Outfit/Inter typography, baseline grid spacing, and rounded card/pill button shapes. Additionally, all 7 core dashboard screens/tabs (Home, Dimensions, Blueprint, Risk, Financial, Enhancements, Reports) will be adapted to reflect the localized French/Arabic layout layouts.

## Technical Context

**Language/Version**: Kotlin 1.9+

**Primary Dependencies**: Jetpack Compose (Material 3), Compose Navigation, Hilt, Room (SQLite), Firebase (Firestore & Auth)

**Storage**: Room (SQLite) local database, Firebase Firestore remote database

**Testing**: JUnit 5 + MockK (Unit testing), Turbine (Flow/State testing), Room integration tests, Compose UI tests

**Target Platform**: Android API 26+ (8.0)

**Project Type**: Mobile App (Android)

**Performance Goals**: Screen transition and rendering under 300ms, data updates in 2D preview canvas under 300ms, WCAG AA compliance (contrast ratio >= 4.5:1), touch targets >= 48dp x 48dp

**Constraints**: Offline-first, UI layout mirrored for RTL (Arabic), localization in French and Arabic

**Scale/Scope**: 7 core screens/tabs: Home Screen, House Dimensions Wizard Step, 2D Blueprint Canvas Tab, Risk Assessment Tab, Financial Analysis Tab, Technical Improvements Tab, Performance Reports Tab.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

1. **Clean Layer Isolation**: MVVM with Clean Architecture. ViewModels pass StateFlow to Composables. No Android/UI imports in the domain layer. (Pass)
2. **Offline-First Data Flow**: Reads/writes go through Room database first. Calculations and PDF exports operate offline. (Pass)
3. **Single Activity Architecture**: Using one MainActivity and Compose Navigation. (Pass)
4. **Unidirectional Data Flow**: Views send sealed intents to ViewModels; view states collected via StateFlow. (Pass)
5. **Dependency Injection**: Hilt for all injectable components. (Pass)
6. **Code Style**: ktlint checks and Detekt rules (no `!!`, method length limit). (Pass)
7. **Spacing & Shapes**: Enforces standard baseline grid (8dp) and shapes (16dp cards, 24dp buttons, 8dp badges, 28dp dialogs) from design tokens. (Pass)
8. **Localization**: App is localized in French (default) and Arabic (RTL mirroring). (Pass)

## Project Structure

### Documentation (this feature)

```text
specs/004-ui-design-adaptation/
├── plan.md              # This file (/speckit-plan command output)
├── research.md          # Phase 0 output (/speckit-plan command)
├── data-model.md        # Phase 1 output (/speckit-plan command)
├── quickstart.md        # Phase 1 output (/speckit-plan command)
├── checklists/
│   └── requirements.md  # Spec quality checklist
├── contracts/
│   └── composables.md   # Phase 1 output (/speckit-plan command)
└── tasks.md             # Phase 2 output (/speckit-tasks command - NOT created by /speckit-plan)
```

### Source Code (repository root)

```text
app/src/main/
├── java/com/poultry/broiler/
│   ├── data/                    # Data layer
│   │   ├── local/               # Room DAOs, entities, database
│   │   ├── remote/              # Firebase, Retrofit services
│   │   ├── repository/          # Repository implementations
│   │   └── mapper/              # Entity ↔ Domain model mappers
│   ├── domain/                  # Domain layer (pure Kotlin)
│   │   ├── model/               # Domain models
│   │   ├── repository/          # Repository interfaces
│   │   └── usecase/             # Use case classes
│   ├── presentation/            # Presentation layer
│   │   ├── home/                # Home screen composables + ViewModel
│   │   ├── wizard/              # Wizard steps composables + ViewModel
│   │   ├── dashboard/           # Dashboard tabs composables + ViewModels
│   │   ├── catalog/             # Equipment/breed catalog screens
│   │   ├── settings/            # Settings screen
│   │   ├── components/          # Shared reusable composables
│   │   └── theme/               # Theme.kt, Color.kt, Type.kt, Shape.kt
│   ├── di/                      # Hilt modules
│   ├── util/                    # Utility classes and extensions
│   └── PoultryApp.kt            # Application class
├── res/
│   ├── values/                  # Default strings (French)
│   ├── values-fr/               # French strings
│   └── values-ar/               # Arabic strings (RTL)
└── assets/
    └── seed/                    # Preloaded breed and equipment databases
```

**Structure Decision**: Standard Android single-module project containing clean architecture package layers under `app/src/main/java/com/poultry/broiler/`.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

*No violations detected. Visual presentation layer updates conform directly to the design tokens and architecture specified in the Project Constitution.*
