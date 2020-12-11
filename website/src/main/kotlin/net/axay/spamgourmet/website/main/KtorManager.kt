package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.axay.spamgourmet.website.pages.pageIndex

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

    routing {

        static("/static") {
            resources("static")
        }

        get("/") {
            call.respondHtml { pageIndex() }
        }

        get("/github") {
            call.respondRedirect("https://github.com/bluefireoly/SpamgourmetReloaded")
        }

    }
}