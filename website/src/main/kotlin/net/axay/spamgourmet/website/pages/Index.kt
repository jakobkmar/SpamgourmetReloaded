package net.axay.spamgourmet.website.pages

import kotlinx.html.*
import net.axay.spamgourmet.website.pages.common.pageWrapper
import net.axay.spamgourmet.website.util.svgObject

fun HTML.pageIndex() = pageWrapper("Home",
    head = {
        link("/static/styles/index.css", "stylesheet")
    }
) {

    div {
        div("flex_center") {
            svgObject("static/svg/index/topimage.svg", "topimage")
        }
        div("account_panel flex_center") {
            div("login_panel account_panel_part") {
                span { +"LOGIN" }
            }
            div("account_panel_placeholder")
            div("signup_panel account_panel_part") {
                span { +"SIGN UP" }
            }
        }
    }

}