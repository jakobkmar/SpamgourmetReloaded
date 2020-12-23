plugins {

    `common-build-script-kt`

    kotlin("plugin.serialization") version "1.4.21"

}

dependencies {

    // BLUEUTILS
    api("net.axay", "BlueUtils", "1.0.1")

    // SERIALIZATION
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.0.1")

    // KMONGO
    api("org.litote.kmongo", "kmongo-core", "4.2.0")
    api("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.0")

}