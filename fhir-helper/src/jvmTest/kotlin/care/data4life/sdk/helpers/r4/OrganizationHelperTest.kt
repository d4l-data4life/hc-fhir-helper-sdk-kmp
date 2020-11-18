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

import com.google.common.truth.Truth.assertThat
import care.data4life.fhir.stu3.model.*
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Test

class OrganizationHelperTest {

    val code = "dept"
    val display = "Hospital Department"
    val system = "http://hl7.org/fhir/organization-type"
    val type = CodeableConcept().apply {
        val organizationCoding = Coding()
        organizationCoding.code = code
        organizationCoding.display = display
        organizationCoding.system = system
        coding = mutableListOf(organizationCoding)
    }
    val name = "Health Level Seven International"
    val street = "3300 Washtenaw Avenue, Suite 227"
    val postalCode = "48104"
    val city = "Ann Arbor"
    val telephone = "(+1) 734-677-7777"
    val website = "www.website.com"

    val ADDITIONAL_ID = "id"
    val PARTNER_ID = "partner"


    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun buildWithShouldReturnOrganization() {
        // When
        val organization = OrganizationBuilder.buildWith(type, name, street, postalCode, city, telephone, website)

        // Then
        assertThat(organization.type).hasSize(1)
        assertThat(organization.type?.first()?.coding).hasSize(1)
        assertThat(organization.type?.first()?.coding?.first()?.code).isEqualTo(code)
        assertThat(organization.type?.first()?.coding?.first()?.display).isEqualTo(display)
        assertThat(organization.type?.first()?.coding?.first()?.system).isEqualTo(system)
        assertThat(organization.name).isEqualTo(name)
        assertThat(organization.address).hasSize(1)
        assertThat(organization.address?.first()?.line).hasSize(1)
        assertThat(organization.address?.first()?.line?.first()).isEqualTo(street)
        assertThat(organization.address?.first()?.postalCode).isEqualTo(postalCode)
        assertThat(organization.address?.first()?.city).isEqualTo(city)
        assertThat(organization.telecom).hasSize(2)
        assertThat(organization.telecom?.first()?.value).isEqualTo(telephone)
        assertThat(organization.telecom?.first()?.system).isEqualTo(CodeSystemContactPointSystem.PHONE)
        assertThat(organization.telecom?.get(1)?.value).isEqualTo(website)
        assertThat(organization.telecom?.get(1)?.system).isEqualTo(CodeSystemContactPointSystem.URL)
        assertThat(organization.getType()).isEqualTo(type)
        assertThat(organization.getStreet()).isEqualTo(street)
        assertThat(organization.getPostalCode()).isEqualTo(postalCode)
        assertThat(organization.getCity()).isEqualTo(city)
        assertThat(organization.getTelephone()).isEqualTo(telephone)
        assertThat(organization.getWebsite()).isEqualTo(website)
    }

    @Test
    fun addAdditionalIdShouldAddId() {
        // given
        FhirHelperConfig.init(PARTNER_ID)
        val o = Organization()

        // when
        o.addAdditionalId(ADDITIONAL_ID)

        // then
        assertThat(o.identifier).hasSize(1)
        assertThat(o.identifier?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.first()?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun setAdditionalIdsShouldSetIds() {
        // given
        FhirHelperConfig.init(PARTNER_ID)
        val o = Organization()
        val newIds = listOf(ADDITIONAL_ID, ADDITIONAL_ID)
        o.addAdditionalId("oldId")

        // when
        o.setAdditionalIds(newIds)

        // then
        assertThat(o.identifier).hasSize(2)
        assertThat(o.identifier?.get(0)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.get(0)?.assigner?.reference).isEqualTo(PARTNER_ID)
        assertThat(o.identifier?.get(1)?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(o.identifier?.get(1)?.assigner?.reference).isEqualTo(PARTNER_ID)
    }

    @Test
    fun getAdditionalIdsShouldReturnIds() {
        // given
        FhirHelperConfig.init(PARTNER_ID)
        val o = Organization()
        o.addAdditionalId(ADDITIONAL_ID)
        FhirHelperConfig.init("newPartnerId")
        o.addAdditionalId(ADDITIONAL_ID)

        // when
        val ids = o.getAdditionalIds()

        // then
        assertThat(o.identifier).hasSize(2)
        assertThat(ids).hasSize(1)
        assertThat(ids?.first()?.value).isEqualTo(ADDITIONAL_ID)
        assertThat(ids?.first()?.assigner?.reference).isEqualTo("newPartnerId")
    }
}
