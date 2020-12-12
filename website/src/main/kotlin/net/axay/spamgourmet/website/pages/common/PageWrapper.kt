package net.axay.spamgourmet.website.pages.common

import kotlinx.html.*
import net.axay.spamgourmet.common.main.Values

inline fun HTML.pageWrapper(
    title: String,
    classes: String? = null,
    crossinline head: HEAD.() -> Unit,
    crossinline builder: MAIN.() -> Unit
) {

    head {
        meta(charset = Values.CHARSET.name())
        title(title)

        link("/static/styles/common.css", "stylesheet")
        
        head.invoke(this)
    }

    body("no_periphery") {

        header {
            ul("nav_box dark_background no_periphery") {
                li { a("/home") { +"Home" } }
                li { a("/about") { +"About" } }
                li { a("/contact") { +"Contact" } }
                li { a("/forum") { +"Forum" } }
                li { a("/donate") { +"Donate" } }
            }
        }

        main(classes) {
            builder.invoke(this)
        }

        footer {
            div("dark_background no_periphery") {

            }
        }

    }

}