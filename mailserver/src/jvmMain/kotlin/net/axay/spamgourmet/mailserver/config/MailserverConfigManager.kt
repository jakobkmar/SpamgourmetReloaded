package net.axay.spamgourmet.mailserver.config

import net.axay.blueutils.gson.jsonConfig
import net.axay.spamgourmet.common.config.ConfigManager
import net.axay.spamgourmet.common.data.DomainInformation
import net.axay.spamgourmet.common.data.SMTPLoginInformation
import java.io.File

class MailserverConfigManager(applicationFolder: File) : ConfigManager(applicationFolder) {
    val smtpLoginInformation by jsonConfig(ConfigFile("smtpLoginInformation.json")) {
        SMTPLoginInformation("notset", 12345, "notset", "notset")
    }

    val domainInformation by jsonConfig(ConfigFile("domainInformation.json")) {
        DomainInformation("notset")
    }
}