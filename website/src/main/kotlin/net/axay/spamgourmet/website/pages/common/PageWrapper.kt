package net.axay.spamgourmet.website.pages.common

import kotlinx.html.*
import net.axay.spamgourmet.common.main.Values

inline fun HTML.pageWrapper(title: String, crossinline builder: MAIN.() -> Unit) {

    head {
        meta(charset = Values.CHARSET.name())
        title(title)

        link("/static/styles/index.css", "stylesheet")
    }

    body("no_periphery") {

        header {
            div("nav_box dark_background no_periphery") {
                ul("no_periphery") {
                    li { a("/home") { +"Home" } }
                    li { a("/about") { +"About" } }
                    li { a("/contact") { +"Contact" } }
                    li { a("/forum") { +"Forum" } }
                    li { a("/donate") { +"Donate" } }
                }
            }
        }

        main {
            builder.invoke(this)
        }

        footer {
            div("dark_background no_periphery") {

            }
        }

    }

}