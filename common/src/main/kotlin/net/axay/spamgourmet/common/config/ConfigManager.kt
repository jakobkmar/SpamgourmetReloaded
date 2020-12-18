package net.axay.spamgourmet.common.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.gson.jsonConfig
import java.io.File

class ConfigManager(applicationFolder: File) {
    private val configFolder = File(applicationFolder, "/config/")

    val databaseLoginInformation by jsonConfig(File(configFolder, "databaseLoginInformation.json")) {
        DatabaseLoginInformation.NOTSET_DEFAULT
    }
}