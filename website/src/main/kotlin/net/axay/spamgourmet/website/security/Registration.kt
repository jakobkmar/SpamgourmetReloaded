package net.axay.spamgourmet.website.security

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import net.axay.blueutils.database.mongodb.insertOneCatchDuplicate
import net.axay.spamgourmet.common.data.UserData
import net.axay.spamgourmet.common.data.UserInfo
import net.axay.spamgourmet.website.main.db
import org.litote.kmongo.*
import java.time.Instant

@Serializable
class RegistrationPostData(
    val emailAddress: String,
    val password: String,
    val username: String
)

fun Routing.registration() = post("/register") {

    val postData = call.receiveOrNull<RegistrationPostData>()
    if (postData == null) {
        call.respondRedirect("/error")
        return@post
    }

    val hashedPassword = BCrypt.withDefaults().hash(12, postData.password.toCharArray())

    val userData = UserData(
        postData.username,
        hashedPassword,
        postData.emailAddress,
        information = UserInfo(joined = Instant.now())
    )

    val insert = db.userData.insertOneCatchDuplicate(userData)

    if (!insert.wasAcknowledged()) {
        call.respondText("es gab schon jemanden in der datenbank")
    } else {
        call.respondText("jo nun in die datenbank hinzugefügt :poggles:")
    }

}