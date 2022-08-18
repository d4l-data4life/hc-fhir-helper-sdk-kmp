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
import care.data4life.fhir.stu3.model.Annotation
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.StringUtils
import java.util.*

object MedicationRequestHelper {

    @JvmStatic
    fun buildWith(
        patient: Patient?,
        medication: Medication?,
        dosage: List<Dosage>?,
        note: String?,
        reason: String?
    ): MedicationRequest {
        Preconditions.checkArgument(patient != null, "patient is required")
        Preconditions.checkArgument(medication != null, "medication is required")
        Preconditions.checkArgument(dosage != null, "dosage is required")

        if (medication?.id == null) medication?.id = StringUtils.randomUUID()
        val medicationReference = Reference()
        medicationReference.reference = "#" + medication?.id!!

        if (patient!!.id == null) patient.id = StringUtils.randomUUID()
        val patientReference = Reference()
        patientReference.reference = "#" + patient.id!!

        val medicationRequest = MedicationRequest(
            CodeSystemMedicationRequestIntent.PLAN,
            medicationReference,
            patientReference
        )

        medicationRequest.contained = ArrayList()
        medicationRequest.contained!!.add(medication)

        medicationRequest.dosageInstruction = ArrayList()
        medicationRequest.dosageInstruction!!.addAll(dosage!!)

        if (!note.isNullOrEmpty()) {
            medicationRequest.note = mutableListOf()
            medicationRequest.note!!.add(Annotation(note))
        }

        if (!StringUtils.isNullOrEmpty(reason)) {
            medicationRequest.reasonCode = ArrayList()
            val reasonCode = FhirHelpers.buildWith(reason)
            medicationRequest.reasonCode!!.add(reasonCode)
        }

        return medicationRequest
    }

    @JvmStatic
    fun getMedication(medicationRequest: MedicationRequest?): Medication? {
        if (medicationRequest == null) {
            return null
        } else if (medicationRequest.contained == null || medicationRequest.contained!!.size != 1) {
            return null
        } else if (medicationRequest.medicationReference == null) {
            return null
        } else if (medicationRequest.medicationReference!!.reference == null) return null

        return FhirHelpers.getResourceById(
            medicationRequest.contained!!,
            medicationRequest.medicationReference!!.reference!!,
            Medication::class.java
        )
    }

    @JvmStatic
    fun getDosages(medicationRequest: MedicationRequest?): List<Dosage>? {
        if (medicationRequest == null) {
            return null
        } else if (medicationRequest.dosageInstruction == null || medicationRequest.dosageInstruction!!.isEmpty()) {
            return null
        }

        return medicationRequest.dosageInstruction
    }

    @JvmStatic
    fun getNote(medicationRequest: MedicationRequest?): String? {
        if (medicationRequest == null) {
            return null
        } else if (medicationRequest.note == null || medicationRequest.note!!.size != 1) return null

        return medicationRequest.note!![0].text
    }

    @JvmStatic
    fun getReason(medicationRequest: MedicationRequest?): String? {
        if (medicationRequest == null) {
            return null
        } else if (medicationRequest.reasonCode == null || medicationRequest.reasonCode!!.size != 1) {
            return null
        } else if (FhirHelpers.isCodingNullOrEmpty(medicationRequest.reasonCode!![0])) {
            return null
        } else if (medicationRequest.reasonCode!![0].coding!!.size != 1) return null

        return medicationRequest.reasonCode!![0].coding!![0].display
    }
}
