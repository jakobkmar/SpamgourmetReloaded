package net.axay.spamgourmet.website.pages

import kotlinx.html.*
import net.axay.spamgourmet.website.pages.common.pageWrapper
import net.axay.spamgourmet.website.util.svgObject

fun HTML.pageIndex() = pageWrapper("Home",
    head = {
        link("/static/styles/index.css", "stylesheet")
    }
) {

    div("topview flex_center") {
        div("topimage_container") {
            svgObject("static/svg/index/topimage.svg", "topimage")
        }

        div("account_panel flex_center") {
            div("login_panel account_panel_part") {
                h1 { +"LOGIN" }
                form {
                    input(name = "username") {
                        placeholder = "USERNAME"
                    }
                    input(name = "password", type = InputType.password) {
                        placeholder = "PASSWORD"
                    }
                    div("flex_right") {
                        button(type = ButtonType.submit, formMethod = ButtonFormMethod.post) {
                            formAction = "/login"
                            +"Log in"
                        }
                    }
                }
                span {
                    +"You have to log in to access your dashboard!"
                }
            }

            div("account_panel_placeholder")

            div("signup_panel account_panel_part") {
                h1 { +"SIGN UP" }
            }
        }
    }

}