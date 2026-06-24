# Feature Specification: Project Management

**Feature Branch**: `002-project-management`

**Created**: 2026-06-24

**Status**: Draft

**Input**: User description: "Project Management is the entry point of the application. Consultants and farmers need a centralized place to create, organize, and manage their broiler house design projects. This feature delivers the Home Screen with the project list, creation dialog, and full CRUD operations on projects."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Create a New Project (Priority: P1)

A consultant opens the application for the first time and wants to start designing a new broiler house. From the Home Screen, they tap the "+" placeholder card or the Floating Action Button to open the project creation dialog. They enter a project name (e.g., "Ferme El Baraka — Bâtiment 3"), select the project type "New Installation", and optionally add a location (e.g., "Blida, Algeria"). Upon confirmation, the project is created with "Draft" status and the user is taken to the design wizard to begin configuring the house.

**Why this priority**: Creating projects is the foundational action — without it, no other feature in the application can be used. This is the minimum viable entry point.

**Independent Test**: Can be fully tested by launching the app, tapping the creation trigger, filling in project details, and verifying the project appears in the list with correct metadata. Delivers immediate value as the user can begin working.

**Acceptance Scenarios**:

1. **Given** the Home Screen is displayed with no projects, **When** the user taps the "+" placeholder card, **Then** the project creation dialog opens with a name field, type selector (New Installation / Existing Assessment), and an optional location field.
2. **Given** the creation dialog is open, **When** the user enters a valid project name and selects "New Installation", **Then** the project is persisted locally and appears on the Home Screen as a card with "Draft" status and "NEW" type badge.
3. **Given** the creation dialog is open, **When** the user submits without entering a project name, **Then** an inline error message is displayed and the project is not created.
4. **Given** a new project of type "New Installation" has just been created, **When** creation completes, **Then** the application navigates to the design wizard screen.
5. **Given** the creation dialog is open, **When** the user selects "Existing Assessment" and submits, **Then** the project is created and the application navigates to the dashboard view.

---

### User Story 2 - Browse and Search Projects (Priority: P1)

A consultant who manages multiple farms opens the application and sees all their projects displayed as rich cards in a bento grid layout. Each card shows the project name, type badge (NEW or EXISTING), location, creation date, capacity count, ventilation state indicator, and a circular compliance score. The consultant uses the docked search bar to filter projects by name or location to quickly find the one they need.

**Why this priority**: Browsing and finding projects is essential for returning users. Without a usable project list, the application has no navigable entry point for existing work.

**Independent Test**: Can be tested by pre-populating projects and verifying they render correctly as cards with all metadata, and that search filtering works by name and location.

**Acceptance Scenarios**:

1. **Given** the user has 5 projects saved, **When** the Home Screen loads, **Then** all 5 projects are displayed as cards in a responsive bento grid layout showing name, type badge, location, creation date, and status.
2. **Given** the Home Screen displays multiple projects, **When** the user types "Baraka" in the search bar, **Then** only projects whose name or location contains "Baraka" are shown (case-insensitive).
3. **Given** the search bar has a filter applied, **When** the user clears the search text, **Then** all projects are displayed again.
4. **Given** the user has 50+ projects saved, **When** the Home Screen loads, **Then** the project list renders smoothly with no perceptible lag or frame drops.
5. **Given** a project has capacity and compliance data from later features, **When** the Home Screen displays the card, **Then** the capacity count and circular compliance score are shown (defaulting to placeholder values of 0 and N/A for newly created projects).

---

### User Story 3 - Edit Project Metadata (Priority: P2)

A consultant realizes they mistyped the project name or wants to update the location information. They long-press on the project card to reveal a context menu, then select "Edit". A dialog appears pre-filled with the current project name and location, allowing the consultant to make changes and save.

**Why this priority**: Editing is a natural complement to creation — users frequently need to correct or update project information, but the app is usable without it initially.

**Independent Test**: Can be tested by creating a project, triggering edit, modifying the name and location, and verifying the updated values persist and display correctly.

**Acceptance Scenarios**:

1. **Given** a project card is displayed, **When** the user long-presses the card, **Then** a context menu appears with options: Edit, Duplicate, Delete.
2. **Given** the context menu is open, **When** the user selects "Edit", **Then** a dialog appears pre-filled with the current project name and location.
3. **Given** the edit dialog is showing, **When** the user changes the project name and saves, **Then** the updated name is persisted and reflected on the project card immediately.
4. **Given** the edit dialog is showing, **When** the user clears the project name and attempts to save, **Then** an inline error prevents saving with an empty name.

---

### User Story 4 - Duplicate a Project (Priority: P2)

A consultant has completed one broiler house design and wants to create a variant for a neighboring building with similar dimensions. They long-press the project card and select "Duplicate". A copy of the project is created with the name appended with " (Copy)" and "Draft" status, preserving all configuration data from the original.

**Why this priority**: Duplication saves significant time for consultants who design similar houses for the same farm. It builds on existing CRUD operations and is valuable but not blocking.

**Independent Test**: Can be tested by creating a project with metadata, duplicating it, and verifying the copy has all original data, a modified name, and "Draft" status.

**Acceptance Scenarios**:

1. **Given** a project exists with name "Ferme A", **When** the user selects "Duplicate" from the context menu, **Then** a new project is created with name "Ferme A (Copy)" and status "Draft".
2. **Given** a project has been duplicated, **When** the Home Screen refreshes, **Then** both the original and the copy appear as separate cards.
3. **Given** the duplicated project, **When** the user opens it, **Then** all configuration data from the original project is present in the copy.

---

### User Story 5 - Delete a Project (Priority: P2)

A consultant wants to remove an old or abandoned project. They long-press the project card and select "Delete". A confirmation dialog appears asking them to confirm the deletion. Upon confirmation, the project and all its associated data are permanently removed.

**Why this priority**: Deletion is necessary for data hygiene but is less frequently used than creation or editing. The confirmation dialog prevents accidental loss.

**Independent Test**: Can be tested by creating a project, deleting it via the context menu, confirming, and verifying it no longer appears in the project list or the database.

**Acceptance Scenarios**:

1. **Given** a project card is displayed, **When** the user selects "Delete" from the context menu, **Then** a confirmation dialog appears with the project name and a warning message.
2. **Given** the delete confirmation dialog is showing, **When** the user confirms deletion, **Then** the project is permanently removed from the database and disappears from the Home Screen.
3. **Given** the delete confirmation dialog is showing, **When** the user cancels, **Then** the project remains unchanged.

---

### User Story 6 - Navigate Between App Sections (Priority: P3)

A user sees the bottom navigation bar with four tabs: Projects, Design, Reports, and Health. The Projects tab is active and fully functional. The other three tabs show placeholder screens indicating they are coming in future updates.

**Why this priority**: The navigation bar establishes the app's information architecture but the non-Projects tabs are stubs for now. This is a structural requirement, not a user-facing feature.

**Independent Test**: Can be tested by tapping each tab and verifying the correct screen loads — the Projects tab shows the Home Screen, and other tabs show placeholder content.

**Acceptance Scenarios**:

1. **Given** the Home Screen is displayed, **When** the user looks at the bottom of the screen, **Then** a bottom navigation bar with 4 tabs (Projects, Design, Reports, Health) is visible with the Projects tab highlighted as active.
2. **Given** the Projects tab is active, **When** the user taps the Design tab, **Then** a placeholder screen for Design is displayed.
3. **Given** a non-Projects tab is active, **When** the user taps the Projects tab, **Then** the Home Screen with the project list is displayed.

---

### Edge Cases

- What happens when the user creates a project with a name that already exists? The system allows it — project names are not required to be unique. Users may have identically named projects for different locations.
- What happens when the user attempts to delete the only remaining project? The deletion proceeds normally and the Home Screen shows the empty state with the "+" placeholder card.
- What happens when the search bar returns no results? A friendly empty state is shown with a message like "No projects match your search" and a suggestion to clear the filter.
- How does the project list behave with very long project names? Names are truncated with ellipsis on the card but shown in full in the edit dialog.
- What happens if the user closes the creation dialog without saving? The dialog is dismissed and no project is created — no unsaved changes warning needed since the form is minimal.
- How does the system handle the status lifecycle? Status transitions are automatic: a project starts as "Draft", moves to "In Progress" when the wizard begins, and becomes "Completed" when all required wizard steps are finalized. Manual status changes are not supported.
- What if the user selected the wrong project type during creation? The project type is immutable after creation. The user must delete the project and create a new one with the correct type. This is a low-cost operation since mistakes are typically caught early in "Draft" status.

## Clarifications

### Session 2026-06-24

- Q: What happens when a user taps an existing project card to re-open it? → A: Navigate based on project type — wizard for New Installation, dashboard for Existing Assessment (mirrors post-creation behavior).
- Q: How are projects sorted/ordered on the Home Screen? → A: Most recently modified first (newest activity on top).
- Q: Can the project type (New Installation / Existing Assessment) be changed after creation? → A: Type is immutable after creation (delete + re-create to fix mistakes).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST display a Home Screen as the application entry point showing all saved projects in a responsive bento grid layout, sorted by most recently modified first.
- **FR-002**: System MUST allow users to create a new project by providing a project name (required), selecting a type ("New Installation" or "Existing Assessment"), and optionally entering a location.
- **FR-003**: System MUST display each project as a rich card showing: project name, type badge (NEW/EXISTING), location, creation date, status indicator, capacity count (default 0), and circular compliance score (default N/A).
- **FR-004**: System MUST provide a docked search bar that filters the project list by project name or location (case-insensitive, partial match).
- **FR-005**: System MUST support project creation from two entry points: the "+" dashed-border placeholder card and a Floating Action Button.
- **FR-006**: System MUST validate that the project name is non-empty before allowing creation or editing to proceed.
- **FR-007**: System MUST navigate to the design wizard after creating a "New Installation" project and to the dashboard view after creating an "Existing Assessment" project.
- **FR-008**: System MUST navigate to the design wizard when the user taps a "New Installation" project card, and to the dashboard view when the user taps an "Existing Assessment" project card.
- **FR-009**: System MUST display a context menu (Edit, Duplicate, Delete) when the user long-presses a project card, with the scale-down micro-interaction animation.
- **FR-010**: System MUST allow editing of a project's name and location through a pre-filled edit dialog. The project type (New Installation / Existing Assessment) is immutable after creation and MUST NOT be editable.
- **FR-011**: System MUST allow duplicating a project, creating a copy with " (Copy)" appended to the name, "Draft" status, and all configuration data preserved.
- **FR-012**: System MUST require user confirmation before deleting a project, showing the project name in the confirmation dialog.
- **FR-013**: System MUST permanently delete a project and all associated data upon confirmed deletion.
- **FR-014**: System MUST persist all project data locally so that it survives application restarts with zero data loss.
- **FR-015**: System MUST assign a status lifecycle to each project: Draft (initial), In Progress (wizard started), Completed (wizard finalized). Status transitions are automatic based on user progress.
- **FR-016**: System MUST render the project list smoothly (no perceptible lag or frame drops) with 50 or more projects.
- **FR-017**: System MUST display a bottom navigation bar with four tabs: Projects (active, functional), Design (stub), Reports (stub), and Health (stub).
- **FR-018**: System MUST display an empty state on the Home Screen when no projects exist, featuring the "+" placeholder card to guide users toward project creation.
- **FR-019**: System MUST display a "no results" empty state when the search filter matches no projects.
- **FR-020**: System MUST truncate long project names with ellipsis on cards while displaying the full name in edit dialogs.
- **FR-021**: System MUST record a creation timestamp and last-modified timestamp for each project.
- **FR-022**: System MUST include a sync timestamp field in each project record to support future cloud synchronization capabilities.

### Key Entities

- **Project**: Represents a broiler house design project. Key attributes: unique identifier, name, type (New Installation / Existing Assessment), status (Draft / In Progress / Completed), location (optional), creation timestamp, last-modified timestamp, sync timestamp. A project is the top-level organizational unit that contains all design configurations, calculations, and reports.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can create a new project (name + type selection) and see it on the Home Screen in under 30 seconds from app launch.
- **SC-002**: The Home Screen loads and displays all project cards within 1 second, even with 50+ projects.
- **SC-003**: Search results update within 300 milliseconds of user input, without blocking the interface.
- **SC-004**: Users can successfully complete all CRUD operations (create, read, edit, duplicate, delete) without encountering errors or data loss across 10 consecutive app restarts.
- **SC-005**: All project data survives app termination and device restart with 100% data integrity.
- **SC-006**: Users can locate a specific project among 20+ projects using the search bar within 5 seconds.
- **SC-007**: The project creation flow requires no more than 3 user inputs (name, type, optional location) before the project is ready.

## Assumptions

- The application scaffold, design system, and navigation infrastructure from Feature #001 (Project Scaffolding & Design System) are complete and available, including the theme tokens, bottom navigation bar, and Compose Navigation setup.
- Project names do not need to be unique — users may create identically named projects for different locations or variants.
- The design wizard and dashboard screens referenced in navigation after project creation are stubs at this stage; they will be fully implemented in subsequent features (#003+).
- Capacity count and compliance score on project cards display default placeholder values (0 and N/A) until the corresponding features (house dimensions, compliance calculations) are implemented in later features.
- Status transitions (Draft → In Progress → Completed) are handled by the system automatically based on wizard progress — there is no manual status override.
- The search function performs local in-memory filtering (not full-text search) since all data resides in the local database.
- No network connectivity is required for any Project Management functionality — this is a fully offline feature per Constitution Article 1.2.
- The "+" placeholder card and Floating Action Button are both always visible on the Home Screen (not conditionally shown).
- Long-press context menu is the primary interaction pattern for edit/duplicate/delete operations on cards (no swipe-to-dismiss or other gesture patterns).
- French is the default display language for all UI text, with Arabic support as a secondary locale, per Constitution Article 8.1.
