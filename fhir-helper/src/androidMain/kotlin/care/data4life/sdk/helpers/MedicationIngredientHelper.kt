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

package care.data4life.sdk.helpers

import care.data4life.fhir.stu3.model.Medication
import care.data4life.fhir.stu3.model.Ratio
import care.data4life.fhir.stu3.model.Reference
import care.data4life.fhir.stu3.model.Substance
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.StringUtils

object MedicationIngredientHelper {

    @JvmStatic
    internal fun buildWith(
        ingredientName: String?,
        ingredientQuantity: Float?,
        ingredientUnit: String?
    ): Pair<Medication.MedicationIngredient, Substance> {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(ingredientName), "ingredientName is required")
        Preconditions.checkArgument(ingredientQuantity != null, "ingredientQuantity is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(ingredientUnit), "ingredientUnit is required")

        val substanceCode = FhirHelpers.buildWith(ingredientName)
        val substance = Substance(substanceCode)
        substance.id = StringUtils.randomUUID()

        val substanceRef = Reference()
        substanceRef.reference = "#" + substance.id!!
        val ingredient = Medication.MedicationIngredient(substanceRef)

        val ratio = Ratio()
        ratio.numerator = FhirHelpers.buildWith(ingredientQuantity!!, ingredientUnit)
        ingredient.amount = ratio
        return Pair(ingredient, substance)
    }

    @JvmStatic
    internal fun getIngredient(
        ingredient: Medication.MedicationIngredient?,
        substance: Substance?
    ): Triple<String, Float, String>? {
        if (ingredient == null || substance == null) return null

        if (FhirHelpers.isCodingNullOrEmpty(substance.code))
            return null
        else if (substance.code.coding!![0] == null) return null
        val ingredientName = substance.code.coding!![0].display!!
        if (StringUtils.isNullOrEmpty(ingredientName)) return null

        if (ingredient.amount == null)
            return null
        else if (ingredient.amount!!.numerator == null)
            return null
        else if (ingredient.amount!!.numerator!!.value === null)
            return null
        else if (ingredient.amount!!.numerator!!.unit == null)
            return null
        else if (ingredient.amount!!.numerator!!.value!!.decimal == null) return null
        val quantity = ingredient.amount!!.numerator!!.value!!.decimal.toFloat()
        val unit = ingredient.amount!!.numerator!!.unit!!

        return if (StringUtils.isNullOrEmpty(unit)) null else Triple(
            ingredientName,
            quantity,
            unit
        )

    }
}
