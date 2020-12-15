package net.axay.spamgourmet.website.main

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
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

data class SessionCookie(val username: String)

fun Application.mainModule() {

    install(Authentication) {
        form("login") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                UserIdPrincipal(credentials.name)
            }
        }
    }

    install(Sessions) {
        cookie<SessionCookie>("SESSION")
    }

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

        route("/login") {
            authenticate("login") {
                post {
                    call.sessions.set(SessionCookie(
                        call.principal<UserIdPrincipal>()?.name ?: error("Missing principal")
                    ))
                    call.respondRedirect("/logintest")
                }
            }
        }

        get("/logintest") {
            val session = call.sessions.get<SessionCookie>()
            if (session != null) {
                call.respondText("Welcome back, ${session.username}!")
            } else {
                call.respondText("You're logged out!")
            }
        }

        get("/logout") {
            call.sessions.clear<SessionCookie>()
        }

    }
}