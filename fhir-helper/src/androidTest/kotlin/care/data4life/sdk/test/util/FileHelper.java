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

package care.data4life.sdk.test.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class FileHelper {
    private static final String FILE_ENCODING = "UTF-8";

    public static String loadString(String fileName) throws IOException {
        InputStream inputStream = FileHelper.class.getClassLoader().getResourceAsStream(fileName);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(FILE_ENCODING);
    }


    public static List<String> loadFileList(String path) throws URISyntaxException {
        URL dirUrl = FileHelper.class.getClassLoader().getResource(path);
        if (dirUrl != null && dirUrl.getProtocol().equals("file")) {
            return Arrays.asList(new File(dirUrl.toURI()).list());
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirUrl);
    }
}
