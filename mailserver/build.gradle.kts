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

    // distribution

    application

    id("com.github.johnrengelman.shadow") version "6.1.0"

}

/*
 * DEPENDENCY MANAGEMENT
 */

kotlin {

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val jvmMain by getting {
            dependencies {

                implementation(project(":common"))

                // SIMPLE JAVA MAIL
                implementation("org.simplejavamail:simple-java-mail:6.4.4")
                implementation("org.simplejavamail:batch-module:6.4.4")
                implementation("org.simplejavamail:smime-module:6.4.4")

                // SUBETHA SMTP
                implementation("org.subethamail:subethasmtp:3.1.7")

                // LOGGER
                implementation("org.slf4j:slf4j-simple:1.7.30")

            }
        }

    }

}

/*
 * BUILD
 */

// MAIN CLASS

application {
    mainClass.set(main_class)

    // until https://github.com/johnrengelman/shadow/issues/609 is fixed
    @Suppress("DEPRECATION")
    mainClassName = main_class
}

// SHADOW

tasks.shadowJar {
    archiveBaseName.set(project.name)
    archiveVersion.set("")
    archiveClassifier.set("")
}
