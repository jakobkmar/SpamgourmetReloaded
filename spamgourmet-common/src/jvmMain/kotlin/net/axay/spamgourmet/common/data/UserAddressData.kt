package net.axay.spamgourmet.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserAddressData(
    @SerialName("_id") val address: String,
    val usesLeft: Int,
    val settings: UserAddressSettings? = null
)

@Serializable
class UserAddressSettings(
    val ifOnlyTrusted: Boolean = false,
    val trustedSenders: LinkedHashSet<String> = linkedSetOf(),
    val alternateForwardTo: String? = null
)