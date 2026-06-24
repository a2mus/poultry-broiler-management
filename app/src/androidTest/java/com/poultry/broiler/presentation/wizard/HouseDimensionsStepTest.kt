package com.poultry.broiler.presentation.wizard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.onNodeWithSubstring
import com.poultry.broiler.domain.model.RoofType
import com.poultry.broiler.presentation.theme.PoultryTheme
import org.junit.Rule
import org.junit.Test

/**
 * Smoke Compose UI test for [HouseDimensionsStep].
 *
 * Renders the step within [PoultryTheme] and verifies:
 * - the Length / Width / Wall Height labels render
 * - text input into the Length field flows back through `onIntent`
 *
 * Constitution Art 2.1 SHOULD write Compose UI tests for critical user flows.
 */
class HouseDimensionsStepTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun houseDimensionsStep_rendersAllStructuralFieldLabels() {
        var observedLength = ""
        composeRule.setContent {
            PoultryTheme {
                HouseDimensionsStep(
                    formState = DimensionsFormState(),
                    onIntent = { intent ->
                        if (intent is WizardIntent.UpdateLength) {
                            observedLength = intent.value
                        }
                    },
                )
            }
        }
        composeRule.onNodeWithText("Longueur").assertIsDisplayed()
        composeRule.onNodeWithText("Largeur").assertIsDisplayed()
        composeRule.onNodeWithText("Hauteur des murs").assertIsDisplayed()

        // Type into the Length field and assert the intent fires.
        val lengthField = composeRule.onNodeWithSubstring("Longueur")
        lengthField.performTextInput("100")
        assert(observedLength.contains("100"))
    }

    @Test
    fun houseDimensionsStep_rendersRoofSectionCard_whenRoofTypeProvided() {
        composeRule.setContent {
            PoultryTheme {
                HouseDimensionsStep(
                    formState = DimensionsFormState(roofType = RoofType.PITCHED),
                    onIntent = {},
                )
            }
        }
        composeRule.onNodeWithText("Toiture").assertIsDisplayed()
    }
}