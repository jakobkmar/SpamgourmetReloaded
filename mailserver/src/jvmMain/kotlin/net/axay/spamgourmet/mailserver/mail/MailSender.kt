package net.axay.spamgourmet.mailserver.mail

import net.axay.spamgourmet.mailserver.main.Manager
import org.simplejavamail.api.email.Email
import org.simplejavamail.mailer.MailerBuilder

object MailSender {

    private val mailer = MailerBuilder
        .withSMTPServer(
                Manager.configManager.smtpLoginInformation.host,
                Manager.configManager.smtpLoginInformation.port,
                Manager.configManager.smtpLoginInformation.username,
                Manager.configManager.smtpLoginInformation.password
        )
        .clearEmailAddressCriteria()
        .buildMailer()

    fun sendMail(email: Email) {
        mailer.sendMail(email, true)
    }

}