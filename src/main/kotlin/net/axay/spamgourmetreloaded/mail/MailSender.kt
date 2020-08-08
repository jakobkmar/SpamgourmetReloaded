package net.axay.spamgourmetreloaded.mail

import org.simplejavamail.api.email.Email
import org.simplejavamail.mailer.MailerBuilder

object MailSender {

    private val mailer = MailerBuilder
        .withSMTPServer("smtp.mailtrap.io", 2525, "7e207e8e6fcb79", "4163009e830a42")
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