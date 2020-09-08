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

import care.data4life.fhir.stu3.model.*
import care.data4life.sdk.config.DataRestriction
import care.data4life.sdk.config.DataRestrictionException
import care.data4life.sdk.util.Base64
import care.data4life.sdk.util.MimeType
import care.data4life.sdk.util.StringUtils

object DocumentReferenceBuilder {

    @JvmStatic
    fun buildWith(
        title: String,
        indexed: FhirInstant,
        status: CodeSystems.DocumentReferenceStatus,
        attachments: List<Attachment>,
        type: CodeableConcept,
        author: Practitioner,
        practiceSpeciality: CodeableConcept? = null
    ): DocumentReference {

        return build(
            title,
            indexed,
            status,
            attachments,
            type,
            author,
            practiceSpeciality
        )
    }

    @JvmStatic
    fun buildWith(
        title: String,
        indexed: FhirInstant,
        status: CodeSystems.DocumentReferenceStatus,
        attachments: List<Attachment>,
        type: CodeableConcept,
        author: Organization,
        practiceSpeciality: CodeableConcept? = null
    ): DocumentReference {

        return build(
            title,
            indexed,
            status,
            attachments,
            type,
            author,
            practiceSpeciality
        )
    }

    @Throws(DataRestrictionException.UnsupportedFileType::class, DataRestrictionException.MaxDataSizeViolation::class)
    private fun <T : DomainResource> build(
        title: String,
        indexed: FhirInstant,
        status: CodeSystems.DocumentReferenceStatus,
        attachments: List<Attachment>,
        type: CodeableConcept,
        author: T,
        practiceSpeciality: CodeableConcept? = null
    ): DocumentReference {

        if (!(author is Practitioner || author is Organization)) {
            throw IllegalArgumentException("'author' should be of type Practitioner or Organization")
        }

        attachments.forEach {
            val data: ByteArray? = it.data?.let { dataBase64String -> Base64.decode(dataBase64String) }

            if (MimeType.recognizeMimeType(data) == MimeType.UNKNOWN) {
                throw DataRestrictionException.UnsupportedFileType()
            }
            data?.let {
                if (data.size > DataRestriction.DATA_SIZE_MAX_BYTES) {
                    throw DataRestrictionException.MaxDataSizeViolation()
                }
            }
        }

        val content = attachments.map { DocumentReference.DocumentReferenceContent(it) }
        val document = DocumentReference(content, indexed, status, type)
        if (author.id.isNullOrEmpty()) author.id = StringUtils.randomUUID()
        document.author = mutableListOf(FhirHelpers.contain(document, author))
        document.description = title
        val context = DocumentReference.DocumentReferenceContext()
        context.practiceSetting = practiceSpeciality
        document.context = context

        return document
    }
}
