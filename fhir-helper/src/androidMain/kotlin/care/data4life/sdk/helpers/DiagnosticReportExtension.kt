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

@file:JvmName("DiagnosticReportExtension")

package care.data4life.sdk.helpers

import care.data4life.fhir.stu3.model.DiagnosticReport
import care.data4life.fhir.stu3.model.Identifier
import care.data4life.fhir.stu3.model.Observation

fun DiagnosticReport.getLaboratoryName(): String? {
    return performer?.first()?.actor?.display
}

fun DiagnosticReport.getObservations(): List<Observation>? {
    return result?.map {
        FhirHelpers.getResourceById(contained!!, it?.reference!!, Observation::class.java)
            ?: throw RuntimeException("Observation not found for id=$it.id")
    }
}

fun DiagnosticReport.addAdditionalId(id: String) {
    identifier = FhirHelpers.appendIdentifier(id, identifier)
}

fun DiagnosticReport.setAdditionalIds(ids: List<String>) {
    identifier = FhirHelpers.buildIdentifiers(ids)
}

fun DiagnosticReport.getAdditionalIds(): List<Identifier>? {
    return FhirHelpers.extractIdentifiersForCurrentPartnerId(identifier)
}
