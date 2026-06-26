package com.poultry.broiler.domain.repository

import com.poultry.broiler.domain.model.EquipmentCategory
import com.poultry.broiler.domain.model.EquipmentItem
import kotlinx.coroutines.flow.Flow

interface EquipmentRepository {
    fun getAllEquipment(): Flow<List<EquipmentItem>>

    fun getEquipmentByCategory(category: EquipmentCategory): Flow<List<EquipmentItem>>

    suspend fun getEquipmentById(id: Long): EquipmentItem?
}
