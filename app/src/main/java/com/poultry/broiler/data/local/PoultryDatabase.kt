package com.poultry.broiler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.poultry.broiler.data.local.converter.GrowthTargetListConverter
import com.poultry.broiler.data.local.dao.BreedProfileDao
import com.poultry.broiler.data.local.dao.EquipmentItemDao
import com.poultry.broiler.data.local.entity.BreedProfileEntity
import com.poultry.broiler.data.local.entity.EquipmentItemEntity

@Database(
    entities = [
        BreedProfileEntity::class,
        EquipmentItemEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(GrowthTargetListConverter::class)
abstract class PoultryDatabase : RoomDatabase() {

    abstract fun breedProfileDao(): BreedProfileDao

    abstract fun equipmentItemDao(): EquipmentItemDao
}
