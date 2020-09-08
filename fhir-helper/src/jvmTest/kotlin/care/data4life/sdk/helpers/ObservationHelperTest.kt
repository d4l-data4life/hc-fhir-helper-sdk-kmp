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

import care.data4life.fhir.stu3.model.CodeSystems
import care.data4life.fhir.stu3.model.CodeableConcept
import care.data4life.fhir.stu3.model.Coding
import care.data4life.fhir.stu3.model.Observation
import care.data4life.fhir.stu3.util.FhirDateTimeParser
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ObservationHelperTest {
    val categoryCode = "vital-signs"
    val categoryDisplay = "Vital Signs"
    val categorySystem = "http://hl7.org/fhir/observation-category"
    val categoryText = "Vital Signs"
    val observationTypeCode = "9279-1"
    val observationTypeDisplay = "Respiratory rate"
    val observationTypeSystem = "http://loinc.org"
    val observationTypeText = "Respiratory rate"
    val observationUnit = "breaths/minute"
    val observationValue = 26f
    val observationStatus = CodeSystems.ObservationStatus.FINAL

    val ADDITIONAL_ID = "id"
    val PARTNER_ID = "partner"

    @Before
    fun setUp() {
        FhirHelperConfig.init(PARTNER_ID)
    }

    @Test
    fun buildWithShouldReturnObservation() {
        // Given
        val observationCoding = Coding().apply {
            code = observationTypeCode
            display = observationTypeDisplay
            system = observationTypeSystem
        }
        val observationCode = CodeableConcept().apply {
            text = observationTypeText
            coding = mutableListOf(observationCoding)
        }


        val categoryCoding = Coding().apply {
            code = categoryCode
            display = categoryDisplay
            system = categorySystem
        }
        val categoryCodeable = CodeableConcept().apply {
            text = categoryText
            coding = mutableListOf(categoryCoding)
        }

        val issuedDate = FhirDateTimeParser.parseInstant("2013-04-03T15:30:10+01:00")
        val effectiveDate = FhirDateTimeParser.parseDateTime("2013-04-03")

        // When
        val observation = ObservationBuilder.buildWith(
            observationCode,
            observationValue,
            observationUnit,
            observationStatus,
            issuedDate,
            effectiveDate,
            categoryCodeable
        )

        // Then
        assertThat(observation.code?.text).isEqualTo(observationTypeText)
        assertThat(observation.code?.coding).hasSize(1)
        assertThat(observation.code?.coding?.first()?.code).isEqualTo(observationTypeCode)
        assertThat(observation.code?.coding?.first()?.display).isEqualTo(observationTypeDisplay)
        assertThat(observation.code?.coding?.first()?.system).isEqualTo(observationTypeSystem)

        assertThat(observation.valueQuantity?.value?.decimal?.toFloat()).isEqualTo(observationValue)
        assertThat(observation.valueQuantity?.unit).isEqualTo(observationUnit)
        assertThat(observation.status).isEqualTo(observationStatus)
        assertThat(observation.issued).isEqualTo(issuedDate)
        assertThat(observation.effectiveDateTime).isEqualTo(effectiveDate)

        assertThat(observation.category).hasSize(1)
        assertThat(observation.category?.first()?.text).isEqualTo(categoryText)
        assertThat(observation.category?.first()?.coding).hasSize(1)
        assertThat(observation.category?.first()?.coding?.first()?.code).isEqualTo(categoryCode)
        assertThat(observation.category?.first()?.coding?.first()?.display).isEqualTo(categoryDisplay)
        assertThat(observation.category?.first()?.coding?.first()?.system).isEqualTo(categorySystem)

        assertThat(observation.getObservationType()).isEqualTo(observationCode)
        assertThat(observation.getIssuedDate()).isEqualTo(issuedDate)
        assertThat(observation.getEffectiveDate()).isEqualTo(effectiveDate)
        assertThat(observation.getValue()).isEqualTo(observationValue)
        assertThat(observation.getUnit()).isEqualTo(observationUnit)
        assertThat(observation.getStatus()).isEqualTo(observationStatus)
        assertThat(observation.getObservationCategory()).hasSize(1)
        assertThat(observation.getObservationCategory()?.first()).isEqualTo(categoryCodeable)
        assertThat(observation.getObservationText()).isEqualTo(null)
        assertThat(observation.getObservationRanges()).isEqualTo(null)
    }

    @Test
    fun addAdditionalIdShouldAddId() {
        val o = Observation(mockk(), mockk())

        // when
        o.addAdditionalId(ADDITIONAL_ID)

        // then
        assertThat(o.identifier).hasSize(1)
        assertThat(o.identifier?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.first()?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIdsShouldSetIds() {
        // given
        val o = Observation(mockk(), mockk())
        val newIds = listOf(ADDITIONAL_ID, ADDITIONAL_ID)
        o.addAdditionalId("oldId")

        // when
        o.setAdditionalIds(newIds)

        // then
        assertThat(o.identifier).hasSize(2)
        assertThat(o.identifier?.get(0)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.get(0)?.assigner?.reference).isEqualTo(PARTNER_ID)
        assertThat(o.identifier?.get(1)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.get(1)?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIdsShouldReturnIds() {
        // given
        val o = Observation(mockk(), mockk())
        o.addAdditionalId(ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        o.addAdditionalId(ADDITIONAL_ID)

        // when
        val ids = o.getAdditionalIds()

        // then
        assertThat(o.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids?.first()?.assigner?.reference).isEqualTo("newPartnerId")
    }
}
