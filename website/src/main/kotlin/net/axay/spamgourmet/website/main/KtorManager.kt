package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import net.axay.spamgourmet.website.pages.pageIndex
import net.axay.spamgourmet.website.security.SessionCookie
import net.axay.spamgourmet.website.security.login
import net.axay.spamgourmet.website.security.registration

object KtorManager {

    fun start() {

        embeddedServer(
            Netty,
            watchPaths = listOf("/website"),
            port = 8080,
            module = Application::mainModule
        ).start(wait = true)

    }

}

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

    }
}