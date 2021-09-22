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

import care.data4life.fhir.r4.FhirR4Parser
import care.data4life.fhir.r4.model.*
import care.data4life.sdk.test.util.FileHelper
import care.data4life.sdk.util.StringUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.fail

class CarePlanBuilderTest {

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
            CodeSystemMedicationrequestStatus.ACTIVE,
            CodeSystemMedicationRequestIntent.PLAN,
            mockk(),
            mockk()
        )
        dummyMedRequest.contained = ArrayList<Resource>()
        dummyMedRequest.contained?.add(dummyMedication)
        dummyRef = Reference()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    @Throws(Exception::class)
    @Ignore("Ignored due to resources not bundled")
    fun buildWith_shouldConformToCarePlanJsonSpecification() {
        // Given
        mockkStatic(StringUtils::class)
        every { StringUtils.randomUUID() } returnsMany
            listOf(
                "9EAC9107-9242-4179-B7C7-226E1B1C109D", // substanceId
                "8360DBFA-2537-4F9F-B6A0-DCFFE5FF1E39", // medicationId
                "2A96CAD7-DA31-418D-B4BD-18C52AAEB577", // patientId
                "1090A188-DDBD-49FE-88DE-5455E51C3612", // practitionerId
                "0D24DF43-C228-4FE3-871C-5E55A31EE174"
            ) // medRequestId

        val carePlanJsonSpec = FileHelper.loadString("careplan-inline.json")
        val parser = FhirR4Parser()
        val patient = PatientHelper.buildWith("John", "Doe")
        val practitioner = PractitionerBuilder.buildWith("Dr. Bruce Banner, Praxis fuer Allgemeinmedizin")
        val medication = MedicationHelper.buildWith("Ibuprofen-ratiopharm", "tablets")
        MedicationHelper.addIngredient(medication, "Ibuprofen", 40f, "mg")
        val morningDosage = DosageHelper.buildWith(2f, "Stueck", "morning")
        val eveningDosage = DosageHelper.buildWith(2f, "Stueck", "evening")
        val medicationRequest = MedicationRequestHelper.buildWith(
            patient,
            medication,
            Arrays.asList(morningDosage, eveningDosage),
            "zur Oralen Einnahme",
            "Erkaeltungsbeschwerden bekaempfen",
            CodeSystemMedicationrequestStatus.ACTIVE
        )

        // When
        val carePlan = CarePlanBuilder.buildWith(patient, practitioner, Arrays.asList(medicationRequest))

        // Then
        JSONAssert.assertEquals(parser.fromFhir(carePlan), carePlanJsonSpec, false)
    }

    @Test
    fun buildWith_shouldReturnCarePlan_whenAllArgsProvided() {
        // Given
        val PATIENT_ID = "patientId"
        val PRACTITIONER_ID = "practitionerId"
        val MEDICATION_REQ_ID = "medReqId"

        mockkStatic(StringUtils::class)
        mockkStatic(FhirHelpers::class)

        every { StringUtils.randomUUID() } returnsMany listOf(
            PATIENT_ID, PRACTITIONER_ID, MEDICATION_REQ_ID
        )

        every { FhirHelpers.getAllContainedResources(dummyMedRequest, true) } returns listOf<Resource>(
            dummySubstance,
            dummyMedication
        )

        every { FhirHelpers.contain(any(), any()) } answers {
            val d = this.args[0] as DomainResource
            val r = this.args[1] as Resource
            if (d.contained == null) d.contained = ArrayList()
            d.contained?.add(r)
            dummyRef!!
        }

        // When
        val carePlan = CarePlanBuilder.buildWith(dummyPatient, dummyPractitioner, listOf(dummyMedRequest))

        // Then
        assertThat(dummyPatient.id).isEqualTo(PATIENT_ID)
        assertThat(dummyPractitioner.id).isEqualTo(PRACTITIONER_ID)
        assertThat(dummyMedRequest.id).isEqualTo(MEDICATION_REQ_ID)
        assertThat(carePlan.intent).isEqualTo(CodeSystemRequestIntent.PLAN)
        assertThat(carePlan.status).isEqualTo(CodeSystemRequestStatus.ACTIVE)
        assertThat(carePlan.activity).hasSize(1)
        assertThat(carePlan.activity!![0].reference).isEqualTo(dummyRef)
        assertThat(carePlan.subject).isEqualTo(dummyRef)
        assertThat(carePlan.author!!).isEqualTo(dummyRef)
        assertThat(carePlan.contained).containsExactlyElementsIn(
            Arrays.asList(
                dummySubstance,
                dummyMedication,
                dummyMedRequest,
                dummyPatient,
                dummyPractitioner
            )
        ).inOrder()
    }

    @Test
    fun builtWith_shouldThrow_whenMedicationsNotProvided() {
        try {
            CarePlanBuilder.buildWith(Patient(), Practitioner(), ArrayList())
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medications are required")
        }
    }

    @Test
    fun patientId_shouldBePreserved_whenPresent() {
        // Given
        val patientId = "patientId"
        val dummyPatient = Patient()
        dummyPatient.id = patientId

        // When
        val carePlan = CarePlanBuilder.buildWith(
            dummyPatient,
            Practitioner(),
            Arrays.asList(
                MedicationRequest(
                    CodeSystemMedicationrequestStatus.ACTIVE,
                    CodeSystemMedicationRequestIntent.PLAN,
                    mockk(),
                    mockk()
                )
            )
        )

        // Then
        assertThat(carePlan.getPatient()?.id).isEqualTo(patientId)
    }

    @Test
    fun practitionerId_shouldBePreserved_whenPresent() {
        // Given
        val PRACTITIONER_ID = "practitionerId"
        val dummyPractitioner = Practitioner()
        dummyPractitioner.id = PRACTITIONER_ID

        // When
        CarePlanBuilder.buildWith(
            Patient(),
            dummyPractitioner,
            Arrays.asList(
                MedicationRequest(
                    CodeSystemMedicationrequestStatus.ACTIVE,
                    CodeSystemMedicationRequestIntent.PLAN,
                    mockk(),
                    mockk()
                )
            )
        )

        // Then
        assertThat(dummyPractitioner.id).isEqualTo(PRACTITIONER_ID)
    }

    @Test
    fun medRequestIds_shouldBePreserved_whenPresent() {
        // Given
        val MEDICATION_REQ_ID = "medicationReqId"
        val dummyMedRequest = MedicationRequest(
            CodeSystemMedicationrequestStatus.ACTIVE,
            CodeSystemMedicationRequestIntent.PLAN,
            mockk(),
            mockk()
        )
        dummyMedRequest.id = MEDICATION_REQ_ID

        // When
        CarePlanBuilder.buildWith(
            Patient(),
            Practitioner(),
            Arrays.asList(dummyMedRequest)
        )

        // Then
        assertThat(dummyMedRequest.id).isEqualTo(MEDICATION_REQ_ID)
    }
}
