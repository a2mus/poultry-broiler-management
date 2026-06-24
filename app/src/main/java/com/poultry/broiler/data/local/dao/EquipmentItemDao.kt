package com.poultry.broiler.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.poultry.broiler.data.local.entity.EquipmentItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipmentItemDao {

    @Query("SELECT * FROM equipment_items ORDER BY category ASC, name ASC")
    fun getAll(): Flow<List<EquipmentItemEntity>>

    @Query("SELECT * FROM equipment_items WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<EquipmentItemEntity>>

    @Query("SELECT * FROM equipment_items WHERE id = :id")
    suspend fun getById(id: Long): EquipmentItemEntity?

    @Query("SELECT COUNT(*) FROM equipment_items")
    suspend fun count(): Int
}
