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

import care.data4life.fhir.stu3.model.*
import care.data4life.fhir.util.Preconditions
import care.data4life.sdk.util.ArrayUtils
import care.data4life.sdk.util.StringUtils
import java.math.BigDecimal
import java.util.*

object FhirHelpers {

    @JvmStatic
    fun buildWith(street: String?, postalCode: String?, city: String?): Address {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(street), "street is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(postalCode), "postalCode is required")
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(city), "city is required")

        val address = Address()
        address.line = ArrayUtils.asMutableList(street)
        address.postalCode = postalCode
        address.city = city

        return address
    }

    @JvmStatic
    fun buildWith(value: String?, system: CodeSystemContactPointSystem?): ContactPoint {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(value), "value is required")
        Preconditions.checkArgument(system != null, "system is required")
        val contactPoint = ContactPoint()
        contactPoint.value = value
        contactPoint.system = system
        return contactPoint
    }

    @JvmStatic
    fun buildWith(display: String?): CodeableConcept {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(display), "display is required")

        val coding = Coding()
        coding.display = display
        val concept = CodeableConcept()
        concept.coding = ArrayUtils.asMutableList(coding)
        return concept
    }

    @JvmStatic
    fun isCodingNullOrEmpty(codeable: CodeableConcept?): Boolean {
        return codeable?.coding == null || codeable.coding?.isEmpty() ?: false
    }

    @JvmStatic
    fun buildWith(value: Float?, unit: String?): Quantity {
        Preconditions.checkArgument(!StringUtils.isNullOrEmpty(unit), "unit is required")
        Preconditions.checkArgument(value!! > 0, "value should be > 0")

        val quantity = Quantity()
        quantity.value = FhirDecimal(BigDecimal.valueOf(value.toDouble()))
        quantity.unit = unit
        return quantity
    }

    @JvmStatic
    fun contain(parent: DomainResource?, child: Resource?): Reference {
        Preconditions.checkArgument(parent != null, "parent is required")
        Preconditions.checkArgument(child != null, "child is required")
        Preconditions.checkArgument(!child?.id.isNullOrEmpty(), "child.id is required")
        if (parent?.contained != null)
            Preconditions.checkArgument(!parent.contained!!.contains(child), "child already contained")

        if (parent?.contained == null) parent?.contained = mutableListOf()
        parent?.contained!!.add(child)

        val r = Reference()
        r.reference = "#" + child!!.id
        return r
    }

    @JvmStatic
    fun getAllContainedResources(resource: DomainResource?, nullifyContained: Boolean): List<Resource> {
        Preconditions.checkArgument(resource != null, "resource is required")
        if (resource?.contained == null || resource.contained!!.isEmpty()) return mutableListOf()

        val result = ArrayList<Resource>()
        for (r in resource.contained!!) {
            if (r is DomainResource) {
                result.addAll(getAllContainedResources(r, nullifyContained))
                if (nullifyContained) r.contained = null
            }
            result.add(r)
        }
        return result
    }

    @JvmStatic
    fun <T : Resource> getResourceById(resources: List<Resource>?, id: String?, expectedType: Class<T>?): T? {
        Preconditions.checkArgument(resources != null, "resources are required")
        Preconditions.checkArgument(id != null, "id is required")
        Preconditions.checkArgument(id!!.startsWith("#"), "id should start with #")
        Preconditions.checkArgument(expectedType != null, "expectedType is required")

        val rawId = id.replaceFirst("#".toRegex(), "")
        if (resources != null) {
            for (r in resources) {
                if (r.id != null && r.id.equals(
                        rawId,
                        ignoreCase = true
                    ) && expectedType!!.isInstance(r)
                ) return expectedType.cast(r)
            }
        }
        return null
    }

    internal fun appendIdentifier(appendingId: String?, identifiers: List<Identifier>?): List<Identifier> {
        check(!appendingId.isNullOrEmpty()) { "appendingId is required" }

        val result = ArrayList<Identifier>()
        if (identifiers != null) result.addAll(identifiers)
        result.add(buildIdentifier(appendingId))

        return result
    }

    internal fun buildIdentifiers(ids: List<String>?): List<Identifier>? {
        if (ids == null) return null

        val result = ArrayList<Identifier>()
        for (id in ids) result.add(buildIdentifier(id))

        return result
    }

    internal fun extractIdentifiersForCurrentPartnerId(ids: List<Identifier>?): List<Identifier>? {
        if (ids == null) return null

        val result = ArrayList<Identifier>()
        for (id in ids) {
            if (id.assigner == null)
                continue
            else if (id.assigner!!.reference == null) continue

            if (id.assigner!!.reference == FhirHelperConfig.partnerId) result.add(id)
        }

        return result
    }

    internal fun buildIdentifier(id: String?): Identifier {
        check(!id.isNullOrEmpty()) { "id is required" }

        val reference = Reference()
        reference.reference = FhirHelperConfig.partnerId
        val newIdentifier = Identifier()
        newIdentifier.assigner = reference
        newIdentifier.value = id

        return newIdentifier
    }
}
