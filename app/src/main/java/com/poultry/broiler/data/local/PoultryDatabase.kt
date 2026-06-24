package com.poultry.broiler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.poultry.broiler.data.local.converter.GrowthTargetListConverter
import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.local.dao.EquipmentItemDao
import com.poultry.broiler.data.local.dao.ProjectDao
import com.poultry.broiler.data.local.entity.BreedProfileEntity
import com.poultry.broiler.data.local.entity.EquipmentItemEntity
import com.poultry.broiler.data.local.entity.ProjectEntity

@Database(
    entities = [
        BreedProfileEntity::class,
        EquipmentItemEntity::class,
        ProjectEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(GrowthTargetListConverter::class)
abstract class PoultryDatabase : RoomDatabase() {

    abstract fun breedProfileDao(): BreedProfileDao

    abstract fun equipmentItemDao(): EquipmentItemDao

    abstract fun projectDao(): ProjectDao

    companion object {
        /**
         * Migration from database version 1 → 2.
         *
         * Adds the `projects` table (Feature #002 Project Management) with an
         * index on `updatedAt` to support the default most-recently-modified
         * sort order.
         */
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
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
    }
}
