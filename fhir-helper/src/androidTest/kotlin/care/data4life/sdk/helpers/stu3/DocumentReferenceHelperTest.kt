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

import care.data4life.fhir.stu3.model.*
import care.data4life.fhir.stu3.util.FhirDateTimeParser
import care.data4life.sdk.helpers.lang.DataRestrictionException
import care.data4life.sdk.lang.D4LException
import care.data4life.sdk.util.Base64
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DocumentReferenceHelperTest {
    private val title = "Physical"
    private val indexed: FhirInstant = FhirDateTimeParser.parseInstant("2013-04-03T15:30:10+01:00")
    private val status = CodeSystemDocumentReferenceStatus.CURRENT
    private val documentCode = "34108-1"
    private val documentDisplay = "Outpatient Note"
    private val documentSystem = "http://loinc.org"
    private val practiceSpecialityCode = "General Medicine"
    private val practiceSpecialityDisplay = "General Medicine"
    private val practiceSpecialitySystem = "http://www.ihe.net/xds/connectathon/practiceSettingCodes"
    private val startingChar = "#"

    private val ADDITIONAL_ID = "id"
    private val PARTNER_ID = "partner"

    @Before
    fun setup() {
        FhirHelperConfig.init(PARTNER_ID)
    }

    @Test
    fun buildWithShouldReturnDocumentReference() {
        // Given
        val attachment = Attachment()
        val pdf = byteArrayOf(0x25, 0x50, 0x44, 0x46, 0x2d)
        attachment.data = Base64.encodeToString(pdf)
        val author = Practitioner()
        val docTypeCoding = Coding().apply {
            code = documentCode
            display = documentDisplay
            system = documentSystem
        }
        val docType = CodeableConcept()
        docType.coding = listOf(docTypeCoding)

        val practiceSpecialityCoding = Coding().apply {
            code = practiceSpecialityCode
            display = practiceSpecialityDisplay
            system = practiceSpecialitySystem
        }
        val practiceSpeciality = CodeableConcept()
        practiceSpeciality.coding = listOf(practiceSpecialityCoding)

        // When
        val document = DocumentReferenceBuilder.buildWith(
            title,
            indexed,
            status,
            listOf(attachment),
            docType,
            author,
            practiceSpeciality
        )

        // Then
        assertThat(document.description).isEqualTo(title)
        assertThat(document.author).hasSize(1)
        assertThat(document.author?.first()?.reference).startsWith(startingChar)
        assertThat(document.status).isEqualTo(status)
        assertThat(document.type).isEqualTo(docType)
        assertThat(document.type?.coding).hasSize(1)
        assertThat(document.type?.coding?.first()?.code).isEqualTo(documentCode)
        assertThat(document.type?.coding?.first()?.display).isEqualTo(documentDisplay)
        assertThat(document.type?.coding?.first()?.system).isEqualTo(documentSystem)
        assertThat(document.indexed).isEqualTo(indexed)
        assertThat(document.content).hasSize(1)
        assertThat(document.content?.first()?.attachment).isEqualTo(attachment)
        assertThat(document.context?.practiceSetting).isEqualTo(practiceSpeciality)
        assertThat(document.context?.practiceSetting?.coding).hasSize(1)
        assertThat(document.context?.practiceSetting?.coding?.first()?.code).isEqualTo(practiceSpecialityCode)
        assertThat(document.context?.practiceSetting?.coding?.first()?.display).isEqualTo(practiceSpecialityDisplay)
        assertThat(document.context?.practiceSetting?.coding?.first()?.system).isEqualTo(practiceSpecialitySystem)
        assertThat(document.getTitle()).isEqualTo(title)
        assertThat(document.getAttachments()).containsExactly(attachment)
        assertThat(document.getPractitioner()).isEqualTo(author)
        assertThat(document.getOrganization()).isEqualTo(null)
        assertThat(document.getPracticeSpeciality()).isEqualTo(practiceSpeciality)
    }

    @Test
    fun buildWithShouldThrowForUnsupportedFileType() {
        // given
        val attachment = Attachment()
        val unsupportedData = byteArrayOf(0x00, 0x00)
        attachment.data = Base64.encodeToString(unsupportedData)

        // when
        try {
            DocumentReferenceBuilder.buildWith(
                title,
                indexed,
                status,
                listOf(attachment),
                CodeableConcept(),
                Practitioner(),
                CodeableConcept()
            )
        } catch (ex: D4LException) {
            // then
            assertThat(ex).isInstanceOf(DataRestrictionException.UnsupportedFileType::class.java)
            assertThat(ex.message).isEqualTo("Only this file types are supported: JPEG, PNG, TIFF, PDF and DCM!")
        }
    }

    @Test
    fun addAdditionalIdShouldAddId() {
        // given
        val d = DocumentReference(mockk(), mockk(), mockk(), mockk())

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
        val d = DocumentReference(mockk(), mockk(), mockk(), mockk())
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
    fun setAdditionalIdsShouldReturnIds() {
        // given
        val d = DocumentReference(mockk(), mockk(), mockk(), mockk())
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
