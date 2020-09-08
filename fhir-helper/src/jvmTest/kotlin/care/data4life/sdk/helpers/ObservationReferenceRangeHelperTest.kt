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
import care.data4life.fhir.stu3.model.CodeableConcept
import care.data4life.fhir.stu3.model.Coding
import care.data4life.fhir.stu3.model.FhirDecimal
import care.data4life.fhir.stu3.model.Quantity
import org.junit.Test
import java.math.BigDecimal

class ObservationReferenceRangeHelperTest {
    val bmiCode = "39156-5"
    val bmiDisplay = "Body mass index (BMI) [Ratio]"
    val bmiSystem = "http://loinc.org"
    val bmiText = "BMI"
    val bmiUnit = "kg/m2"
    val bmiValueSystem = "http://unitsofmeasure.org"
    val bmiValueCode = bmiUnit
    val lowValue = FhirDecimal(BigDecimal.valueOf(18.5))
    val highValue = FhirDecimal(BigDecimal.valueOf(25))

    @Test
    fun buildWithShouldReturnObservationReferenceRange() {
        // Given
        val bmiCoding = Coding().apply {
            code = bmiCode
            display = bmiDisplay
            system = bmiSystem
        }

        val bmiCodeable = CodeableConcept().apply {
            text = bmiText
            coding = mutableListOf(bmiCoding)
        }

        val lowBmi = Quantity().apply {
            value = lowValue
            unit = bmiUnit
            system = bmiValueSystem
            code = bmiValueCode
        }


        val highBmi = Quantity().apply {
            value = highValue
            unit = bmiUnit
            system = bmiValueSystem
            code = bmiValueCode
        }

        // When
        val bmiReferenceRange = ObservationReferenceRangeBuilder.buildWith(bmiCodeable, lowBmi, highBmi)

        // Then
        assertThat(bmiReferenceRange.type?.text).isEqualTo(bmiText)
        assertThat(bmiReferenceRange.type?.coding).hasSize(1)
        assertThat(bmiReferenceRange.type?.coding?.first()?.code).isEqualTo(bmiCode)
        assertThat(bmiReferenceRange.type?.coding?.first()?.display).isEqualTo(bmiDisplay)
        assertThat(bmiReferenceRange.type?.coding?.first()?.system).isEqualTo(bmiSystem)

        assertThat(bmiReferenceRange.low?.value).isEqualTo(lowValue)
        assertThat(bmiReferenceRange.low?.unit).isEqualTo(bmiUnit)
        assertThat(bmiReferenceRange.low?.system).isEqualTo(bmiValueSystem)
        assertThat(bmiReferenceRange.low?.code).isEqualTo(bmiValueCode)

        assertThat(bmiReferenceRange.high?.value).isEqualTo(highValue)
        assertThat(bmiReferenceRange.high?.unit).isEqualTo(bmiUnit)
        assertThat(bmiReferenceRange.high?.system).isEqualTo(bmiValueSystem)
        assertThat(bmiReferenceRange.high?.code).isEqualTo(bmiValueCode)

        assertThat(bmiReferenceRange.getLowValue()).isEqualTo(18.5f)
        assertThat(bmiReferenceRange.getHighValue()).isEqualTo(25f)
        assertThat(bmiReferenceRange.getUnit()).isEqualTo(bmiUnit)
    }
}
