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

    application

    kotlin("plugin.serialization") version "1.4.10"

    id("org.kravemir.gradle.sass") version "1.2.4"

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

    // CRYPT
    implementation("at.favre.lib", "bcrypt", "0.9.0")

}

/*
 * BUILD
 */

// MAIN CLASS

application.mainClass.set(main_class)

// SCSS

val sassTask by tasks.register("sassCompile", SassCompileTask::class) {
    srcDir = file("$projectDir/src/main/resources/static/styles")
    outDir = file("$buildDir/resources/main/static/styles")

    minify = true
}

tasks.build.get().dependsOn(sassTask)

sourceSets.main.get().resources.exclude("static/styles")
