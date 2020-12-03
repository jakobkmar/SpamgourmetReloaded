package net.axay.spamgourmet.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
class UserBounceData(
    @SerialName("_id") val username: String,
    val bounces: Set<Id<BounceData>> = linkedSetOf()
)