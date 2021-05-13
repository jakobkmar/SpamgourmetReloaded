package net.axay.spamgourmet.mailserver.console

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

object ConsoleListener {
    fun listen() {
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val input = readLine() ?: continue
                when (input) {
                    "stop" -> {
                        exitProcess(1)
                    }
                }
            }
        }
    }
}
