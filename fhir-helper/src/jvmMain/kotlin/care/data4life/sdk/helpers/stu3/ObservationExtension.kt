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

@file:JvmName("ObservationExtension")

package care.data4life.sdk.helpers.stu3

import care.data4life.fhir.stu3.model.*

fun Observation.getObservationType(): CodeableConcept? = code

fun Observation.getStatus(): CodeSystemObservationStatus? = status

fun Observation.getIssuedDate(): FhirInstant? = issued

fun Observation.getEffectiveDate(): FhirDateTime? = effectiveDateTime

fun Observation.getValue(): Float? = valueQuantity?.value?.decimal?.toFloat()

fun Observation.getUnit(): String? = valueQuantity?.unit

fun Observation.getObservationText(): String? = valueString

fun Observation.getObservationCategory(): List<CodeableConcept?>? = category

fun Observation.getObservationRanges(): List<Observation.ObservationReferenceRange?>? = referenceRange

fun Observation.addAdditionalId(id: String) {
    identifier = FhirHelpers.appendIdentifier(id, identifier)
}

fun Observation.setAdditionalIds(ids: List<String>) {
    identifier = FhirHelpers.buildIdentifiers(ids)
}

fun Observation.getAdditionalIds(): List<Identifier>? {
    return FhirHelpers.extractIdentifiersForCurrentPartnerId(identifier)
}
