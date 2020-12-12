package net.axay.spamgourmet.website.util

import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.htmlObject

fun FlowOrInteractiveOrPhrasingContent.svgObject(
    src: String,
    classes: String? = null,
    width: String? = null
) = htmlObject(classes) {
    type = "image/svg+xml"
    width?.let { this.width = it }
    data = src
}