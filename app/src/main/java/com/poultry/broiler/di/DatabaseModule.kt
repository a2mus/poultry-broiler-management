package com.poultry.broiler.di

import android.content.Context
import androidx.room.Room
import com.poultry.broiler.data.local.PoultryDatabase
import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.local.dao.EquipmentItemDao
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
            .createFromAsset("seed/poultry.db")
            .addMigrations(PoultryDatabase.MIGRATION_1_2)
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
}
