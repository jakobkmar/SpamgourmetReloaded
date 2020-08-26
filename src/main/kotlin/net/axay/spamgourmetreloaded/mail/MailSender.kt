package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.main.Manager
import org.simplejavamail.api.email.Email
import org.simplejavamail.mailer.MailerBuilder

object MailSender {

    private val mailer = MailerBuilder
        .withSMTPServer(
                Manager.configManager.mainConfig.smtpLoginInformation.host,
                Manager.configManager.mainConfig.smtpLoginInformation.port,
                Manager.configManager.mainConfig.smtpLoginInformation.username,
                Manager.configManager.mainConfig.smtpLoginInformation.password
        )
        .clearEmailAddressCriteria()
        .buildMailer()

    fun sendMail(email: Email) {
        mailer.sendMail(email, true)
    }

}