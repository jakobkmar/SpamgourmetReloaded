package net.axay.spamgourmetreloaded.database.data

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserAddressData(
    val address: String,
    val usesLeft: Int,
    val ifOnlyTrusted: Boolean? = null,
    val trustedSenders: Collection<String>? = null,
    val alternativeForwardTo: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnswerAddressData(
    val address: String,
    val answerAsAddress: String,
    val answerToAddress: String,
    val forUser: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BounceAddressData(
    val address: String,
    val informUser: String
)