package com.poultry.broiler.data.mapper

import com.poultry.broiler.data.local.entity.EquipmentItemEntity
import com.poultry.broiler.domain.model.EquipmentCategory
import com.poultry.broiler.domain.model.EquipmentItem

fun EquipmentItemEntity.toDomain(): EquipmentItem =
    EquipmentItem(
        id = id,
        name = name,
        category = EquipmentCategory.valueOf(category),
        brand = brand,
        modelNumber = modelNumber,
        capacity = capacity,
        powerWatts = powerWatts,
        unit = unit,
        coverageM2 = coverageM2,
        description = description,
    )
