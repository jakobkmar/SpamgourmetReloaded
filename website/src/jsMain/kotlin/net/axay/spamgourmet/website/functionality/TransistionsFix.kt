package net.axay.spamgourmet.website.functionality

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.removeClass
import org.w3c.dom.asList

fun fixTransitions() {
    window.onload = { _ ->
        document.getElementsByClassName("preload").asList()
            .forEach { it.removeClass("preload") }
    }
}