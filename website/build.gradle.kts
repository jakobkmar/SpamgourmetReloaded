@file:Suppress("PropertyName")

import org.kravemir.gradle.sass.SassCompileTask

/*
 * BUILD CONSTANTS
 */

val main_class = "net.axay.spamgourmet.website.main.ManagerKt"
val ktor_version = "1.5.0"

/*
 * PLUGINS
 */

plugins {

    `common-build-script-kt`

    // compilation

    kotlin("plugin.serialization") version "1.4.21"

    id("org.kravemir.gradle.sass") version "1.2.4"

    // distribution

    application

    id("com.github.johnrengelman.shadow") version "6.1.0"

}

/*
 * DEPENDENCY MANAGEMENT
 */

kotlin {

    js {
        browser {
            @Suppress("EXPERIMENTAL_API_USAGE")
            distribution {
                directory = File("$buildDir/processedResources/jvm/main/static/scripts")
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {

        val jvmMain by getting {

            dependencies {

                implementation(project(":common"))

                // KTOR
                // core
                implementation("io.ktor:ktor-server-netty:$ktor_version")
                implementation("io.ktor:ktor-server-core:$ktor_version")
                // website building
                implementation("io.ktor:ktor-html-builder:$ktor_version")
                // sessions and authentication
                implementation("io.ktor:ktor-server-sessions:$ktor_version")
                implementation("io.ktor:ktor-auth:$ktor_version")
                // serialization
                implementation("io.ktor:ktor-serialization:$ktor_version")

                // LOGGER
                implementation("org.slf4j:slf4j-simple:1.7.30")

                // KOTLINX
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

                // CRYPT
                implementation("at.favre.lib:bcrypt:0.9.0")

            }

            resources.exclude("static/styles")

        }

    }
}

/*
 * BUILD
 */

// MAIN CLASS

application {
    mainClass.set(main_class)

    // until https://github.com/johnrengelman/shadow/issues/609
    @Suppress("DEPRECATION")
    mainClassName = main_class
}

// SCSS

val sassTask by tasks.register("sassCompile", SassCompileTask::class) {
    srcDir = file("$projectDir/src/jvmMain/resources/static/styles")
    outDir = file("$buildDir/processedResources/jvm/main/static/styles")

    minify = true
}

tasks.jvmProcessResources.get().dependsOn(sassTask)
