package net.axay.spamgourmet.website.main

import net.axay.blueutils.database.mongodb.CoroutineMongoDB
import net.axay.spamgourmet.common.logging.logMajorInfo

val configManager = ConfigManager()
val mongoDB = CoroutineMongoDB(configManager.databaseLoginInformation)
val db = Database(mongoDB)

fun main(args: Array<String>) {

    logMajorInfo("Starting webserver...")
    io.ktor.server.netty.EngineMain.main(args)

}