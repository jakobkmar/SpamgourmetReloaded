package net.axay.spamgourmet.website.security

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

data class SessionCookie(val username: String)

fun Routing.login() {

    route("/login") {
        authenticate("login") {
            post {
                call.sessions.set(
                    SessionCookie(
                    call.principal<UserIdPrincipal>()?.name ?: error("Missing principal")
                )
                )
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