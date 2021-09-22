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
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.fail

class CarePlanExtensionTest {

    private lateinit var dummyPatient: Patient
    private lateinit var dummyPractitioner: Practitioner
    private lateinit var dummyMedication: Medication
    private lateinit var dummySubstance: Substance
    private lateinit var dummyMedRequest: MedicationRequest
    private var dummyRef: Reference? = null

    @Before
    fun setup() {
        dummyPatient = Patient()
        dummyPractitioner = Practitioner()
        dummyMedication = Medication()
        dummySubstance = mockk()
        dummyMedication.contained = arrayListOf<Resource>()
        dummyMedication.contained?.add(dummySubstance)
        dummyMedRequest = MedicationRequest(
            CodeSystemMedicationRequestIntent.PLAN,
            mockk(),
            mockk()
        )
        dummyMedRequest.contained = ArrayList<Resource>()
        dummyMedRequest.contained?.add(dummyMedication)
        dummyRef = Reference()
    }

    @Test
    fun getPatient_shouldReturnPatient() {
        // Given
        val carePlan = CarePlanBuilder.buildWith(dummyPatient, dummyPractitioner, Arrays.asList(dummyMedRequest))

        // When
        val patient = carePlan.getPatient()

        // Then
        assertThat(patient).isEqualTo(dummyPatient)
    }

    @Test
    fun getPractitioner_shouldReturnPractitioner() {
        // Given
        val carePlan = CarePlanBuilder.buildWith(dummyPatient, dummyPractitioner, Arrays.asList(dummyMedRequest))

        // When
        val practitioner = carePlan.getPractitioner()

        // Then
        assertThat(practitioner).isEqualTo(dummyPractitioner)
    }

    @Test
    fun getMedications_shouldReturnMedications() {
        // Given
        val carePlan = CarePlanBuilder.buildWith(dummyPatient, dummyPractitioner, listOf(dummyMedRequest))

        // When
        val medications = carePlan.getMedications()

        // Then
        assertThat(medications).isEqualTo(Arrays.asList<MedicationRequest>(dummyMedRequest))
    }

    @Test
    fun addAdditionalId_shouldAddId() {
        // given
        FhirHelperConfig.init(PARTNER_ID)
        val c = CarePlan(
            CodeSystemCarePlanStatus.ACTIVE,
            CodeSystemCarePlanIntent.PLAN,
            mockk()
        )

        // when
        c.addAdditionalId(ADDITIONAL_ID)

        // then
        assertThat(c.identifier).hasSize(1)
        assertThat(c.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(c.identifier!![0].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIds_shouldSetIds() {
        // given
        FhirHelperConfig.init(PARTNER_ID)
        val c = CarePlan(
            CodeSystemCarePlanStatus.ACTIVE,
            CodeSystemCarePlanIntent.PLAN,
            mockk()
        )
        val newIds = Arrays.asList<String>(
            ADDITIONAL_ID,
            ADDITIONAL_ID
        )
        c.addAdditionalId("oldId")

        // when
        c.setAdditionalIds(newIds)

        // then
        assertThat(c.identifier).hasSize(2)
        assertThat(c.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(c.identifier!![0].assigner?.reference).isEqualTo(PARTNER_ID)
        assertThat(c.identifier!![1].value).isEqualTo(ADDITIONAL_ID)
        assertThat(c.identifier!![1].assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIds_shouldReturnIds() {
        // given
        val c = CarePlan(
            CodeSystemCarePlanStatus.ACTIVE,
            CodeSystemCarePlanIntent.PLAN,
            mockk()
        )
        FhirHelperConfig.init(PARTNER_ID)
        c.addAdditionalId(ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        c.addAdditionalId(ADDITIONAL_ID)

        // when
        val ids = c.getAdditionalIds()

        // then
        assertThat(c.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids[0].assigner?.reference).isEqualTo("newPartnerId")
    }

    @Test
    fun addAdditionalId_shouldThrow_whenCarePlanNotProvided() {
        FhirHelperConfig.init(null)
        val c = CarePlan(
            CodeSystemCarePlanStatus.ACTIVE,
            CodeSystemCarePlanIntent.PLAN,
            mockk()
        )

        try {
            // when
            c.addAdditionalId(ADDITIONAL_ID)
            fail("Exception expected")
        } catch (e: IllegalStateException) {

            // then
            assertThat(e.message).isEqualTo("FhirHelperConfig not initialized.")
        }
    }

    @Test
    fun addAdditionalId_shouldThrow_whenIdNotProvided() {
        // given
        val c = CarePlan(
            CodeSystemCarePlanStatus.ACTIVE,
            CodeSystemCarePlanIntent.PLAN,
            mockk()
        )

        try {
            // when
            c.addAdditionalId("")
            fail("Exception expected")
        } catch (e: IllegalStateException) {

            // then
            assertThat(e.message).isEqualTo("appendingId is required")
        }
    }

    companion object {
        private const val ADDITIONAL_ID = "id"
        private const val PARTNER_ID = "partnerId"
    }
}
