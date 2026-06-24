package com.poultry.broiler.data.repository

import com.poultry.broiler.data.local.dao.EquipmentItemDao
import com.poultry.broiler.data.mapper.toDomain
import com.poultry.broiler.domain.model.EquipmentCategory
import com.poultry.broiler.domain.model.EquipmentItem
import com.poultry.broiler.domain.repository.EquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EquipmentRepositoryImpl @Inject constructor(
    private val equipmentItemDao: EquipmentItemDao,
) : EquipmentRepository {

    override fun getAllEquipment(): Flow<List<EquipmentItem>> {
        return equipmentItemDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getEquipmentByCategory(category: EquipmentCategory): Flow<List<EquipmentItem>> {
        return equipmentItemDao.getByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getEquipmentById(id: Long): EquipmentItem? {
        return equipmentItemDao.getById(id)?.toDomain()
    }
}
