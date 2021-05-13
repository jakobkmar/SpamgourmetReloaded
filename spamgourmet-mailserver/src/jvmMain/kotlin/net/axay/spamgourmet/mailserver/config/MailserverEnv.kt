package net.axay.spamgourmet.mailserver.config

import java.lang.System.getenv

object MailserverEnv {
    val smtpHost = getenv("SMTP_HOST") ?: "localhost"
    val smtpPort = getenv("SMTP_PORT")?.toInt() ?: 25
    val smtpUsername = getenv("SMTP_USERNAME") ?: "notset"
    val smtpPassword = getenv("SMTP_PASSWORD") ?: "notset"

    val addressDomain = getenv("SG_ADDRESS_DOMAIN") ?: "axay.net"
}
