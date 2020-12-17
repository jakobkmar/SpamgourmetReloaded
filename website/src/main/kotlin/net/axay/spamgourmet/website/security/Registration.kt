package net.axay.spamgourmet.website.security

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

@Serializable
class RegistrationPostData(
    val emailAddress: String,
    val password: String,
    val username: String,
    val name: String
)

fun Routing.registration() = post("/register") {

    val postData = call.receive<RegistrationPostData>()

}