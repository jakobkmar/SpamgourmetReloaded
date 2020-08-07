package net.axay.spamgourmetreloaded.util

import net.axay.spamgourmetreloaded.main.ValueHolder
import java.io.PrintStream
import java.lang.StringBuilder

fun logInfo(message: Any?, stream: PrintStream = System.out) {
    stream.println("${ValueHolder.PREFIX}$message")
}

fun logInfoMultiline(vararg messageLines: Any?) {
    val builder = StringBuilder("${ValueHolder.PREFIX}:")
    messageLines.forEach {
        var isSecond = false
        it.toString().split('\n').forEach { innerLine ->
            if (!isSecond) {
                builder.append('\n').append(" -> ").append(innerLine)
                isSecond = true
            } else {
                builder.append('\n').append("  |-> ").append(innerLine)
            }
        }
    }
    stream.println(builder.toString())
}