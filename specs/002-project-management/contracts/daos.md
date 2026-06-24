# DAO Contracts: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## ProjectDao

**Package**: `com.poultry.broiler.data.local.dao`

Room DAO interface for the `projects` table. All query methods return `Flow` for reactive observation. Write methods are `suspend` functions.

### Queries

#### `getAllSortedByUpdatedAt(): Flow<List<ProjectEntity>>`

```sql
SELECT * FROM projects ORDER BY updatedAt DESC
```

- Returns all project entities sorted by most recently modified.
- Reactive: emits a new list on any table change.

#### `searchByNameOrLocation(query: String): Flow<List<ProjectEntity>>`

```sql
SELECT * FROM projects
WHERE name LIKE '%' || :query || '%' COLLATE NOCASE
   OR location LIKE '%' || :query || '%' COLLATE NOCASE
ORDER BY updatedAt DESC
```

- Case-insensitive partial match on `name` and `location`.
- Results sorted by `updatedAt` descending.

#### `getById(id: String): Flow<ProjectEntity?>`

```sql
SELECT * FROM projects WHERE id = :id
```

- Returns a single entity by primary key, or `null`.
- Reactive: emits updates when the specific row changes.

### Write Operations

#### `suspend insert(project: ProjectEntity)`

- `@Insert(onConflict = OnConflictStrategy.ABORT)`
- Inserts a new project. Aborts on ID collision (should never happen with UUID).

#### `suspend update(project: ProjectEntity)`

- `@Update`
- Updates all mutable fields of an existing project entity.

#### `suspend deleteById(id: String)`

```sql
DELETE FROM projects WHERE id = :id
```

- Deletes a single project by primary key.
- No-op if the ID does not exist.

#### `suspend getProjectCount(): Int`

```sql
SELECT COUNT(*) FROM projects
```

- Returns the total number of projects. Utility for testing and diagnostics.

## ProjectEntity

**Table name**: `projects`

| Column | Type | Annotation | Notes |
|--------|------|-----------|-------|
| id | TEXT | `@PrimaryKey` | UUID string |
| name | TEXT | `@ColumnInfo(name = "name")` | NOT NULL |
| type | TEXT | `@ColumnInfo(name = "type")` | NOT NULL, stored as enum name string |
| status | TEXT | `@ColumnInfo(name = "status", defaultValue = "DRAFT")` | NOT NULL |
| location | TEXT? | `@ColumnInfo(name = "location")` | NULLABLE |
| createdAt | Long | `@ColumnInfo(name = "createdAt")` | NOT NULL, epoch milliseconds |
| updatedAt | Long | `@ColumnInfo(name = "updatedAt")` | NOT NULL, epoch milliseconds |
| syncTimestamp | Long? | `@ColumnInfo(name = "syncTimestamp")` | NULLABLE, epoch milliseconds |

**Indices**: `@Index(value = ["updatedAt"])` — supports default sort order query.

## ProjectMapper

**Package**: `com.poultry.broiler.data.mapper`

Bidirectional mapper between `ProjectEntity` (data layer) and `Project` (domain model).

### `ProjectEntity.toDomain(): Project`

Maps all entity fields to domain model fields. Converts string enum values to `ProjectType` and `ProjectStatus` enum instances.

### `Project.toEntity(): ProjectEntity`

Maps all domain model fields to entity fields. Converts `ProjectType` and `ProjectStatus` enums to their string name representations for Room storage.
