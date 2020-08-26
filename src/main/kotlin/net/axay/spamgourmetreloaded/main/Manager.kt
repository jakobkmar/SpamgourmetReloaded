package net.axay.spamgourmetreloaded.main

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmetreloaded.config.ConfigManager
import net.axay.spamgourmetreloaded.console.ConsoleListener
import net.axay.spamgourmetreloaded.database.DataManager
import net.axay.spamgourmetreloaded.mail.MailHandler
import net.axay.spamgourmetreloaded.mail.SpamgourmetMailListener
import net.axay.spamgourmetreloaded.util.logInfo
import org.subethamail.smtp.server.SMTPServer
import kotlin.system.exitProcess

fun main() {
    Manager.start()
}

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
    val dataManager = DataManager(mongoDB)

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