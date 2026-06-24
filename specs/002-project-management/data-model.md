# Data Model: Project Management

**Feature**: 002-project-management | **Date**: 2026-06-24

## Entity Overview

This feature introduces a single entity: **Project**. It is the top-level organizational unit of the application. All future entities (house dimensions, breed configurations, equipment layouts, compliance results) will reference the Project via foreign key.

## Entities

### Project

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | UUID (String) | PK, auto-generated | Unique identifier for the project |
| name | String | NOT NULL, non-empty (validated) | User-provided project name |
| type | Enum (String) | NOT NULL, one of: `NEW_INSTALLATION`, `EXISTING_ASSESSMENT` | Project category; immutable after creation |
| status | Enum (String) | NOT NULL, default: `DRAFT` | Lifecycle status; system-managed transitions |
| location | String? | NULLABLE | Optional free-text location description |
| createdAt | Long (epoch ms) | NOT NULL, auto-set on creation | Creation timestamp |
| updatedAt | Long (epoch ms) | NOT NULL, auto-set on creation and modification | Last modification timestamp; used for sort order |
| syncTimestamp | Long? (epoch ms) | NULLABLE, default: null | Reserved for future cloud sync (#013); null = never synced |

### Enumerations

#### ProjectType

| Value | Display Label (FR) | Badge Text |
|-------|-------------------|------------|
| `NEW_INSTALLATION` | Nouvelle Installation | NEW |
| `EXISTING_ASSESSMENT` | Évaluation Existante | EXISTING |

- Immutable after project creation.
- Determines post-creation and tap navigation target (wizard vs. dashboard).

#### ProjectStatus

| Value | Display Label (FR) | Transition Trigger |
|-------|-------------------|--------------------|
| `DRAFT` | Brouillon | Default on creation |
| `IN_PROGRESS` | En cours | Wizard started (future feature) |
| `COMPLETED` | Terminé | All wizard steps finalized (future feature) |

- Transitions are automatic and unidirectional: DRAFT → IN_PROGRESS → COMPLETED.
- Manual status changes are not supported.
- At this feature stage, all projects remain in DRAFT; status transitions activate with Feature #003+.

## Relationships

```text
Project (1) ──── (0..*) [Future: HouseDimension]     (Feature #003)
Project (1) ──── (0..*) [Future: BreedConfiguration]  (Feature #004)
Project (1) ──── (0..*) [Future: EquipmentLayout]     (Future)
Project (1) ──── (0..1) [Future: ComplianceResult]    (Future)
```

No relationships are implemented in this feature. The `Project.id` field serves as the foreign key target for all future child entities.

## Validation Rules

| Field | Rule | Error Message (FR) | Enforcement Layer |
|-------|------|-------------------|-------------------|
| name | Non-empty (trimmed) | "Le nom du projet est requis" | UI (Compose field) + Domain (UseCase precondition) |
| name | Max 200 characters | "Le nom ne doit pas dépasser 200 caractères" | UI (Compose field) + Domain (UseCase precondition) |
| type | Must be valid enum value | N/A (enforced by type system) | Type system (compile-time) |
| location | Max 300 characters (if provided) | "L'emplacement ne doit pas dépasser 300 caractères" | UI (Compose field) + Domain (UseCase precondition) |

## Identity & Uniqueness

- **Primary Key**: `id` (UUID string, auto-generated via `java.util.UUID.randomUUID().toString()`)
- **Uniqueness**: Only `id` is unique. Project names are explicitly NOT unique per spec clarification — users may create identically named projects.
- **Duplicate Detection**: No duplicate detection or warning for matching names.

## State Transitions

```text
                    ┌──────────┐
   [Create] ──────► │  DRAFT   │
                    └────┬─────┘
                         │ Wizard started (Feature #003+)
                         ▼
                    ┌──────────────┐
                    │ IN_PROGRESS  │
                    └────┬─────────┘
                         │ All wizard steps finalized
                         ▼
                    ┌──────────────┐
                    │  COMPLETED   │
                    └──────────────┘
```

- No backward transitions.
- No manual status override.
- Deletion is allowed from any status.
- Duplication always creates a copy in DRAFT status regardless of source status.

## Database Migration

**Migration**: Version 1 → Version 2

The Feature #001 database (version 1) contains `breed_profiles` and `equipment_items` tables. This feature adds the `projects` table.

**Migration SQL**:
```sql
CREATE TABLE IF NOT EXISTS projects (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'DRAFT',
    location TEXT,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL,
    syncTimestamp INTEGER
);

CREATE INDEX IF NOT EXISTS index_projects_updatedAt ON projects(updatedAt);
```

**Index Rationale**: `updatedAt` index supports the default sort order (most recently modified first) efficiently for 50+ projects.

## Data Volume Assumptions

- Typical user: 5–20 projects
- Power user: 50–100 projects
- Stress test target: 200 projects (2x the 100 upper bound)
- Each project record: ~500 bytes (estimated)
- Total storage at 200 projects: ~100 KB (negligible)
