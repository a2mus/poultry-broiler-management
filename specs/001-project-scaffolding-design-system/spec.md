# Feature Specification: Project Scaffolding & Design System

**Feature Branch**: `001-project-scaffolding-design-system`

**Created**: 2026-06-24

**Status**: Draft

**Input**: User description: "Project Scaffolding & Design System — foundational Android project setup with Kotlin/Jetpack Compose/Material 3, Room database, Hilt DI, design tokens, single-activity navigation, Clean Architecture layers, CI/CD skeleton, seed data, French-first localization, and shared composable library."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Developer Opens and Builds the Project (Priority: P1)

A new developer clones the repository, opens the project in Android Studio, and runs a Gradle sync followed by a build. The project compiles successfully on API 26+ without manual configuration beyond the standard Android SDK setup. The developer can immediately launch the app on an emulator or device and see a themed welcome screen with the Forest Teal light palette.

**Why this priority**: Without a clean, compilable project scaffold, no subsequent feature work can begin. This is the absolute foundation.

**Independent Test**: Can be tested by cloning the repo and running `./gradlew assembleDevDebug` on a clean machine with Android SDK installed; delivers a running, themed app shell.

**Acceptance Scenarios**:

1. **Given** a freshly cloned repository with Android SDK available, **When** the developer runs `./gradlew assembleDevDebug`, **Then** the build completes with zero errors and produces a valid APK
2. **Given** a built APK installed on an API 26 emulator, **When** the app launches, **Then** a themed screen appears using the Forest Teal light palette with Outfit/Inter typography
3. **Given** the project opened in Android Studio, **When** the developer inspects the dependency tree, **Then** all dependencies are resolved from the Gradle Version Catalog (`libs.versions.toml`) with pinned versions

---

### User Story 2 - App Displays Consistent Visual Theme (Priority: P1)

A user launches the app and sees a polished, consistent visual experience. In light mode, the app displays the Forest Teal color palette designed for outdoor field glare reduction. Switching to dark mode activates the Sleek Carbon palette. All typography uses Outfit for headings and Inter for body text. Cards have rounded 16dp corners, buttons are pill-shaped (24dp radius), and spacing follows the 8dp grid.

**Why this priority**: The design system is the visual contract for every subsequent feature. Inconsistent theming would cascade errors across all 13 remaining features.

**Independent Test**: Can be verified by launching the app in light/dark mode and visually confirming color palettes, typography, shape tokens, and spacing against the UI spec.

**Acceptance Scenarios**:

1. **Given** the app is launched in light mode, **When** the user views any screen, **Then** all colors match the Forest Teal palette (primary, secondary, tertiary, surface, background, on-primary, on-surface) as defined in the UI spec §1.1
2. **Given** the app is in light mode, **When** the user toggles the system theme to dark mode, **Then** all colors transition to the Sleek Carbon palette with no hardcoded light-mode colors visible
3. **Given** any screen, **When** the user views text elements, **Then** headings use Outfit font and body text uses Inter font at the defined type scale sizes
4. **Given** any screen with card components, **When** inspecting shape rendering, **Then** cards have 16dp corner radius, buttons have 24dp pill radius, and badges have 8dp corners

---

### User Story 3 - Developer Navigates Between Placeholder Screens (Priority: P2)

A developer or QA tester launches the app and uses the bottom navigation bar to switch between the major sections: Home, Wizard, Dashboard, Catalog, and Settings. Each screen shows a labeled placeholder confirming the route is wired correctly. The navigation follows the single-activity architecture using Compose Navigation with no Fragments.

**Why this priority**: Navigation infrastructure must exist before any feature screen can be implemented. It is a structural dependency but not visible to end users.

**Independent Test**: Can be tested by tapping each bottom navigation item and verifying the correct placeholder screen loads with its label.

**Acceptance Scenarios**:

1. **Given** the app is launched, **When** the user taps on the "Home" tab in the bottom navigation, **Then** the Home placeholder screen is displayed
2. **Given** any screen, **When** the user taps each of the 5 navigation tabs sequentially (Home, Wizard, Dashboard, Catalog, Settings), **Then** the corresponding placeholder screen appears with the correct label and no navigation errors
3. **Given** the app is on the Wizard screen, **When** the user presses the system back button, **Then** the app navigates back to the Home screen (start destination) without crashing

---

### User Story 4 - App Loads Seed Data on First Launch (Priority: P2)

When the app is installed and launched for the first time, the Room database initializes with preloaded seed data for breed profiles (Ross 308, Cobb 500) and equipment catalog items. This data is available immediately for browsing in the Catalog section without any network connectivity.

**Why this priority**: Seed data is required for the Equipment Catalog (#007) and Wizard (#003) features to have reference data. It is foundational but secondary to the visual and structural scaffold.

**Independent Test**: Can be tested by installing the app fresh, opening the database inspector in Android Studio, and verifying breed and equipment records exist.

**Acceptance Scenarios**:

1. **Given** the app is freshly installed, **When** it launches for the first time, **Then** the Room database contains at least 2 breed profile records (Ross 308, Cobb 500) from the seed asset
2. **Given** the app is freshly installed, **When** it launches for the first time, **Then** the Room database contains equipment catalog records from the seed asset
3. **Given** the seed database is loaded, **When** the user navigates to the Catalog placeholder, **Then** the data is available for querying without network access

---

### User Story 5 - App Displays Content in French (Priority: P2)

The app launches with all user-facing text displayed in French, as this is the primary language for the target user base. The resource structure is prepared for future Arabic localization but Arabic content is not required at this stage.

**Why this priority**: French is the working language of the target audience. All subsequent features will add French strings, so the localization infrastructure must be established now.

**Independent Test**: Can be tested by launching the app on a device set to French locale and verifying all visible text is in French.

**Acceptance Scenarios**:

1. **Given** the device locale is set to French, **When** the app launches, **Then** all visible text (navigation labels, screen titles, placeholder messages) is displayed in French
2. **Given** the device locale is set to any non-supported locale (e.g., English), **When** the app launches, **Then** the app falls back to French (default) for all strings
3. **Given** the project structure, **When** a developer inspects the `res/` directory, **Then** resource directories `values/`, `values-fr/`, and `values-ar/` exist, with `values/` and `values-fr/` containing French strings

---

### User Story 6 - CI Pipeline Validates Code Quality (Priority: P3)

When a developer opens a Pull Request, the CI pipeline automatically runs code quality checks: ktlint formatting, Detekt static analysis, Android Lint, and unit tests. The pipeline must pass before the PR can be merged, ensuring consistent code quality from the first commit.

**Why this priority**: CI enforcement is important but does not block feature development locally. It is a process safeguard that becomes critical at scale.

**Independent Test**: Can be tested by pushing a branch with a formatting violation and verifying the CI pipeline catches it.

**Acceptance Scenarios**:

1. **Given** a GitHub Actions workflow is configured, **When** a PR is opened, **Then** the pipeline runs ktlint check, Detekt analysis, Android Lint, and unit tests in sequence
2. **Given** code with a ktlint violation is pushed, **When** the CI pipeline runs, **Then** the build fails with a clear error message pointing to the violation
3. **Given** all code passes quality checks, **When** the CI pipeline completes, **Then** it reports a green status and the PR is eligible for merge

---

### User Story 7 - Gradle Build Flavors Switch Environments (Priority: P3)

A developer can build the app with either the `dev` or `prod` Gradle build flavor. The `dev` flavor connects to the `poultry-dev` Firebase project for testing, while the `prod` flavor uses the `poultry-prod` Firebase project for production releases.

**Why this priority**: Environment separation is important for safe development but is not needed until Firebase integration is actively used in later features.

**Independent Test**: Can be tested by building both `assembleDevDebug` and `assembleProdRelease` and verifying each uses its respective `google-services.json`.

**Acceptance Scenarios**:

1. **Given** the Gradle configuration, **When** the developer runs `./gradlew assembleDevDebug`, **Then** the build uses the `dev` flavor Firebase configuration
2. **Given** the Gradle configuration, **When** the developer runs `./gradlew assembleProdRelease`, **Then** the build uses the `prod` flavor Firebase configuration
3. **Given** the build variants, **When** inspecting the generated BuildConfig, **Then** each flavor exposes a distinct application ID suffix (e.g., `.dev` for development)

---

### Edge Cases

- What happens when the app runs on an API level below 26? The app must declare `minSdk = 26` and the Play Store must filter incompatible devices.
- What happens if the seed database asset file is corrupted or missing? The app must display a user-friendly error screen with a "Retry" button that reattempts seed loading. If the retry also fails, the app must display a message directing the user to reinstall the application.
- What happens when switching between light and dark mode rapidly? Theme transitions must remain smooth without visual artifacts or state loss.
- What happens when the device language is set to Arabic (before Arabic translations are added)? The app must fall back to French strings without crashing, while the RTL layout direction flag may activate.
- What happens if a dependency version in `libs.versions.toml` becomes unavailable? The build must fail with a clear dependency resolution error, never silently substituting a different version.

## Clarifications

### Session 2026-06-24

- Q: What level of functionality should shared composable stubs (FR-018) include? → A: Themed shells — accept content parameters and apply design tokens, but no interactive behavior or state callbacks.
- Q: What recovery behavior after corrupt/missing seed database? → A: Retry + reinstall guidance — show error with retry button; on second failure, prompt user to reinstall the app.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The app MUST compile and run on Android API 26+ using Kotlin, Jetpack Compose with Material 3, Room, and Hilt
- **FR-002**: The app MUST implement a Material 3 theme with two color palettes — Forest Teal (light) and Sleek Carbon (dark) — as defined in the UI spec §1.1
- **FR-003**: The app MUST use Outfit for display/heading typography and Inter for body/data typography at the scale sizes defined in UI spec §1.2
- **FR-004**: The app MUST enforce an 8dp baseline spacing grid with all spacing values being multiples of 4dp
- **FR-005**: The app MUST implement shape tokens: 16dp card corners, 24dp pill button corners, 8dp badge corners, 28dp dialog corners
- **FR-006**: The app MUST implement a 4-level elevation system: 0dp (flat), 2dp (card), 6dp (hover/focus), 12dp (modal)
- **FR-007**: The app MUST use a single-activity architecture with Jetpack Compose Navigation hosting routes for Home, Wizard, Dashboard, Catalog, and Settings
- **FR-008**: The app MUST implement bottom navigation with labeled tabs for all five primary destinations
- **FR-009**: The Room database MUST initialize on first launch with migration infrastructure and preloaded seed data for breed profiles (Ross 308, Cobb 500) and equipment catalog items
- **FR-010**: Seed data MUST be packaged as read-only assets and loaded via Room's `createFromAsset()` mechanism
- **FR-011**: The Domain layer MUST contain zero Android framework imports (`android.*`, `androidx.*`) to enforce clean architecture boundaries
- **FR-012**: All dependencies MUST be managed via Gradle Version Catalog (`libs.versions.toml`) with pinned versions — no `+` or `latest` qualifiers
- **FR-013**: The app MUST use Hilt for dependency injection across all ViewModels, Repositories, Use Cases, and Data Sources
- **FR-014**: French MUST be the default and primary language; all user-facing strings MUST be defined in resource files (`values/strings.xml` and `values-fr/strings.xml`)
- **FR-015**: The resource directory structure MUST include `values/`, `values-fr/`, and `values-ar/` directories, with Arabic prepared as empty stubs
- **FR-016**: Gradle build MUST define `dev` and `prod` product flavors that switch Firebase project configuration
- **FR-017**: A CI/CD pipeline (GitHub Actions) MUST run ktlint, Detekt, Android Lint, and unit tests on every Pull Request
- **FR-018**: The app MUST provide shared reusable composables: StatusBadge, NumericInputField (with trailing unit label and numeric keypad), and BottomSheet. These MUST be themed shells that accept content parameters and apply design tokens, but without interactive behavior or state callbacks (full interactivity deferred to consuming features)
- **FR-019**: All interactive elements MUST have a minimum touch target of 48dp × 48dp per WCAG AA compliance
- **FR-020**: All text MUST meet WCAG AA contrast ratios (≥ 4.5:1 for body, ≥ 7:1 for critical warnings)

### Key Entities *(include if feature involves data)*

- **BreedProfile**: Represents a poultry breed with its growth characteristics, density requirements, and performance targets. Key attributes: breed name, growth curve data, recommended density ranges, feed conversion ratios. Pre-seeded with Ross 308 and Cobb 500 profiles.
- **EquipmentItem**: Represents a piece of poultry house equipment with specifications and catalog information. Key attributes: equipment name, category (ventilation, feeding, heating, lighting), technical specifications, unit of measurement. Pre-seeded from equipment catalog assets.
- **NavigationRoute**: Represents a destination in the app's navigation graph. Defines the five primary screen routes (Home, Wizard, Dashboard, Catalog, Settings) and their associated navigation metadata.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: A developer can clone the project and have a successful build within 5 minutes on a standard development machine
- **SC-002**: The app launches and displays a themed screen in under 3 seconds on a mid-range Android device (API 26+)
- **SC-003**: 100% of visible UI elements use design tokens — zero hardcoded color values, font families, or spacing values exist in composable functions
- **SC-004**: Light-to-dark theme switching occurs within 200ms with no visual artifacts or state loss
- **SC-005**: The Domain layer contains zero imports from `android.*` or `androidx.*` packages, verified by static analysis
- **SC-006**: All 5 navigation destinations are reachable from the bottom navigation bar within a single tap
- **SC-007**: Seed database loads on first launch with all breed and equipment records available for query within 2 seconds
- **SC-008**: CI pipeline completes all quality checks (ktlint, Detekt, Android Lint, unit tests) within 10 minutes per PR
- **SC-009**: All interactive elements meet the 48dp minimum touch target requirement, verified by accessibility scanner
- **SC-010**: The app displays all content in French by default regardless of device locale

## Assumptions

- Developers have Android Studio (latest stable) with Android SDK API 26+ installed
- The project uses a single `app` module for the initial scaffold; multi-module decomposition may be introduced in later features if needed
- Firebase `google-services.json` files for both `dev` and `prod` flavors will be provided separately and are excluded from version control via `.gitignore`
- Breed profiles and equipment catalog seed data are static reference data that will not be modified by users; updates will come via app version updates
- The Weather API key referenced in the constitution is not needed for this scaffolding feature and will be configured when the weather-dependent features are implemented
- Network connectivity is not required for any functionality in this feature — all operations are offline-first
- Shared composables (StatusBadge, NumericInputField, BottomSheet) are themed shells that accept content parameters and apply design tokens; full interactive behavior and state callbacks will be added by consuming features
- Tablet adaptive layouts (8-column grid) are prepared in the theme system but detailed per-screen tablet optimizations will be handled in individual feature specs
