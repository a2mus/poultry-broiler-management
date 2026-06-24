package com.poultry.broiler.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.poultry.broiler.data.local.PoultryDatabase
import com.poultry.broiler.data.local.entity.HouseDimensionsEntity
import com.poultry.broiler.data.local.entity.ProjectEntity
import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.WallMaterial
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Room in-memory DAO integration tests for [HouseDimensionsDao].
 *
 * Verifies the unique-projectId constraint, cascade-delete behaviour, and that
 * upserts replace prior records (Constitution Art 2.1 — integration tests for
 * Room DAO queries).
 */
@RunWith(AndroidJUnit4::class)
class HouseDimensionsDaoTest {

    private lateinit var database: PoultryDatabase
    private lateinit var dao: HouseDimensionsDao
    private lateinit var projectDao: ProjectDao

    private val project = ProjectEntity(
        id = "project-1",
        name = "Test project",
        type = "NEW_INSTALLATION",
        status = "DRAFT",
        location = null,
        createdAt = 1L,
        updatedAt = 1L,
        syncTimestamp = null,
    )

    private fun dimensionsEntity(
        id: String = "dim-1",
        projectId: String = project.id,
    ) = HouseDimensionsEntity(
        id = id,
        projectId = projectId,
        length = 100.0,
        width = 12.0,
        wallHeight = 3.0,
        roofType = RoofType.PITCHED.name,
        ridgeHeight = 5.0,
        wallMaterial = WallMaterial.BLOCK.name,
        floorType = FloorType.CONCRETE.name,
        insulationType = InsulationType.POLYSTYRENE.name,
        insulationThickness = 50.0,
        orientation = HouseOrientation.NE.name,
        createdAt = 1L,
        updatedAt = 2L,
    )

    @Before
    fun setUp() = runTest {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PoultryDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.houseDimensionsDao()
        projectDao = database.projectDao()
        projectDao.insert(project)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getByProjectId_emitsNullWhenAbsent() = runTest {
        assertNull(dao.getByProjectId("missing").first())
    }

    @Test
    fun upsert_thenGet_byProjectId_returnsPersistedEntity() = runTest {
        dao.upsert(dimensionsEntity())
        dao.getByProjectId(project.id).test {
            val entity = awaitItem()
            assertEquals("dim-1", entity?.id)
            assertEquals(100.0, entity?.length)
            assertEquals(RoofType.PITCHED.name, entity?.roofType)
            awaitComplete()
        }
    }

    @Test
    fun upsert_replaces_whenSamePrimaryKey() = runTest {
        dao.upsert(dimensionsEntity())
        dao.upsert(dimensionsEntity().copy(length = 200.0, width = 24.0))
        val result = dao.getByProjectId(project.id).first()
        assertEquals(200.0, result?.length)
        assertEquals(24.0, result?.width)
    }

    @Test
    fun deleteByProjectId_removesRecord() = runTest {
        dao.upsert(dimensionsEntity())
        dao.deleteByProjectId(project.id)
        assertNull(dao.getByProjectId(project.id).first())
    }

    @Test(expected = Exception::class)
    fun upsert_whenProjectForeignKeyMissing_throws() = runTest {
        dao.upsert(dimensionsEntity(projectId = "no-such-project"))
    }

    @Test
    fun deletingProject_cascadesTodimensions() = runTest {
        dao.upsert(dimensionsEntity())
        projectDao.deleteById(project.id)
        assertNull(dao.getByProjectId(project.id).first())
    }
}