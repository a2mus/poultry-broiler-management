package com.poultry.broiler.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.poultry.broiler.data.local.entity.HouseDimensionsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for the `house_dimensions` table.
 *
 * The [upsert] method relies on `OnConflictStrategy.REPLACE` on the primary key
 * `id` to support the "save on every field change" pattern without an existence
 * check (research.md §4).
 */
@Dao
interface HouseDimensionsDao {
    /**
     * Observes the dimensions record for [projectId], emitting `null` while no
     * record has been persisted yet and the populated entity thereafter.
     */
    @Query("SELECT * FROM house_dimensions WHERE projectId = :projectId")
    fun getByProjectId(projectId: String): Flow<HouseDimensionsEntity?>

    /**
     * Inserts or replaces the dimensions record keyed by [entity].id, allowing
     * the wizard to overwrite the previous draft state immediately.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: HouseDimensionsEntity)

    /**
     * Removes the dimensions record associated with [projectId]. Used by
     * project-deletion cascades and the "reset wizard step" affordance.
     */
    @Query("DELETE FROM house_dimensions WHERE projectId = :projectId")
    suspend fun deleteByProjectId(projectId: String)
}
