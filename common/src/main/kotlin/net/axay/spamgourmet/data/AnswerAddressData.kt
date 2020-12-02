package net.axay.spamgourmet.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AnswerAddressData(
    @SerialName("_id") val address: String,
    val answerAsAddress: String,
    val answerToAddress: String,
    val forUser: String,
    val settings: AnswerAddressSettings = AnswerAddressSettings()
)

@Serializable
class AnswerAddressSettings(
    val useGlobalAdditionalUserAddresses: Boolean = true,
    val additionalUserAddresses: LinkedHashSet<String> = linkedSetOf()
)