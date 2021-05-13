package net.axay.spamgourmet.mailserver.main

import kotlinx.coroutines.runBlocking
import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.server.smtpServer
import net.axay.simplekotlinmail.server.start
import net.axay.spamgourmet.common.logging.logInfo
import net.axay.spamgourmet.common.logging.logMajorInfo
import net.axay.spamgourmet.common.logging.logSuccess
import net.axay.spamgourmet.mailserver.console.ConsoleListener
import net.axay.spamgourmet.mailserver.mail.MailerUtils
import net.axay.spamgourmet.mailserver.mail.SpamgourmetEmail
import kotlin.concurrent.thread

fun main() {
    Manager.start()
}

object Manager {
    private val smtpServer = smtpServer {
        mailListener { SpamgourmetEmail.process(it) }
    }

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
