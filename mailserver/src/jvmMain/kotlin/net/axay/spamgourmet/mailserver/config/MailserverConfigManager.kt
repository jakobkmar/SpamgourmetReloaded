package net.axay.spamgourmet.mailserver.config

import net.axay.blueutils.hocon.hoconConfig
import net.axay.spamgourmet.common.config.ConfigManager
import net.axay.spamgourmet.common.data.DomainInformation
import net.axay.spamgourmet.common.data.SMTPLoginInformation

class MailserverConfigManager : ConfigManager() {
    val smtpLoginInformation by hoconConfig(ConfigFile("smtpLoginInformation.conf")) {
        SMTPLoginInformation("notset", 12345, "notset", "notset")
    }

    val domainInformation by hoconConfig(ConfigFile("domainInformation.conf")) {
        DomainInformation("notset")
    }
}