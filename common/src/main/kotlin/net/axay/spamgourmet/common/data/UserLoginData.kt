package net.axay.spamgourmet.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserLoginData(
    @SerialName("_id") val username: String,
    val password: ByteArray,
    val passwordAlgorithm: PasswordAlgorithm
)

enum class PasswordAlgorithm {
    BCRYPT
}
