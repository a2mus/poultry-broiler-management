package com.poultry.broiler.domain.model

data class EquipmentItem(
    val id: Long,
    val name: String,
    val category: EquipmentCategory,
    val brand: String?,
    val modelNumber: String?,
    val capacity: String?,
    val powerWatts: Double?,
    val unit: String,
    val coverageM2: Double?,
    val description: String?,
)
