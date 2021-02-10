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

import care.data4life.fhir.stu3.model.HumanName
import care.data4life.fhir.stu3.model.Identifier
import care.data4life.fhir.stu3.model.Patient
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.StringUtils
import java.util.*

object PatientHelper {

    @JvmStatic
    fun buildWith(firstName: String?, lastName: String?): Patient {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(firstName), "firstName is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(lastName), "lastName is required")

        val humanName = HumanName()
        humanName.given = Arrays.asList(firstName)
        humanName.family = lastName

        val patient = Patient()
        patient.name = Arrays.asList(humanName)
        return patient
    }

    @JvmStatic
    fun getFirstName(patient: Patient?): String? {
        if (patient == null)
            return null
        else if (patient.name == null)
            return null
        else if (patient.name!!.size != 1)
            return null
        else if (patient.name!![0].given!!.size != 1) return null

        return patient.name!![0].given!![0]
    }

    @JvmStatic
    fun getLastName(patient: Patient?): String? {
        if (patient == null)
            return null
        else if (patient.name == null)
            return null
        else if (patient.name!!.size != 1) return null

        return patient.name!![0].family
    }

    @JvmStatic
    fun addAdditionalId(patient: Patient?, id: String?) {
        Preconditions.checkArgument(patient != null, "patient is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(id), "id is required")

        patient!!.identifier = FhirHelpers.appendIdentifier(id!!, patient.identifier)
    }

    @JvmStatic
    fun setAdditionalIds(patient: Patient?, ids: List<String>?) {
        Preconditions.checkArgument(patient != null, "patient is required")

        patient!!.identifier = FhirHelpers.buildIdentifiers(ids)
    }

    @JvmStatic
    fun getAdditionalIds(patient: Patient?): List<Identifier>? {
        Preconditions.checkArgument(patient != null, "patient is required")

        return FhirHelpers.extractIdentifiersForCurrentPartnerId(patient!!.identifier)
    }
}
