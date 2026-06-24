# Tasks: Project Scaffolding & Design System

**Input**: Design documents from `specs/001-project-scaffolding-design-system/`

**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/ (repositories.md, daos.md, composables.md), quickstart.md

**Tests**: Not explicitly requested for this feature. Testing infrastructure (JUnit 5 + MockK + Turbine) is configured in the build system but individual test tasks are deferred to consuming features.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Mobile (Android)**: `app/src/main/java/com/poultry/broiler/` for Kotlin sources, `app/src/main/res/` for resources, `app/src/main/assets/` for assets
- Paths based on plan.md project structure

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Gradle build system, version catalog, and project configuration files

- [ ] T001 Create full Android project directory structure per plan.md: `app/src/main/java/com/poultry/broiler/{data/{local/{dao,entity,converter},repository,mapper},domain/{model,repository},presentation/{home,wizard,dashboard,catalog,settings,components,navigation,theme},di,util}`, `app/src/main/res/{values,values-fr,values-ar,font}`, `app/src/main/assets/seed/`, `app/src/test/java/com/poultry/broiler/{data,domain}`, `app/src/androidTest/java/com/poultry/broiler/{data/local,presentation}`, `app/src/dev/`, `app/src/prod/`, `.github/workflows/`
- [ ] T002 Create Gradle Version Catalog with all pinned dependency versions (Compose BOM, Material 3, Room 2.6+, Hilt 2.51+, Navigation Compose, kotlinx-serialization, JUnit 5, MockK, Turbine, Detekt, ktlint) in `gradle/libs.versions.toml`
- [ ] T003 [P] Create root `build.gradle.kts` with plugin declarations for Kotlin, Compose, Hilt, Room KSP, Android application, and ktlint referencing Version Catalog
- [ ] T004 [P] Create `settings.gradle.kts` with pluginManagement, dependencyResolutionManagement (Google Maven + Maven Central repositories), and project include for `:app`
- [ ] T005 Create `app/build.gradle.kts` with compileSdk 35, minSdk 26, targetSdk 35, Compose enabled, Room schema export, Hilt + KSP, dev/prod product flavors (dev: applicationIdSuffix `.dev`), Version Catalog library references, and test dependencies
- [ ] T006 [P] Create `.gitignore` with Android defaults, `**/google-services.json`, IDE files, build outputs, and `local.properties`
- [ ] T007 [P] Create `.editorconfig` with ktlint-compatible Kotlin coding conventions (indent_size=4, max_line_length=120)
- [ ] T008 [P] Create `gradle.properties` with AndroidX opt-in, Kotlin code style, non-transitive R classes, and Compose compiler settings

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core Android application skeleton required before ANY user story can render content

**CRITICAL**: No user story work can begin until this phase is complete

- [ ] T009 Create `app/src/main/AndroidManifest.xml` with single Activity declaration (`MainActivity`), application class reference (`PoultryApp`), theme reference, and exported launcher intent filter
- [ ] T010 [P] Create `app/src/main/java/com/poultry/broiler/PoultryApp.kt` as `@HiltAndroidApp` annotated Application class with KDoc documentation
- [ ] T011 Create `app/src/main/java/com/poultry/broiler/MainActivity.kt` as `@AndroidEntryPoint` Activity with `setContent {}` shell wrapping a placeholder composable (will be enhanced by US2 Theme and US3 Navigation)
- [ ] T012 [P] Create `app/proguard-rules.pro` with default Android ProGuard rules and Room/Hilt keep rules

**Checkpoint**: Project compiles with `./gradlew assembleDevDebug` — empty app launches on emulator

---

## Phase 3: User Story 1 — Developer Opens and Builds the Project (Priority: P1) 🎯 MVP

**Goal**: A developer clones the repo, runs Gradle sync and build, and gets a compilable APK targeting API 26+ with all dependencies resolved from the Version Catalog.

**Independent Test**: Run `./gradlew assembleDevDebug` on a clean machine — build completes with zero errors, APK produced at `app/build/outputs/apk/dev/debug/`. Full themed visual confirmation requires US2 completion.

**Dependencies**: Requires Phase 1 (Setup) and Phase 2 (Foundational) complete.

> **Note**: US1 is primarily delivered by Setup (Phase 1) and Foundational (Phase 2). The tasks below complete the build configuration. Full acceptance (themed screen with Forest Teal palette and Outfit/Inter typography) is achieved after US2 Phase 4 completes.

### Implementation for User Story 1

- [ ] T013 [US1] Create `app/src/main/res/values/colors.xml` with placeholder Material 3 color resources (overridden by Compose theme in US2) and `app/src/main/res/values/themes.xml` with base Android theme for splash/launch
- [ ] T014 [US1] Create `app/src/main/res/values/strings.xml` with minimal app_name string in French ("Bâtiment Avicole") as default resource fallback

**Checkpoint**: `./gradlew assembleDevDebug` succeeds, APK installs and launches showing a minimal screen

---

## Phase 4: User Story 2 — App Displays Consistent Visual Theme (Priority: P1)

**Goal**: The app displays the Forest Teal light palette and Sleek Carbon dark palette with Outfit heading and Inter body typography, 8dp spacing grid, shape tokens (8/16/24/28dp corners), and 4-level elevation system. All visual styling via design tokens — zero hardcoded values.

**Independent Test**: Launch app in light mode, verify Forest Teal colors; switch system to dark mode, verify Sleek Carbon colors; inspect typography uses Outfit headings and Inter body text.

**Dependencies**: Requires Phase 2 (Foundational) complete.

### Implementation for User Story 2

- [ ] T015 [P] [US2] Add Outfit font files (regular, medium, semibold, bold .ttf) to `app/src/main/res/font/` — download from Google Fonts (SIL Open Font License)
- [ ] T016 [P] [US2] Add Inter font files (regular, medium, semibold, bold .ttf) to `app/src/main/res/font/` — download from Google Fonts (SIL Open Font License)
- [ ] T017 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Color.kt` with Forest Teal light palette (primary, secondary, tertiary, surface, background, on-* colors) and Sleek Carbon dark palette per UI spec §1.1 — all as Compose `Color` values, no dynamic color
- [ ] T018 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Type.kt` with OutfitFontFamily and InterFontFamily definitions referencing bundled font resources, plus Material 3 Typography (displayLarge/Medium/Small → Outfit; bodyLarge/Medium/Small → Inter) per UI spec §1.2
- [ ] T019 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Shape.kt` with design token shapes: 8dp badge corners, 16dp card corners, 24dp pill button corners, 28dp dialog corners per FR-005
- [ ] T020 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Spacing.kt` with 8dp baseline grid CompositionLocal provider: spacing values as multiples of 4dp (xxs=4dp, xs=8dp, sm=12dp, md=16dp, lg=24dp, xl=32dp, xxl=48dp) per FR-004
- [ ] T021 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Elevation.kt` with 4-level elevation token object: flat=0dp, card=2dp, hover=6dp, modal=12dp per FR-006
- [ ] T022 [US2] Create `app/src/main/java/com/poultry/broiler/presentation/theme/Theme.kt` with `PoultryTheme` composable using `lightColorScheme()` and `darkColorScheme()` with explicit token assignments, `isSystemInDarkTheme()` detection, and CompositionLocal providers for Spacing — no `dynamicDarkColorScheme()` per R-003
- [ ] T023 [US2] Wire `PoultryTheme` into `MainActivity.kt` `setContent` block — update `app/src/main/java/com/poultry/broiler/MainActivity.kt` to wrap all content in `PoultryTheme {}`
- [ ] T024 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/components/StatusBadge.kt` themed shell composable with 8dp corner radius, Inter labelSmall typography, primaryContainer background, 4dp/8dp padding, @Preview annotation per contracts/composables.md
- [ ] T025 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/components/NumericInputField.kt` themed shell composable with OutlinedTextField, trailing unitLabel, KeyboardType.Decimal, 48dp+ touch target, @Preview annotation per contracts/composables.md
- [ ] T026 [P] [US2] Create `app/src/main/java/com/poultry/broiler/presentation/components/BottomSheet.kt` themed shell composable with ModalBottomSheet, 28dp top corners, 12dp modal elevation, drag handle, titleMedium Outfit title, 16dp/24dp padding, @Preview annotation per contracts/composables.md

**Checkpoint**: App launches with full design system — Forest Teal light, Sleek Carbon dark, Outfit/Inter typography, all tokens applied. US1 acceptance criteria fully satisfied.

---

## Phase 5: User Story 3 — Developer Navigates Between Placeholder Screens (Priority: P2)

**Goal**: Bottom navigation bar with 5 labeled tabs (Accueil, Assistant, Tableau de bord, Catalogue, Paramètres) routes to placeholder screens using Compose Navigation. Single-activity, no Fragments.

**Independent Test**: Launch app, tap each bottom nav tab — corresponding placeholder screen loads with correct French label. Press back from any screen — returns to Home.

**Dependencies**: Requires Phase 4 (US2 Theme) complete. Can run in parallel with US4 and US5.

### Implementation for User Story 3

- [ ] T027 [US3] Create `app/src/main/java/com/poultry/broiler/presentation/navigation/NavRoute.kt` sealed class with 5 routes (Home, Wizard, Dashboard, Catalog, Settings) each defining `route: String`, `icon: ImageVector`, and `labelResId: Int` per R-002 and data-model.md NavRoute specification
- [ ] T028 [P] [US3] Create `app/src/main/java/com/poultry/broiler/presentation/home/HomeScreen.kt` placeholder composable displaying screen title in Outfit heading, themed with PoultryTheme tokens
- [ ] T029 [P] [US3] Create `app/src/main/java/com/poultry/broiler/presentation/wizard/WizardScreen.kt` placeholder composable displaying screen title in Outfit heading, themed with PoultryTheme tokens
- [ ] T030 [P] [US3] Create `app/src/main/java/com/poultry/broiler/presentation/dashboard/DashboardScreen.kt` placeholder composable displaying screen title in Outfit heading, themed with PoultryTheme tokens
- [ ] T031 [P] [US3] Create `app/src/main/java/com/poultry/broiler/presentation/catalog/CatalogScreen.kt` placeholder composable displaying screen title in Outfit heading, themed with PoultryTheme tokens
- [ ] T032 [P] [US3] Create `app/src/main/java/com/poultry/broiler/presentation/settings/SettingsScreen.kt` placeholder composable displaying screen title in Outfit heading, themed with PoultryTheme tokens
- [ ] T033 [US3] Create `app/src/main/java/com/poultry/broiler/presentation/navigation/BottomNavBar.kt` composable using Material 3 `NavigationBar` with `NavigationBarItem` for each NavRoute, 48dp+ touch targets, French labels from string resources
- [ ] T034 [US3] Create `app/src/main/java/com/poultry/broiler/presentation/navigation/PoultryNavHost.kt` composable wrapping `NavHost` with all 5 routes, start destination Home, back navigation popUpTo Home per R-002
- [ ] T035 [US3] Integrate PoultryNavHost and BottomNavBar into `app/src/main/java/com/poultry/broiler/MainActivity.kt` with Scaffold layout — bottom bar persists across all destinations

**Checkpoint**: All 5 tabs navigable, placeholder screens render with correct labels, back returns to Home

---

## Phase 6: User Story 4 — App Loads Seed Data on First Launch (Priority: P2)

**Goal**: Room database initializes on first launch with `createFromAsset("seed/poultry.db")` containing BreedProfile records (Ross 308, Cobb 500) and EquipmentItem records across 6 categories. Data available offline immediately.

**Independent Test**: Fresh install → launch app → open Database Inspector → verify `breed_profiles` has ≥2 records, `equipment_items` has records across VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING categories.

**Dependencies**: Requires Phase 2 (Foundational) complete. Can run in parallel with US3 and US5.

### Implementation for User Story 4

- [ ] T036 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/model/EquipmentCategory.kt` pure Kotlin enum with values VENTILATION, FEEDING, HEATING, LIGHTING, COOLING, WATERING per data-model.md
- [ ] T037 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/model/GrowthTarget.kt` data class with week, targetWeightG, dailyFeedG, dailyWaterMl per data-model.md
- [ ] T038 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/model/BreedProfile.kt` domain model with breedName, supplier, growthTargets (List<GrowthTarget>), densityRange, targetFcr, cycleDurationDays, targetWeightGrams, mortalityRatePercent, description per data-model.md
- [ ] T039 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/model/EquipmentItem.kt` domain model with name, category (EquipmentCategory), brand, modelNumber, capacity, powerWatts, unit, coverageM2, description per data-model.md
- [ ] T040 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/local/entity/BreedProfileEntity.kt` Room @Entity(tableName="breed_profiles") with all columns per data-model.md: id (PK auto), breed_name (UNIQUE), supplier, growth_targets_json, min/max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description. Add UNIQUE index on breed_name
- [ ] T041 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/local/entity/EquipmentItemEntity.kt` Room @Entity(tableName="equipment_items") with all columns per data-model.md: id (PK auto), name, category, brand, model_number, capacity, power_watts, unit, coverage_m2, description. Add index on category
- [ ] T042 [US4] Create `app/src/main/java/com/poultry/broiler/data/local/converter/GrowthTargetListConverter.kt` Room @TypeConverter for JSON ↔ List<GrowthTarget> conversion using kotlinx.serialization per data-model.md
- [ ] T043 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/local/dao/BreedProfileDao.kt` Room @Dao interface with getAll(): Flow<List<BreedProfileEntity>>, getById(id: Long), getByName(breedName: String), count() per contracts/daos.md
- [ ] T044 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/local/dao/EquipmentItemDao.kt` Room @Dao interface with getAll(): Flow<List<EquipmentItemEntity>>, getByCategory(category: String), getById(id: Long), count() per contracts/daos.md
- [ ] T045 [US4] Create `app/src/main/java/com/poultry/broiler/data/local/PoultryDatabase.kt` Room @Database(version=1, entities=[BreedProfileEntity, EquipmentItemEntity], exportSchema=true) with @TypeConverters(GrowthTargetListConverter), abstract DAO accessors, and createFromAsset("seed/poultry.db") per R-001
- [ ] T046 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/mapper/BreedProfileMapper.kt` with extension functions mapping BreedProfileEntity ↔ BreedProfile domain model including growth_targets_json deserialization to List<GrowthTarget> and density range mapping
- [ ] T047 [P] [US4] Create `app/src/main/java/com/poultry/broiler/data/mapper/EquipmentItemMapper.kt` with extension functions mapping EquipmentItemEntity ↔ EquipmentItem domain model including String ↔ EquipmentCategory enum conversion
- [ ] T048 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/repository/BreedRepository.kt` interface with getAllBreeds(): Flow<List<BreedProfile>>, getBreedById(id: Long): BreedProfile?, getBreedByName(breedName: String): BreedProfile? per contracts/repositories.md
- [ ] T049 [P] [US4] Create `app/src/main/java/com/poultry/broiler/domain/repository/EquipmentRepository.kt` interface with getAllEquipment(): Flow<List<EquipmentItem>>, getEquipmentByCategory(category: EquipmentCategory), getEquipmentById(id: Long) per contracts/repositories.md
- [ ] T050 [US4] Create `app/src/main/java/com/poultry/broiler/data/repository/BreedRepositoryImpl.kt` implementing BreedRepository, @Inject constructor with BreedProfileDao, using BreedProfileMapper for entity-to-domain conversion
- [ ] T051 [US4] Create `app/src/main/java/com/poultry/broiler/data/repository/EquipmentRepositoryImpl.kt` implementing EquipmentRepository, @Inject constructor with EquipmentItemDao, using EquipmentItemMapper for entity-to-domain conversion
- [ ] T052 [US4] Create `app/src/main/java/com/poultry/broiler/di/DatabaseModule.kt` Hilt @Module @InstallIn(SingletonComponent) with @Provides @Singleton provideDatabase (createFromAsset), @Provides provideBreedProfileDao, @Provides provideEquipmentItemDao per contracts/daos.md
- [ ] T053 [US4] Create `app/src/main/java/com/poultry/broiler/di/RepositoryModule.kt` Hilt @Module @InstallIn(SingletonComponent) abstract class with @Binds bindBreedRepository and @Binds bindEquipmentRepository per contracts/repositories.md
- [ ] T054 [US4] Build pre-populated SQLite seed database file with Ross 308 and Cobb 500 breed profiles (including growth_targets_json) and 6 equipment items across all categories, placing it at `app/src/main/assets/seed/poultry.db` — schema must match Room entity definitions exactly per R-001
- [ ] T055 [US4] Create `app/src/main/java/com/poultry/broiler/util/SeedErrorHandler.kt` with SeedLoadResult sealed class (Success, Error with retryCount), retry logic using SharedPreferences, and French error messages ("Veuillez réinstaller l'application" after 2 failures) per R-005

**Checkpoint**: Fresh app install loads seed database — breed_profiles and equipment_items tables populated, queryable via DAOs

---

## Phase 7: User Story 5 — App Displays Content in French (Priority: P2)

**Goal**: All user-facing text is in French by default. Resource directories for French (primary) and Arabic (stub) exist. Non-supported locales fall back to French.

**Independent Test**: Launch on French locale — all text in French. Switch to English — falls back to French. Verify `values/`, `values-fr/`, `values-ar/` directories exist.

**Dependencies**: Requires Phase 5 (US3 Navigation) complete for navigation labels. Can run in parallel with US4.

### Implementation for User Story 5

- [ ] T056 [US5] Update `app/src/main/res/values/strings.xml` with all French default strings: app_name, navigation labels (nav_home="Accueil", nav_wizard="Assistant", nav_dashboard="Tableau de bord", nav_catalog="Catalogue", nav_settings="Paramètres"), placeholder screen titles, error messages for seed failure per FR-014
- [ ] T057 [P] [US5] Create `app/src/main/res/values-fr/strings.xml` mirroring all strings from default `values/strings.xml` (identical French content) per FR-015
- [ ] T058 [P] [US5] Create `app/src/main/res/values-ar/strings.xml` as empty stub file with XML header and empty `<resources>` element for future Arabic localization per FR-015

**Checkpoint**: App shows French text on all locales, `values-ar/` directory exists as stub

---

## Phase 8: User Story 6 — CI Pipeline Validates Code Quality (Priority: P3)

**Goal**: GitHub Actions workflow runs ktlint → Detekt → Android Lint → unit tests on every PR to `main`. Sequential execution for fast failure.

**Independent Test**: Push branch with formatting violation → CI pipeline catches it and fails.

**Dependencies**: Requires Phase 1 (Setup) complete. Can run in parallel with all user story phases.

### Implementation for User Story 6

- [ ] T059 [US6] Create `.github/workflows/ci.yml` GitHub Actions workflow: trigger on pull_request to main, ubuntu-latest runner, Java 17 setup, Gradle cache, sequential steps (ktlintCheck → detekt → lint → testDevDebugUnitTest), 15-minute timeout per R-007
- [ ] T060 [P] [US6] Create `detekt.yml` Detekt configuration at project root with custom rules: UnsafeCallOnNullableType (ERROR), LongMethod (WARNING, threshold 30), ComplexCondition (WARNING, threshold 4) per Constitution Art 2.2
- [ ] T061 [P] [US6] Create Detekt baseline file and configure ktlint Gradle plugin tasks in `app/build.gradle.kts` — ensure `./gradlew ktlintCheck` and `./gradlew detekt` run without errors on scaffold code

**Checkpoint**: `./gradlew ktlintCheck && ./gradlew detekt && ./gradlew lint && ./gradlew testDevDebugUnitTest` all pass locally

---

## Phase 9: User Story 7 — Gradle Build Flavors Switch Environments (Priority: P3)

**Goal**: `dev` and `prod` build flavors with distinct applicationIdSuffix and Firebase config source sets. `dev` connects to poultry-dev, `prod` to poultry-prod.

**Independent Test**: `./gradlew assembleDevDebug` and `./gradlew assembleProdRelease` both compile (signing failure acceptable for prod).

**Dependencies**: Requires Phase 1 (Setup, build flavors already defined in T005). Can run in parallel with all user story phases.

### Implementation for User Story 7

- [ ] T062 [US7] Create placeholder README files in `app/src/dev/` and `app/src/prod/` source set directories documenting that `google-services.json` must be placed here for Firebase configuration, plus `.gitkeep` files to preserve directory structure per R-008
- [ ] T063 [US7] Verify build flavor configuration in `app/build.gradle.kts`: dev flavor has `applicationIdSuffix = ".dev"` with `resValue("string", "app_name", "Bâtiment Avicole Dev")`, prod flavor has production app name, and google-services plugin is conditionally applied only when `google-services.json` is present per R-008

**Checkpoint**: Both `assembleDevDebug` and `assembleProdDebug` compile; `dev` has distinct applicationId

---

## Phase 10: Polish & Cross-Cutting Concerns

**Purpose**: Accessibility compliance, preview annotations, and final validation

- [ ] T064 [P] Verify all interactive elements meet 48dp × 48dp minimum touch target per FR-019 — audit BottomNavBar items, NumericInputField, and any buttons in `app/src/main/java/com/poultry/broiler/presentation/`
- [ ] T065 [P] Verify WCAG AA contrast ratios (body text ≥ 4.5:1, critical warnings ≥ 7:1) for both Forest Teal light and Sleek Carbon dark palettes in `app/src/main/java/com/poultry/broiler/presentation/theme/Color.kt` per FR-020
- [ ] T066 [P] Add `contentDescription` to all navigation icons (French resource strings) and set decorative icons to `contentDescription = null` per Constitution Art 3.3
- [ ] T067 Run full quickstart.md validation: Scenarios 1-10 (clean build, theme, navigation, seed data, localization, domain purity, CI pipeline, build flavors, accessibility, shared composables)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion — BLOCKS all user stories
- **US1 (Phase 3)**: Depends on Foundational — minimal tasks (mostly delivered by Setup + Foundational)
- **US2 (Phase 4)**: Depends on Foundational — completes US1 acceptance (themed screen)
- **US3 (Phase 5)**: Depends on US2 (Theme) — placeholder screens use design tokens
- **US4 (Phase 6)**: Depends on Foundational only — data layer is independent of UI (can run in parallel with US2/US3)
- **US5 (Phase 7)**: Depends on US3 (Navigation labels) — but string files can be started early
- **US6 (Phase 8)**: Depends on Setup only — CI config is independent (can run in parallel with any story)
- **US7 (Phase 9)**: Depends on Setup only — build flavor config is independent (can run in parallel with any story)
- **Polish (Phase 10)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (P1)**: Delivered by Setup + Foundational + US2 — no unique blocking dependencies
- **US2 (P1)**: Foundational only — independent of other stories
- **US3 (P2)**: Requires US2 (theme for styled screens) — can run in parallel with US4
- **US4 (P2)**: Requires Foundational only — fully independent of US2/US3 (pure data layer)
- **US5 (P2)**: Requires US3 for navigation label integration — but string files can be pre-created
- **US6 (P3)**: Fully independent — can start after Setup
- **US7 (P3)**: Fully independent — can start after Setup

### Within Each User Story

- Theme tokens (Color, Type, Shape, Spacing, Elevation) before Theme.kt composition
- Theme.kt before wiring into MainActivity
- NavRoute before NavHost and BottomNavBar
- Domain models before Room entities
- Room entities before DAOs
- DAOs before PoultryDatabase
- PoultryDatabase before DatabaseModule
- Repository interfaces before implementations
- Repository implementations before RepositoryModule
- Core implementation before integration

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel (T003, T004, T006, T007, T008)
- Font file tasks T015/T016 can run in parallel with Color/Shape/Spacing/Elevation tasks
- All 5 placeholder screen tasks (T028-T032) can run in parallel
- All domain model tasks (T036-T039) can run in parallel
- Both Room entity tasks (T040, T041) can run in parallel
- Both DAO tasks (T043, T044) can run in parallel
- Both mapper tasks (T046, T047) can run in parallel
- Both repository interface tasks (T048, T049) can run in parallel
- All 3 shared composable tasks (T024, T025, T026) can run in parallel
- US4 and US6 can run in parallel with US2/US3 (no file overlap)
- Localization string files (T057, T058) can run in parallel

---

## Parallel Example: User Story 2 (Design System)

```
# Launch all font + token tasks together:
Task: T015 "Add Outfit font files to app/src/main/res/font/"
Task: T016 "Add Inter font files to app/src/main/res/font/"
Task: T017 "Create Color.kt with Forest Teal and Sleek Carbon palettes"
Task: T018 "Create Type.kt with Outfit/Inter FontFamily definitions"
Task: T019 "Create Shape.kt with design token shapes"
Task: T020 "Create Spacing.kt with 8dp baseline grid"
Task: T021 "Create Elevation.kt with 4-level elevation"

# Then sequential: Theme.kt (depends on all tokens)
Task: T022 "Create Theme.kt with PoultryTheme composable"

# Then wire into Activity:
Task: T023 "Wire PoultryTheme into MainActivity.kt"

# Launch all composables in parallel (depend on Theme):
Task: T024 "Create StatusBadge.kt"
Task: T025 "Create NumericInputField.kt"
Task: T026 "Create BottomSheet.kt"
```

## Parallel Example: User Story 4 (Seed Data)

```
# Launch all domain models together:
Task: T036 "Create EquipmentCategory enum"
Task: T037 "Create GrowthTarget data class"
Task: T038 "Create BreedProfile domain model"
Task: T039 "Create EquipmentItem domain model"

# Launch both Room entities together:
Task: T040 "Create BreedProfileEntity"
Task: T041 "Create EquipmentItemEntity"

# Launch both DAOs together (after entities):
Task: T043 "Create BreedProfileDao"
Task: T044 "Create EquipmentItemDao"

# Launch both mappers and both repo interfaces together:
Task: T046 "Create BreedProfileMapper"
Task: T047 "Create EquipmentItemMapper"
Task: T048 "Create BreedRepository interface"
Task: T049 "Create EquipmentRepository interface"
```

---

## Implementation Strategy

### MVP First (User Stories 1 + 2 Only)

1. Complete Phase 1: Setup (Gradle, configs)
2. Complete Phase 2: Foundational (App class, Activity, Manifest)
3. Complete Phase 3: US1 (minimal resources)
4. Complete Phase 4: US2 (full design system)
5. **STOP and VALIDATE**: App compiles, launches with Forest Teal theme, toggles to Sleek Carbon dark mode
6. **US1 + US2 acceptance criteria met** — themed, compilable Android app

### Incremental Delivery

1. Complete Setup + Foundational + US1 + US2 → Themed app shell (MVP!)
2. Add US3 (Navigation) → 5 navigable screens with bottom bar
3. Add US4 (Seed Data) → Room database with breed/equipment data (can parallel with US3)
4. Add US5 (Localization) → All text in French
5. Add US6 (CI) → Automated quality checks (can parallel with US3-US5)
6. Add US7 (Build Flavors) → Environment separation (can parallel with US3-US5)
7. Polish → Accessibility audit, final validation

### Parallel Team Strategy

With multiple developers after Setup + Foundational:

1. **Developer A**: US2 (Design System) → US3 (Navigation) → US5 (Localization)
2. **Developer B**: US4 (Seed Data / Room) → US7 (Build Flavors)
3. **Developer C**: US6 (CI Pipeline) → Polish & Validation

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- [Story] label maps task to specific user story for traceability
- Each user story is independently completable and testable
- No test tasks included — testing infrastructure configured but individual tests deferred to consuming features
- All composables are themed shells — no interactive behavior per spec clarification
- Domain layer must have ZERO `android.*` or `androidx.*` imports (Constitution Art 1.2.1)
- All strings in resource files, no hardcoded text in composables (Constitution Art 8.2)
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
