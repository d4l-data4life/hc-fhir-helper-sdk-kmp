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

@file:JvmName("PractitionerExtension")

package care.data4life.sdk.helpers.stu3

import care.data4life.fhir.stu3.model.*

fun Practitioner.getFirstName(): String? {
    return name?.get(0)?.given?.get(0)
}

fun Practitioner.getLastName(): String? {
    return name?.get(0)?.family
}

fun Practitioner.getText(): String? {
    return name?.get(0)?.text
}

fun Practitioner.getPrefix(): String? {
    return name?.get(0)?.prefix?.get(0)
}

fun Practitioner.getSuffix(): String? {
    return name?.get(0)?.suffix?.get(0)
}

fun Practitioner.getStreet(): String? {
    return address?.first()?.line?.first()
}

fun Practitioner.getPostalCode(): String? {
    return address?.first()?.postalCode
}

fun Practitioner.getCity(): String? {
    return address?.first()?.city
}

fun Practitioner.getTelephone(): String? {
    return telecom?.first { it?.system == CodeSystemContactPointSystem.PHONE }?.value
}

fun Practitioner.getWebsite(): String? {
    return telecom?.first { it?.system == CodeSystemContactPointSystem.URL }?.value
}

fun Practitioner.addAdditionalId(id: String) {
    identifier = FhirHelpers.appendIdentifier(id, identifier)
}

fun Practitioner.setAdditionalIds(ids: List<String>) {
    identifier = FhirHelpers.buildIdentifiers(ids)
}

fun Practitioner.getAdditionalIds(): List<Identifier>? {
    return FhirHelpers.extractIdentifiersForCurrentPartnerId(identifier)
}
