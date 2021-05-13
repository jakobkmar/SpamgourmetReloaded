plugins {
    `kotlin-build-script`
    kotlin("plugin.serialization") version "1.5.0"
}

kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")

                api("org.litote.kmongo:kmongo-coroutine-core:4.2.7")
                api("org.litote.kmongo:kmongo-serialization-mapping:4.2.7")

                api("com.github.ajalt.mordant:mordant:2.0.0-beta1")

                api("io.github.config4k:config4k:0.4.2")
            }
        }
    }
}
