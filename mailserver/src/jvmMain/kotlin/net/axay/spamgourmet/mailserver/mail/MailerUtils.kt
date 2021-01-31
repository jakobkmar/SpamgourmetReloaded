package net.axay.spamgourmet.mailserver.mail

import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.spamgourmet.mailserver.main.Manager

object MailerUtils {

    fun setupMailer() {
        val smtpInfo = Manager.configManager.smtpLoginInformation
        MailerManager.defaultMailer = mailerBuilder(
            smtpInfo.host,
            smtpInfo.port,
            smtpInfo.username,
            smtpInfo.password
        )
    }

}