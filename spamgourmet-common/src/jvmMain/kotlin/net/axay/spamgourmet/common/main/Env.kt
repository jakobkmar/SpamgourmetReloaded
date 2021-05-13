package net.axay.spamgourmet.common.main

import java.lang.System.getenv

object Env {
    val dbHost = getenv("DB_HOST") ?: "localhost"
    val dbPort = getenv("DB_PORT")?.toInt() ?: 27017
    val dbDatabase = getenv("DB_DATABASE") ?: "spamgourmet"
}
