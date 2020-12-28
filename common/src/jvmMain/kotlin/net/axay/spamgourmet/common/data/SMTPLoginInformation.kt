package net.axay.spamgourmet.common.data

import kotlinx.serialization.Serializable

@Serializable
class SMTPLoginInformation(
    val host: String,
    val port: Int,
    val username: String?,
    val password: String?
)