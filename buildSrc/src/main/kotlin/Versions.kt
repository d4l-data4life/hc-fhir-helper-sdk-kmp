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

object Versions {

    object GradlePlugins {
        const val kotlin = Versions.kotlin
        const val android = "4.1.1"

        /**
         * [DependencyUpdates](https://github.com/ben-manes/gradle-versions-plugin)
         */
        const val dependencyUpdates = "0.34.0"

        /**
         * [jGitVer](https://github.com/jgitver/gradle-jgitver-plugin)
         */
        const val gitVersioning = "0.6.1"

        /**
         * [Gradle Git Publish](https://github.com/d4l-data4life/gradle-git-publish)
         */
        const val gitPublish = "3.2.0"
    }

    // Kotlin
    // https://github.com/JetBrains/kotlin
    const val kotlin = "1.4.21"


    // Android
    // https://developer.android.com/studio/write/java8-support
    const val androidDesugar = "1.0.4"

    // AndroidX
    // https://developer.android.com/jetpack/androidx
    const val androidX = "1.1.0"
    const val androidXKtx = "1.3.1"
    const val androidXAppCompat = "1.2.0"

    const val androidXConstraintLayout = "2.0.1"

    // https://developer.android.com/testing
    const val androidXTest = "1.1.2"
    const val androidXEspresso = "3.3.0"
    const val androidXUiAutomator = "2.2.0"

    // Material
    /**
     * [Material Android](https://github.com/material-components/material-components-android)
     */
    const val material = "1.2.0"

    // Junit Test
    const val testJUnit = "4.13.1"

    /**
     * [Robolectric](https://github.com/robolectric/robolectric)
     */
    const val robolectric = "4.4"

    // D4L
    /**
     * [hc-fhir-sdk-java](https://github.com/d4l-data4life/hc-fhir-sdk-java)
     */
    const val fhir = "1.2.1"

    /**
     * [hc-util-sdk-kmp](https://github.com/d4l-data4life/hc-util-sdk-kmp)
     */
    const val sdkUtil = "1.6.1"

    /**
     * [mockk](http://mockk.io)
     */
    const val testMockk = "1.9"

    const val testTruth = "0.42"

    /**
     * [mockito](https://github.com/mockito/mockito)
     */
    const val testMockito = "2.23.0"

    const val testJsonAssert = "1.5.0"

    // Android Test
    const val androidTest = "1.0.2"
    const val androidTestEspresso = "3.0.2"
}
