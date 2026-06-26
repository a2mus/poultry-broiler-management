package com.poultry.broiler.presentation.wizard

import android.content.Context
import app.cash.turbine.test
import com.poultry.broiler.domain.model.FloorType
import com.poultry.broiler.domain.model.HouseDimensions
import com.poultry.broiler.domain.model.HouseOrientation
import com.poultry.broiler.domain.model.InsulationType
import com.poultry.broiler.domain.model.Meters
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.domain.model.SquareMeters
import com.poultry.broiler.domain.model.WallMaterial
import com.poultry.broiler.domain.usecase.CalculateFloorAreaUseCase
import com.poultry.broiler.domain.usecase.GetHouseDimensionsUseCase
import com.poultry.broiler.domain.usecase.SaveHouseDimensionsUseCase
import com.poultry.broiler.domain.usecase.ValidateHouseDimensionsUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WizardViewModelTest {
    private val appContext: Context = mockk(relaxed = true)
    private val getUseCase: GetHouseDimensionsUseCase = mockk()
    private val saveUseCase: SaveHouseDimensionsUseCase = mockk(relaxed = true)
    private val calculateFloorAreaUseCase = CalculateFloorAreaUseCase()
    private val validateUseCase = ValidateHouseDimensionsUseCase()

    private fun viewModel(): WizardViewModel =
        WizardViewModel(
            appContext = appContext,
            getHouseDimensionsUseCase = getUseCase,
            saveHouseDimensionsUseCase = saveUseCase,
            calculateFloorAreaUseCase = calculateFloorAreaUseCase,
            validateHouseDimensionsUseCase = validateUseCase,
        )

    private val existingDimensions =
        HouseDimensions(
            id = "id-existing",
            projectId = "p-1",
            length = Meters(100.0),
            width = Meters(12.0),
            wallHeight = Meters(3.0),
            roofType = RoofType.FLAT,
            ridgeHeight = null,
            wallMaterial = WallMaterial.BLOCK,
            floorType = FloorType.CONCRETE,
            insulationType = InsulationType.NONE,
            insulationThickness = null,
            orientation = HouseOrientation.N,
            createdAt = 0L,
            updatedAt = 0L,
        )

    init {
        // Mockk returns "" for any string resource lookup when relaxed.
        every { appContext.getString(any()) } returns ""
        every { appContext.getString(any(), *anyVararg<Any>()) } returns ""
    }

    @Test
    fun load_emitsActiveWithRestoredDimensions() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(existingDimensions)
            val vm = viewModel()
            vm.load("p-1")
            vm.uiState.test {
                // Allow the load coroutine to complete via launch
                val firstActive =
                    (awaitItem() as? WizardUiState.Active) ?: WizardUiState.Active(
                        currentStep = 1,
                        totalSteps = 6,
                        dimensions = DimensionsFormState(),
                        canGoNext = false,
                        canGoPrevious = false,
                        saveError = null,
                    )
                assertEquals(1, firstActive.currentStep)
                assertEquals(6, firstActive.totalSteps)
                assertEquals("100.0", firstActive.dimensions.length)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun onIntent_updateLength_recomputesFloorArea() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            coEvery { saveUseCase(any()) } returns Result.success(existingDimensions)
            val vm = viewModel()
            vm.load("p-1")
            // Issue an UpdateLength intent without waiting for state collection
            vm.onIntent(WizardIntent.UpdateLength("100.0"), "p-1")
            vm.onIntent(WizardIntent.UpdateWidth("12.0"), "p-1")
            val active = vm.uiState.value as? WizardUiState.Active
            assertNotNull(active)
            assertEquals(SquareMeters(1200.0).value, active?.dimensions?.floorArea?.value)
        }

    @Test
    fun onIntent_selectRoofTypePitched_clearsRidgeHeightWhenSwitchingAway() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            vm.load("p-1")
            vm.onIntent(WizardIntent.SelectRoofType(RoofType.PITCHED), "p-1")
            vm.onIntent(WizardIntent.UpdateRidgeHeight("5.0"), "p-1")
            // Switch away from PITCHED → ridge should clear
            vm.onIntent(WizardIntent.SelectRoofType(RoofType.FLAT), "p-1")
            val active = vm.uiState.value as? WizardUiState.Active
            assertEquals("", active?.dimensions?.ridgeHeight)
        }

    @Test
    fun onIntent_selectInsulationNone_clearsThickness() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            vm.load("p-1")
            vm.onIntent(WizardIntent.SelectInsulationType(InsulationType.POLYSTYRENE), "p-1")
            vm.onIntent(WizardIntent.UpdateInsulationThickness("50.0"), "p-1")
            vm.onIntent(WizardIntent.SelectInsulationType(InsulationType.NONE), "p-1")
            val active = vm.uiState.value as? WizardUiState.Active
            assertEquals("", active?.dimensions?.insulationThickness)
        }

    @Test
    fun onIntent_goNextValid_advancesStep() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            vm.load("p-1")
            fillValidForm(vm)
            val beforeStep = (vm.uiState.value as WizardUiState.Active).currentStep
            vm.onIntent(WizardIntent.GoNext, "p-1")
            val afterStep = (vm.uiState.value as WizardUiState.Active).currentStep
            assertEquals(beforeStep + 1, afterStep)
        }

    @Test
    fun onIntent_goNextWithInvalidForm_blocksAdvanceAndProducesFieldErrors() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            vm.load("p-1")
            // Only length set — no width, no other required fields
            vm.onIntent(WizardIntent.UpdateLength("100.0"), "p-1")
            vm.onIntent(WizardIntent.GoNext, "p-1")
            val active = vm.uiState.value as WizardUiState.Active
            assertEquals(1, active.currentStep)
            assertFalse(active.canGoNext)
            assertTrue(active.dimensions.fieldErrors.isNotEmpty())
        }

    @Test
    fun onIntent_updateLengthNegative_surfacesFieldError() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            vm.load("p-1")
            vm.onIntent(WizardIntent.UpdateLength("-10.0"), "p-1")
            val active = vm.uiState.value as WizardUiState.Active
            assertNotNull(active.dimensions.fieldErrors[com.poultry.broiler.domain.validation.DimensionField.LENGTH])
        }

    @Test
    fun computeState_loadingFirst_thenActive() =
        runTest {
            every { getUseCase("p-1") } returns flowOf(null)
            val vm = viewModel()
            assertTrue(vm.uiState.value is WizardUiState.Loading)
            vm.load("p-1")
            // Voluntarily overwrite
            vm.onIntent(WizardIntent.UpdateLength("50.0"), "p-1")
            val active = vm.uiState.value as WizardUiState.Active
            assertEquals(50.0, active.dimensions.length.toDoubleOrNull())
        }

    private fun fillValidForm(vm: WizardViewModel) {
        vm.onIntent(WizardIntent.UpdateLength("100.0"), "p-1")
        vm.onIntent(WizardIntent.UpdateWidth("12.0"), "p-1")
        vm.onIntent(WizardIntent.UpdateWallHeight("3.0"), "p-1")
        vm.onIntent(WizardIntent.SelectRoofType(RoofType.FLAT), "p-1")
        vm.onIntent(WizardIntent.SelectWallMaterial(WallMaterial.BLOCK), "p-1")
        vm.onIntent(WizardIntent.SelectFloorType(FloorType.CONCRETE), "p-1")
        vm.onIntent(WizardIntent.SelectInsulationType(InsulationType.POLYSTYRENE), "p-1")
        vm.onIntent(WizardIntent.UpdateInsulationThickness("50.0"), "p-1")
        vm.onIntent(WizardIntent.SelectOrientation(HouseOrientation.NE), "p-1")
    }
}
