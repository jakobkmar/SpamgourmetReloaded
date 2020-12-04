@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
 */

val main_class = "net.axay.spamgourmet.website.main.ManagerKt"
val ktor_version = "1.4.0"

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
    implementation("io.ktor", "ktor-server-netty", ktor_version)

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(main_class)