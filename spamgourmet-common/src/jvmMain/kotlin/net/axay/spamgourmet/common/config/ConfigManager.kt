package net.axay.spamgourmet.common.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.hocon.hoconConfig
import net.axay.spamgourmet.common.logging.logInfo
import java.io.File

open class ConfigManager(applicationFolder: File = File(".")) {
    private val configFolder = File(applicationFolder, "/config/")

    protected inner class ConfigFile(path: String) : File(configFolder, path)

    init {
        logInfo("Config folder: ${configFolder.canonicalPath}")
    }

    val databaseLoginInformation by hoconConfig(ConfigFile("databaseLoginInformation.conf")) {
        DatabaseLoginInformation.NOTSET_DEFAULT
    }
}