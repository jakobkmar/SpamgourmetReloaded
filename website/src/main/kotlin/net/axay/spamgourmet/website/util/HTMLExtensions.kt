package net.axay.spamgourmet.website.util

import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.htmlObject

fun FlowOrInteractiveOrPhrasingContent.svgObject(
    src: String,
    width: String? = null,
    classes: String? = null
) = htmlObject(classes) {
    type = "image/svg+xml"
    width?.let { this.width = it }
    data = "static/svg/index/topimage.svg"
}