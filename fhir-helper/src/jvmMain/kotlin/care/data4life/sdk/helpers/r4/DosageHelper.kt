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

import care.data4life.fhir.r4.model.Dosage
import care.data4life.fhir.r4.model.Timing
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.StringUtils
import java.util.*

object DosageHelper {

    private val EXCEPTION_DOSAGE_INFORMAATION = "Failed to extract dosage information"

    @JvmStatic
    fun buildWith(value: Float, unit: String?, `when`: String?): Dosage {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(unit), "unit is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(`when`), "when is required")

        val timing = Timing()
        val timingRepeat = Timing.TimingRepeat()
        timingRepeat.`when` = Arrays.asList(`when`)
        timing.repeat = timingRepeat

        val dosage = Dosage()
        dosage.timing = timing

        val doseAndRate = Dosage.DosageDoseAndRate()
        doseAndRate.doseQuantity = FhirHelpers.buildWith(value, unit!!)
        dosage.doseAndRate = mutableListOf(doseAndRate)

        return dosage
    }

    @JvmStatic
    fun getDosage(dosage: Dosage?): Triple<Float, String, String>? {
        if (dosage == null)
            return null
        else if (dosage.timing == null)
            return null
        else if (dosage.timing?.repeat == null)
            return null
        else if (dosage.timing?.repeat?.`when` == null)
            return null
        else if (dosage.timing?.repeat?.`when`?.size != 1)
            return null
        else if (StringUtils.isNullOrEmpty(dosage.timing?.repeat?.`when`!![0])) return null

        val `when` = dosage.timing?.repeat?.`when`?.get(0) ?: throw IllegalStateException(EXCEPTION_DOSAGE_INFORMAATION)

        val doseAndRate = dosage.doseAndRate ?: return null
        if (doseAndRate.size != 1)
            return null

        val doseQuantity = doseAndRate.first().doseQuantity ?: return null

        return when {
            doseQuantity == null -> null
            doseQuantity?.value === null -> null
            doseQuantity?.value?.decimal == null -> null
            doseQuantity?.unit.isNullOrEmpty() -> null
            doseQuantity?.value?.decimal == null -> null
            else -> {
                val value = doseQuantity?.value?.decimal?.toFloat()
                    ?: throw IllegalStateException(EXCEPTION_DOSAGE_INFORMAATION)
                val unit = doseQuantity?.unit ?: throw IllegalStateException(EXCEPTION_DOSAGE_INFORMAATION)

                Triple(value, unit, `when`)
            }
        }
    }

}
