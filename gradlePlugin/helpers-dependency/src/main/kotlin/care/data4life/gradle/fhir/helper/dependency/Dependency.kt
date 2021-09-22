/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
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
package care.data4life.gradle.fhir.helper.dependency

object Dependency {
    val kotlin = Kotlin
    object Kotlin {
        const val stdLib = Multiplatform.Kotlin.stdlibJdk
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Version.kotlin}"
    }

    val multiplatform = Multiplatform
    object Multiplatform {

        val kotlin = Kotlin
        object Kotlin {
            const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlin}"
            const val stdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin}"
            const val stdlibJs = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            const val stdlibNative = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"
            const val stdlibAndroid = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"

            const val testCommon = "org.jetbrains.kotlin:kotlin-test-common:${Version.kotlin}"
            const val testCommonAnnotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Version.kotlin}"
            const val testJvm = "org.jetbrains.kotlin:kotlin-test:${Version.kotlin}"
            const val testJvmJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
        }
    }

    val test = Test
    object Test {
        const val junit = "junit:junit:${Version.testJUnit}"

        const val truth = "com.google.truth:truth:${Version.testTruth}"

        const val mockkCommon = "io.mockk:mockk-common:${Version.testMockk}"
        const val mockkAndroid = "io.mockk:mockk-android:${Version.testMockk}"
        const val mockkJvm = "io.mockk:mockk:${Version.testMockk}"

        const val mockitoInline = "org.mockito:mockito-inline:${Version.testMockito}"
        const val mockitoCore = "org.mockito:mockito-core:${Version.testMockito}"

        const val jsonAssert = "org.skyscreamer:jsonassert:${Version.testJsonAssert}"
    }

    val android = Android
    object Android {
        // Kotlin
        const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"

        // Android
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.androidDesugar}"

        val androidX = AndroidX
        object AndroidX {
            // AndroidX
            const val ktx = "androidx.core:core-ktx:${Version.androidXKtx}"
            const val appCompat = "androidx.appcompat:appcompat:${Version.androidXAppCompat}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.androidXConstraintLayout}"
        }

        // Material
        const val material = "com.google.android.material:material:${Version.material}"

        // Test
        const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
    }

    val androidTest = AndroidTest
    object AndroidTest {
        const val core = "androidx.test:core:${Version.androidXTest}"
        const val runner = "androidx.test:runner:${Version.androidXTest}"
        const val rules = "androidx.test:rules:${Version.androidXTest}"

        const val junit = "androidx.test.ext:junit:${Version.androidXTest}"

        const val espressoCore = "androidx.test.espresso:espresso-core:${Version.androidXEspresso}"
        const val espressoIntents =
            "androidx.test.espresso:espresso-intents:${Version.androidXEspresso}"
        const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.androidXEspresso}"

        const val uiAutomator =
            "androidx.test.uiautomator:uiautomator:${Version.androidXUiAutomator}"
    }

    val d4l = D4L
    object D4L {
        val fhirJvm = "care.data4life.hc-fhir-sdk-java:fhir-java:${Version.fhir}"

        const val utilCommon = "care.data4life.hc-util-sdk-kmp:util:${Version.sdkUtil}"
        const val utilJvm = "care.data4life.hc-util-sdk-kmp:util-jvm:${Version.sdkUtil}"
        const val utilAndroid = "care.data4life.hc-util-sdk-kmp:util-android:${Version.sdkUtil}"

        const val resultErrorCommon = "care.data4life.hc-result-sdk-kmp:error:${Version.sdkResult}"
        const val resultErrorAndroid = "care.data4life.hc-result-sdk-kmp:error-android:${Version.sdkResult}"
        const val resultErrorJvm = "care.data4life.hc-result-sdk-kmp:error-jvm:${Version.sdkResult}"
    }
}
