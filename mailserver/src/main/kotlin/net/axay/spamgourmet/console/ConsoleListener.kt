package net.axay.spamgourmet.console

import net.axay.spamgourmet.main.Manager
import kotlin.concurrent.thread

object ConsoleListener {

    fun listen() {

        thread {
            while (true) {
                val input = readLine()
                if (input != null) {
                    when (input) {
                        "stop" -> Manager.stop()
                    }
                }
            }
        }

    }

}