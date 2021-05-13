@file:Suppress("PropertyName")

plugins {
    `kotlin-build-script`
    `application-build-script`
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":spamgourmet-common"))

                implementation("net.axay:simplekotlinmail-core:1.3.0")
                implementation("net.axay:simplekotlinmail-server:1.3.0")
                implementation("net.axay:simplekotlinmail-client:1.3.0")

                implementation("org.slf4j:slf4j-simple:1.7.30")
            }
        }
    }
}

application {
    mainClass.set("net.axay.spamgourmet.mailserver.main.ManagerKt")
}
