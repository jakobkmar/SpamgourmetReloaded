package net.axay.spamgourmetreloaded.database.data

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserData(
    val username: String,
    val settings: UserSettings,
    val realAddress: String,
    val bounceData: Collection<BounceData>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserSettings(
    val lockedAnswerAddresses: Boolean = true
)

data class BounceData(
    val time: Instant,
    val bounceFrom: String?,
    val bounceTo: String
)