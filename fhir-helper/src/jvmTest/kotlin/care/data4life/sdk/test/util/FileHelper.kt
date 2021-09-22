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

package care.data4life.sdk.test.util

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.util.*

object FileHelper {
    private val FILE_ENCODING = "UTF-8"

    @Throws(IOException::class)
    fun loadString(fileName: String): String {
        throw UnsupportedOperationException("currently resources are not bundled")

        val inputStream = this::class.java.classLoader.getResourceAsStream(fileName)
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int

        length = inputStream.read(buffer)

        while (length != -1) {
            result.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }

        return result.toString(FILE_ENCODING)
    }

    @Throws(URISyntaxException::class)
    fun loadFileList(path: String): List<String> {
        val dirUrl = FileHelper::class.java.classLoader.getResource(path)
        if (dirUrl != null && dirUrl.protocol == "file") {
            return Arrays.asList(*File(dirUrl.toURI()).list()!!)
        }

        throw UnsupportedOperationException("Cannot list files for URL " + dirUrl!!)
    }
}
