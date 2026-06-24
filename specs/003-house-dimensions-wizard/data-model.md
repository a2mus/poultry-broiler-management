# Data Model: House Dimensions Wizard Step

**Feature**: 003-house-dimensions-wizard
**Date**: 2026-06-24

## Domain Models (Pure Kotlin)

### HouseDimensions

The primary domain model representing the physical structure configuration of a broiler house.

```kotlin
data class HouseDimensions(
    val id: String,                    // UUID string (unique per record)
    val projectId: String,             // FK → Project.id (1:1 relationship)
    val length: Meters,                // Building length (0 < value ≤ 500)
    val width: Meters,                 // Building width (0 < value ≤ 100)
    val wallHeight: Meters,            // Wall height (0 < value ≤ 15)
    val roofType: RoofType,            // FLAT, PITCHED, ARCHED
    val ridgeHeight: Meters?,          // Required when roofType == PITCHED; null otherwise (0 < value ≤ 20)
    val wallMaterial: WallMaterial,    // BLOCK, STEEL, PREFAB
    val floorType: FloorType,          // CONCRETE, DIRT, SLAT
    val insulationType: InsulationType, // NONE, POLYSTYRENE, POLYURETHANE, MINERAL_WOOL
    val insulationThickness: Millimeters?, // Required when insulationType != NONE; null otherwise (0 < value ≤ 500)
    val orientation: HouseOrientation, // N, NE, E, SE, S, SW, W, NW
    val createdAt: Long,               // Epoch millis
    val updatedAt: Long,               // Epoch millis
) {
    val floorArea: SquareMeters        // Computed: length.value × width.value
        get() = SquareMeters(length.value * width.value)
}
```

### Value Classes (Domain Primitives per Constitution Art 2.3)

```kotlin
@JvmInline value class Meters(val value: Double)
@JvmInline value class Millimeters(val value: Double)
@JvmInline value class SquareMeters(val value: Double)
```

### Enums

```kotlin
enum class RoofType(val displayNameFr: String) {
    FLAT("Plat"),
    PITCHED("À pignon"),
    ARCHED("Voûté"),
}

enum class WallMaterial(val displayNameFr: String) {
    BLOCK("Parpaing"),
    STEEL("Acier"),
    PREFAB("Préfabriqué"),
}

enum class FloorType(val displayNameFr: String) {
    CONCRETE("Béton"),
    DIRT("Terre"),
    SLAT("Caillebotis"),
}

enum class InsulationType(val displayNameFr: String) {
    NONE("Aucune"),
    POLYSTYRENE("Polystyrène"),
    POLYURETHANE("Polyuréthane"),
    MINERAL_WOOL("Laine minérale"),
}

enum class HouseOrientation(val displayNameFr: String, val degrees: Int) {
    N("Nord", 0),
    NE("Nord-Est", 45),
    E("Est", 90),
    SE("Sud-Est", 135),
    S("Sud", 180),
    SW("Sud-Ouest", 225),
    W("Ouest", 270),
    NW("Nord-Ouest", 315),
}
```

### Validation Result (Sealed Class)

```kotlin
sealed interface DimensionValidationResult {
    data object Valid : DimensionValidationResult
    data class Invalid(val fieldErrors: Map<DimensionField, String>) : DimensionValidationResult
}

enum class DimensionField {
    LENGTH, WIDTH, WALL_HEIGHT, RIDGE_HEIGHT,
    ROOF_TYPE, WALL_MATERIAL, FLOOR_TYPE,
    INSULATION_TYPE, INSULATION_THICKNESS,
    ORIENTATION,
}
```

## Room Entity

### HouseDimensionsEntity

```kotlin
@Entity(
    tableName = "house_dimensions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index(value = ["projectId"], unique = true)]
)
data class HouseDimensionsEntity(
    @PrimaryKey val id: String,               // UUID
    @ColumnInfo(name = "projectId") val projectId: String,
    val length: Double,                        // meters
    val width: Double,                         // meters
    val wallHeight: Double,                    // meters
    val roofType: String,                      // enum name: "FLAT", "PITCHED", "ARCHED"
    val ridgeHeight: Double?,                  // meters; nullable (only for PITCHED)
    val wallMaterial: String,                  // enum name
    val floorType: String,                     // enum name
    val insulationType: String,                // enum name
    val insulationThickness: Double?,          // millimeters; nullable (only when type != NONE)
    val orientation: String,                   // enum name: "N", "NE", etc.
    val createdAt: Long,                       // epoch millis
    val updatedAt: Long,                       // epoch millis
)
```

**Design decisions**:
- Enums stored as `String` (enum `.name`) rather than ordinal — resilient to enum reordering.
- `projectId` has a unique index enforcing the 1:1 relationship.
- `CASCADE` delete ensures dimensions are removed when a project is deleted.
- Nullable fields (`ridgeHeight`, `insulationThickness`) are only populated when their parent selector requires them.

## Room DAO

### HouseDimensionsDao

```kotlin
@Dao
interface HouseDimensionsDao {
    @Query("SELECT * FROM house_dimensions WHERE projectId = :projectId")
    fun getByProjectId(projectId: String): Flow<HouseDimensionsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: HouseDimensionsEntity)

    @Query("DELETE FROM house_dimensions WHERE projectId = :projectId")
    suspend fun deleteByProjectId(projectId: String)
}
```

**Key design**: `upsert` via `OnConflictStrategy.REPLACE` on the primary key `id` enables the "save on every field change" pattern without checking existence first.

## Mapper

### HouseDimensionsMapper

```kotlin
// HouseDimensionsEntity.toDomain() → HouseDimensions
// HouseDimensions.toEntity() → HouseDimensionsEntity
```

- Uses `valueOf()` for enum string → enum conversion (same pattern as `ProjectMapper`).
- Wraps raw `Double` values in value classes (`Meters`, `Millimeters`).
- Computes `floorArea` is a domain model property — not stored in the entity.

## Repository Interface (Domain Layer)

### HouseDimensionsRepository

```kotlin
interface HouseDimensionsRepository {
    fun getDimensionsByProjectId(projectId: String): Flow<HouseDimensions?>
    suspend fun saveDimensions(dimensions: HouseDimensions)
    suspend fun deleteDimensionsByProjectId(projectId: String)
}
```

## Use Cases

### SaveHouseDimensionsUseCase

```
invoke(dimensions: HouseDimensions): Result<HouseDimensions>
```
1. Calls `ValidateHouseDimensionsUseCase` — if invalid, returns `Result.failure`.
2. Sets `updatedAt` to current timestamp.
3. Delegates to `HouseDimensionsRepository.saveDimensions()`.
4. Returns `Result.success(dimensions)`.

### GetHouseDimensionsUseCase

```
invoke(projectId: String): Flow<HouseDimensions?>
```
Delegates to `HouseDimensionsRepository.getDimensionsByProjectId()`.

### ValidateHouseDimensionsUseCase

```
invoke(dimensions: HouseDimensions): DimensionValidationResult
```
Pure validation logic:
- Length: `0 < value ≤ 500`
- Width: `0 < value ≤ 100`
- Wall height: `0 < value ≤ 15`
- Ridge height (if `roofType == PITCHED`): `0 < value ≤ 20`; must not be null
- Ridge height (if `roofType != PITCHED`): must be null
- Insulation thickness (if `insulationType != NONE`): `0 < value ≤ 500`; must not be null
- Insulation thickness (if `insulationType == NONE`): must be null
- All enum fields must be set (non-null by type system)

Returns `DimensionValidationResult.Valid` or `DimensionValidationResult.Invalid(fieldErrors)`.

### CalculateFloorAreaUseCase

```
invoke(length: Meters, width: Meters): SquareMeters
```
Pure calculation: `SquareMeters(length.value * width.value)`. Rounded to 2 decimal places for display.

## Database Migration

### MIGRATION_2_3

```sql
CREATE TABLE IF NOT EXISTS `house_dimensions` (
    `id` TEXT NOT NULL PRIMARY KEY,
    `projectId` TEXT NOT NULL,
    `length` REAL NOT NULL,
    `width` REAL NOT NULL,
    `wallHeight` REAL NOT NULL,
    `roofType` TEXT NOT NULL,
    `ridgeHeight` REAL,
    `wallMaterial` TEXT NOT NULL,
    `floorType` TEXT NOT NULL,
    `insulationType` TEXT NOT NULL,
    `insulationThickness` REAL,
    `orientation` TEXT NOT NULL,
    `createdAt` INTEGER NOT NULL,
    `updatedAt` INTEGER NOT NULL,
    FOREIGN KEY(`projectId`) REFERENCES `projects`(`id`) ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_house_dimensions_projectId` ON `house_dimensions` (`projectId`);
```

**Integration**: Add to `PoultryDatabase`:
- Bump `version` from 2 to 3
- Register `HouseDimensionsEntity` in `@Database(entities = [...])`
- Add `MIGRATION_2_3` to migration list
- Expose `houseDimensionsDao()` abstract function

## Entity Relationship Diagram

```
┌──────────────────────┐       1:1       ┌──────────────────────────┐
│      projects        │ ──────────────→ │    house_dimensions      │
├──────────────────────┤                 ├──────────────────────────┤
│ id (PK)              │                 │ id (PK)                  │
│ name                 │                 │ projectId (FK, UNIQUE)   │
│ type                 │                 │ length                   │
│ status               │                 │ width                    │
│ location             │                 │ wallHeight               │
│ createdAt            │                 │ roofType                 │
│ updatedAt            │                 │ ridgeHeight?             │
│ syncTimestamp?       │                 │ wallMaterial              │
└──────────────────────┘                 │ floorType                │
                                         │ insulationType           │
                                         │ insulationThickness?     │
                                         │ orientation              │
                                         │ createdAt                │
                                         │ updatedAt                │
                                         └──────────────────────────┘
                                         ON DELETE CASCADE
```

## ViewModel State

### WizardUiState

```kotlin
sealed interface WizardUiState {
    data object Loading : WizardUiState
    data class Active(
        val currentStep: Int,           // 1-based (starts at 1)
        val totalSteps: Int,            // 6
        val dimensions: DimensionsFormState,
        val canGoNext: Boolean,         // true when all fields valid + filled
        val canGoPrevious: Boolean,     // false on step 1
        val saveError: String?,         // persistence error message
    ) : WizardUiState
    data class Error(val message: String) : WizardUiState
}

data class DimensionsFormState(
    val length: String = "",           // Raw text input (parsed to Double)
    val width: String = "",
    val wallHeight: String = "",
    val roofType: RoofType? = null,
    val ridgeHeight: String = "",
    val wallMaterial: WallMaterial? = null,
    val floorType: FloorType? = null,
    val insulationType: InsulationType? = null,
    val insulationThickness: String = "",
    val orientation: HouseOrientation? = null,
    val floorArea: SquareMeters? = null,
    val fieldErrors: Map<DimensionField, String> = emptyMap(),
)
```

### WizardIntent

```kotlin
sealed interface WizardIntent {
    // Dimension inputs
    data class UpdateLength(val value: String) : WizardIntent
    data class UpdateWidth(val value: String) : WizardIntent
    data class UpdateWallHeight(val value: String) : WizardIntent
    data class UpdateRidgeHeight(val value: String) : WizardIntent
    // Selectors
    data class SelectRoofType(val type: RoofType) : WizardIntent
    data class SelectWallMaterial(val material: WallMaterial) : WizardIntent
    data class SelectFloorType(val type: FloorType) : WizardIntent
    data class SelectInsulationType(val type: InsulationType) : WizardIntent
    data class UpdateInsulationThickness(val value: String) : WizardIntent
    data class SelectOrientation(val orientation: HouseOrientation) : WizardIntent
    // Navigation
    data object GoNext : WizardIntent
    data object GoPrevious : WizardIntent
}
```
