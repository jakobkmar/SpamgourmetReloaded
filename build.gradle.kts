/**
 * PROJECT
 */

group = "net.axay"
version = "0.0.1"

/**
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

    // KOTLIN
    implementation(kotlin("stdlib"))

    // MONGODB
    implementation("org.litote.kmongo", "kmongo", "4.0.3")

    // SIMPLE JAVA MAIL
    implementation("org.simplejavamail", "simple-java-mail", "6.4.1")
    implementation("org.simplejavamail", "batch-module", "6.4.1")
    implementation("org.simplejavamail", "smime-module", "6.4.1")

    // SUBETHA SMTP
    implementation("org.subethamail", "subethasmtp", "3.1.7")

    // LOGGER
    implementation("org.slf4j", "slf4j-simple", "1.7.30")

    // BLUEUTILS
    implementation("net.axay.blueutils", "BlueUtils", "1.0-SNAPSHOT")

}

/**
 * BUILD
 */

@Suppress("MayBeConstant")
object BuildConstants {

    val JVM_VERSION_STRING = "13"
    val JVM_VERSION = JavaVersion.VERSION_13

    val MAIN_CLASS = "net.axay.spamgourmet.main.ManagerKt"

}

plugins {

    java
    kotlin("jvm") version "1.3.72"

    application

}

application {
    mainClassName = BuildConstants.MAIN_CLASS
}

java.sourceCompatibility = BuildConstants.JVM_VERSION

tasks {

    compileKotlin {
        kotlinOptions.jvmTarget = BuildConstants.JVM_VERSION_STRING
    }

    jar {
        manifest {
            attributes["Main-Class"] = BuildConstants.MAIN_CLASS
        }
    }

}