# Contract: Room DAOs

**Date**: 2026-06-24
**Feature**: `001-project-scaffolding-design-system`
**Layer**: Data (Android framework — Room)

---

## BreedProfileDao

Located at: `data/local/dao/BreedProfileDao.kt`

```kotlin
@Dao
interface BreedProfileDao {

    /**
     * Returns all breed profiles as a reactive Flow.
     * Room automatically invalidates and re-emits on data changes.
     */
    @Query("SELECT * FROM breed_profiles ORDER BY breed_name ASC")
    fun getAll(): Flow<List<BreedProfileEntity>>

    /**
     * Returns a single breed profile by primary key.
     */
    @Query("SELECT * FROM breed_profiles WHERE id = :id")
    suspend fun getById(id: Long): BreedProfileEntity?

    /**
     * Returns a single breed profile by unique breed name.
     */
    @Query("SELECT * FROM breed_profiles WHERE breed_name = :breedName")
    suspend fun getByName(breedName: String): BreedProfileEntity?

    /**
     * Returns the total count of breed profiles (useful for seed validation).
     */
    @Query("SELECT COUNT(*) FROM breed_profiles")
    suspend fun count(): Int
}
```

**Table name**: `breed_profiles`

---

## EquipmentItemDao

Located at: `data/local/dao/EquipmentItemDao.kt`

```kotlin
@Dao
interface EquipmentItemDao {

    /**
     * Returns all equipment items as a reactive Flow.
     */
    @Query("SELECT * FROM equipment_items ORDER BY category ASC, name ASC")
    fun getAll(): Flow<List<EquipmentItemEntity>>

    /**
     * Returns equipment items filtered by category as a reactive Flow.
     */
    @Query("SELECT * FROM equipment_items WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<EquipmentItemEntity>>

    /**
     * Returns a single equipment item by primary key.
     */
    @Query("SELECT * FROM equipment_items WHERE id = :id")
    suspend fun getById(id: Long): EquipmentItemEntity?

    /**
     * Returns the total count of equipment items (useful for seed validation).
     */
    @Query("SELECT COUNT(*) FROM equipment_items")
    suspend fun count(): Int
}
```

**Table name**: `equipment_items`

---

## Database Provider (Hilt)

Located at: `di/DatabaseModule.kt`

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoultryDatabase {
        return Room.databaseBuilder(
            context,
            PoultryDatabase::class.java,
            "poultry.db"
        )
        .createFromAsset("seed/poultry.db")
        .build()
    }

    @Provides
    fun provideBreedProfileDao(database: PoultryDatabase): BreedProfileDao {
        return database.breedProfileDao()
    }

    @Provides
    fun provideEquipmentItemDao(database: PoultryDatabase): EquipmentItemDao {
        return database.equipmentItemDao()
    }
}
```

**Notes**:
- Error handling for `createFromAsset()` failure is managed by the `SeedErrorHandler` utility, not inside the Hilt module directly (Hilt modules should not contain UI-level error logic)
- Database is a singleton scoped to the application lifecycle
- DAOs are not singletons — Room returns lightweight proxy objects
