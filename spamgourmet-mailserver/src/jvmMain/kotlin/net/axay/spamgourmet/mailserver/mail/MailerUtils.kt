package net.axay.spamgourmet.mailserver.mail

import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.spamgourmet.mailserver.config.MailserverEnv

object MailerUtils {
    fun setupMailer() {
        MailerManager.defaultMailer = mailerBuilder(
            MailserverEnv.smtpHost,
            MailserverEnv.smtpPort,
            MailserverEnv.smtpUsername,
            MailserverEnv.smtpPassword
        )
    }
}
