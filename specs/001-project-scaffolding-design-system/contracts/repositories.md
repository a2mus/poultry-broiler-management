# Contract: Repository Interfaces

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`
**Layer**: Domain (pure Kotlin — no Android imports)

---

## BreedRepository

Located at: `domain/repository/BreedRepository.kt`

```kotlin
interface BreedRepository {

    /**
     * Returns all available breed profiles from the local database.
     * Emits a new list whenever the underlying data changes.
     */
    fun getAllBreeds(): Flow<List<BreedProfile>>

    /**
     * Returns a single breed profile by its unique ID.
     * @param id The breed profile identifier
     * @return The breed profile, or null if not found
     */
    suspend fun getBreedById(id: Long): BreedProfile?

    /**
     * Returns a single breed profile by its unique breed name.
     * @param breedName The breed commercial name (e.g., "Ross 308")
     * @return The breed profile, or null if not found
     */
    suspend fun getBreedByName(breedName: String): BreedProfile?
}
```

**Notes**:
- Read-only interface for this feature — seed data is not user-editable
- `Flow` return for `getAllBreeds()` enables reactive UI updates if seed data is ever refreshed
- Implementation: `BreedRepositoryImpl` in data layer maps `BreedProfileEntity` → `BreedProfile`

---

## EquipmentRepository

Located at: `domain/repository/EquipmentRepository.kt`

```kotlin
interface EquipmentRepository {

    /**
     * Returns all equipment items from the local database.
     * Emits a new list whenever the underlying data changes.
     */
    fun getAllEquipment(): Flow<List<EquipmentItem>>

    /**
     * Returns all equipment items filtered by category.
     * @param category The equipment category to filter by
     */
    fun getEquipmentByCategory(category: EquipmentCategory): Flow<List<EquipmentItem>>

    /**
     * Returns a single equipment item by its unique ID.
     * @param id The equipment item identifier
     * @return The equipment item, or null if not found
     */
    suspend fun getEquipmentById(id: Long): EquipmentItem?
}
```

**Notes**:
- Read-only interface — catalog data is seeded reference data
- Category filtering enables the Catalog screen to show grouped equipment
- Implementation: `EquipmentRepositoryImpl` in data layer maps `EquipmentItemEntity` → `EquipmentItem`

---

## Repository Implementation Bindings (Hilt)

Both repositories are bound in `RepositoryModule`:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBreedRepository(impl: BreedRepositoryImpl): BreedRepository

    @Binds
    abstract fun bindEquipmentRepository(impl: EquipmentRepositoryImpl): EquipmentRepository
}
```
