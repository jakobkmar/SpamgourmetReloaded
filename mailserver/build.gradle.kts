@file:Suppress("PropertyName")

/*
 * BUILD CONSTANTS
 */

val main_class = "net.axay.spamgourmet.mailserver.main.ManagerKt"

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

    // SIMPLE JAVA MAIL
    implementation("org.simplejavamail", "simple-java-mail", "6.4.4")
    implementation("org.simplejavamail", "batch-module", "6.4.4")
    implementation("org.simplejavamail", "smime-module", "6.4.4")

    // SUBETHA SMTP
    implementation("org.subethamail", "subethasmtp", "3.1.7")

    // LOGGER
    implementation("org.slf4j", "slf4j-simple", "1.7.30")

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(main_class)