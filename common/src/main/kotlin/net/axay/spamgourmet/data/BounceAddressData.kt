package net.axay.spamgourmet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerBounceAddressData(
    @SerialName("_id") val address: String,
    val informUser: String,
    val spammerAddress: String,
    val answerAddress: String
)

@Serializable
class SpamBounceAddressData(
    @SerialName("_id") val address: String,
    val informUser: String,
    val userAddress: String
)