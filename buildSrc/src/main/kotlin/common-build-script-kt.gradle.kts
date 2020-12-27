@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_11
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "net.axay"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
}

/*
 * PLUGINS
 */

plugins {

    kotlin("multiplatform")

}

kotlin {
    jvm()
    sourceSets {
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.7.0")
            }
        }
    }
}

/*
 * DEPENDENCY MANAGEMENT
 */

/*
 * BUILD
 */

// JVM VERSION

java.sourceCompatibility = JVM_VERSION
java.targetCompatibility = JVM_VERSION

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JVM_VERSION_STRING
}

// JUNIT

tasks.withType<Test> {
    useJUnitPlatform()
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString: String get() = if (majorVersion.toInt() <= 10) "1.$majorVersion" else majorVersion

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }
