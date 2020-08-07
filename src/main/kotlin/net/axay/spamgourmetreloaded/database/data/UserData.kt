package net.axay.spamgourmetreloaded.database.data

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserData(
    val username: String,
    val settings: UserSettings,
    val realAddress: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserSettings(
    val lockedAnswerAddress: Boolean?
)