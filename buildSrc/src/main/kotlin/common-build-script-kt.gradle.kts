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

/*
 * PLUGINS
 */

plugins {

    kotlin("jvm")

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    jcenter()

    mavenLocal()
}

dependencies {

    // JUNIT
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.7.0")

}

/*
 * BUILD
 */

// JVM VERSION

java.sourceCompatibility = JVM_VERSION
java.targetCompatibility = JVM_VERSION

tasks {
    compileKotlin.configureJvmVersion()
    compileTestKotlin.configureJvmVersion()
}

// JUNIT

tasks.test {
    useJUnitPlatform()
}

/*
 * EXTENSIONS
 */

val JavaVersion.versionString: String get() = if (majorVersion.toInt() <= 10) "1.$majorVersion" else majorVersion

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }
