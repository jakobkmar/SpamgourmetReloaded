package net.axay.spamgourmet.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.serialization.InstantSerializer
import java.time.Instant

@Serializable
class UserData(
    @SerialName("_id") val username: String,
    val realAddress: String,
    val settings: UserSettings = UserSettings(),
    val information: UserInfo
)

@Serializable
class UserSettings(
    val lockedAnswerAddresses: Boolean = true
)

@Serializable
class UserInfo(
    val description: String? = null,
    val realName: Pair<String?, String?> = Pair(null, null),
    @Serializable(with = InstantSerializer::class) val joined: Instant
)