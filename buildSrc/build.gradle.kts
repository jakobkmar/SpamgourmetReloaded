plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.5.0"))
    implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:7.0.0")
}
