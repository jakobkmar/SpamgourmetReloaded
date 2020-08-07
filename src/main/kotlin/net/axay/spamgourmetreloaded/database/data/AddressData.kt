package net.axay.spamgourmetreloaded.database.data

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserAddressData(
    val address: String,
    val usesLeft: Int
/*    ,
    val trustedSenders: Collection<String>?,
    val alternativeForwardTo: String?*/ // TODO implement these features later
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnswerAddressData(
    val address: String,
    val answerAsAddress: String,
    val answerToAddress: String,
    val forAddress: String
)