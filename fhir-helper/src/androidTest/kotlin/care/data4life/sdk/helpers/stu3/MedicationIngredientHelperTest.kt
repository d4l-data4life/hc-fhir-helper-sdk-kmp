/*
 * Copyright (c) 2020 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"), 
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of 
 * applications/third-party applications shall require the conclusion of a license agreement 
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party 
 * applications and/or if you’d like to contribute to the development of the SDK, please 
 * contact D4L by email to help@data4life.care.
 */

package care.data4life.sdk.helpers.stu3

import com.google.common.truth.Truth.assertThat
import care.data4life.fhir.stu3.model.FhirDecimal
import care.data4life.sdk.util.StringUtils
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Assert.fail
import org.junit.Test
import java.math.BigDecimal

class MedicationIngredientHelperTest {


    @Test
    fun buildWith_shouldReturnIngredient_whenAllArgsProvided() {
        // Given
        val substanceId = "substanceId"

        mockkStatic(StringUtils::class)
        every { StringUtils.randomUUID() } returns substanceId

        // When
        val (ingredient, substance) = MedicationIngredientHelper.buildWith(
            INGREDIENT_NAME,
            INGREDIENT_QUANTITY,
            INGREDIENT_UNIT
        )

        // Then

        // Substance
        assertThat(substance.code.coding).hasSize(1)
        assertThat(substance.code.coding!![0].display).isEqualTo(INGREDIENT_NAME)
        assertThat(substance.id).isEqualTo(substanceId)

        // Ingredient
        assertThat(ingredient.itemReference!!.reference).isEqualTo("#$substanceId")
        assertThat(ingredient.amount!!.numerator!!.value).isEqualTo(FhirDecimal(BigDecimal.valueOf(INGREDIENT_QUANTITY.toDouble())))
        assertThat(ingredient.amount!!.numerator!!.unit).isEqualTo(INGREDIENT_UNIT)
        unmockkStatic(StringUtils::class)
    }

    @Test
    fun builtWith_shouldThrow_whenIngrNameNotProvided() {
        try {
            MedicationIngredientHelper.buildWith(
                null,
                INGREDIENT_QUANTITY,
                INGREDIENT_UNIT
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("ingredientName is required")
        }

        try {
            MedicationIngredientHelper.buildWith(
                "",
                INGREDIENT_QUANTITY,
                INGREDIENT_UNIT
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("ingredientName is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenIngrQuantityNotProvided() {
        try {
            MedicationIngredientHelper.buildWith(
                INGREDIENT_NAME,
                null,
                INGREDIENT_UNIT
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("ingredientQuantity is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenIngrUnitNotProvided() {
        try {
            MedicationIngredientHelper.buildWith(
                INGREDIENT_NAME,
                INGREDIENT_QUANTITY,
                null
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("ingredientUnit is required")
        }

        try {
            MedicationIngredientHelper.buildWith(
                INGREDIENT_NAME,
                INGREDIENT_QUANTITY,
                ""
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("ingredientUnit is required")
        }

    }

    @Test
    fun getIngredient_shouldReturnIngredient() {
        // Given
        val (first, second) = MedicationIngredientHelper.buildWith(
            INGREDIENT_NAME,
            INGREDIENT_QUANTITY,
            INGREDIENT_UNIT
        )

        // When
        val ingredientParts = MedicationIngredientHelper.getIngredient(first, second)

        // Then
        assertThat(ingredientParts!!.first).isEqualTo(INGREDIENT_NAME)
        assertThat(ingredientParts.second).isEqualTo(INGREDIENT_QUANTITY)
        assertThat(ingredientParts.third).isEqualTo(INGREDIENT_UNIT)
    }

    companion object {

        private val INGREDIENT_NAME = "Ibuprofen"
        private val INGREDIENT_QUANTITY = 40f
        private val INGREDIENT_UNIT = "mg"
    }
}
