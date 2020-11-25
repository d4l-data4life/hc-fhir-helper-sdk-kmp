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
import care.data4life.fhir.r4.model.Patient
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class PatientHelperTest {

    @Before
    fun setUp() {
        FhirHelperConfig.init(PARTNER_ID)
    }


    @Test
    fun buildWith_shouldReturnPatient_whenAllArgsProvided() {
        // When
        val patient = PatientHelper.buildWith(
            PATIENT_NAME,
            PATIENT_SURNAME
        )

        // Then
        assertThat(patient.name).hasSize(1)
        assertThat(patient.name!![0].given).hasSize(1)
        assertThat(patient.name!![0].given!![0]).isEqualTo(PATIENT_NAME)
        assertThat(patient.name!![0].family).isEqualTo(PATIENT_SURNAME)
    }

    @Test
    fun builtWith_shouldThrow_whenNameNotProvided() {
        try {
            PatientHelper.buildWith(null, PATIENT_SURNAME)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("firstName is required")
        }

        try {
            PatientHelper.buildWith("", PATIENT_SURNAME)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("firstName is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenSurnameNotProvided() {
        try {
            PatientHelper.buildWith(PATIENT_NAME, null)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("lastName is required")
        }

        try {
            PatientHelper.buildWith(PATIENT_NAME, "")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("lastName is required")
        }

    }

    @Test
    fun getFirstName_shouldReturnFirstName() {
        // Given
        val patient = PatientHelper.buildWith(
            PATIENT_NAME,
            PATIENT_SURNAME
        )

        // When
        val firstName = PatientHelper.getFirstName(patient)

        // Then
        assertThat(firstName).isEqualTo(PATIENT_NAME)
    }

    @Test
    fun getLastName_shouldReturnLastName() {
        // Given
        val patient = PatientHelper.buildWith(
            PATIENT_NAME,
            PATIENT_SURNAME
        )

        // When
        val lastName = PatientHelper.getLastName(patient)

        // Then
        assertThat(lastName).isEqualTo(PATIENT_SURNAME)
    }

    @Test
    fun addAdditionalId_shouldAddId() {
        // given
        val p = Patient()

        // when
        PatientHelper.addAdditionalId(p, ADDITIONAL_ID)

        // then
        assertThat(p.identifier).hasSize(1)
        assertThat(p.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![0].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIds_shouldSetIds() {
        // given
        val p = Patient()
        val newIds = Arrays.asList(
            ADDITIONAL_ID,
            ADDITIONAL_ID
        )
        PatientHelper.addAdditionalId(p, "oldId")

        // when
        PatientHelper.setAdditionalIds(p, newIds)

        // then
        assertThat(p.identifier).hasSize(2)
        assertThat(p.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![0].assigner!!.reference).isEqualTo(PARTNER_ID)
        assertThat(p.identifier!![1].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![1].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIds_shouldReturnIds() {
        // given
        val p = Patient()
        PatientHelper.addAdditionalId(p, ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        PatientHelper.addAdditionalId(p, ADDITIONAL_ID)

        // when
        val ids = PatientHelper.getAdditionalIds(p)

        // then
        assertThat(p.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids[0].assigner!!.reference).isEqualTo("newPartnerId")
    }

    @Test
    fun addAdditionalId_shouldThrow_whenPatientNotProvided() {
        try {
            // when
            PatientHelper.addAdditionalId(null, ADDITIONAL_ID)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {

            //then
            assertThat(e.message).isEqualTo("patient is required")
        }

    }

    @Test
    fun addAdditionalId_shouldThrow_whenIdNotProvided() {
        // given
        val patient = Patient()

        try {
            // when
            PatientHelper.addAdditionalId(patient, null)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {

            // then
            assertThat(e.message).isEqualTo("id is required")
        }

        try {
            // when
            PatientHelper.addAdditionalId(patient, "")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {

            // then
            assertThat(e.message).isEqualTo("id is required")
        }

    }

    @Test
    fun getAdditionalIds_shouldThrow_whenPatientNotProvided() {
        try {
            // when
            PatientHelper.getAdditionalIds(null)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {

            //then
            assertThat(e.message).isEqualTo("patient is required")
        }

    }

    companion object {
        private val PATIENT_NAME = "John"
        private val PATIENT_SURNAME = "Doe"

        private val ADDITIONAL_ID = "id"
        private val PARTNER_ID = "partnerId"
    }
}
