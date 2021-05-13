@file:Suppress("PropertyName")

import org.kravemir.gradle.sass.SassCompileTask

val ktorVersion = "1.5.4"

plugins {
    `kotlin-build-script`
    `application-build-script`
    kotlin("plugin.serialization") version "1.5.0"
    id("org.kravemir.gradle.sass") version "1.2.4"
}

kotlin {
    js {
        browser {
            @Suppress("EXPERIMENTAL_API_USAGE")
            distribution {
                directory = File("$buildDir/processedResources/jvm/main/static/scripts")
            }
        }
    }

    sourceSets {
        getByName("jvmMain") {
            dependencies {
                implementation(project(":spamgourmet-common"))

                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")

                implementation("org.slf4j:slf4j-simple:1.7.30")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")

                implementation("at.favre.lib:bcrypt:0.9.0")

            }

            resources.exclude("static/styles")
        }
    }
}

application {
    mainClass.set("net.axay.spamgourmet.website.main.ManagerKt")
}

val sassTask by tasks.register("sassCompile", SassCompileTask::class) {
    srcDir = file("$projectDir/src/jvmMain/resources/static/styles")
    outDir = file("$buildDir/processedResources/jvm/main/static/styles")

    minify = true
}

tasks.jvmProcessResources.get().dependsOn(sassTask)
