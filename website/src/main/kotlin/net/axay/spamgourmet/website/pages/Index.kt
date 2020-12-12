package net.axay.spamgourmet.website.pages

import kotlinx.html.*
import net.axay.spamgourmet.website.pages.common.pageWrapper
import net.axay.spamgourmet.website.util.svgObject

fun HTML.pageIndex() = pageWrapper("Home") {

/*    img(src = "static/svg/index/topimage.svg") {
        width = "1067px"
    }*/
    svgObject("static/svg/index/topimage.svg", "1067px")

}