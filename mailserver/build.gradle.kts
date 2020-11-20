@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
 */

val MAIN_CLASS = "net.axay.spamgourmet.main.ManagerKt"

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

    // KMONGO
    compileOnly("org.litote.kmongo", "kmongo-core", "4.2.0")
    compileOnly("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.0")

    // SIMPLE JAVA MAIL
    implementation("org.simplejavamail", "simple-java-mail", "6.4.4")
    implementation("org.simplejavamail", "batch-module", "6.4.4")
    implementation("org.simplejavamail", "smime-module", "6.4.4")

    // SUBETHA SMTP
    implementation("org.subethamail", "subethasmtp", "3.1.7")

    // LOGGER
    implementation("org.slf4j", "slf4j-simple", "1.7.30")

    // BLUEUTILS
    implementation("net.axay.blueutils", "BlueUtils", "1.0-SNAPSHOT")

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(MAIN_CLASS)