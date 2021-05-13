package net.axay.spamgourmet.website.util

import kotlinx.html.FlowOrInteractiveOrPhrasingContent
import kotlinx.html.htmlObject
import kotlinx.html.tabIndex

fun FlowOrInteractiveOrPhrasingContent.svgObject(
    src: String,
    classes: String? = null
) = htmlObject(classes) {
    type = "image/svg+xml"
    tabIndex = "-1"
    data = src
}