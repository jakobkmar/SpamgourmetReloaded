package net.axay.spamgourmet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.serialization.InstantSerializer
import java.time.Instant

@Serializable
class BounceData(
    @SerialName("_id") val id: Id<BounceData>,
    @Serializable(with=InstantSerializer::class) val time: Instant,
    val type: BounceType,
    val from: String,
    val to: String,
    val subject: String?
)

enum class BounceType {
    SPAM_BOUNCE, ANSWER_BOUNCE
}
