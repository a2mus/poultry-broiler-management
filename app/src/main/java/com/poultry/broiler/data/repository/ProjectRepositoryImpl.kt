package com.poultry.broiler.data.repository

import com.poultry.broiler.data.local.dao.ProjectDao
import com.poultry.broiler.data.mapper.toDomain
import com.poultry.broiler.data.mapper.toEntity
import com.poultry.broiler.domain.model.Project
import com.poultry.broiler.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ProjectRepository] backed by Room via [ProjectDao].
 *
 * Bridges the data-layer [com.poultry.broiler.data.local.entity.ProjectEntity]
 * and the domain-layer [Project] using [ProjectMapper] extensions.
 */
@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
) : ProjectRepository {

    override fun getAllProjectsSortedByModified(): Flow<List<Project>> {
        return projectDao.getAllSortedByUpdatedAt().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProjects(query: String): Flow<List<Project>> {
        return projectDao.searchByNameOrLocation(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProjectById(id: String): Flow<Project?> {
        return projectDao.getById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insertProject(project: Project): String {
        projectDao.insert(project.toEntity())
        return project.id
    }

    override suspend fun updateProject(project: Project) {
        projectDao.update(project.toEntity())
    }

    override suspend fun deleteProject(id: String) {
        projectDao.deleteById(id)
    }
}
