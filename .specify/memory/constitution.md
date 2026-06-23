# Project Constitution: Poultry Broiler House Design & Management

**Ratification Date**: 2026-06-23
**Last Amended**: 2026-06-23
**Version**: 1.0.0
**Derived From**: product-spec.md, ui-spec.md

---

## Preamble

This constitution establishes the governing principles, standards, and practices
for the development of the **Poultry Broiler House Design & Management** Android application.
All development work MUST comply with this document. Amendments require explicit
review and version increment.

---

## Article 1: Architecture

### 1.1 System Architecture
- **Pattern**: MVVM with Clean Architecture (Presentation → Domain → Data)
- **Rationale**: Isolates business logic (use cases) from UI rendering and database storage, enabling independent testing and future platform portability of domain rules.

### 1.2 Architecture Principles

1. **Clean Layer Isolation**: MUST separate UI (Compose), Domain (Use Cases), and Data (Repository/Room) layers with no reverse dependencies. Domain layer MUST NOT import Android framework classes.
2. **Offline-First Data Flow**: MUST perform all reads/writes against Room first; network calls are secondary sync operations. 100% of design, calculation, canvas rendering, and PDF generation MUST work without an active internet connection.
3. **Single Activity Architecture**: MUST use one Activity with Compose Navigation for all screens. Fragment usage is PROHIBITED.
4. **Unidirectional Data Flow**: MUST flow state from ViewModel → Composable via `StateFlow`; events from UI → ViewModel via sealed intent classes. Direct state mutation from composables is PROHIBITED.
5. **Dependency Injection via Hilt**: MUST use Hilt for all injectable components (ViewModels, Repositories, Use Cases, Data Sources). Manual object creation is PROHIBITED for services and repositories.

### 1.3 Technology Stack

| Layer | Technology | Min Version | Justification |
|-------|-----------|-------------|---------------|
| OS Platform | Android | API 26+ (8.0) | Aligns with target hardware used by field consultants |
| Language | Kotlin | 1.9+ | Modern, null-safe language with native Android support |
| UI Framework | Jetpack Compose (Material 3) | BOM latest stable | Declarative UI toolkit for responsive phone/tablet layouts |
| Local Database | Room (SQLite) | 2.6+ | Standard Android ORM for offline data storage |
| 2D Drawing | Compose Canvas API | — | Native high-performance 2D rendering without dependency bloat |
| PDF Rendering | Android `PdfDocument` | — | Built-in printing framework for layout export |
| Backend Service | Firebase (Firestore & Cloud Storage) | — | Lightweight serverless sync and document sharing |
| Authentication | Firebase Auth | — | Secure sign-in and cloud project associations |
| Network Client | Retrofit | 2.9+ | Standard HTTP client for Weather API endpoints |
| Dependency Injection | Hilt | 2.51+ | Constructor injection and test doubles creation |

### 1.4 Dependency Policy
- New third-party dependencies MUST be justified with a rationale documenting why the built-in or existing solution is insufficient.
- Dependencies MUST be sourced from Maven Central or Google Maven only.
- Dependencies MUST be pinned to specific versions (no `+` or `latest` qualifiers).
- Before adding a library, the developer MUST verify it has an active maintenance record (commits within the last 12 months) and a compatible open-source license (Apache 2.0, MIT, or BSD).

---

## Article 2: Code Quality

### 2.1 Testing Standards
- **Unit Testing**: MUST achieve ≥80% line coverage across Domain and Data layers. Framework: JUnit 5 + MockK.
- **Flow/State Testing**: MUST use Turbine for testing `StateFlow` and `SharedFlow` emissions from ViewModels.
- **Integration Testing**: MUST write integration tests for all Repository implementations and Room DAO queries.
- **UI Testing**: SHOULD write Compose UI tests for critical user flows (wizard steps, dashboard tabs, canvas interactions).
- **Test Naming Convention**: `fun methodName_givenCondition_expectedBehavior()` (e.g., `fun calculateCapacity_givenSmallArea_returnsReducedCount()`).

### 2.2 Code Style & Linting
- **Linter**: ktlint with the standard Kotlin coding conventions ruleset.
- **Static Analysis**: Detekt with the following custom rules enabled:
  - `UnsafeCallOnNullableType` (ERROR) — `!!` operator is PROHIBITED without documented justification.
  - `LongMethod` (WARNING, threshold: 30 lines).
  - `ComplexCondition` (WARNING, threshold: 4 clauses).
- **Formatter**: ktlint format on save (IDE integration).
- **Android Lint**: MUST pass with zero errors; warnings SHOULD be resolved or suppressed with justification.

### 2.3 Type Safety
- MUST use Kotlin's null-safety system throughout. Nullable types MUST be handled via safe calls (`?.`), elvis operator (`?:`), or explicit null checks.
- The `!!` (non-null assertion) operator is PROHIBITED unless accompanied by a code comment explaining why null is impossible at that call site.
- MUST use sealed classes/interfaces for representing finite state sets (screen states, navigation events, error types).
- MUST use value classes for domain primitives where type confusion is possible (e.g., `@JvmInline value class Meters(val value: Double)`).

### 2.4 Code Review
- All changes MUST be submitted via Pull Request.
- PRs MUST receive at least 1 approval before merge.
- Reviewers MUST verify:
  - Architecture layer boundaries are respected (no domain → UI imports).
  - Design tokens are used (no hardcoded colors/fonts).
  - New public APIs have KDoc documentation.
  - Tests accompany new functionality.
- Merge strategy: **Squash and Merge** to keep a clean linear history.

### 2.5 Documentation
- All public interfaces (Use Cases, Repository contracts, ViewModel public state/events) MUST have KDoc documentation.
- Complex business logic (ventilation calculations, capacity planning formulas, financial projections) MUST include inline comments explaining the mathematical model and referencing source standards (EU directives, breed datasheets).
- README.md MUST be kept current with build instructions, architecture overview, and contribution guidelines.

---

## Article 3: Design & UI

### 3.1 Design System
- All visual styling MUST be derived from the design tokens defined in `Theme.kt`, which implements the specifications from `ui-spec.md §1`.
- **Color tokens**: HSL-based light mode (Field Glare Reduction) and dark mode (Sleek Carbon) palettes. Hardcoded hex values in composable functions are PROHIBITED.
- **Typography tokens**: Outfit for display/headers, Inter for body/data. No alternative fonts without a constitution amendment.
- **Spacing**: 8dp baseline grid. All margins, padding, and alignments MUST use multiples of 4dp.
- **Shapes**: Card corners 16dp, button corners 24dp (pill), badge corners 8dp, dialog corners 28dp.
- **Elevation**: 4-level system (0dp flat, 2dp card, 6dp hover, 12dp modal).

### 3.2 Component Standards
- **Library**: Jetpack Compose Material 3 components are the default.
- **Custom Components**: Allowed only when Material 3 does not provide the required interaction (e.g., 2D Canvas blueprint, semi-circular gauge). Custom components MUST follow the design token system and be documented with KDoc.
- **Naming Convention**: Composable functions MUST use PascalCase and be prefixed with their feature domain (e.g., `ProjectCard`, `WizardStepIndicator`, `BlueprintCanvas`).

### 3.3 Accessibility
- **Compliance Level**: WCAG AA minimum for all text content; WCAG AAA for critical numerical warnings and alerts.
- **Touch Targets**: All interactive elements MUST have a minimum touch target of 48dp × 48dp (field-use with gloves).
- **Contrast Ratios**: Body text ≥ 4.5:1; critical warnings ≥ 7:1.
- **Screen Readers**: Every icon and interactive element MUST have a descriptive `contentDescription` in localized resource strings. Decorative icons MUST use `contentDescription = null`.
- **TalkBack Testing**: SHOULD be verified on all primary user flows before release.

### 3.4 Responsive Design
- **Approach**: Adaptive layout — phone (compact) and tablet (expanded) configurations.
- **Phone**: 4-column grid layout.
- **Tablet**: 8-column adaptive grid layout with potential for side-by-side panes on dashboard screens.
- **Blueprint Canvas**: MUST prioritize 10-inch tablet form factor for primary layout design; phone screens MUST support pinch-to-zoom and panning controls.

---

## Article 4: Security

### 4.1 Authentication & Authorization
- The app MUST operate fully in anonymous/local mode by default. Sign-in is NOT a barrier to using core offline design features.
- Cloud backup and remote link sharing features MUST require Firebase Auth (Email/Password) authentication.
- Firestore Security Rules MUST enforce that user-owned project records are read/write restricted to the authenticated user's UID.
- No open collection access is permitted in Firestore.

### 4.2 Data Protection
- All data synced to Firestore MUST be transmitted over TLS (Firebase default).
- Local Room database SHOULD use SQLCipher encryption for installations handling commercially sensitive project data.
- PDF exports stored temporarily on device MUST be placed in app-private storage (`Context.filesDir`), not external shared storage.

### 4.3 Input Validation
- All numeric inputs (dimensions, capacities, prices, quantities) MUST be validated at both the UI layer (Compose field validators) and the Domain layer (Use Case preconditions).
- Validation rules:
  - Dimensions: positive numbers, reasonable upper bounds (e.g., length ≤ 500m).
  - Capacity: non-negative integers.
  - Prices: non-negative decimals with 2 decimal places.
- Invalid input MUST surface inline error messages — no silent failures or crashes.

### 4.4 Secrets Management
- Firebase configuration files (`google-services.json`) MUST NOT contain production keys in the public repository.
- API keys (Weather API) MUST be stored in `local.properties` (gitignored) and injected via BuildConfig fields.
- User credentials and session tokens MUST be stored in Android Keystore encrypted storage.

### 4.5 Dependency Auditing
- MUST run OWASP Dependency-Check (`dependencyCheck` Gradle plugin) before each Play Store release.
- GitHub Dependabot MUST be enabled for automated vulnerability alerts.
- Dependencies with known critical CVEs MUST be updated or replaced before release.

---

## Article 5: DevOps & Deployment

### 5.1 CI/CD Pipeline
- Every Pull Request MUST trigger: ktlint check → Detekt analysis → Android Lint → Unit tests → Instrumented tests.
- Release builds MUST be signed via CI using securely stored keystore credentials.
- **Platform**: GitHub Actions.

### 5.2 Environment Strategy

| Environment | Firebase Project | Purpose |
|-------------|-----------------|---------|
| `dev` | poultry-dev | Development and integration testing |
| `prod` | poultry-prod | Production Play Store release |

- Staging testing uses the `dev` Firebase project with production-like configuration.
- Environment switching MUST be handled via Gradle build flavors (`dev`, `prod`).

### 5.3 Release Process
- Releases MUST be tagged in Git with the version number (e.g., `v1.2.0`).
- Play Store uploads MUST use staged rollout: 10% → 50% → 100%.
- Rollback procedure: revert to the previous version tag and re-deploy via CI.
- **Tool**: Gradle Play Publisher plugin for automated Play Store uploads.

### 5.4 Monitoring & Observability
- **Crash Reporting**: MUST integrate Firebase Crashlytics from the initial release.
- **Performance Monitoring**: SHOULD integrate Firebase Performance to track canvas render times, PDF generation duration, and database query latency.
- **Analytics**: MAY integrate Firebase Analytics for tracking feature adoption (wizard completion rates, export frequency).

### 5.5 Versioning
- MUST follow Semantic Versioning: `MAJOR.MINOR.PATCH`.
  - MAJOR: Breaking changes to data schema or export format.
  - MINOR: New features or screens.
  - PATCH: Bug fixes and performance improvements.
- `versionCode` MUST auto-increment on CI builds.
- `versionName` MUST match the Git tag.

---

## Article 6: Git Workflow

### 6.1 Branching Strategy
- **Model**: Trunk-based development with short-lived feature branches.
- `main` branch is always in a releasable state.
- Feature branches MUST be named: `feature/<short-description>` (e.g., `feature/ventilation-wizard`).
- Bugfix branches: `fix/<short-description>`.
- Release branches: `release/vX.Y.Z` (only if stabilization is needed).

### 6.2 Commit Convention
- MUST follow Conventional Commits format:
  ```
  <type>(<scope>): <description>
  ```
- Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `perf`, `style`, `ci`.
- Scope: feature area (e.g., `wizard`, `blueprint`, `financials`, `room`, `firebase`).
- Examples:
  - `feat(wizard): add breed comparison bottom sheet`
  - `fix(blueprint): correct fan placement snap-to-wall logic`
  - `test(capacity): add density threshold unit tests`

### 6.3 PR Process
- PRs MUST include:
  - A description of what changed and why.
  - Screenshots or recordings for UI changes.
  - Link to the related task/issue.
- Merge strategy: Squash and Merge.
- Branch MUST be deleted after merge.

---

## Article 7: Project Organization

### 7.1 Directory Structure

```
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

### 7.2 Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Kotlin files | PascalCase | `ProjectRepository.kt` |
| Composable functions | PascalCase, domain-prefixed | `ProjectCard()`, `BlueprintCanvas()` |
| ViewModel classes | `<Feature>ViewModel` | `WizardViewModel`, `DashboardViewModel` |
| Use Case classes | `<Verb><Noun>UseCase` | `CalculateCapacityUseCase`, `ExportPdfUseCase` |
| Room Entities | `<Name>Entity` | `ProjectEntity`, `EquipmentEntity` |
| Domain Models | Plain name (no suffix) | `Project`, `Equipment`, `BreedProfile` |
| Repository interfaces | `<Name>Repository` | `ProjectRepository`, `BreedRepository` |
| Repository implementations | `<Name>RepositoryImpl` | `ProjectRepositoryImpl` |
| Hilt Modules | `<Layer>Module` | `DataModule`, `NetworkModule` |
| String resources | `snake_case` with screen prefix | `home_search_placeholder`, `wizard_step1_title` |
| Test files | `<ClassUnderTest>Test` | `CalculateCapacityUseCaseTest` |

### 7.3 Module Boundaries
- **Domain layer**: Pure Kotlin only. MUST NOT import `android.*`, `androidx.*`, or any framework-specific classes. This ensures domain logic is portable and independently testable.
- **Data layer**: MAY import Android/Firebase frameworks. MUST expose data through Repository interfaces defined in the Domain layer.
- **Presentation layer**: MAY import Compose and Android frameworks. MUST interact with the Domain layer only through Use Cases (never directly access Repositories).
- **Cross-cutting**: Utility functions that are Android-specific belong in `util/`. Pure Kotlin utilities belong in `domain/` or a shared Kotlin module.

---

## Article 8: Localization

### 8.1 Language Support
- **Primary Language**: French (`values-fr/strings.xml`).
- **Secondary Language**: Arabic (`values-ar/strings.xml`) with full RTL layout support.
- **Default fallback**: French (stored in `values/strings.xml`).

### 8.2 Localization Rules
- All user-facing text MUST be defined in string resource files. Hardcoded strings in composables are PROHIBITED.
- Numeric formatting MUST respect locale (decimal separators, currency symbols).
- Date formatting MUST use `DateTimeFormatter` with locale-aware patterns.
- Arabic RTL: Navigation drawer swipes from right-to-left; wizard next/back buttons are mirrored; canvas coordinates remain mathematically consistent.

---

## Article 9: Governance

### 9.1 Amendment Process
- Changes to this constitution require:
  - A documented proposal explaining what changes and why.
  - Explicit approval from the project lead.
  - Version increment following semantic versioning.
  - All team members MUST be notified of changes.

### 9.2 Compliance
- All Pull Requests MUST comply with this constitution.
- Non-compliance MUST be flagged during code review.
- Exceptions require documented justification in the PR description, including:
  - What rule is being bypassed.
  - Why the exception is necessary.
  - What the plan is to resolve it (if temporary).

### 9.3 Review Schedule
- This constitution SHOULD be reviewed at each major version milestone.
- Design token updates (colors, typography) MUST trigger a constitution review to ensure consistency.

---

## Appendix A: Reference Documents
- [product-spec.md](./product-spec.md) — Product requirements and technical decisions
- [ui-spec.md](./ui-spec.md) — UI specification, design tokens, and screen architecture

## Appendix B: Tool Configuration

### Linting & Static Analysis
- **ktlint**: Standard Kotlin conventions ruleset, enforced via Gradle plugin.
- **Detekt**: Custom configuration with `UnsafeCallOnNullableType` (ERROR), `LongMethod` (WARNING @ 30 lines), `ComplexCondition` (WARNING @ 4 clauses).
- **Android Lint**: Default ruleset, zero-error policy.

### CI/CD
- **Platform**: GitHub Actions
- **Pipeline stages**: Lint → Detekt → Android Lint → Unit Tests → Instrumented Tests → Build → (Release: Sign → Upload to Play Store)

### Dependency Management
- **Vulnerability scanning**: OWASP Dependency-Check Gradle plugin
- **Automated alerts**: GitHub Dependabot
- **Version catalog**: Gradle Version Catalog (`libs.versions.toml`) for centralized dependency version management

### Firebase
- **Dev project**: `poultry-dev`
- **Prod project**: `poultry-prod`
- **Services**: Firestore, Cloud Storage, Auth, Crashlytics, Performance Monitoring
