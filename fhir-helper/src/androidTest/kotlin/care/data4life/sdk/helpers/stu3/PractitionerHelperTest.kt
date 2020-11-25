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
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class PractitionerHelperTest {


    @Before
    fun setUp() {
        FhirHelperConfig.init(PARTNER_ID)
    }

    @Test
    fun buildWith_shouldReturnPractitioner_whenTextGiven() {
        // When
        val practitioner =
            PractitionerBuilder.buildWith(TEXT)

        // Then
        assertThat(practitioner.name).hasSize(1)
        assertThat(practitioner.name!![0].text).isEqualTo(TEXT)
    }

    @Test
    fun buildWith_shouldReturnPractitioner_whenNameGiven() {
        // When
        val practitioner = PractitionerBuilder.buildWith(
            NAME,
            SURNAME
        )

        // Then
        assertThat(practitioner.name).hasSize(1)
        assertThat(practitioner.name!![0].given).hasSize(1)
        assertThat(practitioner.name!![0].given!![0]).isEqualTo(NAME)
        assertThat(practitioner.name!![0].family).isEqualTo(SURNAME)
    }

    @Test
    fun buildWith_shouldReturnPractitioner() {
        // When
        val practitioner = PractitionerBuilder.buildWith(
            NAME,
            SURNAME,
            PREFIX,
            SUFFIX,
            STREET,
            POSTAL_CODE,
            CITY,
            TELEPHONE,
            WEBSITE
        )

        // Then
        assertThat(practitioner.name).hasSize(1)
        assertThat(practitioner.name!![0].given).hasSize(1)
        assertThat(practitioner.name!![0].given!![0]).isEqualTo(NAME)
        assertThat(practitioner.name!![0].family).isEqualTo(SURNAME)
        assertThat(practitioner.name!![0].prefix).hasSize(1)
        assertThat(practitioner.name!![0].prefix!![0]).isEqualTo(PREFIX)
        assertThat(practitioner.name!![0].suffix).hasSize(1)
        assertThat(practitioner.name!![0].suffix!![0]).isEqualTo(SUFFIX)
        assertThat(practitioner.address).hasSize(1)
        assertThat(practitioner.address!![0].line).hasSize(1)
        assertThat(practitioner.address!![0].line!![0]).isEqualTo(STREET)
        assertThat(practitioner.address!![0].postalCode).isEqualTo(POSTAL_CODE)
        assertThat(practitioner.address!![0].city).isEqualTo(CITY)
        assertThat(practitioner.telecom).hasSize(2)
        assertThat(practitioner.telecom!![0].system).isEqualTo(CodeSystemContactPointSystem.PHONE)
        assertThat(practitioner.telecom!![0].value).isEqualTo(TELEPHONE)
        assertThat(practitioner.telecom!![1].system).isEqualTo(CodeSystemContactPointSystem.URL)
        assertThat(practitioner.telecom!![1].value).isEqualTo(WEBSITE)
    }

    @Test
    fun builtWith_shouldThrow_whenTextNotProvided() {
        try {
            PractitionerBuilder.buildWith("")
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("text is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenNameNotProvided() {
        try {
            PractitionerBuilder.buildWith(
                "",
                SURNAME
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("firstName is required")
        }

    }

    @Test
    fun builtWith_shouldThrow_whenSurnameNotProvided() {
        try {
            PractitionerBuilder.buildWith(
                NAME,
                ""
            )
            fail("Exception expected")
        } catch (e: IllegalArgumentException) {
            assertThat(e.message).isEqualTo("lastName is required")
        }

    }

    @Test
    fun getText_shouldReturnExpectedValue() {
        // When
        val practitioner =
            PractitionerBuilder.buildWith(TEXT)

        // Then
        assertThat(practitioner.getText()).isEqualTo(TEXT)
    }

    @Test
    fun getters_shouldReturnExpectedValues() {
        // When
        val practitioner = PractitionerBuilder.buildWith(
            NAME,
            SURNAME
        )

        // Then
        assertThat(practitioner.getFirstName()).isEqualTo(NAME)
        assertThat(practitioner.getLastName()).isEqualTo(SURNAME)
    }

    @Test
    fun getters_shouldReturnExpectedValues_() {
        // When
        val practitioner = PractitionerBuilder.buildWith(
            NAME,
            SURNAME,
            PREFIX,
            SUFFIX,
            STREET,
            POSTAL_CODE,
            CITY,
            TELEPHONE,
            WEBSITE
        )

        // Then
        assertThat(practitioner.getFirstName()).isEqualTo(NAME)
        assertThat(practitioner.getLastName()).isEqualTo(SURNAME)
        assertThat(practitioner.getPrefix()).isEqualTo(PREFIX)
        assertThat(practitioner.getSuffix()).isEqualTo(SUFFIX)
        assertThat(practitioner.getStreet()).isEqualTo(STREET)
        assertThat(practitioner.getPostalCode()).isEqualTo(POSTAL_CODE)
        assertThat(practitioner.getCity()).isEqualTo(CITY)
        assertThat(practitioner.getTelephone()).isEqualTo(TELEPHONE)
        assertThat(practitioner.getWebsite()).isEqualTo(WEBSITE)
    }

    @Test
    fun addAdditionalId_shouldAddId() {
        // given
        val p = Practitioner()

        // when
        p.addAdditionalId(ADDITIONAL_ID)

        // then
        assertThat(p.identifier).hasSize(1)
        assertThat(p.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![0].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIds_shouldSetIds() {
        // given
        val p = Practitioner()
        val newIds = Arrays.asList(
            ADDITIONAL_ID,
            ADDITIONAL_ID
        )
        p.addAdditionalId("oldId")

        // when
        p.setAdditionalIds(newIds)

        // then
        assertThat(p.identifier).hasSize(2)
        assertThat(p.identifier!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![0].assigner!!.reference).isEqualTo(PARTNER_ID)
        assertThat(p.identifier!![1].value).isEqualTo(ADDITIONAL_ID)
        assertThat(p.identifier!![1].assigner!!.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIds_shouldReturnIds() {
        // given
        val p = Practitioner()
        p.addAdditionalId(ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        p.addAdditionalId(ADDITIONAL_ID)

        // when
        val ids = p.getAdditionalIds()

        // then
        assertThat(p.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids!![0].value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids[0].assigner!!.reference).isEqualTo("newPartnerId")
    }

    companion object {

        private val TEXT = "Dr. Bruce Banner, Praxis fuer Allgemeinmedizin"
        private val NAME = "Bruce"
        private val SURNAME = "Banner"
        private val PREFIX = "Dr."
        private val SUFFIX = "MD"
        private val STREET = "Walvisbaai 3"
        private val POSTAL_CODE = "2333ZA"
        private val CITY = "Den helder"
        private val TELEPHONE = "+31715269111"
        private val WEBSITE = "www.webpage.com"

        private val ADDITIONAL_ID = "id"
        private val PARTNER_ID = "partnerId"
    }
}

