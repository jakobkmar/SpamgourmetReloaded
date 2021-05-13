@file:Suppress("unused")

package net.axay.spamgourmet.common.logging

import com.github.ajalt.colormath.Color
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.TextColors.*
import com.github.ajalt.mordant.terminal.TextStyles.bold
import com.github.ajalt.mordant.terminal.TextStyles.underline

val terminal = Terminal()

fun log(message: Any?, prefix: String = "") {
    terminal.println(
        ("$prefix ${
            terminal.renderMuted("(${Thread.currentThread().name}) >>")
        } $message")
    )
}

private fun renderMessage(message: Any?, color: Color? = null) =
    terminal.render(message.toString(), TextStyle(color, bold = true))

fun logInfo(message: Any?) =
    log(renderMessage(message), terminal.render("INFO", bold.style))

fun logMajorInfo(message: Any?) =
    log(renderMessage(message), terminal.render("INFO", TextStyle(magenta, bold = true)))

fun logSuccess(message: Any?) =
    log(renderMessage(message), terminal.render("INFO", TextStyle(brightGreen, bold = true)))

fun logWarning(message: Any?) =
    log(renderMessage(message, yellow), terminal.render("WARN", TextStyle(yellow, bold = true)))

fun logError(message: Any?) =
    log(renderMessage(message, red), terminal.render("ERROR", TextStyle(brightRed, bold = true)))

fun Throwable.logThis(severe: Boolean = true) {
    val string = stackTraceToString()
    val message = string.lines().first()
    val splitMessage = message.split(": ")
    val formattedMessage =
        underline(splitMessage.first().removePrefix("java.lang.")) + ": ${
            splitMessage.takeLast(splitMessage.lastIndex).joinToString(": ")
        }"
    val finalMessage = "$formattedMessage${terminal.renderMarkdown(string.removePrefix(message))}"
    if (severe) logError(finalMessage) else logWarning(finalMessage)
}
