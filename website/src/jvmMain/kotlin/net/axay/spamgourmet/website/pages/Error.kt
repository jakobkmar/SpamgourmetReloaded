package net.axay.spamgourmet.website.pages

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.h1
import kotlinx.html.link
import net.axay.spamgourmet.website.pages.common.pageWrapper

fun Routing.pageError() {

    get("/error") { call.respondHtml {

        pageWrapper("Error",
            head = {
                link("/static/styles/error.css", "stylesheet")
            }
        ) {

            h1 { +"An error has occured" }

        }

    } }

}