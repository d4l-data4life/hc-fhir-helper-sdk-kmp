import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

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

object GradlePlugins {
    const val android = "com.android.tools.build:gradle:${Versions.GradlePlugins.android}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.GradlePlugins.kotlin}"
    const val gitPublish = "care.data4life:gradle-git-publish:${Versions.GradlePlugins.gitPublish}"
}

fun PluginDependenciesSpec.kotlinMultiplatform(apply: Boolean = true): PluginDependencySpec =
    id("org.jetbrains.kotlin.multiplatform").version(Versions.GradlePlugins.kotlin).apply(apply)

fun PluginDependenciesSpec.kotlinMultiplatform(): PluginDependencySpec =
    id("org.jetbrains.kotlin.multiplatform")


fun PluginDependenciesSpec.androidApp(): PluginDependencySpec =
    id("com.android.application")

fun PluginDependenciesSpec.androidLibrary(): PluginDependencySpec =
    id("com.android.library")

fun PluginDependenciesSpec.androidKotlin(): PluginDependencySpec =
    id("kotlin-android")


fun PluginDependenciesSpec.dependencyUpdates(): PluginDependencySpec =
    id("com.github.ben-manes.versions").version(Versions.GradlePlugins.dependencyUpdates)
