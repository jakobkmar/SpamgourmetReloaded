@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
 */

val main_class = "net.axay.spamgourmet.website.main.ManagerKt"
val ktor_version = "1.4.3"

/*
 * PLUGINS
 */

plugins {

    `common-build-script-kt`

    application

    kotlin("plugin.serialization") version "1.4.10"

}

/*
 * DEPENDENCY MANAGEMENT
 */

dependencies {

    implementation(project(":common"))

    // KTOR
    // core
    implementation("io.ktor", "ktor-server-netty", ktor_version)
    implementation("io.ktor", "ktor-server-core", ktor_version)
    // website building
    implementation("io.ktor", "ktor-html-builder", ktor_version)
    // sessions and authentication
    implementation("io.ktor", "ktor-server-sessions", ktor_version)
    implementation("io.ktor", "ktor-auth", ktor_version)
    // serialization
    implementation("io.ktor", "ktor-serialization", ktor_version)

    // LOGGER
    implementation("org.slf4j", "slf4j-simple", "1.7.30")

    // KOTLINX
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(main_class)