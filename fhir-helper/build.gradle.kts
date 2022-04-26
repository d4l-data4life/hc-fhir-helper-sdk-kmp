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

import care.data4life.gradle.fhir.helper.dependency.Dependency
import care.data4life.gradle.fhir.helper.config.LibraryConfig

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    // Publish
    id("care.data4life.gradle.fhir.helper.script.publishing-config")
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
                implementation(Dependency.multiplatform.kotlin.stdlibCommon)

                implementation(Dependency.d4l.utilCommon)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testCommon)
                implementation(Dependency.multiplatform.kotlin.testCommonAnnotations)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibAndroid)

                api(Dependency.d4l.fhirJvm)
                api(Dependency.d4l.utilAndroid)
            }
        }
        val androidAndroidTestRelease by getting
        val androidTestFixtures by getting
        val androidTestFixturesDebug by getting
        val androidTestFixturesRelease by getting
        val androidTest by getting {
            dependsOn(androidAndroidTestRelease)
            dependsOn(androidTestFixtures)
            dependsOn(androidTestFixturesDebug)
            dependsOn(androidTestFixturesRelease)
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)

                implementation(Dependency.test.junit)
                implementation(Dependency.test.truth)
                implementation(Dependency.test.jsonAssert)

                implementation(Dependency.test.mockkJvm)
                implementation(Dependency.test.mockitoInline)
                implementation(Dependency.test.mockitoCore)

                implementation(Dependency.android.robolectric)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.stdlibJdk8)

                api(Dependency.d4l.fhirJvm)
                api(Dependency.d4l.utilJvm)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.testJvm)
                implementation(Dependency.multiplatform.kotlin.testJvmJunit)

                implementation(Dependency.test.junit)
                implementation(Dependency.test.truth)
                implementation(Dependency.test.jsonAssert)

                implementation(Dependency.test.mockkJvm)
                implementation(Dependency.test.mockitoInline)
                implementation(Dependency.test.mockitoCore)
            }
        }
    }
}

android {
    compileSdk = LibraryConfig.android.compileSdkVersion

    defaultConfig {
        minSdk = LibraryConfig.android.minSdkVersion
        targetSdk = LibraryConfig.android.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf("clearPackageData" to "true")
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
            isJniDebuggable = true
            isMinifyEnabled = false
            matchingFallbacks.addAll(
                listOf("release", "debug")
            )
        }
        getByName("release") {
            isJniDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            matchingFallbacks.addAll(
                listOf("release", "debug")
            )
        }
    }
}
