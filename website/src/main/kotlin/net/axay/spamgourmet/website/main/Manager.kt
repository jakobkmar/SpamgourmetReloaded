package net.axay.spamgourmet.website.main

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.spamgourmet.common.config.ConfigManager
import net.axay.spamgourmet.common.database.Database
import java.io.File

val configManager = ConfigManager(File("."))
val mongoDB = MongoDB(configManager.databaseLoginInformation)
val db = Database(mongoDB)

fun main() {

    KtorManager.start()

}