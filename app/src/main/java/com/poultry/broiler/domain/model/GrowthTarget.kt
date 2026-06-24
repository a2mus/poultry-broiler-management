package com.poultry.broiler.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GrowthTarget(
    val week: Int,
    val targetWeightG: Int,
    val dailyFeedG: Int,
    val dailyWaterMl: Int,
)
