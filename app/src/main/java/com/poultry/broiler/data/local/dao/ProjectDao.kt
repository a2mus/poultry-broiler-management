package com.poultry.broiler.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.poultry.broiler.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for the `projects` table.
 *
 * All query methods return [Flow] for reactive observation.
 * Write methods are suspending.
 */
@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllSortedByUpdatedAt(): Flow<List<ProjectEntity>>

    @Query(
        """
        SELECT * FROM projects
        WHERE name LIKE '%' || :query || '%' COLLATE NOCASE
           OR location LIKE '%' || :query || '%' COLLATE NOCASE
        ORDER BY updatedAt DESC
        """,
    )
    fun searchByNameOrLocation(query: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: String): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(project: ProjectEntity)

    @Update
    suspend fun update(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun getProjectCount(): Int
}
