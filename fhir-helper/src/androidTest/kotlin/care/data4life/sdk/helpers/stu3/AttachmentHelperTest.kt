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

import care.data4life.fhir.stu3.util.FhirDateTimeParser
import care.data4life.sdk.config.DataRestrictionException
import care.data4life.sdk.lang.D4LException
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AttachmentHelperTest {
    private val title = "Brain MRI"
    private val creationDate = FhirDateTimeParser.parseDateTime("2013-04-03")
    private val contentType = "image/jpeg"
    private var data = byteArrayOf(0x25, 0x50, 0x44, 0x46, 0x2d)
    private val dataBase64 = "JVBERi0="

    @Test
    fun buildWithShouldReturnAttachment() {
        // When
        val attachment = AttachmentBuilder.buildWith(title, creationDate, contentType, data)

        // Then
        assertThat(attachment.title).isEqualTo(title)
        assertThat(attachment.creation).isEqualTo(creationDate)
        assertThat(attachment.contentType).isEqualTo(contentType)
        assertThat(attachment.data).isEqualTo(dataBase64)
        assertThat(attachment.getData()).isEqualTo(data)
    }

    @Test
    fun buildWithShouldThrowForInvalidAttachmentData() {
        // Given
        val invalidData = "Invalid Data".toByteArray()

        // When
        try {
            AttachmentBuilder.buildWith(title, creationDate, contentType, invalidData)
            fail("Exception expected")
        } catch (e: D4LException) {

            // Then
            assertThat(e).isInstanceOf(DataRestrictionException.UnsupportedFileType::class.java)
            assertThat(e.message).isEqualTo("Only this file types are supported: JPEG, PNG, TIFF, PDF and DCM!")
        }
    }
}
