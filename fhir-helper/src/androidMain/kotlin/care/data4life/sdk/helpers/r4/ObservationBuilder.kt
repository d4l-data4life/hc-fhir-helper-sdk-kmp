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

import care.data4life.fhir.stu3.model.*

object ObservationBuilder {

    @JvmStatic
    fun buildWith(
            type: CodeableConcept,
            value: Quantity,
            status: CodeSystemObservationStatus,
            issuedDate: FhirInstant,
            effectiveDate: FhirDateTime?,
            category: CodeableConcept? = null,
            ranges: List<Observation.ObservationReferenceRange>? = null): Observation {

        return build(
            type,
            status,
            issuedDate,
            effectiveDate,
            value = value,
            category = category,
            ranges = ranges
        )
    }

    @JvmStatic
    fun buildWith(
            type: CodeableConcept,
            observationValue: Float,
            observationUnit: String,
            status: CodeSystemObservationStatus,
            issuedDate: FhirInstant,
            effectiveDate: FhirDateTime?,
            category: CodeableConcept? = null,
            ranges: List<Observation.ObservationReferenceRange>? = null): Observation {

        return build(
            type,
            status,
            issuedDate,
            effectiveDate,
            observationValue,
            observationUnit,
            category = category,
            ranges = ranges
        )
    }

    @JvmStatic
    fun buildWith(
            type: CodeableConcept,
            observationSampledData: SampledData,
            observationUnit: String,
            status: CodeSystemObservationStatus,
            issuedDate: FhirInstant,
            effectiveDate: FhirDateTime?,
            category: CodeableConcept? = null,
            ranges: List<Observation.ObservationReferenceRange>? = null): Observation {

        return build(
            type,
            status,
            issuedDate,
            effectiveDate,
            observationUnit = observationUnit,
            observationSampledData = observationSampledData,
            category = category,
            ranges = ranges
        )
    }

    @JvmStatic
    fun buildWith(
            type: CodeableConcept,
            text: String,
            status: CodeSystemObservationStatus,
            issuedDate: FhirInstant,
            effectiveDate: FhirDateTime?,
            category: CodeableConcept? = null,
            ranges: List<Observation.ObservationReferenceRange>? = null): Observation {

        return build(
            type,
            status,
            issuedDate,
            effectiveDate,
            text = text,
            category = category,
            ranges = ranges
        )
    }

    private fun build(
            type: CodeableConcept,
            status: CodeSystemObservationStatus,
            issuedDate: FhirInstant,
            effectiveDate: FhirDateTime?,
            observationValue: Float? = null,
            observationUnit: String? = null,
            observationSampledData: SampledData? = null,
            value: Quantity? = null,
            text: String? = null,
            category: CodeableConcept? = null,
            ranges: List<Observation.ObservationReferenceRange>? = null): Observation {

        val observation = Observation(status, type)

        if (value != null) observation.valueQuantity = value
        else if (observationValue != null && observationUnit != null) observation.valueQuantity =
            FhirHelpers.buildWith(observationValue, observationUnit)
        else if (text != null) observation.valueString = text
        else if (observationSampledData != null) observation.valueSampledData
        else throw IllegalArgumentException("Necessary arguments not provided")

        observation.issued = issuedDate
        observation.effectiveDateTime = effectiveDate
        observation.category = category?.let { mutableListOf(category) }
        observation.referenceRange = ranges

        return observation
    }
}
