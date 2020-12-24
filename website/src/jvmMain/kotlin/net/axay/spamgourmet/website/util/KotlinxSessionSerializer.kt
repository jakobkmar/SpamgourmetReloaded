package net.axay.spamgourmet.website.util

import io.ktor.serialization.*
import io.ktor.sessions.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

inline fun <reified T> kotlinxSessionSerializer() = object : SessionSerializer<T> {
    override fun deserialize(text: String) = DefaultJson.decodeFromString<T>(text)
    override fun serialize(session: T) = DefaultJson.encodeToString(session)
}