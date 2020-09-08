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

package care.data4life.sdk.helpers

import com.google.common.truth.Truth.assertThat
import care.data4life.fhir.stu3.model.Medication
import care.data4life.fhir.stu3.model.Substance
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Assert.fail
import org.junit.Test

class MedicationHelperTest {

    @Test
    fun buildWith_shouldReturnMedication_whenAllArgsProvided() {
        // When
        val medication = MedicationHelper.buildWith(MEDICATION_NAME, MEDICATION_FORM)

        // Then
        assertThat(medication.code!!.coding).hasSize(1)
        assertThat(medication.code!!.coding!![0].display).isEqualTo(MEDICATION_NAME)
        assertThat(medication.form!!.coding).hasSize(1)
        assertThat(medication.form!!.coding!![0].display).isEqualTo(MEDICATION_FORM)
    }

    @Test
    fun builtWith_shouldThrow_whenMedNameNotProvided() {
        try {
            MedicationHelper.buildWith(null, MEDICATION_FORM)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medicationName is required")
        }

        try {
            MedicationHelper.buildWith("", MEDICATION_FORM)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medicationName is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenMedFormNotProvided() {
        try {
            MedicationHelper.buildWith(MEDICATION_NAME, null)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medicationForm is required")
        }

        try {
            MedicationHelper.buildWith(MEDICATION_NAME, "")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medicationForm is required")
        }

    }

    @Test
    fun addIngredient_shouldReturnIngredient_whenAllArgsProvided() {
        // Given
        val medication = MedicationHelper.buildWith("ignore", "ignore")

        val mockIngredient = mockk<Medication.MedicationIngredient>()
        val mockSubstance = mockk<Substance>()
        val pair = Pair(mockIngredient, mockSubstance)

        mockkStatic(MedicationIngredientHelper::class)
        every {
            MedicationIngredientHelper.buildWith(
                INGREDIENT_NAME,
                INGREDIENT_QUANTITY,
                INGREDIENT_UNIT
            )
        } returns pair

        // When
        MedicationHelper.addIngredient(medication, INGREDIENT_NAME, INGREDIENT_QUANTITY, INGREDIENT_UNIT)

        // Then
        assertThat(medication.ingredient).hasSize(1)
        assertThat(medication.ingredient).contains(mockIngredient)
        assertThat(medication.contained).hasSize(1)
        assertThat(medication.contained).contains(mockSubstance)
        unmockkStatic(MedicationIngredientHelper::class)
    }

    @Test
    fun addIngredient_shouldThrow_whenMedicationNotProvided() {
        try {
            MedicationHelper.addIngredient(null, "", 0f, "")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medication is required")
        }

    }

    @Test
    fun getProductName_shouldReturnProductName() {
        // Given
        val medication = MedicationHelper.buildWith(MEDICATION_NAME, MEDICATION_FORM)

        // When
        val medicationName = MedicationHelper.getMedicationName(medication)

        // Then
        assertThat(medicationName).isEqualTo(MEDICATION_NAME)
    }

    @Test
    fun getProductForm_shouldReturnProductForm() {
        // Given
        val medication = MedicationHelper.buildWith(MEDICATION_NAME, MEDICATION_FORM)

        // When
        val productForm = MedicationHelper.getMedicationForm(medication)

        // Then
        assertThat(productForm).isEqualTo(MEDICATION_FORM)
    }

    @Test
    fun getIngredients_shouldReturnIngredients() {
        // Given
        val medication = MedicationHelper.buildWith(MEDICATION_NAME, MEDICATION_FORM)
        MedicationHelper.addIngredient(medication, INGREDIENT_NAME, INGREDIENT_QUANTITY, INGREDIENT_UNIT)

        // When
        val ingredients = MedicationHelper.getIngredients(medication)

        // Then
        assertThat(ingredients?.size).isEqualTo(1)
        assertThat(ingredients!![0].first).isEqualTo(INGREDIENT_NAME)
        assertThat(ingredients[0].second).isEqualTo(INGREDIENT_QUANTITY)
        assertThat(ingredients[0].third).isEqualTo(INGREDIENT_UNIT)
    }

    companion object {

        private val MEDICATION_NAME = "Ibuprofen-ratiopharm"
        private val MEDICATION_FORM = "tablets"
        private val INGREDIENT_NAME = "Ibuprofen"
        private val INGREDIENT_QUANTITY = 40f
        private val INGREDIENT_UNIT = "mg"
    }
}
