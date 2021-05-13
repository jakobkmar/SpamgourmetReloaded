package net.axay.spamgourmet.website.security

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.core.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import net.axay.blueutils.database.mongodb.insertOneCatchDuplicate
import net.axay.spamgourmet.common.data.PasswordAlgorithm
import net.axay.spamgourmet.common.data.UserData
import net.axay.spamgourmet.common.data.UserInfo
import net.axay.spamgourmet.common.data.UserLoginData
import net.axay.spamgourmet.website.main.db
import net.axay.spamgourmet.website.pages.common.pageWrapper
import org.litote.kmongo.*
import java.time.Instant

@Serializable
class RegistrationPostData(
    val username: String,
    val emailAddress: String,
    val password: String
)

fun Routing.registration() {

    get("/register") { call.respondHtml {

        pageWrapper("Register",
            head = {
                link("/static/styles/register.css", "stylesheet")
            }
        ) {

            form {
                input(name = "username", type = InputType.text) {
                    placeholder = "USERNAME"
                }
                input(name = "emailAddress", type = InputType.email) {
                    placeholder = "EMAIL ADDRESS"
                }
                input(name = "password", type = InputType.password) {
                    placeholder = "PASSWORD"
                }
                button(type = ButtonType.submit, formMethod = ButtonFormMethod.post) {
                    formAction = "/register"
                    +"SIGN UP"
                }
            }

        }

    } }

    post("/register") {

        val parameters = call.receiveParameters()
        val postData = RegistrationPostData(
            parameters["username"] ?: return@post,
            parameters["emailAddress"] ?: return@post,
            parameters["password"] ?: return@post
        )

        val hashedPassword = BCrypt.withDefaults().hash(12, postData.password.toCharArray())

        val userLoginData = UserLoginData(
            postData.username,
            hashedPassword,
            PasswordAlgorithm.BCRYPT
        )

        val userData = UserData(
            postData.username,
            postData.emailAddress,
            information = UserInfo(joined = Instant.now())
        )

        val userLoginDataInsert = db.userLoginData.insertOneCatchDuplicate(userLoginData)
        val userDataInsert = db.userData.insertOneCatchDuplicate(userData)

        if (userLoginDataInsert.wasAcknowledged() && userDataInsert.wasAcknowledged()) {

            call.respondRedirect("/")

        } else call.respond(HttpStatusCode.NotAcceptable)

    }

}