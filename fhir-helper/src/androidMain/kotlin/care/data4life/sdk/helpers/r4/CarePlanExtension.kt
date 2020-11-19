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

@file:JvmName("CarePlanExtension")

package care.data4life.sdk.helpers.r4

import care.data4life.fhir.r4.model.*
import care.data4life.sdk.util.StringUtils
import java.util.*

fun CarePlan.getPatient(): Patient? = when {
    subject == null || subject.reference == null || contained == null || contained!!.isEmpty() -> null
    else -> FhirHelpers.getResourceById(contained, subject.reference, Patient::class.java)
}

fun CarePlan.getPractitioner(): Practitioner? = when {
    subject == null || author == null || author!!.reference == null || contained == null || contained!!.isEmpty() -> null
    else -> FhirHelpers.getResourceById(
        contained!!,
        author!!.reference!!,
        Practitioner::class.java
    )
}

fun CarePlan.getMedications() = when {
    contained == null || contained!!.isEmpty() -> null
    activity == null || activity!!.isEmpty() -> null
    else -> {
        val medications = ArrayList<MedicationRequest>()
        for (item in activity!!) {
            if (item.reference == null || StringUtils.isNullOrEmpty(item.reference!!.reference)) continue
            val medication =
                FhirHelpers.getResourceById(
                    contained!!,
                    item.reference!!.reference!!,
                    MedicationRequest::class.java
                )
            if (medication != null) medications.add(medication)
        }
        medications
    }
}


fun CarePlan.addAdditionalId(id: String) {
    identifier = FhirHelpers.appendIdentifier(id, identifier)
}

fun CarePlan.setAdditionalIds(ids: List<String>) {
    identifier = FhirHelpers.buildIdentifiers(ids)
}

fun CarePlan.getAdditionalIds(): List<Identifier>? {
    return FhirHelpers.extractIdentifiersForCurrentPartnerId(identifier)
}
