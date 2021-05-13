@file:Suppress("unused")

package net.axay.spamgourmet.common.logging

import com.github.ajalt.colormath.Color
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.terminal.Terminal

val terminal = Terminal()

fun log(prefix: String, message: Any?, messageColor: Color? = null) {
    terminal.println(
        "$prefix ${
            terminal.theme.muted("(${Thread.currentThread().name}) >>")
        } ${TextStyle(messageColor, bold = true).invoke(message.toString())}"
    )
}

fun logInfo(message: Any?) = log(bold("INFO"), message)

fun logMajorInfo(message: Any?) = log(magenta(bold("INFO")), message)

fun logSuccess(message: Any?) = log(brightGreen(bold("INFO")), message)

fun logWarning(message: Any?) = log(yellow(bold("WARN")), message, yellow)

fun logError(message: Any?) = log(brightRed(bold("ERROR")), message, red)
