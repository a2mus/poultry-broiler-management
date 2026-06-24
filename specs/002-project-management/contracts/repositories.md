# Repository Contracts: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## ProjectRepository (Domain Interface)

**Package**: `com.poultry.broiler.domain.repository`

The domain-level contract for all project data operations. Implementation lives in the data layer (`ProjectRepositoryImpl`). All methods return `Flow` for observable queries or `suspend` for one-shot operations.

### Methods

#### `getAllProjectsSortedByModified(): Flow<List<Project>>`

- Returns all projects ordered by `updatedAt` descending (most recently modified first).
- Emits a new list whenever the underlying data changes (Room's reactive `Flow` support).
- Used by: `GetProjectsUseCase` → `HomeViewModel` (default Home Screen list).

#### `searchProjects(query: String): Flow<List<Project>>`

- Returns projects whose `name` or `location` contains `query` (case-insensitive, partial match).
- Results ordered by `updatedAt` descending.
- Empty `query` returns all projects (equivalent to `getAllProjectsSortedByModified`).
- Used by: `SearchProjectsUseCase` → `HomeViewModel` (search bar filtering).

#### `getProjectById(id: String): Flow<Project?>`

- Returns a single project by ID, or `null` if not found.
- Reactive: emits updates when the project changes.
- Used by: future features needing to observe a specific project.

#### `suspend insertProject(project: Project): String`

- Inserts a new project into the database.
- Returns the project ID (same as the one on the input model, which is pre-generated).
- Precondition: `project.name` is non-empty (enforced at UseCase level).
- Sets `createdAt` and `updatedAt` to current timestamp if not already set.
- Used by: `CreateProjectUseCase`, `DuplicateProjectUseCase`.

#### `suspend updateProject(project: Project)`

- Updates an existing project's mutable fields (name, location, status, updatedAt).
- The `type` field is NOT updated (immutable after creation).
- Sets `updatedAt` to current timestamp.
- Precondition: project with given ID exists.
- Used by: `UpdateProjectUseCase`.

#### `suspend deleteProject(id: String)`

- Permanently deletes the project with the given ID and all associated data.
- No-op if project does not exist (idempotent).
- Used by: `DeleteProjectUseCase`.

## Use Case Contracts

### CreateProjectUseCase

**Input**: `name: String`, `type: ProjectType`, `location: String?`

**Output**: `Result<Project>` — success with created project, or failure with validation error.

**Validation**:
- `name` must be non-empty after trimming.
- `name` must be ≤200 characters.
- `location` (if provided) must be ≤300 characters.

**Behavior**:
1. Validate inputs.
2. Generate UUID for new project.
3. Set `status = DRAFT`, `createdAt = now`, `updatedAt = now`, `syncTimestamp = null`.
4. Insert via repository.
5. Return the created `Project`.

### GetProjectsUseCase

**Input**: None (parameterless).

**Output**: `Flow<List<Project>>` — reactive list sorted by `updatedAt` DESC.

**Behavior**: Delegates directly to `ProjectRepository.getAllProjectsSortedByModified()`.

### SearchProjectsUseCase

**Input**: `query: String`

**Output**: `Flow<List<Project>>` — filtered reactive list sorted by `updatedAt` DESC.

**Behavior**: Delegates to `ProjectRepository.searchProjects(query)`. The ViewModel applies a 300ms debounce before invoking this UseCase.

### UpdateProjectUseCase

**Input**: `projectId: String`, `name: String`, `location: String?`

**Output**: `Result<Project>` — success with updated project, or failure with validation error.

**Validation**: Same as `CreateProjectUseCase` for `name` and `location`.

**Behavior**:
1. Validate inputs.
2. Fetch existing project by ID (fail if not found).
3. Update `name`, `location`, `updatedAt = now`.
4. Do NOT modify `type` or `status`.
5. Save via repository.
6. Return the updated `Project`.

### DuplicateProjectUseCase

**Input**: `sourceProjectId: String`

**Output**: `Result<Project>` — success with the new copy, or failure if source not found.

**Behavior**:
1. Fetch source project by ID (fail if not found).
2. Create new `Project` with:
   - New UUID.
   - Name = `"${source.name} (Copy)"`.
   - Same `type` as source.
   - `status = DRAFT` (regardless of source status).
   - Same `location` as source.
   - `createdAt = now`, `updatedAt = now`, `syncTimestamp = null`.
3. Insert via repository.
4. Return the new `Project`.

### DeleteProjectUseCase

**Input**: `projectId: String`

**Output**: `Result<Unit>` — success, or failure if project not found.

**Behavior**:
1. Delete via repository (cascading deletion of associated data — currently none, but future-proofed).
2. Return success.
