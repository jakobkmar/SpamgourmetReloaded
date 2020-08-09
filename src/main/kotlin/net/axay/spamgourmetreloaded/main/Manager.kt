package net.axay.spamgourmetreloaded.main

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmetreloaded.config.ConfigManager
import net.axay.spamgourmetreloaded.console.ConsoleListener
import net.axay.spamgourmetreloaded.database.DataManager
import net.axay.spamgourmetreloaded.mail.MailHandler
import net.axay.spamgourmetreloaded.mail.MailListener
import net.axay.spamgourmetreloaded.mail.MailSender
import net.axay.spamgourmetreloaded.mail.SpamgourmetMailListener
import net.axay.spamgourmetreloaded.util.logInfo
import net.axay.spamgourmetreloaded.util.logInfoMultiline
import org.simplejavamail.converter.EmailConverter
import org.simplejavamail.email.EmailBuilder
import org.subethamail.smtp.server.SMTPServer
import javax.mail.internet.MimeMessage
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

            mailHandler.register(object : MailListener {
                override fun onReceive(envelopeFrom: String, recipients: List<String>, mimeMessage: MimeMessage) {
                    logInfoMultiline("sender: $envelopeFrom", "receiver: $recipients", EmailConverter.mimeMessageToEmail(mimeMessage))
                }
            })
            mailHandler.register(SpamgourmetMailListener)

            logInfo("Program started!")

            MailSender.sendLocalEmail(
                EmailBuilder.startingBlank()
                    .from("bluefiredata@gmail.com")
                    .to("gram.40.blue@axay.net")
                    .withSubject("test mail")
                    .withHTMLText(ValueHolder.TEST_HTML)
                    .withPlainText("Please view this email in a modern email client!")
                    .buildEmail()
            )

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