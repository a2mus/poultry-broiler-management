package com.poultry.broiler.domain.usecase

import com.poultry.broiler.domain.model.Meters
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalculateFloorAreaUseCaseTest {
    private val useCase = CalculateFloorAreaUseCase()

    @org.junit.jupiter.api.DisplayName("area is length times width")
    @Test
    fun calculateFloorArea_givenBasicInputs_returnsProduct() {
        val area = useCase(Meters(100.0), Meters(12.0))
        assertEquals(1200.0, area.value)
    }

    @Test
    fun calculateFloorArea_givenDecimalInputs_returnsRoundedToTwoDecimals() {
        val area = useCase(Meters(10.5), Meters(4.25))
        // 10.5 × 4.25 = 44.625 → rounded to 44.63
        assertEquals(44.63, area.value)
    }

    @Test
    fun calculateFloorArea_givenVerySmallValues_returnsCalculatedArea() {
        val area = useCase(Meters(0.1), Meters(0.1))
        assertEquals(0.01, area.value)
    }

    @Test
    fun calculateFloorArea_givenLargeValues_returnsCalculatedArea() {
        val area = useCase(Meters(500.0), Meters(100.0))
        assertEquals(50000.0, area.value)
    }

    @Test
    fun calculateFloorArea_givenZeroWidth_returnsZero() {
        val area = useCase(Meters(10.0), Meters(0.0))
        assertEquals(0.0, area.value)
    }

    @Test
    fun calculateFloorArea_givenTruncationRounding_truncatesCorrectly() {
        val area = useCase(Meters(3.0), Meters(3.0))
        assertEquals(9.0, area.value)
    }

    @Test
    fun calculateFloorArea_givenTwoDecimalsExactlyRepresentable_returnsExactProduct() {
        // 1.1 × 1.1 ≈ 1.21 (no extra rounding needed for binary-exact 1.21 output)
        val area = useCase(Meters(1.1), Meters(1.1))
        assertEquals(1.21, area.value, 0.0001)
    }

    @Test
    fun calculateFloorArea_givenRoundingHalfUpBinaryExactInput_roundsHalfAwayFromZero() {
        // 5.5 × 1.0 = 5.5 (exactly representable) → stays two decimals
        val area = useCase(Meters(5.5), Meters(1.0))
        assertEquals(5.5, area.value)
    }
}
