package net.axay.spamgourmet.mailserver.main

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmet.mailserver.config.ConfigManager
import net.axay.spamgourmet.mailserver.console.ConsoleListener
import net.axay.spamgourmet.common.database.Database
import net.axay.spamgourmet.mailserver.mail.MailHandler
import net.axay.spamgourmet.mailserver.mail.SpamgourmetMailListener
import net.axay.spamgourmet.mailserver.util.logInfo
import org.subethamail.smtp.server.SMTPServer
import kotlin.system.exitProcess

fun main() {
    Manager.start()
}

val db get() = Manager.database

object Manager {

    val configManager = ConfigManager()

    private val mailHandler = MailHandler()
    private val smtpServer = SMTPServer(mailHandler)
    init {
        smtpServer.apply {
            // set host data
            port = 25
        }
    }

    private val mongoDB = MongoDB(configManager.mainConfig.databaseLoginInformation)
    val database = Database(mongoDB)

    fun start() {
        try {

            logInfo("Starting program...")

            ConsoleListener.listen()

            smtpServer.start()

            mailHandler.register(SpamgourmetMailListener)

            logInfo("Program started!")

        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    fun stop() {

        logInfo("Stopping program...")

        smtpServer.stop()

        logInfo("Program stopped!")

        exitProcess(1)

    }

}