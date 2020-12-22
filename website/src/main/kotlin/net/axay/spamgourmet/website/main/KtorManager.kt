package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.sessions.*
import net.axay.spamgourmet.website.pages.dashboard
import net.axay.spamgourmet.website.pages.pageError
import net.axay.spamgourmet.website.pages.pageIndex
import net.axay.spamgourmet.website.security.SessionCookie
import net.axay.spamgourmet.website.security.login
import net.axay.spamgourmet.website.security.registration

fun Application.mainModule() {

    login()

    install(Sessions) {
        cookie<SessionCookie>("SESSION")
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
        dashboard()
        pageError()

    }
}