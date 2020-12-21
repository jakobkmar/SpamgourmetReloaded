package net.axay.spamgourmet.website.pages

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*
import kotlinx.html.link
import net.axay.spamgourmet.website.pages.common.*

fun Routing.dashboard() {

    get("/dashboard") { call.respondHtml {

        pageWrapper("Dashboard",
            head = {
                link("/static/styles/dashboard.css", "stylesheet")
            }
        ) {



        }

    } }

}