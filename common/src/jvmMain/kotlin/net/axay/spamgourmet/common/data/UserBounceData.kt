package net.axay.spamgourmet.common.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
class UserBounceData(
    @SerialName("_id") val username: String,
    val bounces: Set<@Contextual Id<BounceData>> = linkedSetOf()
)