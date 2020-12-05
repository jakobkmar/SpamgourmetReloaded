@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
 */

val main_class = "net.axay.spamgourmet.website.main.ManagerKt"
val ktor_version = "1.4.2"

/*
 * PLUGINS
 */

plugins {

    `common-build-script-kt`

    application

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
    // more
    implementation("io.ktor", "ktor-server-sessions", ktor_version)

    // LOGGER
    implementation("org.slf4j", "slf4j-simple", "1.7.30")

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(main_class)