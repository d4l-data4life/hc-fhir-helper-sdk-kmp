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
import care.data4life.sdk.util.ArrayUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import java.math.BigDecimal
import java.util.*
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class FHIRHelpersTest {

    @Before
    fun setUp() {
        FhirHelperConfig.init(PARTNER_ID)
    }

    //region buildWith() : CodeableConcept
    @Test
    fun buildWith_shouldReturnCodeableConcept_whenDisplayProvided() {
        // Given
        val display = "display"

        // When
        val codeableConcept = FhirHelpers.buildWith(display)

        // Then
        assertThat(codeableConcept.coding).hasSize(1)
        assertThat(codeableConcept.coding!![0].display).isEqualTo(display)
    }

    @Test
    fun buildWith_shouldThrow_whenDisplayNotProvided() {
        // Given
        var display: String? = null
        try {
            // When
            FhirHelpers.buildWith(display)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("display is required")
        }

        // Given
        display = ""
        try {
            // When
            FhirHelpers.buildWith(display)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("display is required")
        }
    }
    //endregion

    //region buildWith() : Quantity
    @Test
    fun buildWith_shouldReturnQuantity_whenAllArgsProvided() {
        // Given
        val value = 1f
        val unit = "unit"

        // When
        val quantity = FhirHelpers.buildWith(value, unit)

        // Then
        assertThat(quantity.value).isEqualTo(FhirDecimal(BigDecimal.valueOf(value.toDouble())))
        assertThat(quantity.unit).isEqualTo(unit)
    }

    @Test
    fun buildWith_shouldThrow_whenValueNotCorrect() {
        // Given
        var value = -1f
        try {
            // When
            FhirHelpers.buildWith(value, "unit")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("value should be > 0")
        }

        // Given
        value = 0f
        try {
            // When
            FhirHelpers.buildWith(value, "unit")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("value should be > 0")
        }
    }

    @Test
    fun buildWith_shouldThrow_whenUnitNotProvided() {
        // Given
        var unit: String? = null
        try {
            // When
            FhirHelpers.buildWith(1f, unit)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("unit is required")
        }

        // Given
        unit = ""
        try {
            // When
            FhirHelpers.buildWith(1f, unit)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("unit is required")
        }
    }
    //endregion

    //region contain() : Reference
    @Test
    fun contain_shouldReturnReference_whenAllArgsProvided() {
        // Given
        val childId = "childId"
        val parent = mockk<MedicationRequest>()
        val child = Medication()
        child.id = childId

        // When
        val reference = FhirHelpers.contain(parent, child)

        // Then
        assertThat(parent.contained).hasSize(1)
        assertThat(parent.contained!![0]).isEqualTo(child)
        assertThat(reference.reference).isEqualTo("#$childId")
    }

    @Test
    fun contain_shouldThrow_whenParentNotProvided() {
        // Given
        val parent: DomainResource? = null

        try {
            // When
            FhirHelpers.contain(parent, Medication())
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("parent is required")
        }
    }

    @Test
    fun contain_shouldThrow_whenChildNotProvided() {
        // Given
        val child: Resource? = null

        try {
            // When
            FhirHelpers.contain(Medication(), child)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("child is required")
        }
    }

    @Test
    fun contain_shouldThrow_whenChildIdNotProvided() {
        // Given
        val parent = mockk<MedicationRequest>()
        val child = Medication()
        child.id = null

        try {
            // When
            FhirHelpers.contain(parent, child)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("child.id is required")
        }
    }

    @Test
    fun contain_shouldThrow_whenChildAlreadyContained() {
        // Given
        val parent = mockk<MedicationRequest>()
        val child = Medication()
        child.id = "childId"
        parent.contained = ArrayUtils.asMutableList<Resource>(child)

        try {
            // When
            FhirHelpers.contain(parent, child)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("child already contained")
        }
    }
    //endregion

    //region getAllContainedResources() : List<Resource>
    @Test
    fun getAllContainedResources_shouldReturnContainedResources_whenAllArgsProvided() {
        // Given
        val shouldNullify = true
        val medRequest = mockk<MedicationRequest>()
        val medication = Medication()
        val substance = mockk<Substance>()
        val practitioner = Practitioner()
        val patient = Patient()

        val patientList = ArrayList<Resource>()
        patientList.add(practitioner)

        val medicationList = ArrayList<Resource>()
        medicationList.add(substance)
        medicationList.add(patient)

        val medRequests = ArrayList<Resource>()
        medRequests.add(medication)

        patient.contained = patientList
        medication.contained = medicationList
        medRequest.contained = medRequests

        // When
        val resources = FhirHelpers.getAllContainedResources(medRequest, shouldNullify)

        // Then
        assertThat(resources).hasSize(4)
        assertThat(resources).containsExactlyElementsIn(
            Arrays.asList(substance, practitioner, patient, medication)
        )
            .inOrder()
        assertThat(medRequest.contained).isNotNull()
        assertThat(medication.contained).isNull()
        assertThat(patient.contained).isNull()
    }

    @Test
    fun getAllContainedResources_shouldThrow_whenResourceNotProvided() {
        // Given
        val medication: Medication? = null

        // When
        try {
            // When
            FhirHelpers.getAllContainedResources(medication, true)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("resource is required")
        }
    }

    @Test
    fun getAllContainedResources_shouldReturnEmptyList_whenNothingContained() {
        // Given
        var medication = Medication()
        medication.contained = null
        // When
        var resources = FhirHelpers.getAllContainedResources(medication, true)
        // Then
        assertThat(resources).isEmpty()

        // Given
        medication = Medication()
        medication.contained = ArrayList()
        // When
        resources = FhirHelpers.getAllContainedResources(medication, true)
        // Then
        assertThat(resources).isEmpty()
    }

    @Test
    fun getAllContainedResources_shouldNotNullifyContained() {
        // Given
        val shouldNullify = false
        val medication = Medication()
        val substance = mockk<Substance>()
        val patient = Patient()

        val medicationList = ArrayList<Resource>()
        medicationList.add(substance)

        val patientList = ArrayList<Resource>()
        patientList.add(patient)

        medication.contained = medicationList
        substance.contained = patientList

        // When
        val resources = FhirHelpers.getAllContainedResources(medication, shouldNullify)

        // Then
        assertThat(medication.contained).isNotNull()
        assertThat(substance.contained).isNotNull()
    }
    //endregion

    //region getResourceById
    @Test
    fun getResourceById_shouldReturnResource() {
        // Given
        val id = "id"
        val resource = Medication()
        resource.id = id
        val resources = Arrays.asList<Resource>(resource)

        // When
        val medication = FhirHelpers.getResourceById(resources, "#$id", Medication::class.java)

        // Then
        assertThat(medication).isEqualTo(resource)
    }

    @Test
    fun getResourceById_shouldThrow_whenResourcesNotProvided() {
        // Given
        val resources: List<Resource>? = null
        try {
            // When
            FhirHelpers.getResourceById(resources, "#id", Medication::class.java)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("resources are required")
        }
    }

    @Test
    fun getResourceById_shouldThrow_whenIdNotProvided() {
        // Given
        val id: String? = null
        try {
            // When
            FhirHelpers.getResourceById(ArrayList(), id, Medication::class.java)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("id is required")
        }
    }

    @Test
    fun getResourceById_shouldThrow_whenIdNotCorrectlyFormatted() {
        // Given
        val id = "id"
        try {
            // When
            FhirHelpers.getResourceById(ArrayList(), id, Medication::class.java)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("id should start with #")
        }
    }

    @Test
    fun getResourceById_shouldThrow_whenIdExpectedTypeNotProvided() {
        // Given
        val expectedType: Class<Medication>? = null
        try {
            // When
            FhirHelpers.getResourceById(ArrayList(), "#id", expectedType)
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            // Then
            assertThat(e.message).isEqualTo("expectedType is required")
        }
    }
    //endregion

    @Test
    fun isCodingNullOrEmpty_shouldReturnTrue() {
        // Given
        var concept: CodeableConcept? = CodeableConcept()
        concept!!.coding = emptyList()
        // When
        var isCodingNullOrEmpty = FhirHelpers.isCodingNullOrEmpty(concept)
        // Then
        assertThat(isCodingNullOrEmpty).isTrue()

        // Given
        concept.coding = null
        // When
        isCodingNullOrEmpty = FhirHelpers.isCodingNullOrEmpty(concept)
        // Then
        assertThat(isCodingNullOrEmpty).isTrue()

        // Given
        concept = null
        // When
        isCodingNullOrEmpty = FhirHelpers.isCodingNullOrEmpty(concept)
        // Then
        assertThat(isCodingNullOrEmpty).isTrue()
    }

    //region additional ids helpers
    @Test
    fun appendIdentifier_shouldAppendIdentifier() {
        // Given
        val identifier = Identifier()
        val identifiers = Arrays.asList(identifier)

        // When
        val result = FhirHelpers.appendIdentifier(ID, identifiers)

        // Then
        assertThat(result !== identifiers).isTrue()
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(identifier)
        assertThat(result[1].value).isEqualTo(ID)
        assertThat(result[1].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun buildIdentifier_shouldBuildIdentifier() {
        // When
        val identifier = FhirHelpers.buildIdentifier(ID)

        // Then
        assertThat(identifier.value).isEqualTo(ID)
        assertThat(identifier.assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun buildIdentifiers_shouldBuildIdentifiers() {
        // Given
        val ids = Arrays.asList(
            ID,
            ID
        )

        // When
        val result = FhirHelpers.buildIdentifiers(ids)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result!![0].value).isEqualTo(ID)
        assertThat(result[0].assigner!!.reference).isEqualTo(PARTNER_ID)
        assertThat(result[1].value).isEqualTo(ID)
        assertThat(result[1].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun buildIdentifiers_shouldReturnNull() {
        // when
        val result = FhirHelpers.buildIdentifiers(null)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun extractIdentifiersForCurrentPartnerId_shouldExtractIdentifiers() {
        // Given
        val first = FhirHelpers.buildIdentifier(ID)
        FhirHelperConfig.init("newPartnerId")
        val second = FhirHelpers.buildIdentifier(ID)
        val identifiers = Arrays.asList(first, second)

        // When
        val result = FhirHelpers.extractIdentifiersForCurrentPartnerId(identifiers)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result!![0].value).isEqualTo(ID)
        assertThat(result[0].assigner!!.reference).isEqualTo("newPartnerId")
    }

    @Test
    fun extractIdentifiersForCurrentPartnerId_shouldReturnNull() {
        // when
        val result = FhirHelpers.extractIdentifiersForCurrentPartnerId(null)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun appendIdentifier_shouldThrow_whenAppendingIdNotProvided() {
        try {
            // When
            FhirHelpers.appendIdentifier(null, null)
            fail("Exception expected")
        } catch (e: IllegalStateException) {
            // Then
            assertThat(e.message).isEqualTo("appendingId is required")
        }

        try {
            // When
            FhirHelpers.appendIdentifier("", null)
            fail("Exception expected")
        } catch (e: IllegalStateException) {
            // Then
            assertThat(e.message).isEqualTo("appendingId is required")
        }
    }

    @Test
    fun buildIdentifier_shouldThrow_whenIdNotProvided() {
        try {
            // When
            FhirHelpers.buildIdentifier(null)
            fail("Exception expected")
        } catch (e: IllegalStateException) {
            // Then
            assertThat(e.message).isEqualTo("id is required")
        }

        try {
            // When
            FhirHelpers.buildIdentifier("")
            fail("Exception expected")
        } catch (e: IllegalStateException) {
            // Then
            assertThat(e.message).isEqualTo("id is required")
        }
    }

    companion object {
        private val ID = "id"
        private val PARTNER_ID = "partnerId"
    }
    //endregion
}
