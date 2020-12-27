package net.axay.spamgourmet.website.main

import net.axay.blueutils.database.mongodb.CoroutineMongoDB
import net.axay.spamgourmet.common.config.ConfigManager
import net.axay.spamgourmet.common.database.Database
import net.axay.spamgourmet.common.logging.logMajorInfo
import java.io.File

val configManager = ConfigManager(File("."))
val mongoDB = CoroutineMongoDB(configManager.databaseLoginInformation)
val db = Database(mongoDB)

fun main(args: Array<String>) {

    logMajorInfo("Starting webserver...")
    io.ktor.server.netty.EngineMain.main(args)

}