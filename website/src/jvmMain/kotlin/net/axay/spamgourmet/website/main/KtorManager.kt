package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import net.axay.spamgourmet.common.logging.logInfo
import net.axay.spamgourmet.common.logging.logSuccess
import net.axay.spamgourmet.website.pages.pageDashboard
import net.axay.spamgourmet.website.pages.pageError
import net.axay.spamgourmet.website.pages.pageIndex
import net.axay.spamgourmet.website.security.login
import net.axay.spamgourmet.website.security.registration

fun Application.mainModule() {

    if (developmentMode)
        logInfo("Click this link for local testing: http://127.0.0.1:8080")

    this.login()

    install(DefaultHeaders)
    install(Compression)

    install(Sessions) {
        this.login()
    }

    install(ContentNegotiation) {
        json()
    }

    routing {

        static("/static") {
            resources("static")
        }

        this.pageIndex()

        get("/github") {
            call.respondRedirect("https://github.com/bluefireoly/SpamgourmetReloaded")
        }

        this.registration()
        this.login()
        this.pageDashboard()
        this.pageError()

    }

    environment.monitor.subscribe(ApplicationStarted) {
        logSuccess("Started application")
    }

}