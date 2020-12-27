package net.axay.spamgourmet.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SessionData(
    @SerialName("_id") val id: String,
    val data: ByteArray
)