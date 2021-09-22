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
import care.data4life.sdk.util.StringUtils

object CarePlanBuilder {

    @JvmStatic
    fun buildWith(
        patient: Patient,
        practitioner: Practitioner,
        medications: List<MedicationRequest>
    ): CarePlan {
        require(medications.isNotEmpty()) { "medications are required" }

        if (patient.id == null) patient.id = StringUtils.randomUUID()
        if (practitioner.id == null) practitioner.id = StringUtils.randomUUID()

        val emptyReference = Reference()
        val carePlan = CarePlan(CodeSystemRequestStatus.ACTIVE, CodeSystemRequestIntent.PLAN, emptyReference)

        val containedResources = arrayListOf<Resource>()
        medications.forEach {
            // set id if not existing
            if (it.id == null) it.id = StringUtils.randomUUID()

            containedResources.addAll(FhirHelpers.getAllContainedResources(it, true))
            it.contained = null
        }
        carePlan.contained = containedResources

        val medicationRefs = arrayListOf<Reference>()
        medications.forEach { medicationRefs.add(FhirHelpers.contain(carePlan, it)) }

        val activities = arrayListOf<CarePlan.CarePlanActivity>()
        var current: CarePlan.CarePlanActivity
        medicationRefs.forEach {
            current = CarePlan.CarePlanActivity()
            current.reference = it
            activities.add(current)
        }

        carePlan.activity = activities

        val patientRef = FhirHelpers.contain(carePlan, patient)
        carePlan.subject = patientRef
        val practitionerRef = FhirHelpers.contain(carePlan, practitioner)
        carePlan.author = practitionerRef

        return carePlan
    }
}
