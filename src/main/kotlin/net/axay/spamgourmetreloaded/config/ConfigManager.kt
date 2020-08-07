package net.axay.spamgourmetreloaded.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.gson.GsonConfigManager
import java.io.File

class ConfigManager {

    private val configFolder = File("./configs/")

    private val mainConfigFile = File(configFolder, "mainConfig.json")

    val mainConfig: MainConfig

    init {

        mainConfig = GsonConfigManager.loadOrCreateDefault(mainConfigFile, MainConfig::class.java) {
            MainConfig(
                DatabaseLoginInformation("host", 12345, "database", "user", "password"),
                "example.org",
                SMTPLoginInformation("host", 12345, "username", "password")
            )
        }

    }

}

data class MainConfig(
    val databaseLoginInformation: DatabaseLoginInformation,
    val addressDomain: String,
    val smtpLoginInformation: SMTPLoginInformation
)

data class SMTPLoginInformation(
    val host: String,
    val port: Int,
    val username: String?,
    val password: String?
)