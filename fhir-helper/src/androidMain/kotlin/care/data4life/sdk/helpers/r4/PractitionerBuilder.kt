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

import care.data4life.fhir.r4.model.*

object PractitionerBuilder {

    @JvmStatic
    fun buildWith(firstName: String, lastName: String): Practitioner {
        require(firstName.isNotEmpty()) { "firstName is required" }
        require(lastName.isNotEmpty()) { "lastName is required" }

        val humanName = HumanName().apply {
            given = mutableListOf(firstName)
            family = lastName
        }
        return buildWith(humanName)
    }

    @JvmStatic
    fun buildWith(text: String): Practitioner {
        require(text.isNotEmpty()) { "text is required" }

        val humanName = HumanName()
        humanName.text = text
        return buildWith(humanName)
    }

    @JvmStatic
    fun buildWith(
        firstName: String,
        lastName: String,
        prefix: String? = null,
        suffix: String? = null,
        street: String? = null,
        postalCode: String? = null,
        city: String? = null,
        telephone: String? = null,
        website: String? = null
    ): Practitioner {

        val practitioner = buildWith(firstName, lastName)
        val humanName = practitioner.name?.firstOrNull()
        if (humanName != null) {
            humanName.prefix = prefix?.let { mutableListOf(it) }
            humanName.suffix = suffix?.let { mutableListOf(it) }
        }

        if (street != null || postalCode != null || city != null) {
            val address = Address()
            address.line = street?.let { mutableListOf(street) }
            address.postalCode = postalCode
            address.city = city
            practitioner.address = mutableListOf(address)
        }

        if (telephone != null || website != null) practitioner.telecom = mutableListOf()
        telephone?.let {
            val tel = ContactPoint().apply {
                system = CodeSystemContactPointSystem.PHONE
                value = telephone
            }
            practitioner.telecom?.add(tel)
        }
        website?.let {
            val web = ContactPoint().apply {
                system = CodeSystemContactPointSystem.URL
                value = website
            }
            practitioner.telecom?.add(web)
        }

        return practitioner
    }

    private fun buildWith(humanName: HumanName): Practitioner {
        val practitioner = Practitioner()
        practitioner.name = mutableListOf(humanName)
        return practitioner
    }
}
