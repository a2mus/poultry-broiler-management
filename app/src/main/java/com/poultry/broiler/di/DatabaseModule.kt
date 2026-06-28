package com.poultry.broiler.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.poultry.broiler.data.local.PoultryDatabase
import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.local.dao.EquipmentItemDao
import com.poultry.broiler.data.local.dao.HouseDimensionsDao
import com.poultry.broiler.data.local.dao.ProjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PoultryDatabase {
        return Room.databaseBuilder(
            context,
            PoultryDatabase::class.java,
            "poultry.db",
        )
            .addMigrations(PoultryDatabase.MIGRATION_1_2, PoultryDatabase.MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Seed default breed profiles
                    db.execSQL("""
                        INSERT OR IGNORE INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
                        VALUES ('Cobb 500', 'Cobb-Vantress', '[]', 30.0, 42.0, 1.65, 42, 2500, 2.5, 'Standard broiler breed')
                    """.trimIndent())
                    db.execSQL("""
                        INSERT OR IGNORE INTO breed_profiles (breed_name, supplier, growth_targets_json, min_density_kg_m2, max_density_kg_m2, target_fcr, cycle_duration_days, target_weight_g, mortality_rate_pct, description)
                        VALUES ('Ross 308', 'Aviagen', '[]', 30.0, 42.0, 1.60, 42, 2700, 2.5, 'High performance breed')
                    """.trimIndent())
                }
            })
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

    @Provides
    fun provideProjectDao(database: PoultryDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    fun provideHouseDimensionsDao(database: PoultryDatabase): HouseDimensionsDao {
        return database.houseDimensionsDao()
    }
}
