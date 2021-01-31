package net.axay.spamgourmet.mailserver.main

import net.axay.blueutils.database.mongodb.CoroutineMongoDB
import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.server.smtpServer
import net.axay.simplekotlinmail.server.start
import net.axay.spamgourmet.common.database.Database
import net.axay.spamgourmet.common.logging.logInfo
import net.axay.spamgourmet.common.logging.logMajorInfo
import net.axay.spamgourmet.common.logging.logSuccess
import net.axay.spamgourmet.mailserver.config.MailserverConfigManager
import net.axay.spamgourmet.mailserver.console.ConsoleListener
import net.axay.spamgourmet.mailserver.mail.MailerUtils
import net.axay.spamgourmet.mailserver.mail.SpamgourmetEmail
import java.io.File
import kotlin.system.exitProcess

fun main() {
    Manager.start()
}

val db get() = Manager.database

object Manager {

    val configManager = MailserverConfigManager(File("."))

    private val smtpServer = smtpServer {
        mailListener { SpamgourmetEmail.process(it) }
    }

    private val mongoDB = CoroutineMongoDB(configManager.databaseLoginInformation)
    val database = Database(mongoDB)

    fun start() {
        try {

            logMajorInfo("Starting mailserver...")

            ConsoleListener.listen()
            MailerUtils.setupMailer()
            smtpServer.start(keepAlive = true)

            logSuccess("Started mailserver!")

        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    suspend fun stop() {

        logInfo("Stopping program...")

        smtpServer.stop()
        MailerManager.shutdownMailers()

        logMajorInfo("Program stopped!")

        exitProcess(1)

    }

}