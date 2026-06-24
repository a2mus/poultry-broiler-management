package com.poultry.broiler.domain.model

data class BreedProfile(
    val id: Long,
    val breedName: String,
    val supplier: String,
    val growthTargets: List<GrowthTarget>,
    val densityRange: ClosedFloatingPointRange<Double>,
    val targetFcr: Double,
    val cycleDurationDays: Int,
    val targetWeightGrams: Int,
    val mortalityRatePercent: Double,
    val description: String?,
)
