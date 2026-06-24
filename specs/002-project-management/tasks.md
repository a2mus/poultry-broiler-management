# Tasks: Project Management

**Input**: Design documents from `specs/002-project-management/`

**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Not explicitly requested in the feature specification. Test tasks are omitted. Constitution requires ≥80% coverage (JUnit 5 + MockK, Turbine, Compose UI Test) — tests should be added as a follow-up or during implementation.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Android app**: `app/src/main/java/com/poultry/broiler/` for source
- **Resources**: `app/src/main/res/` for strings, layouts
- **Tests**: `app/src/test/` (unit), `app/src/androidTest/` (instrumented)
- Structure follows Constitution §7.1 and plan.md

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Database migration and localization resources for the Project Management feature

- [X] T001 Add Room database migration (version 1 → 2) creating the `projects` table with index on `updatedAt` in `app/src/main/java/com/poultry/broiler/data/local/PoultryDatabase.kt`
- [X] T002 [P] Add French string resources for project management UI labels (home screen, dialogs, search bar, context menu, error messages, empty states) in `app/src/main/res/values/strings.xml`
- [X] T003 [P] Add Arabic stub string resources for project management keys in `app/src/main/res/values-ar/strings.xml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Domain models, data layer, repository, ViewModel skeleton, and shared UI components that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Domain Layer

- [X] T004 [P] Create `ProjectType` enum (NEW_INSTALLATION, EXISTING_ASSESSMENT) in `app/src/main/java/com/poultry/broiler/domain/model/ProjectType.kt`
- [X] T005 [P] Create `ProjectStatus` enum (DRAFT, IN_PROGRESS, COMPLETED) in `app/src/main/java/com/poultry/broiler/domain/model/ProjectStatus.kt`
- [X] T006 Create `Project` domain model data class with all fields (id, name, type, status, location, createdAt, updatedAt, syncTimestamp) in `app/src/main/java/com/poultry/broiler/domain/model/Project.kt`
- [X] T007 [P] Create `ProjectRepository` interface with all method signatures (getAllSortedByModified, searchProjects, getProjectById, insertProject, updateProject, deleteProject) in `app/src/main/java/com/poultry/broiler/domain/repository/ProjectRepository.kt`

### Data Layer

- [X] T008 Create `ProjectEntity` Room entity with `@Entity(tableName = "projects")`, all columns, and `@Index` on updatedAt in `app/src/main/java/com/poultry/broiler/data/local/entity/ProjectEntity.kt`
- [X] T009 Create `ProjectDao` Room DAO interface with all queries (getAllSortedByUpdatedAt, searchByNameOrLocation, getById, insert, update, deleteById) in `app/src/main/java/com/poultry/broiler/data/local/dao/ProjectDao.kt`
- [X] T010 [P] Create `ProjectMapper` with bidirectional mapping (ProjectEntity.toDomain() and Project.toEntity()) in `app/src/main/java/com/poultry/broiler/data/mapper/ProjectMapper.kt`
- [X] T011 Create `ProjectRepositoryImpl` implementing ProjectRepository interface, delegating to ProjectDao with ProjectMapper conversions in `app/src/main/java/com/poultry/broiler/data/repository/ProjectRepositoryImpl.kt`

### Dependency Injection

- [X] T012 Create `ProjectModule` Hilt module binding ProjectRepository to ProjectRepositoryImpl and providing all UseCases in `app/src/main/java/com/poultry/broiler/di/ProjectModule.kt`

### Presentation Layer Skeleton

- [X] T013 [P] Create `HomeUiState` sealed interface with Loading, Empty, Content (projects, searchQuery, isSearchActive, noSearchResults), and Error variants in `app/src/main/java/com/poultry/broiler/presentation/home/HomeUiState.kt`
- [X] T014 [P] Create `HomeIntent` sealed class with all user action variants (CreateProject, UpdateSearchQuery, OpenProject, EditProject, DuplicateProject, DeleteProject, ClearSearch) in `app/src/main/java/com/poultry/broiler/presentation/home/HomeIntent.kt`
- [X] T015 Create `HomeViewModel` skeleton with Hilt injection, StateFlow<HomeUiState> emission, and onIntent(HomeIntent) dispatch method in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

### Shared UI Component

- [X] T016 Create `ProjectContextMenu` composable (DropdownMenu with Edit, Duplicate, Delete items, icons, error color for delete) in `app/src/main/java/com/poultry/broiler/presentation/home/components/ProjectContextMenu.kt`

**Checkpoint**: Foundation ready — domain models, data layer, repository, DI, ViewModel skeleton, and shared context menu are all in place. User story implementation can now begin.

---

## Phase 3: User Story 1 — Create a New Project (Priority: P1) 🎯 MVP

**Goal**: Users can create a new project (New Installation or Existing Assessment) with a name and optional location, and be navigated to the appropriate next screen.

**Independent Test**: Launch app → tap "+" or FAB → fill name + type → confirm → project appears on Home Screen with correct metadata. Empty name rejected with inline error.

### Implementation for User Story 1

- [X] T017 [US1] Create `CreateProjectUseCase` with validation (non-empty name, ≤200 chars, location ≤300 chars), UUID generation, timestamp assignment, and repository insertion in `app/src/main/java/com/poultry/broiler/domain/usecase/CreateProjectUseCase.kt`
- [X] T018 [P] [US1] Create `NewProjectPlaceholderCard` composable (dashed border, "+" icon, "Nouveau projet" label, onClick handler) in `app/src/main/java/com/poultry/broiler/presentation/home/components/NewProjectPlaceholderCard.kt`
- [X] T019 [US1] Create `NewProjectDialog` composable (name OutlinedTextField with validation, SegmentedButton for type selection, optional location field, confirm/cancel buttons) in `app/src/main/java/com/poultry/broiler/presentation/home/components/NewProjectDialog.kt`
- [X] T020 [US1] Implement empty state UI in `HomeScreen` showing NewProjectPlaceholderCard and guidance text when no projects exist, plus FAB for creation in `app/src/main/java/com/poultry/broiler/presentation/home/HomeScreen.kt`
- [X] T021 [US1] Wire CreateProject intent handling in HomeViewModel: invoke CreateProjectUseCase, emit navigation event to wizard (New Installation) or dashboard (Existing Assessment), update UI state in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

**Checkpoint**: At this point, User Story 1 should be fully functional — users can create projects from the empty state and see them appear. Navigation to wizard/dashboard stubs works.

---

## Phase 4: User Story 2 — Browse and Search Projects (Priority: P1)

**Goal**: Users see all projects as rich cards in a bento grid layout sorted by most recently modified. A search bar filters by name or location in real-time.

**Independent Test**: Pre-populate 5+ projects → Home Screen displays all as cards with correct metadata (name, badge, location, date, status) → type in search bar → list filters in real-time → clear search → all cards return.

### Implementation for User Story 2

- [X] T022 [US2] Create `GetProjectsUseCase` delegating to ProjectRepository.getAllProjectsSortedByModified() in `app/src/main/java/com/poultry/broiler/domain/usecase/GetProjectsUseCase.kt`
- [X] T023 [P] [US2] Create `SearchProjectsUseCase` delegating to ProjectRepository.searchProjects(query) in `app/src/main/java/com/poultry/broiler/domain/usecase/SearchProjectsUseCase.kt`
- [X] T024 [US2] Create `ProjectCard` composable with project name (ellipsis truncation), type badge (NEW/EXISTING with colors), location, creation date, status indicator, capacity count (default 0), circular compliance score (default N/A), tap and long-press handlers with scale-down animation in `app/src/main/java/com/poultry/broiler/presentation/home/components/ProjectCard.kt`
- [X] T025 [P] [US2] Create `ProjectSearchBar` composable (docked search field, search icon, localized placeholder "Rechercher un projet...", clear button) in `app/src/main/java/com/poultry/broiler/presentation/home/components/ProjectSearchBar.kt`
- [X] T026 [US2] Implement bento grid layout in HomeScreen using LazyVerticalStaggeredGrid with adaptive columns (phone 2-col, tablet 3-col), NewProjectPlaceholderCard as first item, ProjectCards for all projects, and "no results" empty state in `app/src/main/java/com/poultry/broiler/presentation/home/HomeScreen.kt`
- [X] T027 [US2] Wire project list loading and search flow in HomeViewModel: collect GetProjectsUseCase Flow, implement 300ms debounced search via SearchProjectsUseCase, map to HomeUiState.Content with searchQuery and noSearchResults flags in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`
- [X] T028 [US2] Wire OpenProject intent in HomeViewModel: navigate to wizard for NEW_INSTALLATION or dashboard for EXISTING_ASSESSMENT based on project type in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently — users can create projects, browse them as cards, search/filter, and tap to open.

---

## Phase 5: User Story 3 — Edit Project Metadata (Priority: P2)

**Goal**: Users can long-press a project card, select "Edit" from the context menu, and update the project name and location through a pre-filled dialog.

**Independent Test**: Long-press a project card → context menu appears with scale-down animation → select Edit → dialog pre-fills name and location → change name → save → card reflects updated name immediately. Empty name rejected.

### Implementation for User Story 3

- [X] T029 [US3] Create `UpdateProjectUseCase` with validation (non-empty name ≤200 chars, location ≤300 chars), updating name/location/updatedAt while preserving immutable type, in `app/src/main/java/com/poultry/broiler/domain/usecase/UpdateProjectUseCase.kt`
- [X] T030 [US3] Create `EditProjectDialog` composable (pre-filled name and location OutlinedTextFields with validation, read-only type display, save/cancel buttons) in `app/src/main/java/com/poultry/broiler/presentation/home/components/EditProjectDialog.kt`
- [X] T031 [US3] Add long-press gesture handling to ProjectCard using Modifier.combinedClickable(onLongClick) with scale-down animateFloatAsState, wiring to context menu expanded state in `app/src/main/java/com/poultry/broiler/presentation/home/components/ProjectCard.kt`
- [X] T032 [US3] Wire EditProject intent in HomeViewModel: invoke UpdateProjectUseCase, handle success/validation error, dismiss dialog state in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

**Checkpoint**: At this point, User Stories 1, 2, AND 3 should all work — full create, browse, search, and edit functionality.

---

## Phase 6: User Story 4 — Duplicate a Project (Priority: P2)

**Goal**: Users can duplicate a project from the context menu, creating a copy with " (Copy)" suffix and Draft status.

**Independent Test**: Long-press a project → select Duplicate → new card appears at top of list with " (Copy)" suffix, Draft status, same type and location as original.

### Implementation for User Story 4

- [X] T033 [US4] Create `DuplicateProjectUseCase` that fetches source project, creates a deep copy with new UUID, " (Copy)" name suffix, DRAFT status, and fresh timestamps in `app/src/main/java/com/poultry/broiler/domain/usecase/DuplicateProjectUseCase.kt`
- [X] T034 [US4] Wire DuplicateProject intent in HomeViewModel: invoke DuplicateProjectUseCase, handle success (new project appears via reactive Flow), dismiss context menu state in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

**Checkpoint**: At this point, User Stories 1–4 should all work independently — create, browse, search, edit, and duplicate.

---

## Phase 7: User Story 5 — Delete a Project (Priority: P2)

**Goal**: Users can delete a project with a confirmation dialog. Deletion is permanent and the project disappears from the list.

**Independent Test**: Long-press a project → select Delete → confirmation dialog shows project name → confirm → project removed from list and database. Cancel preserves the project. Deleting the last project shows empty state.

### Implementation for User Story 5

- [X] T035 [US5] Create `DeleteProjectUseCase` that permanently deletes a project by ID via ProjectRepository in `app/src/main/java/com/poultry/broiler/domain/usecase/DeleteProjectUseCase.kt`
- [X] T036 [P] [US5] Create `DeleteConfirmationDialog` composable (AlertDialog with warning icon, project name in bold, destructive confirm button in error color, cancel button) in `app/src/main/java/com/poultry/broiler/presentation/home/components/DeleteConfirmationDialog.kt`
- [X] T037 [US5] Wire DeleteProject intent in HomeViewModel: show confirmation dialog state, invoke DeleteProjectUseCase on confirm, handle empty state transition when last project deleted in `app/src/main/java/com/poultry/broiler/presentation/home/HomeViewModel.kt`

**Checkpoint**: At this point, User Stories 1–5 should all work — full CRUD operations (create, read, edit, duplicate, delete) are complete.

---

## Phase 8: User Story 6 — Navigate Between App Sections (Priority: P3)

**Goal**: Bottom navigation bar shows Projects (active), Design (stub), Reports (stub), and Health (stub) tabs. Tapping each tab navigates to the correct screen.

**Independent Test**: Tap each bottom nav tab → correct screen loads (Projects = Home Screen, others = placeholder). Returning to Projects tab preserves the project list state.

### Implementation for User Story 6

- [X] T038 [US6] Wire HomeScreen as the Projects tab destination in the NavHost, connecting HomeViewModel navigation events to Compose Navigation for wizard and dashboard stub routes in `app/src/main/java/com/poultry/broiler/presentation/navigation/NavHost.kt`
- [X] T039 [US6] Ensure bottom navigation bar highlights Projects tab as active, preserves HomeScreen state on tab switch (using rememberSaveable or NavHost backstack), and connects all 4 tabs to their respective screens in `app/src/main/java/com/poultry/broiler/presentation/navigation/BottomNavBar.kt`

**Checkpoint**: All 6 user stories are now complete and independently functional.

---

## Phase 9: Polish & Cross-Cutting Concerns

**Purpose**: Accessibility, performance, and final validation across all user stories

- [X] T040 [P] Add contentDescription to all icons and interactive elements (FAB, search icon, clear button, badge, context menu items, dialog buttons) using French string resources throughout `app/src/main/java/com/poultry/broiler/presentation/home/` composables
- [X] T041 [P] Verify all interactive elements meet 48dp minimum touch target and text contrast ratios (≥4.5:1 body, ≥7:1 delete warning) across all Home Screen composables
- [X] T042 [P] Add KDoc documentation to all public interfaces: ProjectRepository, all UseCases, HomeViewModel public state/events, and composable function signatures
- [ ] T043 Validate performance with 50+ projects: verify Home Screen loads <1s, search responds <300ms, scrolling has no frame drops (run quickstart.md scenarios V12)
- [ ] T044 Run full quickstart.md validation scenarios V1–V11 end-to-end to verify all acceptance criteria

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion — BLOCKS all user stories
- **User Stories (Phase 3–8)**: All depend on Foundational phase completion
  - US1 and US2 can proceed in parallel (both P1, no cross-dependencies)
  - US3, US4, US5 can proceed in parallel after US2 (context menu long-press from US3 T031 is needed, but each UseCase is independent)
  - US6 can start after US1 (needs HomeScreen to exist)
- **Polish (Phase 9)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (P1)**: Can start after Foundational (Phase 2) — No dependencies on other stories
- **US2 (P1)**: Can start after Foundational (Phase 2) — No dependencies on other stories. Can run in parallel with US1.
- **US3 (P2)**: Can start after Foundational (Phase 2) — Adds long-press gesture to ProjectCard (created in US2 T024), so should start after US2 or coordinate on the same file
- **US4 (P2)**: Can start after Foundational (Phase 2) — Reuses context menu (T016). Depends on long-press wiring from US3 T031 for full integration
- **US5 (P2)**: Can start after Foundational (Phase 2) — Reuses context menu (T016). Depends on long-press wiring from US3 T031 for full integration
- **US6 (P3)**: Can start after US1 — Needs HomeScreen to be wired into NavHost

### Within Each User Story

- UseCases before ViewModel wiring
- UI components before ViewModel wiring
- ViewModel wiring last (integrates UseCase + UI)

### Parallel Opportunities

**Setup phase**: T002, T003 can run in parallel (different resource files)

**Foundational phase**:
- T004, T005 can run in parallel (independent enum files)
- T008, T010 can run in parallel after T006 (entity and mapper are independent)
- T013, T014 can run in parallel (independent sealed type files)

**User Story phases**:
- US1 and US2 can run in parallel (different files, no shared implementation)
- US3, US4, US5 UseCase tasks (T029, T033, T035) can run in parallel (different files)
- T036 (DeleteConfirmationDialog) can run in parallel with T035 (DeleteProjectUseCase)

---

## Parallel Example: User Story 1

```text
# Launch US1 model + UI tasks together:
Task: "T017 [US1] Create CreateProjectUseCase in domain/usecase/CreateProjectUseCase.kt"
Task: "T018 [P] [US1] Create NewProjectPlaceholderCard in presentation/home/components/NewProjectPlaceholderCard.kt"
Task: "T019 [US1] Create NewProjectDialog in presentation/home/components/NewProjectDialog.kt"

# Then wire everything together:
Task: "T020 [US1] Implement empty state + FAB in HomeScreen.kt"
Task: "T021 [US1] Wire CreateProject intent in HomeViewModel.kt"
```

## Parallel Example: User Story 2

```text
# Launch US2 UseCases + UI in parallel:
Task: "T022 [US2] Create GetProjectsUseCase in domain/usecase/GetProjectsUseCase.kt"
Task: "T023 [P] [US2] Create SearchProjectsUseCase in domain/usecase/SearchProjectsUseCase.kt"
Task: "T024 [US2] Create ProjectCard in presentation/home/components/ProjectCard.kt"
Task: "T025 [P] [US2] Create ProjectSearchBar in presentation/home/components/ProjectSearchBar.kt"

# Then wire together:
Task: "T026 [US2] Implement bento grid in HomeScreen.kt"
Task: "T027 [US2] Wire list + search flow in HomeViewModel.kt"
Task: "T028 [US2] Wire OpenProject navigation in HomeViewModel.kt"
```

---

## Implementation Strategy

### MVP First (User Stories 1 + 2 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL — blocks all stories)
3. Complete Phase 3: User Story 1 — Create a New Project
4. Complete Phase 4: User Story 2 — Browse and Search Projects
5. **STOP and VALIDATE**: Create a project, see it on the Home Screen, search for it, tap to open
6. Deploy/demo if ready — users can create and browse projects

### Incremental Delivery

1. Setup + Foundational → Foundation ready
2. US1 (Create) → Users can create projects (MVP!)
3. US2 (Browse & Search) → Users can find and open projects
4. US3 (Edit) → Users can fix mistakes
5. US4 (Duplicate) → Power users can create variants
6. US5 (Delete) → Users can clean up
7. US6 (Navigate) → Full app navigation structure
8. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (Create)
   - Developer B: User Story 2 (Browse & Search)
3. After US1 + US2 complete:
   - Developer A: User Story 3 (Edit) + User Story 6 (Navigate)
   - Developer B: User Story 4 (Duplicate) + User Story 5 (Delete)
4. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- All composables use design tokens from Theme.kt — no hardcoded colors, fonts, or spacing
- All user-facing text from string resources — no hardcoded strings in composables
- HomeViewModel is the most-touched file — tasks T021, T027, T028, T032, T034, T037 all modify it sequentially
