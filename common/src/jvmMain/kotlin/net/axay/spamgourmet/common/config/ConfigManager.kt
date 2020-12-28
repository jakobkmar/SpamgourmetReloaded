package net.axay.spamgourmet.common.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.gson.jsonConfig
import net.axay.spamgourmet.common.data.SMTPLoginInformation
import java.io.File

open class ConfigManager(applicationFolder: File) {
    private val configFolder = File(applicationFolder, "/config/")

    protected inner class ConfigFile(path: String) : File(configFolder, path)

    val databaseLoginInformation by jsonConfig(ConfigFile("databaseLoginInformation.json")) {
        DatabaseLoginInformation.NOTSET_DEFAULT
    }
}