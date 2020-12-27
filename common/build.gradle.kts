plugins {

    `common-build-script-kt`

    kotlin("plugin.serialization") version "1.4.21"

}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {

                // BLUEUTILS
                api("net.axay:BlueUtils:1.0.3")

                // SERIALIZATION
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

                // KMONGO
                api("org.litote.kmongo:kmongo-core:4.2.3")
                api("org.litote.kmongo:kmongo-serialization-mapping:4.2.3")

                // MORDANT
                api("com.github.ajalt.mordant:mordant:2.0.0-alpha1")

            }
        }
    }
}