# Feature Brief: Project Management

**Sequence**: #002 of 14
**Category**: :desktop_computer: UI Feature
**Priority**: Must Have
**Estimated Complexity**: Medium

## Dependencies

- **Requires**: #001 Project Scaffolding & Design System
- **Unlocks**: #003 House Dimensions Wizard, #004 Breed Configuration, #013 Cloud Sync & Sharing
- **Parallelizable with**: None

## Feature Description

Project Management is the entry point of the application. Consultants and farmers need a centralized place to create, organize, and manage their broiler house design projects. This feature delivers the Home Screen with the project list, creation dialog, and full CRUD operations on projects.

Users can create new projects categorized as either "New Installation" (designing a broiler house from scratch) or "Existing Assessment" (evaluating and upgrading an operational house). Each project tracks a status lifecycle: Draft, In Progress, and Completed. The Home Screen displays all projects as rich cards in a responsive bento grid layout, showing the project name, type badge (NEW/EXISTING), location, creation date, capacity count, ventilation state indicator, and a circular compliance score. A docked search bar allows filtering projects by name or location.

The project creation flow starts from either the "+" dashed-border placeholder card or the Floating Action Button. A dialog/bottom sheet collects the project name, type selection, and optional location. Once created, the project opens the design wizard for new installations or the dashboard view for existing assessments. Projects can be duplicated (useful for creating variants of the same farm layout), edited (rename, change metadata), and deleted with a confirmation dialog.

All project data persists locally via Room database. The project list must load and render smoothly even with 50+ projects, with no perceptible lag. The bottom navigation bar provides tabs for Projects, Design, Reports, and Health — though only the Projects tab is fully functional in this feature; others serve as navigation stubs.

## Derived From

- **Product Spec**: §5.1 Core Feature 1 — Project Management (create, list, edit, duplicate, delete, categorize, track status)
- **UI Spec**: §4.1 Home Screen / Dashboard (Stitch screens: `c71e8af9`, `b9ff9354`), §2.1 Project Cards, §2.2 Status Badges, §3 Navigation Architecture (HOME route)
- **Constitution**: Article 1.2 (Offline-First Data Flow), Article 3.2 (Component Standards), Article 4.3 (Input Validation)

## Acceptance Criteria Summary

- [ ] Users can create a new project by selecting "New Installation" or "Existing Assessment" and providing a name and optional location
- [ ] Home Screen displays all projects as cards with name, type badge, location, date, and status in a bento grid layout
- [ ] Users can search/filter projects by name or location using the docked search bar
- [ ] Users can edit project metadata (name, location), duplicate a project, and delete a project with confirmation
- [ ] Project status transitions correctly between Draft, In Progress, and Completed based on wizard completion
- [ ] All project data persists in Room and survives app restart with no data loss
- [ ] Bottom navigation bar renders with Projects (active), Design, Reports, and Health tabs

## UI Components (if applicable)

- **ProjectCard**: Rich card with project thumbnail, type badge (NEW/EXISTING), metadata rows, compliance score indicator — per ui-spec §2.1
- **Home Screen bento grid**: Responsive card layout with active projects section, performance analysis card, and new project placeholder card
- **NewProjectDialog**: Bottom sheet or dialog for project creation with name input, type selector (segmented button), and location field
- **SearchBar**: Docked search field with localized placeholder ("Rechercher un projet...")
- **BottomNavigationBar**: 4-tab bar (Projects, Design, Reports, Health) with icons per ui-spec §4.1

## Technical Hints

- Constitution Article 1.4 requires Unidirectional Data Flow: ViewModel exposes project list via StateFlow, UI sends intents for CRUD operations
- Project entity should include fields for: id, name, type (NEW/EXISTING), status (DRAFT/IN_PROGRESS/COMPLETED), location, createdAt, updatedAt, and a syncTimestamp for future cloud sync (#013)
- Long-press on a card should trigger a context menu (edit/duplicate/delete) with the scale-down micro-interaction per ui-spec §2.1

---

**To create the full specification, run:**
```
/speckit-specify [paste the Feature Description section above]
```
