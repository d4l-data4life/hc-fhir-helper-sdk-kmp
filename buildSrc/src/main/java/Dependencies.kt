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
    val android = "com.android.tools.build:gradle:${Versions.GradlePlugin.android}"
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.GradlePlugin.kotlin}"

    val dexcount = "com.getkeepsafe.dexcount:dexcount-gradle-plugin:${Versions.GradlePlugin.dexcount}"

    /**
     * [Gradle DownloadTask](https://github.com/michel-kraemer/gradle-download-task)
     */
    val downloadTask = "de.undercouch:gradle-download-task:${Versions.GradlePlugin.downloadTask}"

    val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.GradlePlugin.dokka}"
}


object Libraries {
    // Kotlin
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val kotlinStdLibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
    val kotlinStdLibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlinStdLibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"

    // Java
    val javaXAnnotation = "com.google.code.findbugs:jsr305:${Versions.javaXAnnotation}"

    // Android
    val androidSupportAppCompatV7 = "com.android.support:appcompat-v7:${Versions.androidSupport}"
    val androidSupportV4 = "com.android.support:support-v4:${Versions.androidSupport}"
    val androidSupportMediaCompat = "com.android.support:support-media-compat:${Versions.androidSupport}"
    val androidSupportDesign = "com.android.support:design:${Versions.androidSupport}"
    val androidSupportCustomtabs = "com.android.support:customtabs:${Versions.androidSupport}"

    val androidSupportConstraintLayout =
        "com.android.support.constraint:constraint-layout:${Versions.androidConstraintLayout}"

    // Google
    val googlePlayServicesBase = "com.google.android.gms:play-services-base:${Versions.googlePlayServices}"

    // Crypto
    val bouncyCastleJdk15 = "org.bouncycastle:bcprov-jdk15on:${Versions.bouncyCastle}"
    val tink = "com.google.crypto.tink:tink-android:${Versions.tink}"

    // Authorization
    val appAuth = "net.openid:appauth:${Versions.appAuth}"
    val appAuthPatch = "com.github.gesundheitscloud:AppAuth-Android:${Versions.appAuthPatch}"

    val scribeCore = "com.github.scribejava:scribejava-core:${Versions.scribe}"

    // Network
    val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    val testOkHttpMockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    val retrofitAdapterRxJava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"

    // Data
    val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    val fhir = "care.data4life.hc-fhir-sdk-java:hc-fhir-sdk-java:${Versions.fhir}"
    val sdkUtilAndroid = "care.data4life.hc-util-sdk-kmp:util-android:${Versions.sdkUtil}"
    val sdkUtilJvm = "care.data4life.hc-util-sdk-kmp:util-jvm:${Versions.sdkUtil}"
    val sdkUtilCommon = "care.data4life.hc-util-sdk-kmp:util:${Versions.sdkUtil}"

    // Date
    val threeTenBP = "org.threeten:threetenbp:${Versions.threeTenBP}"
    val threeTenABP = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTenABP}"

    // Injection
    val koinCore = "org.koin:koin-core:${Versions.koin}"
    val koinJava = "org.koin:koin-java:${Versions.koin}"
    val testKoin = "org.koin:koin-test:${Versions.koin}"

    // Rx
    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"

    // Ui
    val cmdClickt = "com.github.ajalt:clikt:${Versions.clikt}"


    // Test
    val testJunit = "junit:junit:${Versions.testJUnit}"

    val testTruth = "com.google.truth:truth:${Versions.testTruth}"

    val testMockito = "org.mockito:mockito-inline:${Versions.testMockito}"
    val testMockitoCore = "org.mockito:mockito-core:${Versions.testMockito}"

    val testKotlin = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val testKotlinCommon = "org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}"
    val testKotlinJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val testKotlinAnnotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}"

    val testKotlinMockkCommon = "io.mockk:mockk-common:${Versions.testMockk}"
    val testKotlinMockkAndroid = "io.mockk:mockk-android:${Versions.testMockk}"
    val testKotlinMockkJvm = "io.mockk:mockk:${Versions.testMockk}"

    val testJsonAssert = "org.skyscreamer:jsonassert:${Versions.testJsonAssert}"

    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    // Android Test
    val androidTestRunner = "com.android.support.test:runner:${Versions.androidTest}"
    val androidTestRules = "com.android.support.test:rules:${Versions.androidTest}"
    val androidTestOrchestrator = "com.android.support.test:orchestrator:${Versions.androidTest}"

    val androidTestEspressoCore = "com.android.support.test.espresso:espresso-core:${Versions.androidTestEspresso}"
    val androidTestEspressoIntents =
        "com.android.support.test.espresso:espresso-intents:${Versions.androidTestEspresso}"
    val androidTestEspressoWeb = "com.android.support.test.espresso:espresso-web:${Versions.androidTestEspresso}"
}
