package net.axay.spamgourmet.website.security

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import net.axay.spamgourmet.common.data.UserData
import net.axay.spamgourmet.website.main.db
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

data class SessionCookie(val username: String)

fun Application.login() {

    install(Authentication) {
        form("login") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->

                val hashedPassword = db.userData.findOne(
                    UserData::username eq credentials.name
                )?.password ?: return@validate null

                if (BCrypt.verifyer().verify(credentials.password.toCharArray(), hashedPassword).verified)
                    UserIdPrincipal(credentials.name)
                else null

            }
        }
    }

}

fun Routing.login() {

    route("/login") {
        authenticate("login") {
            post {
                val name = call.principal<UserIdPrincipal>()?.name ?: error("Missing principal")
                call.sessions.set(SessionCookie(name))
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