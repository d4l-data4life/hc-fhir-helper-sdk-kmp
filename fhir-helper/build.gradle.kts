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

plugins {
    kotlinMultiplatform()

    // Android
    androidLibrary()

    // Publish
    id("scripts.publishing-config")
}

group = LibraryConfig.group

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibCommon)

                implementation(Dependencies.d4l.util)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testCommon)
                implementation(Dependencies.multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibAndroid)

                api(Dependencies.d4l.fhirJvm)
                api(Dependencies.d4l.util)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testJvm)
                implementation(Dependencies.multiplatform.kotlin.testJvmJunit)

                implementation(Dependencies.test.junit)
                implementation(Dependencies.test.truth)
                implementation(Dependencies.test.jsonAssert)

                implementation(Dependencies.test.mockkJvm)
                implementation(Dependencies.test.mockitoInline)
                implementation(Dependencies.test.mockitoCore)

                implementation(Dependencies.android.robolectric)

                implementation(Dependencies.d4l.fhirJvm)
                implementation(Dependencies.d4l.util)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibJdk8)

                api(Dependencies.d4l.fhirJvm)
                api(Dependencies.d4l.util)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.testJvm)
                implementation(Dependencies.multiplatform.kotlin.testJvmJunit)

                implementation(Dependencies.test.junit)
                implementation(Dependencies.test.truth)
                implementation(Dependencies.test.jsonAssert)

                implementation(Dependencies.test.mockkJvm)
                implementation(Dependencies.test.mockitoInline)
                implementation(Dependencies.test.mockitoCore)

                implementation(Dependencies.d4l.fhirJvm)
                implementation(Dependencies.d4l.util)
            }
        }
    }
}

android {
    compileSdkVersion(LibraryConfig.android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(LibraryConfig.android.minSdkVersion)
        targetSdkVersion(LibraryConfig.android.targetSdkVersion)

        versionCode = 1
        versionName = "${project.version}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(
            mapOf(
                "clearPackageData" to "true"
            )
        )
    }

    resourcePrefix(LibraryConfig.android.resourcePrefix)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(setOf("src/androidMain/kotlin"))
            res.setSrcDirs(setOf("src/androidMain/res"))
        }

        getByName("test") {
            java.setSrcDirs(setOf("src/androidTest/kotlin"))
            res.setSrcDirs(setOf("src/androidTest/res"))
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            setMatchingFallbacks("release", "debug")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            setMatchingFallbacks("release", "debug")
        }
    }
}
