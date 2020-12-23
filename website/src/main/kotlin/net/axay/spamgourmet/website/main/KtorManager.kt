package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import net.axay.spamgourmet.common.logging.logInfo
import net.axay.spamgourmet.website.pages.pageDashboard
import net.axay.spamgourmet.website.pages.pageError
import net.axay.spamgourmet.website.pages.pageIndex
import net.axay.spamgourmet.website.security.SessionCookie
import net.axay.spamgourmet.website.security.login
import net.axay.spamgourmet.website.security.registration
import net.axay.spamgourmet.website.util.kotlinxSessionSerializer

fun Application.mainModule() {

    if (developmentMode)
        logInfo("Click this link for local testing: http://127.0.0.1:8080")

    login()

    install(Sessions) {
        login()
    }

    install(ContentNegotiation) {
        json()
    }

    routing {

        static("/static") {
            resources("static")
        }

        pageIndex()

        get("/github") {
            call.respondRedirect("https://github.com/bluefireoly/SpamgourmetReloaded")
        }

        registration()
        login()
        pageDashboard()
        pageError()

    }
}