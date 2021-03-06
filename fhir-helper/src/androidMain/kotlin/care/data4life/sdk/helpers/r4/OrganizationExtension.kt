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

@file:JvmName("OrganizationExtension")

package care.data4life.sdk.helpers.r4

import care.data4life.fhir.r4.model.*

fun Organization.getType(): CodeableConcept? {
    return type?.first()
}

fun Organization.getStreet(): String? {
    return address?.first()?.line?.first()
}

fun Organization.getPostalCode(): String? {
    return address?.first()?.postalCode
}

fun Organization.getCity(): String? {
    return address?.first()?.city
}

fun Organization.getTelephone(): String? {
    return telecom?.first { it?.system == CodeSystemContactPointSystem.PHONE }?.value
}

fun Organization.getWebsite(): String? {
    return telecom?.first { it?.system == CodeSystemContactPointSystem.URL }?.value
}

fun Organization.addAdditionalId(id: String) {
    identifier = FhirHelpers.appendIdentifier(id, identifier)
}

fun Organization.setAdditionalIds(ids: List<String>) {
    identifier = FhirHelpers.buildIdentifiers(ids)
}

fun Organization.getAdditionalIds(): List<Identifier>? {
    return FhirHelpers.extractIdentifiersForCurrentPartnerId(identifier)
}
