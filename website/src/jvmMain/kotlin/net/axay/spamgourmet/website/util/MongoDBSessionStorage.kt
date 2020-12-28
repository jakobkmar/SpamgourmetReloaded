package net.axay.spamgourmet.website.util

import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.spamgourmet.common.data.SessionData
import net.axay.spamgourmet.website.main.db
import org.litote.kmongo.eq

class MongoDBSessionStorage : SessionStorage {
    override suspend fun invalidate(id: String) {
        db.sessionData.deleteOne(SessionData::id eq id)
    }

    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R {
        return consumer(ByteReadChannel(
            db.sessionData.findOne(SessionData::id eq id)?.data
                ?: throw NoSuchElementException("Session $id not found in database")
        ))
    }

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        db.sessionData.save(
            SessionData(
                id,
                CoroutineScope(Dispatchers.IO).writer { provider(channel) }.channel.toByteArray()
            )
        )
    }
}