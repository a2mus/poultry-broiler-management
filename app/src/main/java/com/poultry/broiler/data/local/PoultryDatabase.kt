package com.poultry.broiler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.poultry.broiler.data.local.converter.GrowthTargetListConverter
import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.local.dao.EquipmentItemDao
import com.poultry.broiler.data.local.dao.HouseDimensionsDao
import com.poultry.broiler.data.local.dao.ProjectDao
import com.poultry.broiler.data.local.entity.BreedProfileEntity
import com.poultry.broiler.data.local.entity.EquipmentItemEntity
import com.poultry.broiler.data.local.entity.HouseDimensionsEntity
import com.poultry.broiler.data.local.entity.ProjectEntity

@Database(
    entities = [
        BreedProfileEntity::class,
        EquipmentItemEntity::class,
        ProjectEntity::class,
        HouseDimensionsEntity::class,
    ],
    version = 3,
    exportSchema = true,
)
@TypeConverters(GrowthTargetListConverter::class)
abstract class PoultryDatabase : RoomDatabase() {
    abstract fun breedProfileDao(): BreedProfileDao

    abstract fun equipmentItemDao(): EquipmentItemDao

    abstract fun projectDao(): ProjectDao

    abstract fun houseDimensionsDao(): HouseDimensionsDao

    companion object {
        /**
         * Migration from database version 1 → 2.
         *
         * Adds the `projects` table (Feature #002 Project Management) with an
         * index on `updatedAt` to support the default most-recently-modified
         * sort order.
         */
        val MIGRATION_1_2: Migration =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS projects (
                            id TEXT NOT NULL PRIMARY KEY,
                            name TEXT NOT NULL,
                            type TEXT NOT NULL,
                            status TEXT NOT NULL DEFAULT 'DRAFT',
                            location TEXT,
                            createdAt INTEGER NOT NULL,
                            updatedAt INTEGER NOT NULL,
                            syncTimestamp INTEGER
                        )
                        """.trimIndent(),
                    )
                    db.execSQL(
                        "CREATE INDEX IF NOT EXISTS index_projects_updatedAt ON projects(updatedAt)",
                    )
                }
            }

        /**
         * Migration from database version 2 → 3.
         *
         * Adds the `house_dimensions` table (Feature #003 House Dimensions
         * Wizard) with a foreign key to `projects(id)` and a unique index on
         * `projectId` enforcing the 1:1 relationship. `CASCADE` delete keeps
         * dimensions tidy when a project is removed.
         */
        val MIGRATION_2_3: Migration =
            object : Migration(2, 3) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
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
                        )
                        """.trimIndent(),
                    )
                    db.execSQL(
                        "CREATE UNIQUE INDEX IF NOT EXISTS `index_house_dimensions_projectId` " +
                            "ON `house_dimensions` (`projectId`)",
                    )
                }
            }
    }
}
