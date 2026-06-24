package com.poultry.broiler.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "breed_profiles",
    indices = [
        Index(value = ["breed_name"], unique = true),
    ],
)
data class BreedProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "breed_name")
    val breedName: String,

    val supplier: String,

    @ColumnInfo(name = "growth_targets_json")
    val growthTargetsJson: String,

    @ColumnInfo(name = "min_density_kg_m2")
    val minDensityKgM2: Double,

    @ColumnInfo(name = "max_density_kg_m2")
    val maxDensityKgM2: Double,

    @ColumnInfo(name = "target_fcr")
    val targetFcr: Double,

    @ColumnInfo(name = "cycle_duration_days")
    val cycleDurationDays: Int,

    @ColumnInfo(name = "target_weight_g")
    val targetWeightG: Int,

    @ColumnInfo(name = "mortality_rate_pct")
    val mortalityRatePct: Double,

    val description: String?,
)
