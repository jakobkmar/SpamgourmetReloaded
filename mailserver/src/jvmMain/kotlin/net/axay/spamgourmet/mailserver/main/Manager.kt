package net.axay.spamgourmet.mailserver.main

import kotlinx.coroutines.runBlocking
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
import kotlin.concurrent.thread

fun main() {
    Manager.start()
}

val db get() = Manager.database

object Manager {

    val configManager = MailserverConfigManager()

    private val smtpServer = smtpServer {
        mailListener { SpamgourmetEmail.process(it) }
    }

    private val mongoDB = CoroutineMongoDB(configManager.databaseLoginInformation)
    val database = Database(mongoDB)

    fun start() {

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(thread(start = false) { stop() })
        // listen to console input
        ConsoleListener.listen()

        logMajorInfo("Starting mailserver...")

        MailerUtils.setupMailer()
        smtpServer.start(keepAlive = true)

        logSuccess("Started mailserver!")

    }

    private fun stop() = runBlocking {

        logInfo("Stopping mailserver...")

        smtpServer.stop()
        MailerManager.shutdownMailers()

        logMajorInfo("Mailserver stopped!")

    }

}