pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

includeBuild("gradlePlugin/helpers-dependency")

plugins {
    id("com.gradle.enterprise") version("3.7")
}

include(
    ":fhir-helper"
)

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "hc-fhir-helper-sdk-kmp"
