package net.axay.spamgourmet.common.logging

import java.io.PrintStream

fun log(message: Any?, stream: PrintStream = System.out, prefix: String = "") {
    stream.println("$prefix >> $message")
}

fun logInfo(message: Any?, stream: PrintStream = System.out) =
    log(message, stream, "INFO")

fun logWarning(message: Any?, stream: PrintStream = System.out) =
    log(message, stream, "WARN")

fun logError(message: Any?, stream: PrintStream = System.err) =
    log(message, stream, "ERROR")
