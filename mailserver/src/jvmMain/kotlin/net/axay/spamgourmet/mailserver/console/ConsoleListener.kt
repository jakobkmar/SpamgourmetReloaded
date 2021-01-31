package net.axay.spamgourmet.mailserver.console

import kotlinx.coroutines.runBlocking
import net.axay.spamgourmet.mailserver.main.Manager
import kotlin.concurrent.thread

object ConsoleListener {

    fun listen() {

        thread {
            while (true) {
                val input = readLine()
                if (input != null) {
                    when (input) {
                        "stop" -> runBlocking { Manager.stop() }
                    }
                }
            }
        }

    }

}