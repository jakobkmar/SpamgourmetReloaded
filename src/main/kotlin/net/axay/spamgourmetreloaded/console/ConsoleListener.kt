package net.axay.spamgourmetreloaded.console

import net.axay.spamgourmetreloaded.main.Manager
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