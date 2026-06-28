package com.poultry.broiler.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "equipment_items",
    indices = [
        Index(value = ["category"]),
    ],
)
data class EquipmentItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val brand: String?,
    @ColumnInfo(name = "model_number")
    val modelNumber: String?,
    val capacity: String?,
    @ColumnInfo(name = "power_watts")
    val powerWatts: Double?,
    val unit: String,
    @ColumnInfo(name = "coverage_m2")
    val coverageM2: Double?,
    val description: String?,
)
