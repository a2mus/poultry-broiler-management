# Research: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## Research Summary

No NEEDS CLARIFICATION items were identified in the Technical Context. The technology stack is fully established from Feature #001 (Project Scaffolding & Design System) and the project constitution. This research document consolidates best practices and pattern decisions for the implementation.

## Decision Log

### D1: Room Entity Design for Project

**Decision**: Single `ProjectEntity` table with all scalar fields; no embedded objects or relations needed at this stage.

**Rationale**: The Project entity has only scalar fields (strings, enums, timestamps). No nested collections or relationships exist within this feature scope. Future features (#003 House Dimensions, #004 Breed Configuration) will add their own entities with foreign keys referencing `ProjectEntity.id`. Keeping the entity flat avoids premature complexity.

**Alternatives Considered**:
- Embedded `@Embedded` sub-objects for metadata grouping — rejected because there are no logical sub-groups of fields that would benefit from embedding, and it adds mapping complexity without value.
- JSON blob for extensible fields — rejected because it defeats Room's type safety and query capabilities. All fields are known at this stage.

### D2: Search Implementation Strategy

**Decision**: Room `@Query` with SQL `LIKE` operator for search, combined with `StateFlow` debounce in the ViewModel.

**Rationale**: The spec requires case-insensitive partial match filtering on name and location fields. Room's `LIKE` with `COLLATE NOCASE` handles this efficiently at the database level. With 50+ projects (the stated scale), in-memory filtering would also work, but query-level filtering is more scalable and avoids loading all projects into memory before filtering. A 300ms debounce in the ViewModel prevents excessive query re-execution during rapid typing.

**Alternatives Considered**:
- In-memory filtering via Kotlin `filter {}` on the full list — rejected for scale concerns; loads all projects even when filtering reduces the result set.
- FTS (Full-Text Search) virtual table — rejected as over-engineered for partial match on 2 string fields at this scale.

### D3: Project Duplication Strategy

**Decision**: Deep copy at the domain level — read the source project, create a new domain model with modified name/status/timestamps, and insert as a new entity.

**Rationale**: At this feature stage, a Project has only scalar fields, so "deep copy" is effectively a field-by-field copy with overrides. This approach is explicit, testable, and will naturally extend when future features add related entities (the UseCase will need to copy those related entities too). A SQL-level `INSERT INTO ... SELECT` would be faster but less maintainable and harder to test.

**Alternatives Considered**:
- SQL-level copy (`INSERT INTO projects SELECT ... FROM projects WHERE id = ?`) — rejected because it bypasses domain logic, is harder to test, and won't extend cleanly when related tables are added.
- Kotlin `copy()` on data class — this is actually what we use internally within the domain model, so it aligns with the chosen approach.

### D4: Sort Order Implementation

**Decision**: Room `@Query` with `ORDER BY updatedAt DESC` for default sort order.

**Rationale**: The clarification session confirmed "most recently modified first" as the sort order. Applying the sort at the query level is the most efficient approach — Room returns pre-sorted results via the database index. No additional Kotlin-side sorting needed.

**Alternatives Considered**:
- Kotlin `sortedByDescending` on the full list — rejected; duplicates work the database can do more efficiently with an index.

### D5: Project Card Bento Grid Layout

**Decision**: Use `LazyVerticalStaggeredGrid` (Compose Foundation) with adaptive column sizing for the bento grid effect.

**Rationale**: The spec calls for a "responsive bento grid layout" for project cards. `LazyVerticalStaggeredGrid` provides lazy loading (critical for 50+ projects performance), adaptive column counts based on screen width (phone vs. tablet), and staggered item heights for the bento aesthetic. This is a built-in Compose Foundation component — no third-party dependency needed.

**Alternatives Considered**:
- `LazyVerticalGrid` with fixed columns — rejected because it doesn't support staggered/variable-height items needed for the bento layout.
- Custom Canvas-based grid — rejected as over-engineered; the built-in component handles this.

### D6: Context Menu Implementation

**Decision**: Custom `DropdownMenu` triggered by long-press gesture with scale-down animation via `Modifier.combinedClickable` and `animateFloatAsState`.

**Rationale**: Material 3 does not provide a built-in long-press context menu component. The spec requires a context menu (Edit/Duplicate/Delete) activated by long-press with a "scale-down micro-interaction". Using `Modifier.combinedClickable(onLongClick = ...)` to trigger a `DropdownMenu` is the standard Compose pattern. The scale-down animation is achieved via `animateFloatAsState` controlling `Modifier.scale()` on the card during the long-press gesture.

**Alternatives Considered**:
- Material 3 `ExposedDropdownMenu` — rejected; designed for form inputs, not contextual actions.
- Bottom sheet with action list — rejected; the spec explicitly calls for a context menu, not a bottom sheet, for CRUD actions.

### D7: State Management Pattern

**Decision**: Single `HomeUiState` sealed interface with `Loading`, `Empty`, `Content`, and `Error` variants. Single `HomeIntent` sealed class for all user actions.

**Rationale**: This follows Constitution Art 1.2.4 (UDF via StateFlow). A sealed interface for state ensures exhaustive `when` handling in the composable. A sealed class for intents centralizes all user actions (create, search, edit, duplicate, delete, navigate) into a single processing pipeline in the ViewModel. This pattern was established in Feature #001 and is consistent across the codebase.

**Alternatives Considered**:
- Multiple `StateFlow` properties (one per UI concern) — rejected; fragments state across multiple observables, making testing and state consistency harder.
- MVI with `Reducer` pattern — rejected as over-engineered for the current complexity level; a simple `when` dispatch in the ViewModel is sufficient.

## Technology Confirmations

All technologies confirmed from Feature #001 — no new dependencies required:

| Technology | Version | Usage in This Feature |
|-----------|---------|----------------------|
| Room | 2.6+ | ProjectEntity, ProjectDao, database migration |
| Hilt | 2.51+ | ProjectModule for DI bindings |
| Compose Material 3 | BOM latest | ProjectCard, dialogs, FAB, search bar, bottom sheet |
| Compose Navigation | latest | Navigate to wizard/dashboard stubs on project tap/create |
| Turbine | latest | ViewModel StateFlow testing |
| MockK | latest | UseCase and Repository mocking |
| JUnit 5 | latest | Test runner |

## Database Migration Note

Feature #001 creates the initial Room database with `BreedProfileEntity` and `EquipmentItemEntity` tables via `createFromAsset()`. This feature adds a `ProjectEntity` table, which requires a **Room migration** (version 1 → 2). The migration adds the `projects` table with all columns. The `createFromAsset()` seed database from #001 is unaffected since it only contains breed and equipment seed data.
