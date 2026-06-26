package com.poultry.broiler.data.mapper

import com.poultry.broiler.data.local.entity.BreedProfileEntity
import com.poultry.broiler.domain.model.BreedProfile
import com.poultry.broiler.domain.model.GrowthTarget
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

fun BreedProfileEntity.toDomain(): BreedProfile =
    BreedProfile(
        id = id,
        breedName = breedName,
        supplier = supplier,
        growthTargets = json.decodeFromString<List<GrowthTarget>>(growthTargetsJson),
        densityRange = minDensityKgM2..maxDensityKgM2,
        targetFcr = targetFcr,
        cycleDurationDays = cycleDurationDays,
        targetWeightGrams = targetWeightG,
        mortalityRatePercent = mortalityRatePct,
        description = description,
    )
