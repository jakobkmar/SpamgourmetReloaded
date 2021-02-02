plugins {

    `common-build-script-kt`

    kotlin("plugin.serialization") version "1.4.21"

}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {

                // BLUEUTILS
                api("net.axay:BlueUtils:1.0.9")

                // SERIALIZATION
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

                // KMONGO
                api("org.litote.kmongo:kmongo-coroutine-core:4.2.4")
                api("org.litote.kmongo:kmongo-serialization-mapping:4.2.4")

                // MORDANT
                api("com.github.ajalt.mordant:mordant:2.0.0-alpha1")

                // CONFIG4K
                api("io.github.config4k:config4k:0.4.2")

            }
        }
    }
}