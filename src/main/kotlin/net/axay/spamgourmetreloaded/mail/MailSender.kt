package net.axay.spamgourmetreloaded.mail

import net.axay.spamgourmetreloaded.main.Manager
import org.simplejavamail.api.email.Email
import org.simplejavamail.mailer.MailerBuilder

object MailSender {

    private val mailer = MailerBuilder
        .withSMTPServer("smtp.mailtrap.io", 2525, "7e207e8e6fcb79", "4163009e830a42")
        .withSMTPServer(
                Manager.configManager.mainConfig.smtpLoginInformation.host,
                Manager.configManager.mainConfig.smtpLoginInformation.port,
                Manager.configManager.mainConfig.smtpLoginInformation.username,
                Manager.configManager.mainConfig.smtpLoginInformation.password
        )
        .clearEmailAddressCriteria()
        .buildMailer()

    private val localMailer = MailerBuilder
            .withSMTPServer("localhost", 25)
            .clearEmailAddressCriteria()
            .buildMailer()

    fun sendMail(email: Email) {
        mailer.sendMail(email)
    }

    fun sendLocalEmail(email: Email) {
        localMailer.sendMail(email)
    }

}