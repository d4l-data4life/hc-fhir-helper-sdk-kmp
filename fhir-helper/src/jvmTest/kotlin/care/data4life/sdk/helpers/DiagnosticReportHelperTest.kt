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
import care.data4life.fhir.stu3.model.*
import care.data4life.fhir.stu3.util.FhirDateTimeParser
import org.junit.Before
import org.junit.Test

class DiagnosticReportHelperTest {
    private val reportTypeCode = "GHP"
    private val reportTypeDisplay = "General Health Profile"
    private val reportTypeSystem = "http://acme.com/labs/reports"
    private val reportStatus = CodeSystemDiagnosticReportStatus.FINAL
    private val laboratoryName = "Acme Laboratory, Inc"
    private val id = "id"
    private val startingCharacter = "#"

    private val ADDITIONAL_ID = "id"
    private val PARTNER_ID = "partner"

    @Before
    fun setUp() {
        FhirHelperConfig.init(PARTNER_ID)
    }

    @Test
    fun buildWithShouldReturnDiagnosticReport() {
        // Given
        val reportCoding = Coding().apply {
            code = reportTypeCode
            display = reportTypeDisplay
            system = reportTypeSystem
        }
        val reportCode = CodeableConcept()
        reportCode.coding = mutableListOf(reportCoding)

        val issuedDate = FhirDateTimeParser.parseInstant("2013-04-03T15:30:10+01:00")
        val observationA = Observation(CodeSystemObservationStatus.FINAL, CodeableConcept())
        val observationB = Observation(CodeSystemObservationStatus.FINAL, CodeableConcept())
        observationB.id = id

        // When
        val report = DiagnosticReportBuilder.buildWith(
            reportCode,
            reportStatus,
            laboratoryName,
            issuedDate,
            listOf(observationA, observationB)
        )

        // Then
        assertThat(report.code?.coding).hasSize(1)
        assertThat(report.code?.coding?.first()?.code).isEqualTo(reportTypeCode)
        assertThat(report.code?.coding?.first()?.display).isEqualTo(reportTypeDisplay)
        assertThat(report.code?.coding?.first()?.system).isEqualTo(reportTypeSystem)
        assertThat(report.status).isEqualTo(reportStatus)
        assertThat(report.performer).hasSize(1)
        assertThat(report.performer?.first()?.actor?.display).isEqualTo(laboratoryName)
        assertThat(report.issued).isEqualTo(issuedDate)
        assertThat(report.result).hasSize(2)
        assertThat(report.result?.get(0)?.reference?.startsWith(startingCharacter)).isTrue()
        assertThat(report.result?.get(1)?.reference).isEqualTo("$startingCharacter$id")
        assertThat(report.contained).containsExactly(observationA, observationB)
        assertThat(report.getLaboratoryName()).isEqualTo(laboratoryName)
        assertThat(report.getObservations()).containsExactly(observationA, observationB)
    }

    @Test
    fun addAdditionalIdShouldAddId() {
        // given
        val d = DiagnosticReport(CodeSystemDiagnosticReportStatus.FINAL, CodeableConcept())

        // when
        d.addAdditionalId(ADDITIONAL_ID)

        // then
        assertThat(d.identifier).hasSize(1)
        assertThat(d.identifier?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(d.identifier?.first()?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIdsShouldSetIds() {
        // given
        val d = DiagnosticReport(CodeSystemDiagnosticReportStatus.FINAL, CodeableConcept())
        val newIds = listOf(ADDITIONAL_ID, ADDITIONAL_ID)
        d.addAdditionalId("oldId")

        // when
        d.setAdditionalIds(newIds)

        // then
        assertThat(d.identifier).hasSize(2)
        assertThat(d.identifier?.get(0)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(d.identifier?.get(0)?.assigner?.reference).isEqualTo(PARTNER_ID)
        assertThat(d.identifier?.get(1)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(d.identifier?.get(1)?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIdsShouldReturnIds() {
        // given
        val d = DiagnosticReport( CodeSystemDiagnosticReportStatus.FINAL, CodeableConcept())

        d.addAdditionalId(ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        d.addAdditionalId(ADDITIONAL_ID)

        // when
        val ids = d.getAdditionalIds()

        // then
        assertThat(d.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids?.first()?.assigner?.reference).isEqualTo("newPartnerId")
    }
}
