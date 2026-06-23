# Feature Brief: Breed Configuration & Reference Database

**Sequence**: #004 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System, #002 Project Management
- **Unlocks**: #005 Capacity Planning
- **Parallelizable with**: #003 House Dimensions Wizard Step

## Feature Description

Broiler house design decisions depend heavily on the specific bird breed being raised. Different breeds have different space requirements, growth rates, feed conversion ratios, comfort temperature ranges, and airflow needs. This feature provides a built-in breed reference database and a wizard step for selecting and configuring breed parameters for each project.

The app ships with a preloaded catalog of common commercial broiler breeds — at minimum Ross 308 and Cobb 500 — seeded from the app assets database established in Feature #001. Each breed profile contains standardized parameters: optimal density (space per bird in m²), feed conversion ratio (FCR), target slaughter weight (kg), growth cycle duration (days), comfort temperature range (min/max °C), comfort humidity range (min/max %), and required airflow per bird (CFM).

When a user reaches the Breed Selection wizard step (Step 2), they browse the breed catalog via a searchable list or bottom sheet. Selecting a breed auto-populates all breed-specific parameters into the project configuration. These parameters are displayed clearly so the user understands the implications. Users can override individual parameters if their local conditions or supplier specifications differ from the defaults, but overridden values are visually flagged.

The breed catalog is also accessible as a standalone reference screen from the main navigation (CATALOG_BREEDS route) where users can browse breed profiles, compare parameters side-by-side, and learn about breed characteristics — without needing to be inside a project wizard. This serves a learning and reference purpose for farmers.

## Derived From

- **Product Spec**: §5.1 Core Feature 3 — Breed Configuration & Reference Database (breed catalog, auto-retrieve parameters, FCR, growth days, comfort ranges, CFM)
- **UI Spec**: §3 Navigation (CATALOG_BREEDS route), §4.8 Step 2: Breed Selection (Cobb 500, Ross 308, pre-loaded catalog)
- **Constitution**: Article 1.2 (Offline-First — preloaded catalog), Article 7.1 (assets/seed/ for preloaded databases)

## Acceptance Criteria Summary

- [ ] App ships with preloaded breed profiles (at minimum Ross 308 and Cobb 500) accessible offline
- [ ] Breed Selection wizard step (Step 2) allows browsing and selecting a breed from the catalog
- [ ] Selecting a breed auto-populates project parameters: density, FCR, slaughter weight, growth days, temperature/humidity ranges, and CFM per bird
- [ ] Users can override auto-populated parameters with custom values; overridden fields are visually flagged
- [ ] Standalone Breed Catalog screen is accessible from main navigation for reference browsing
- [ ] Breed profiles display all parameters clearly with appropriate units and explanations

## UI Components (if applicable)

- **BreedSelectionStep**: Wizard Step 2 composable with searchable breed list and parameter preview
- **BreedProfileCard**: Detailed card showing all breed parameters (density, FCR, weight, growth days, temperature/humidity, CFM)
- **BreedCatalogScreen**: Standalone browsable catalog accessible from CATALOG_BREEDS navigation route
- **BreedComparisonSheet**: Bottom sheet for comparing two breed profiles side-by-side (parameter column comparison)

## Technical Hints

- Breed data is seeded from `assets/seed/` via Room's `createFromAsset()` or a pre-populate callback during database initialization (#001)
- Domain model `BreedProfile` should be a pure Kotlin data class in the Domain layer; the Room entity `BreedEntity` maps to it via the Data layer mapper
- Constitution Article 2.5 requires KDoc on complex business logic — breed parameter relationships and their impact on downstream calculations should be documented

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
