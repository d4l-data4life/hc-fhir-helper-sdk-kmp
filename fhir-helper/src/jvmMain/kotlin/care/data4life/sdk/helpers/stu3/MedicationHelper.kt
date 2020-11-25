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

import care.data4life.fhir.stu3.model.Medication
import care.data4life.fhir.stu3.model.Substance
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.StringUtils
import java.util.*

object MedicationHelper {

    @JvmStatic
    fun buildWith(medicationName: String?, medicationForm: String?): Medication {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(medicationName), "medicationName is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(medicationForm), "medicationForm is required")

        val med = Medication()

        val medicationCode = FhirHelpers.buildWith(medicationName)
        med.code = medicationCode
        val formCode = FhirHelpers.buildWith(medicationForm)
        med.form = formCode

        return med
    }

    @JvmStatic
    fun addIngredient(
        medication: Medication?,
        ingredientName: String?,
        ingredientQuantity: Float?,
        ingredientUnit: String
    ) {
        Preconditions.checkArgument(medication != null, "medication is required")

        val (first, second) = MedicationIngredientHelper.buildWith(
            ingredientName,
            ingredientQuantity,
            ingredientUnit
        )
        if (medication?.ingredient == null) medication?.ingredient = mutableListOf<Medication.MedicationIngredient>()
        medication?.ingredient?.add(first)
        if (medication?.contained == null) medication?.contained = mutableListOf()
        medication?.contained!!.add(second)
    }

    @JvmStatic
    fun getMedicationName(medication: Medication?): String? {
        if (medication == null)
            return null
        else if (FhirHelpers.isCodingNullOrEmpty(medication.code)) return null
        return if (medication.code!!.coding!![0] == null) null else medication.code!!.coding!![0].display

    }

    @JvmStatic
    fun getMedicationForm(medication: Medication?): String? {
        if (medication == null)
            return null
        else if (FhirHelpers.isCodingNullOrEmpty(medication.form))
            return null
        else if (medication.form!!.coding!![0] == null) return null

        return medication.form!!.coding!![0].display
    }

    @JvmStatic
    fun getIngredients(medication: Medication?): List<Triple<String, Float, String>>? {
        val ingredients = ArrayList<Triple<String, Float, String>>()

        if (medication == null)
            return null
        else if (medication.ingredient == null)
            return null
        else if (medication.ingredient!!.isEmpty()) return null

        for (ingredient in medication.ingredient!!) {
            val substanceRef = ingredient.itemReference
            if (substanceRef == null)
                continue
            else if (substanceRef.reference == null) continue

            val substance =
                FhirHelpers.getResourceById(
                    medication.contained!!,
                    substanceRef.reference!!,
                    Substance::class.java
                )
                    ?: continue

            val ingredientParts = MedicationIngredientHelper.getIngredient(
                ingredient,
                substance
            ) ?: continue

            ingredients.add(ingredientParts)
        }
        return ingredients
    }
}
