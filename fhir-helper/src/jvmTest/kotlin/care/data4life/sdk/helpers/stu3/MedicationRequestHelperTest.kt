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
import care.data4life.sdk.util.StringUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class MedicationRequestHelperTest {

    private var patient: Patient? = null
    private var medication: Medication? = null
    private var dosage: Dosage? = null

    @Before
    fun setup() {
        patient = Patient()
        medication = Medication()
        dosage = Dosage()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun buildWith_shouldReturnMedRequest_whenAllArgsProvided() {
        // Given
        val MEDICATION_ID = "medicationId"
        val PATIENT_ID = "patientId"

        mockkObject(StringUtils)
        every { StringUtils.randomUUID() } returnsMany listOf(MEDICATION_ID, PATIENT_ID)

        // When
        val medRequest =
            MedicationRequestHelper.buildWith(
                patient, medication!!, Arrays.asList<Dosage>(dosage),
                NOTE,
                REASON
            )

        // Then
        assertThat(medRequest.intent).isEqualTo(CodeSystemMedicationRequestIntent.PLAN)
        assertThat(medRequest.medicationReference!!.reference).isEqualTo("#$MEDICATION_ID")
        assertThat(medRequest.subject.reference).isEqualTo("#$PATIENT_ID")
        assertThat(medRequest.contained).contains(medication)
        assertThat(medRequest.dosageInstruction).contains(dosage)
        assertThat(medRequest.note).hasSize(1)
        assertThat(medRequest.note!![0].text).isEqualTo(NOTE)
        assertThat(medRequest.reasonCode).hasSize(1)
        assertThat(medRequest.reasonCode!![0].coding).hasSize(1)
        assertThat(medRequest.reasonCode!![0].coding!![0].display).isEqualTo(REASON)
        unmockkStatic(StringUtils::class)
    }

    @Test
    fun builtWith_shouldThrow_whenPatientNotProvided() {
        try {
            MedicationRequestHelper.buildWith(
                null, medication!!, Arrays.asList<Dosage>(dosage),
                NOTE,
                REASON
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("patient is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenMedicationNotProvided() {
        try {
            MedicationRequestHelper.buildWith(
                patient, null, Arrays.asList<Dosage>(dosage),
                NOTE,
                REASON
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("medication is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenDosageNotProvided() {
        try {
            MedicationRequestHelper.buildWith(
                patient, medication, null,
                NOTE,
                REASON
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("dosage is required")
        }

    }

    @Test
    fun medicationId_shouldBePreserved_whenPresent() {
        // Given
        val MEDICATION_ID = "medicationId"
        medication!!.id = MEDICATION_ID

        // When
        val medRequest = buildMedRequest()

        // Then
        assertThat(medication!!.id).isEqualTo(MEDICATION_ID)
        assertThat(medRequest.medicationReference!!.reference).isEqualTo("#$MEDICATION_ID")
    }

    @Test
    fun medicationId_shouldBeGenerated_whenNotPresent() {
        // Given
        medication!!.id = null

        // When
        val medRequest = buildMedRequest()

        // Then
        assertThat(medication!!.id).isNotEmpty()
        assertThat(medRequest.medicationReference!!.reference).isEqualTo("#" + medication!!.id!!)
    }

    @Test
    fun patientId_shouldBePreserved_whenPresent() {
        // Given
        val PATIENT_ID = "patientId"
        patient!!.id = PATIENT_ID

        // When
        val medRequest = buildMedRequest()

        // Then
        assertThat(patient!!.id).isEqualTo(PATIENT_ID)
        assertThat(medRequest.subject.reference).isEqualTo("#$PATIENT_ID")
    }

    @Test
    fun patientId_shouldBeGenerated_whenNotPresent() {
        // Given
        patient!!.id = null

        // When
        val medRequest = buildMedRequest()

        // Then
        assertThat(patient!!.id).isNotEmpty()
        assertThat(medRequest.subject.reference).isEqualTo("#" + patient!!.id!!)
    }

    @Test
    fun note_shouldBeNull_whenNotProvided() {
        // When
        val medRequest =
            MedicationRequestHelper.buildWith(
                patient, medication!!, Arrays.asList<Dosage>(dosage), null,
                REASON
            )

        // Then
        assertThat(medRequest.note).isNull()
    }

    @Test
    fun reason_shouldBeNull_whenNotProvided() {
        // When
        val medRequest =
            MedicationRequestHelper.buildWith(
                patient, medication!!, Arrays.asList<Dosage>(dosage),
                NOTE, null
            )

        // Then
        assertThat(medRequest.reasonCode).isNull()
    }

    private fun buildMedRequest(): MedicationRequest {
        return MedicationRequestHelper.buildWith(
            patient, medication!!, Arrays.asList<Dosage>(dosage),
            NOTE,
            REASON
        )
    }

    @Test
    fun getMedication_shouldReturnMedication() {
        // Given
        val medRequest =
            MedicationRequestHelper.buildWith(
                patient, this.medication!!, Arrays.asList<Dosage>(dosage),
                NOTE,
                REASON
            )

        // When
        val medication = MedicationRequestHelper.getMedication(medRequest)

        // Then
        assertThat(medication).isEqualTo(this.medication)
    }

    @Test
    fun getDosages_shouldReturnDosages() {
        // Given
        val medRequest = MedicationRequestHelper.buildWith(
            patient,
            this.medication!!,
            Arrays.asList<Dosage>(this.dosage),
            NOTE,
            REASON
        )

        // When
        val dosages = MedicationRequestHelper.getDosages(medRequest)

        // Then
        assertThat(dosages).hasSize(1)
        assertThat(dosages!![0]).isEqualTo(this.dosage)
    }

    @Test
    fun getNote_shouldReturnNote() {
        // Given
        val medRequest = MedicationRequestHelper.buildWith(
            patient,
            this.medication!!,
            Arrays.asList<Dosage>(this.dosage),
            NOTE,
            REASON
        )

        // When
        val note = MedicationRequestHelper.getNote(medRequest)

        // Then
        assertThat(note).isEqualTo(NOTE)
    }

    @Test
    fun geReason_shouldReturnReason() {
        // Given
        val medRequest = MedicationRequestHelper.buildWith(
            patient,
            this.medication!!,
            Arrays.asList<Dosage>(this.dosage),
            NOTE,
            REASON
        )

        // When
        val reason = MedicationRequestHelper.getReason(medRequest)

        // Then
        assertThat(reason).isEqualTo(REASON)
    }

    companion object {
        private val NOTE = "zur Oralen Einnahme"
        private val REASON = "Erkaeltungsbeschwerden bekaempfe"
    }
}
