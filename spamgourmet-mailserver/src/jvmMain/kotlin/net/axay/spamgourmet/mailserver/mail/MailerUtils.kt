package net.axay.spamgourmet.mailserver.mail

import net.axay.simplekotlinmail.delivery.MailerManager
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.spamgourmet.mailserver.config.MailserverConfigManager

object MailerUtils {

    fun setupMailer() {
        val smtpInfo = MailserverConfigManager.smtpLoginInformation
        MailerManager.defaultMailer = mailerBuilder(
            smtpInfo.host,
            smtpInfo.port,
            smtpInfo.username,
            smtpInfo.password
        )
    }

}