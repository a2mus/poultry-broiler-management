# Feature Brief: Project Scaffolding & Design System

**Sequence**: #001 of 14
**Category**: :building_construction: Foundation
**Priority**: Must Have
**Estimated Complexity**: Large

## Dependencies

- **Requires**: None
- **Unlocks**: #002 Project Management, #007 Equipment Catalog, #014 Arabic Localization, and transitively all other features
- **Parallelizable with**: None (this is the foundational feature)

## Feature Description

The Poultry Broiler House Design & Management app requires a solid project foundation before any functional feature can be built. This feature covers the initial Android project scaffolding with the full Kotlin/Jetpack Compose/Material 3 stack, Room database setup, Hilt dependency injection configuration, and the complete design system implementation.

The design system is the visual backbone of the entire application. It implements the custom color palettes (Forest Teal light theme for outdoor field glare reduction and Sleek Carbon dark theme), the Outfit/Inter typography scale, the 8dp baseline spacing grid, shape tokens (16dp card corners, 24dp pill buttons, 28dp dialogs), and the 4-level elevation system. These tokens must be codified in `Theme.kt`, `Color.kt`, `Type.kt`, and `Shape.kt` so every subsequent feature composes against a consistent visual language.

Beyond theming, this feature establishes the single-activity architecture with Compose Navigation, the MVVM + Clean Architecture layer structure (Presentation/Domain/Data), the Gradle build configuration with dev/prod flavors, and the CI/CD pipeline skeleton (ktlint, Detekt, Android Lint, unit test stages). It also sets up the Room database schema with initial migration infrastructure and the preloaded seed database assets for breed and equipment reference catalogs.

French is configured as the primary and default language from day one, with the localization resource structure (`values/`, `values-fr/`, `values-ar/`) prepared for future Arabic support. Shared reusable composables (status badges, form input fields with unit labels, bottom sheets, project card skeletons) are stubbed as part of the component library.

## Derived From

- **Product Spec**: §4 Product Type & Platform, §6.1 Technology Stack, §6.2 Data Architecture, §7 Non-Functional Requirements
- **UI Spec**: §1 Design System & Design Tokens (§1.1 Color Palettes, §1.2 Typography, §1.3 Spacing, §1.4 Shapes), §2 Global UI Component Standards, §3 Navigation & Screen Architecture Map
- **Constitution**: Article 1 Architecture (§1.1–§1.5), Article 2 Code Quality (§2.2–§2.3), Article 3 Design & UI (§3.1–§3.4), Article 5 DevOps (§5.1–§5.2), Article 7 Project Organization (§7.1–§7.3)

## Acceptance Criteria Summary

- [ ] Android project compiles and runs on API 26+ with Kotlin, Jetpack Compose (Material 3), Room, and Hilt configured
- [ ] Design tokens (colors, typography, spacing, shapes, elevation) are implemented in Theme.kt and visually match the ui-spec light/dark palettes
- [ ] Single-activity Compose Navigation host is functional with placeholder routes for Home, Wizard, Dashboard, Catalog, and Settings
- [ ] Room database initializes with migration infrastructure and preloaded seed data (breed/equipment catalogs) from app assets
- [ ] Clean Architecture layer boundaries are enforced: Domain layer contains no Android imports
- [ ] CI pipeline runs ktlint, Detekt, Android Lint, and unit tests on PR
- [ ] French is the default language with resource structure prepared for Arabic
- [ ] Gradle build flavors (dev/prod) switch Firebase project configuration

## UI Components (if applicable)

- **Theme.kt / Color.kt / Type.kt / Shape.kt**: Complete Material 3 theme implementation with light/dark mode switching
- **Shared StatusBadge composable**: Reusable badge component (Draft, Completed, Welfare Pass/Fail, High Priority) per ui-spec §2.2
- **Shared NumericInputField composable**: Text field with trailing unit label (m, mm, CFM) and numeric keypad trigger per ui-spec §2.3
- **Shared BottomSheet composable**: Standard bottom sheet with drag handle and swipe-to-dismiss per ui-spec §2.4
- **Navigation scaffold**: Top app bar, bottom navigation bar (phone), and FAB placeholder

## Technical Hints

- Constitution Article 1.3 mandates specific minimum versions: Kotlin 1.9+, Room 2.6+, Hilt 2.51+, Retrofit 2.9+
- Constitution Article 1.4 requires all dependencies pinned to specific versions via Gradle Version Catalog (`libs.versions.toml`)
- Constitution Article 3.3 requires WCAG AA compliance and 48dp minimum touch targets from the start
- Constitution Article 5.5 requires Semantic Versioning with auto-incrementing versionCode on CI
- The seed database must package breed profiles (Ross 308, Cobb 500) and equipment catalog items as read-only assets

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
