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
    id("maven-publish")
}

apply {
    plugin("care.data4life.git-publish")
}

group = LibraryConfig.group

kotlin {
    android {
        publishLibraryVariants("release")
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibCommon)

                implementation(Dependencies.d4l.utilCommon)
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
                api(Dependencies.d4l.utilAndroid)
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
                implementation(Dependencies.d4l.utilAndroid)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependencies.multiplatform.kotlin.stdlibJdk8)

                api(Dependencies.d4l.fhirJvm)
                api(Dependencies.d4l.utilJvm)
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
                implementation(Dependencies.d4l.utilJvm)
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

//publishing {
//    repositories {
//        maven {
//            name = "GithubPackages"
//            url = uri("https://maven.pkg.github.com/d4l-data4life/hc-fhir-helper-sdk-kmp")
//            credentials {
//                username = project.findProperty("gpr.user") ?: System.getenv("PACKAGE_REGISTRY_USERNAME")
//                password = project.findProperty("gpr.key") ?: System.getenv("PACKAGE_REGISTRY_TOKEN")
//            }
//        }
//
//        publications.all { publication ->
//            publication.groupId = "$LibraryConfig.group.$LibraryConfig.artifactId"
//            publication.artifactId = "${LibraryConfig.artifactId}"
//            publication.version = "${LibraryConfig.version}"
//
//            if (publication.name == "androidRelease") {
//                publication.artifactId = "$project.name-android"
//            } else if (publication.name == "metadata") {
//                publication.artifactId = "$project.name-metadata"
//            } else if (publication.name == "jvm") {
//                publication.artifactId = "$project.name-jvm"
//            } else if (publication.name == 'kotlinMultiplatform') {
//                publication.artifactId = "$project.name"
//            }
//        }
//    }
//}

configure<PublishingExtension> {
    publications {
        withType<MavenPublication> {
            groupId = "${LibraryConfig.publish.groupId}.${LibraryConfig.publish.name}"

            pom {
                name.set(LibraryConfig.publish.name)
                url.set(LibraryConfig.publish.url)
                inceptionYear.set(LibraryConfig.publish.year)

                licenses {
                    license {
                        name.set(LibraryConfig.publish.licenseName)
                        url.set(LibraryConfig.publish.licenseUrl)
                        distribution.set(LibraryConfig.publish.licenseDistribution)
                    }
                }

                developers {
                    developer {
                        id.set(LibraryConfig.publish.developerId)
                        name.set(LibraryConfig.publish.developerName)
                        email.set(LibraryConfig.publish.developerEmail)
                    }
                }

                scm {
                    connection.set(LibraryConfig.publish.scmConnection)
                    developerConnection.set(LibraryConfig.publish.scmDeveloperConnection)
                    url.set(LibraryConfig.publish.scmUrl)
                }
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url =
                uri("https://maven.pkg.github.com/${LibraryConfig.githubOwner}/${LibraryConfig.githubRepository}")
            credentials {
                username = (project.findProperty("gpr.user")
                    ?: System.getenv("PACKAGE_REGISTRY_USERNAME")).toString()
                password = (project.findProperty("gpr.key")
                    ?: System.getenv("PACKAGE_REGISTRY_TOKEN")).toString()
            }
        }

        val target = "file://${project.buildDir}/gitPublish"

        maven {
            name = "ReleasePackages"
            url = uri("$target/releases")
        }

        maven {
            name = "SnapshotPackages"
            url = uri("$target/snapshots")
        }

        maven {
            name = "FeaturePackages"
            url = uri("$target/features")
        }
    }
}

configure<care.data4life.gradle.git.publish.GitPublishExtension> {
    repoUri.set("git@github.com:d4l-data4life/maven-repository.git")

    branch.set("main")

    contents {
    }

    preserve {
        include("**/*")
    }

    commitMessage.set("Publish ${LibraryConfig.name} $version")
}

val publishFeature by tasks.creating {
    group = "publishing"

    dependsOn("gitPublishReset")
    dependsOn("gitPublishCopy")
    dependsOn("publishAllPublicationsToFeaturePackagesRepository")
    dependsOn("gitPublishCommit")
    dependsOn("gitPublishPush")

    tasks.findByName("gitPublishCopy")!!.mustRunAfter("gitPublishReset")
    tasks.findByName("publishAllPublicationsToFeaturePackagesRepository")!!.mustRunAfter("gitPublishCopy")
    tasks.findByName("gitPublishCommit")!!.mustRunAfter("publishAllPublicationsToFeaturePackagesRepository")
    tasks.findByName("gitPublishPush")!!.mustRunAfter("gitPublishCommit")
}

val publishSnapshot by tasks.creating {
    group = "publishing"

    dependsOn("gitPublishReset")
    dependsOn("gitPublishCopy")
    dependsOn("publishAllPublicationsToSnapshotPackagesRepository")
    dependsOn("gitPublishCommit")
    dependsOn("gitPublishPush")

    tasks.findByName("gitPublishCopy")!!.mustRunAfter("gitPublishReset")
    tasks.findByName("publishAllPublicationsToSnapshotPackagesRepository")!!.mustRunAfter("gitPublishCopy")
    tasks.findByName("gitPublishCommit")!!.mustRunAfter("publishAllPublicationsToSnapshotPackagesRepository")
    tasks.findByName("gitPublishPush")!!.mustRunAfter("gitPublishCommit")
}

val publishRelease by tasks.creating {
    group = "publishing"

    dependsOn("gitPublishReset")
    dependsOn("gitPublishCopy")
    dependsOn("publishAllPublicationsToReleasePackagesRepository")
    dependsOn("gitPublishCommit")
    dependsOn("gitPublishPush")

    tasks.findByName("gitPublishCopy")!!.mustRunAfter("gitPublishReset")
    tasks.findByName("publishAllPublicationsToReleasePackagesRepository")!!.mustRunAfter("gitPublishCopy")
    tasks.findByName("gitPublishCommit")!!.mustRunAfter("publishAllPublicationsToReleasePackagesRepository")
    tasks.findByName("gitPublishPush")!!.mustRunAfter("gitPublishCommit")
}
