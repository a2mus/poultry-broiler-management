# Composable Contracts: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## Screen-Level Composables

### HomeScreen

**Package**: `com.poultry.broiler.presentation.home`

The primary screen composable for the Projects tab. Composes the search bar, project grid, empty states, and FAB.

**Parameters**:
- `uiState: HomeUiState` — current screen state from ViewModel
- `onIntent: (HomeIntent) -> Unit` — callback to send user intents to ViewModel
- `onNavigateToWizard: (projectId: String) -> Unit` — navigation callback for New Installation projects
- `onNavigateToDashboard: (projectId: String) -> Unit` — navigation callback for Existing Assessment projects

**Behavior by State**:
- `HomeUiState.Loading` → centered loading indicator
- `HomeUiState.Empty` → empty state with "+" placeholder card and guidance text
- `HomeUiState.Content` → search bar + bento grid of project cards + FAB
- `HomeUiState.Error` → error message with retry action

## Component Composables

### ProjectCard

**Package**: `com.poultry.broiler.presentation.home.components`

Rich card displaying project metadata in the bento grid.

**Parameters**:
- `project: Project` — domain model to display
- `onClick: () -> Unit` — tap handler (navigates to wizard or dashboard)
- `onLongClick: () -> Unit` — long-press handler (opens context menu)
- `modifier: Modifier` — standard Compose modifier

**Visual Elements**:
- Project name (truncated with ellipsis if long)
- Type badge: "NEW" (primary color) or "EXISTING" (secondary color)
- Location text (if present)
- Creation date (formatted per locale)
- Status indicator (Draft / In Progress / Completed)
- Capacity count (default: "0")
- Circular compliance score indicator (default: "N/A")

**Interactions**:
- Single tap → `onClick`
- Long press → scale-down animation (0.95 scale) + `onLongClick`

### NewProjectPlaceholderCard

**Parameters**:
- `onClick: () -> Unit` — tap handler (opens creation dialog)
- `modifier: Modifier`

**Visual**: Dashed border card with "+" icon and "Nouveau projet" label. Same dimensions as a standard ProjectCard.

### ProjectSearchBar

**Parameters**:
- `query: String` — current search text
- `onQueryChange: (String) -> Unit` — text change callback
- `modifier: Modifier`

**Visual**: Docked search field with search icon, localized placeholder ("Rechercher un projet..."), and clear button when text is present.

### NewProjectDialog

**Parameters**:
- `onDismiss: () -> Unit` — dialog dismiss callback
- `onConfirm: (name: String, type: ProjectType, location: String?) -> Unit` — creation callback

**Fields**:
- Project name `OutlinedTextField` (required, validation: non-empty, ≤200 chars)
- Type selector `SegmentedButton` (New Installation / Existing Assessment)
- Location `OutlinedTextField` (optional, ≤300 chars)
- Confirm button (disabled until name is valid)
- Cancel button

### EditProjectDialog

**Parameters**:
- `project: Project` — project being edited (pre-fills fields)
- `onDismiss: () -> Unit` — dialog dismiss callback
- `onConfirm: (name: String, location: String?) -> Unit` — save callback

**Fields**:
- Project name `OutlinedTextField` (pre-filled, same validation as creation)
- Location `OutlinedTextField` (pre-filled, optional)
- Project type displayed as read-only text (immutable)
- Save button (disabled until name is valid)
- Cancel button

### DeleteConfirmationDialog

**Parameters**:
- `projectName: String` — name shown in the warning message
- `onConfirm: () -> Unit` — deletion confirmed callback
- `onDismiss: () -> Unit` — cancellation callback

**Visual**: AlertDialog with warning icon, project name in bold, destructive action button styled in error color.

### ProjectContextMenu

**Parameters**:
- `expanded: Boolean` — menu visibility state
- `onDismiss: () -> Unit` — dismiss callback
- `onEdit: () -> Unit` — edit action callback
- `onDuplicate: () -> Unit` — duplicate action callback
- `onDelete: () -> Unit` — delete action callback

**Visual**: `DropdownMenu` with three items: Edit (pencil icon), Duplicate (copy icon), Delete (trash icon, error color).

## State & Intent Contracts

### HomeUiState (Sealed Interface)

```
HomeUiState
├── Loading                          # Initial load
├── Empty                            # No projects exist
├── Content                          # Projects available
│   ├── projects: List<Project>      # Displayed project list
│   ├── searchQuery: String          # Current search text
│   ├── isSearchActive: Boolean      # Whether search is filtering
│   └── noSearchResults: Boolean     # Search returned empty
└── Error                            # Database error
    └── message: String
```

### HomeIntent (Sealed Class)

```
HomeIntent
├── CreateProject(name, type, location?)    # FR-002
├── UpdateSearchQuery(query)                # FR-004
├── OpenProject(projectId)                  # FR-008
├── EditProject(projectId, name, location?) # FR-010
├── DuplicateProject(projectId)             # FR-011
├── DeleteProject(projectId)                # FR-013
└── ClearSearch                             # Clear search bar
```

## Accessibility Requirements

All composables MUST comply with Constitution Art 3.3:

- Interactive elements: minimum 48dp x 48dp touch target
- All icons: `contentDescription` from string resources (French default, Arabic stub)
- Decorative icons: `contentDescription = null`
- Text contrast: ≥4.5:1 body text, ≥7:1 critical warnings (delete confirmation)
- Cards: `semantics { heading() }` for project names in the grid
