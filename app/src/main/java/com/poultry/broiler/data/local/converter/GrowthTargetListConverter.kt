package com.poultry.broiler.data.local.converter

import androidx.room.TypeConverter
import com.poultry.broiler.domain.model.GrowthTarget
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GrowthTargetListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromGrowthTargetList(value: List<GrowthTarget>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toGrowthTargetList(value: String): List<GrowthTarget> {
        return json.decodeFromString(value)
    }
}
