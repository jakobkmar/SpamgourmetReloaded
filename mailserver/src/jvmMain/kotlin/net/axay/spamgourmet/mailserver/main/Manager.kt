package net.axay.spamgourmet.mailserver.main

import net.axay.blueutils.database.mongodb.CoroutineMongoDB
import net.axay.spamgourmet.common.database.Database
import net.axay.spamgourmet.common.logging.logInfo
import net.axay.spamgourmet.common.logging.logMajorInfo
import net.axay.spamgourmet.common.logging.logSuccess
import net.axay.spamgourmet.mailserver.config.MailserverConfigManager
import net.axay.spamgourmet.mailserver.console.ConsoleListener
import net.axay.spamgourmet.mailserver.mail.MailHandler
import net.axay.spamgourmet.mailserver.mail.SpamgourmetMailListener
import org.subethamail.smtp.server.SMTPServer
import java.io.File
import kotlin.system.exitProcess

fun main() {
    Manager.start()
}

val db get() = Manager.database

object Manager {

    val configManager = MailserverConfigManager(File("."))

    private val mailHandler = MailHandler()
    private val smtpServer = SMTPServer(mailHandler).apply {
        port = 25
    }

    private val mongoDB = CoroutineMongoDB(configManager.databaseLoginInformation)
    val database = Database(mongoDB)

    fun start() {
        try {

            logMajorInfo("Starting mailserver...")

            ConsoleListener.listen()

            smtpServer.start()

            mailHandler.register(SpamgourmetMailListener)

            logSuccess("Started mailserver!")

        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    fun stop() {

        logInfo("Stopping program...")

        smtpServer.stop()

        logMajorInfo("Program stopped!")

        exitProcess(1)

    }

}