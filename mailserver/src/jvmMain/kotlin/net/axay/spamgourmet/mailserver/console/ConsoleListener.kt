package net.axay.spamgourmet.mailserver.console

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.axay.spamgourmet.mailserver.main.Manager

object ConsoleListener {

    fun listen() {

        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val input = readLine() ?: continue
                when (input) {
                    "stop" -> {
                        runBlocking { Manager.stop() }
                        break
                    }
                }
            }
        }

    }

}