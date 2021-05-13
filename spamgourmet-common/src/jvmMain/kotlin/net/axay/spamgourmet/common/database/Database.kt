package net.axay.spamgourmet.common.database

import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import net.axay.spamgourmet.common.data.*
import net.axay.spamgourmet.common.main.Env
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val db = Database

object Database {
    private val client = KMongo.createClient(
        MongoClientSettings.builder()
            .applyToClusterSettings {
                it.hosts(listOf(ServerAddress(Env.dbHost, Env.dbPort)))
            }
            .build()
    ).coroutine

    private val database = client.getDatabase(Env.dbDatabase)

    val userData = database.getCollection<UserData>()
    val userLoginData = database.getCollection<UserLoginData>()

    val userAddressData = database.getCollection<UserAddressData>()
    val answerAddressData = database.getCollection<AnswerAddressData>()

    val bounceData = database.getCollection<BounceData>()
    val userBounceData = database.getCollection<UserBounceData>()

    val answerBounceAddressData = database.getCollection<AnswerBounceAddressData>()
    val spamBounceAddressData = database.getCollection<SpamBounceAddressData>()

    val sessionData = database.getCollection<SessionData>()
}
