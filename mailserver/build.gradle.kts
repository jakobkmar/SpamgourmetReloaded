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

                // SIMPLE KOTLIN MAIL
                implementation("net.axay:simplekotlinmail-core:1.3.0")
                implementation("net.axay:simplekotlinmail-server:1.3.0")
                implementation("net.axay:simplekotlinmail-client:1.3.0")

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
