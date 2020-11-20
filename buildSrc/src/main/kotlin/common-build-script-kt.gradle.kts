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

    mavenLocal()
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

/*
 * EXTENSIONS
 */

val JavaVersion.versionString get() = majorVersion.let {
    val version = it.toInt()
    if (version <= 10) "1.$it" else it
}

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }
