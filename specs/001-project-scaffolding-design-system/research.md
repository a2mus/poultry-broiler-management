# Research: Project Scaffolding & Design System

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`

---

## R-001: Room createFromAsset() Seed Data Strategy

**Decision**: Use `Room.databaseBuilder().createFromAsset("seed/poultry.db")` with a pre-built SQLite database file packaged in `assets/seed/`.

**Rationale**: `createFromAsset()` is the official Room mechanism for preloading databases. It copies the asset file on first database open, is transactional, and integrates with Room's migration infrastructure. This is simpler and more reliable than programmatic inserts in a `RoomDatabase.Callback.onCreate()`.

**Alternatives considered**:
- `createFromFile()` — requires a file on the filesystem, not suitable for bundled assets
- `RoomDatabase.Callback.onCreate()` with programmatic inserts — more code, slower for large datasets, and harder to maintain seed data
- Raw SQL asset executed in callback — fragile, no compile-time schema validation

**Key implementation notes**:
- The pre-built `.db` file must match the Room schema exactly (same table names, column types, indices)
- Room generates a schema hash; the asset DB must be built against the same entity definitions
- Build a Gradle task or standalone script to generate `poultry.db` from SQL or CSV sources to ensure reproducibility
- Destructive migration fallback should NOT be used; instead, provide explicit migrations from version 1 onward

---

## R-002: Compose Navigation with Bottom Navigation Best Practices

**Decision**: Use `NavHost` with a sealed class `NavRoute` defining 5 destinations. Bottom navigation uses `NavigationBar` (Material 3) with `NavigationBarItem` for each route. Navigation state is hoisted to `MainActivity` level.

**Rationale**: Sealed class routes provide compile-time exhaustive checking. Material 3 `NavigationBar` replaces the deprecated `BottomNavigation`. Hoisting nav state to the Activity level ensures the bottom bar persists across all destinations.

**Alternatives considered**:
- String-based routes — no type safety, prone to typos
- Fragment-based navigation — explicitly prohibited by Constitution Art 1.2.3
- Compose Navigation with type-safe args plugin — unnecessary complexity for placeholder screens; can be adopted later when screens accept parameters

**Key implementation notes**:
- `NavRoute` sealed class with `route: String` and `icon: ImageVector` properties
- Start destination: `Home`
- Back behavior: system back from any screen returns to Home (popUpTo Home, inclusive = false)
- Each screen is a separate composable file in its feature package
- Navigation labels must come from string resources (French)

---

## R-003: Material 3 Dynamic vs. Static Color Scheme

**Decision**: Use static custom color schemes (Forest Teal light, Sleek Carbon dark) defined in `Color.kt`, NOT Material 3 dynamic color (`dynamicDarkColorScheme`/`dynamicLightColorScheme`).

**Rationale**: The UI spec mandates specific HSL-based palettes designed for outdoor field glare reduction. Dynamic color (based on wallpaper) would override these carefully chosen palettes. The design system is a core deliverable of this feature.

**Alternatives considered**:
- Dynamic color with fallback — would only work on Android 12+ and wouldn't match the design spec on supporting devices
- Dynamic color only — violates spec FR-002 and Constitution Art 3.1

**Key implementation notes**:
- `lightColorScheme()` and `darkColorScheme()` with explicit color token assignments
- `isSystemInDarkTheme()` to auto-detect system preference
- All colors defined as Compose `Color` values in `Color.kt`, referenced by the `MaterialTheme.colorScheme` accessor
- No `dynamicDarkColorScheme()` or `dynamicLightColorScheme()` calls

---

## R-004: Custom Font Loading (Outfit + Inter)

**Decision**: Bundle Outfit and Inter as `.ttf` files in `res/font/` and reference them via `FontFamily` definitions in `Type.kt`.

**Rationale**: Bundling fonts ensures offline availability (Constitution Art 1.2.2) and eliminates the need for Google Fonts Downloadable Fonts API, which requires network connectivity and adds latency on first load.

**Alternatives considered**:
- Google Fonts Downloadable Fonts — requires network on first use, violates offline-first mandate
- System fonts only — neither Outfit nor Inter are system fonts on Android
- `androidx.compose.ui:ui-text-google-fonts` — still requires network for initial download

**Key implementation notes**:
- Include `outfit_regular.ttf`, `outfit_medium.ttf`, `outfit_semibold.ttf`, `outfit_bold.ttf`
- Include `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`
- Define `val OutfitFontFamily = FontFamily(...)` and `val InterFontFamily = FontFamily(...)` in `Type.kt`
- Map to Material 3 `Typography`: displayLarge/Medium/Small → Outfit; bodyLarge/Medium/Small → Inter
- Verify font file licenses: both are SIL Open Font License — compatible

---

## R-005: Seed Data Error Handling with Retry

**Decision**: Wrap `createFromAsset()` database initialization in a try-catch. On failure, show a full-screen error composable with a "Retry" button. On second failure, show a reinstall guidance message.

**Rationale**: Spec clarification session mandated retry + reinstall guidance. The error must be caught at database creation time (Application/Activity startup) and surfaced as UI state.

**Alternatives considered**:
- Silent degradation with empty DB — rejected in clarification session
- Crash with standard Android crash dialog — poor UX, no guidance to user

**Key implementation notes**:
- Database creation happens in `PoultryDatabase` companion object via Hilt `@Provides`
- Wrap creation in `SeedLoadResult` sealed class: `Success(db)`, `Error(exception, retryCount)`
- ViewModel observes `SeedLoadResult` and renders appropriate screen state
- Retry resets the database file and re-invokes `createFromAsset()`
- After 2 failures, display French message: "Veuillez réinstaller l'application"
- `retryCount` persisted in `SharedPreferences` (not Room, since Room failed)

---

## R-006: Hilt Module Organization for Scaffolding

**Decision**: Two Hilt modules for this feature: `DatabaseModule` (provides Room database + DAOs) and `RepositoryModule` (binds repository interfaces to implementations).

**Rationale**: Follows Constitution Art 1.2.5 and standard Hilt convention of separating data provision from abstraction binding. Keeps modules focused and testable.

**Alternatives considered**:
- Single `AppModule` — too coarse, harder to test and replace in instrumented tests
- Module-per-entity — premature granularity for 2 entities

**Key implementation notes**:
- `@Module @InstallIn(SingletonComponent::class)` for both (database is app-scoped singleton)
- `DatabaseModule`: `@Provides @Singleton fun provideDatabase()`, `@Provides fun provideBreedDao()`, `@Provides fun provideEquipmentDao()`
- `RepositoryModule`: `@Binds fun bindBreedRepository()`, `@Binds fun bindEquipmentRepository()`
- ViewModels use `@HiltViewModel` with constructor injection of use cases/repositories

---

## R-007: CI/CD Pipeline Configuration (GitHub Actions)

**Decision**: Single workflow file `.github/workflows/ci.yml` with sequential jobs: ktlint → Detekt → Android Lint → Unit Tests. Triggered on `pull_request` to `main`.

**Rationale**: Constitution Art 5.1 mandates this exact pipeline. Sequential execution ensures fast failure — if ktlint fails, no point running tests (saves CI minutes).

**Alternatives considered**:
- Parallel jobs — faster but wastes CI minutes on failing PRs; sequential is more cost-effective for a small team
- Multiple workflow files — unnecessary complexity for a single pipeline

**Key implementation notes**:
- Use `ubuntu-latest` runner with Java 17 (required for Kotlin 1.9+)
- Cache Gradle dependencies with `actions/cache` for `~/.gradle/caches`
- Steps: checkout → setup-java → setup-gradle → ktlint check → detekt → lint → test
- Instrumented tests excluded from PR pipeline (run on release); noted as `# TODO: add emulator job for release`
- Timeout: 15 minutes max per workflow run
- `google-services.json` not needed for lint/test; use a dummy placeholder or conditional apply plugin

---

## R-008: Gradle Version Catalog and Build Flavor Setup

**Decision**: Use `gradle/libs.versions.toml` for all dependency version management. Define `dev` and `prod` product flavors in `app/build.gradle.kts` with distinct `applicationIdSuffix` and Firebase config source sets.

**Rationale**: Constitution Art 1.4 and FR-012 mandate version catalog with pinned versions. FR-016 mandates build flavors for Firebase environment switching.

**Alternatives considered**:
- `buildSrc` with Kotlin DSL — more boilerplate, version catalog is now the standard Gradle approach
- `.properties` files for environment switching — doesn't integrate with Firebase's `google-services.json` mechanism

**Key implementation notes**:
- Version catalog sections: `[versions]`, `[libraries]`, `[bundles]`, `[plugins]`
- Key entries: compose-bom, room, hilt, navigation-compose, material3, junit5, mockk, turbine, detekt, ktlint
- Flavors in `productFlavors { create("dev") { ... } create("prod") { ... } }`
- `dev` flavor: `applicationIdSuffix = ".dev"`, `resValue("string", "app_name", "Poultry Dev")`
- `prod` flavor: no suffix, production app name
- Source sets: `app/src/dev/` and `app/src/prod/` each containing their respective `google-services.json`
- `.gitignore` includes `**/google-services.json`
