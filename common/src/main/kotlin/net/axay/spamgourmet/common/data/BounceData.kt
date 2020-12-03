package net.axay.spamgourmet.common.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.serialization.InstantSerializer
import java.time.Instant

@Serializable
class BounceData(
    @Contextual @SerialName("_id") val id: Id<BounceData>? = null,
    @Serializable(with=InstantSerializer::class) val time: Instant,
    val type: BounceType,
    val from: String,
    val to: String,
    val subject: String?
)

enum class BounceType {
    SPAM_BOUNCE, ANSWER_BOUNCE
}
