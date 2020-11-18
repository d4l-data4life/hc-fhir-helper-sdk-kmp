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

package care.data4life.sdk.helpers.r4

import com.google.common.truth.Truth.assertThat
import care.data4life.fhir.stu3.model.FhirDecimal
import org.junit.Assert.fail
import org.junit.Test
import java.math.BigDecimal

class DosageHelperTest {

    @Test
    fun buildWith_shouldReturnDosage_whenAllArgsProvided() {
        // When
        val dosage = DosageHelper.buildWith(
            DOSE,
            DOSE_UNIT,
            DOSE_TIMING
        )

        // Then
        assertThat(dosage.timing!!.repeat!!.`when`).hasSize(1)
        assertThat(dosage.timing!!.repeat!!.`when`!![0]).isEqualTo(DOSE_TIMING)
        assertThat(dosage.doseQuantity!!.value).isEqualTo(FhirDecimal(BigDecimal.valueOf(DOSE.toDouble())))
        assertThat(dosage.doseQuantity!!.unit).isEqualTo(DOSE_UNIT)
    }

    @Test
    fun builtWith_shouldThrow_whenDoseUnitNotProvided() {
        try {
            DosageHelper.buildWith(
                DOSE,
                null,
                DOSE_TIMING
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("unit is required")
        }

        try {
            DosageHelper.buildWith(
                DOSE,
                "",
                DOSE_TIMING
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("unit is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenDoseTimingNotProvided() {
        try {
            DosageHelper.buildWith(
                DOSE,
                DOSE_UNIT,
                null
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("when is required")
        }

        try {
            DosageHelper.buildWith(
                DOSE,
                DOSE_UNIT,
                ""
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("when is required")
        }

    }

    @Test
    fun getDosage_shouldReturnDosage() {
        // Given
        val dosage = DosageHelper.buildWith(
            DOSE,
            DOSE_UNIT,
            DOSE_TIMING
        )

        // When
        val dosageParts = DosageHelper.getDosage(dosage)

        // Then
        assertThat(dosageParts!!.first).isEqualTo(DOSE)
        assertThat(dosageParts.second).isEqualTo(DOSE_UNIT)
        assertThat(dosageParts.third).isEqualTo(DOSE_TIMING)
    }

    companion object {
        private val DOSE = 2f
        private val DOSE_UNIT = "Stueck"
        private val DOSE_TIMING = "Morgen"
    }
}
