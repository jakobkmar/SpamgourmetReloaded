@file:Suppress("unused")

package net.axay.spamgourmet.common.logging

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.TextColors.*

val terminal = Terminal()

fun log(message: Any?, prefix: String = "") {
    terminal.println("$prefix ${gray(">>")} $message")
}

fun logInfo(message: Any?) =
    log((brightWhite)(message.toString()), white("INFO"))

fun logMajorInfo(message: Any?) =
    log((brightWhite)(message.toString()), magenta("INFO"))

fun logSuccess(message: Any?) =
    log((brightWhite)(message.toString()), green("INFO"))

fun logWarning(message: Any?) =
    log((yellow)(message.toString()), brightYellow("WARN"))

fun logError(message: Any?) =
    log((red)(message.toString()), brightRed("ERROR"))
